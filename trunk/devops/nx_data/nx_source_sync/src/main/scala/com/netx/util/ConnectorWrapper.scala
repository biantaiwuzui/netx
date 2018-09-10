package com.netx.util

import java.net.InetSocketAddress

import com.alibaba.otter.canal.client.{ CanalConnector, CanalConnectors }
import com.alibaba.otter.canal.protocol.Message
import com.netx.actors.{ CanalConnectionInfo, ClusterConnectionInfo, SimpleConnectionInfo }
import com.typesafe.config.Config

object CanalConnectionInstance {
  val BATCH_SIZE = 5 * 1024

  def getConnector(config: Config): CanalConnector = {
    getConnectionInfo(config) match {
      case sConnectionInfo: SimpleConnectionInfo => CanalConnectors.newSingleConnector(
        new InetSocketAddress(sConnectionInfo.ip, sConnectionInfo.port),
        sConnectionInfo.destination, sConnectionInfo.userName, sConnectionInfo.password)
      case cConnectionInfo: ClusterConnectionInfo => CanalConnectors.newClusterConnector(cConnectionInfo.zkServers, cConnectionInfo.destination,
        cConnectionInfo.userName, cConnectionInfo.password)
    }
  }
  private def getConnectionInfo(config: Config): CanalConnectionInfo = {
    if (!config.getString("simple.ip").isEmpty) {
      SimpleConnectionInfo(config.getString("simple.ip"), config.getInt("simple.port"), config.getString("destination"), config.getString("userName"), config.getString("password"))
    } else {
      ClusterConnectionInfo(config.getString("cluster.zkServers"), config.getString("destination"), config.getString("userName"), config.getString("password"))
    }
  }
}

abstract class ConnectorT(config: Config) {
  def connect
  def getMessage: Message
  def ack(batchId: Long)
  def disconnect
}

abstract class TestConnectorT(config: Config) extends ConnectorT(config) {
  def connect() = println("connected")
  def getMessage(): Message = ???
  def ack(batchId: Long) = println(s"batchId:${batchId}")
  def disconnect() = println("disconnected")
}

abstract class ConnectorWrapper(config: Config) extends ConnectorT(config) {
  import CanalConnectionInstance._

  protected val connector: CanalConnector = getConnector(config)

  def connect() = {
    connector.connect()
    connector.subscribe()
  }

  def getMessage(): Message = connector.getWithoutAck(BATCH_SIZE)

  def ack(batchId: Long) = connector.ack(batchId)

  def disconnect() = connector.disconnect()
}
