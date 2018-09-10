package com.netx

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object UserFriendStat {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().master("local[4]").getOrCreate();

    import spark.implicits._

    val otaDeviceVersion = spark.read.format("jdbc").option("url", "jdbc:mysql://xxx.xx.xx.xxx:33061/iot")
      .option("dbtable", "ota_device_version").option("user", "xxxx").option("password", "xxxx").load();

    otaDeviceVersion.show();

    val result = otaDeviceVersion.groupBy($"product_id", $"version").agg(countDistinct($"device_id") as "num");

    result.show()

  }
}
