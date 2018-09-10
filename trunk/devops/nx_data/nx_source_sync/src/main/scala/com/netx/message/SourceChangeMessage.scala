package com.netx.message

import com.alibaba.otter.canal.protocol.CanalEntry.EventType
import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }

object SourceChangeMessage {
  implicit val decodeSCMessage: Decoder[SourceChangeMessage] = deriveDecoder[SourceChangeMessage]
  implicit val encodeSCMessage: Encoder[SourceChangeMessage] = deriveEncoder[SourceChangeMessage]

  implicit val decodeEventType: Decoder[EventType] = Decoder[String].emap {
    case "UPDATE" => Right(EventType.UPDATE)
    case "INSERT" => Right(EventType.INSERT)
    case "DELETE" => Right(EventType.DELETE)
    case _ => Right(EventType.QUERY)
  }
  implicit val encodeEventType: Encoder[EventType] = Encoder[String].contramap(_.toString)
}

case class SourceChangeMessage(dbName: String, tableName: String, eventType: EventType, pkId: Option[String], relationId: Option[String],
  createTime: Option[String], updateTime: Option[String], deleted: Option[Boolean])
