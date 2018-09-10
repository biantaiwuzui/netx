package com.netx.util

import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.ActorLogging
import com.alibaba.otter.canal.protocol.CanalEntry
import com.alibaba.otter.canal.protocol.CanalEntry._
import com.netx.message.SourceChangeMessage
import com.typesafe.scalalogging.StrictLogging

import collection.JavaConverters._
import scala.util.{ Failure, Success, Try }

trait ColumnParser[T] {
  def parseColumn(from: Option[CanalEntry.Column]): Option[T]
}

object ColumnParser {
  private val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  def parseColumn[T](from: Option[CanalEntry.Column])(implicit parser: ColumnParser[T]): Option[T] = parser.parseColumn(from)

  implicit val ColumnToStringParser: ColumnParser[String] = new ColumnParser[String] {
    override def parseColumn(from: Option[Column]): Option[String] = {
      from match {
        case Some(col) => Some(col.getValue)
        case _ => None
      }
    }
  }

  implicit val ColumnToBooleanParser: ColumnParser[Boolean] = new ColumnParser[Boolean] {
    override def parseColumn(from: Option[Column]): Option[Boolean] = {
      from match {
        case Some(col) => Some(!col.getValue.equals("0"))
        case _ => Some(false)
      }
    }
  }

  implicit val ColumnToDateTimeParser: ColumnParser[Date] = new ColumnParser[Date] {
    override def parseColumn(from: Option[Column]): Option[Date] = from match {
      case Some(column) => Try { sdf.parse(column.getValue) } match {
        case Success(value) => Some(value)
        case Failure(_) => None
      }
    }
  }
}

case class TableSchema(tableName: String, pkIdName: String, relationIdName: String) {
  def equal(targetTableName: String): Boolean = targetTableName.equals(tableName)
}

trait CanalTableCollection {
  def tableNames: List[TableSchema]
}

trait CanalMessageParser {
  //  self: StrictLogging =>
  import ColumnParser._

  def parse(entry: Option[Entry]): List[SourceChangeMessage] = {
    val rowChange: Option[RowChange] = entry map { e =>
      {
        try { RowChange.parseFrom(e.getStoreValue) }
        catch { case e: Exception => /* logger.error(s"parse event has an error, data:${entry.toString}");*/ throw new RuntimeException(e) }
      }
    }
    parseInternal(entry, rowChange).flatten
  }

  private def parseInternal(entry: Option[Entry], rowChange: Option[RowChange]): List[Option[SourceChangeMessage]] = {
    val ret = for {
      e <- entry
      r <- rowChange
    } yield {
      val header = e.getHeader
      val eventType = r.getEventType
      r.getRowDatasList.asScala.toList map (row => parseSingleTable(header, eventType, row))
    }
    ret.getOrElse(List.empty)
  }

  private def parseSingleTable(header: Header, eventType: EventType, row: RowData): Option[SourceChangeMessage] = {
    val columnList = eventType match {
      case EventType.DELETE => row.getBeforeColumnsList().asScala.toList
      case _ => row.getAfterColumnsList().asScala.toList
    }
    tableNames.find(t => t.equal(header.getTableName)) match {
      case Some(schema) => Some(fetchData(header, eventType, columnList, schema))
      case _ => None
    }
  }

  private def fetchData(header: Header, eventType: EventType, columns: List[Column], schema: TableSchema): SourceChangeMessage = {
    val pkId = parseColumn[String](columns.find(p => p.getName.equals(schema.pkIdName)))
    val relationId = parseColumn[String](columns.find(p => p.getName.equals(schema.relationIdName)))
    val createTime = parseColumn[String](columns.find(p => p.getName.equals("create_time")))
    val updateTime = parseColumn[String](columns.find(p => p.getName.equals("update_time")))
    val deleted = parseColumn[Boolean](columns.find(p => p.getName.equals("deleted")))

    SourceChangeMessage(header.getSchemaName, header.getTableName, eventType, pkId, relationId, createTime, updateTime, deleted)
  }
  def tableNames: List[TableSchema]
}
