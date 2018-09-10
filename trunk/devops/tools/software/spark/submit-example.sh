#!/usr/bin/env bash
sudo ./bin/spark-submit \
    --class org.apache.spark.examples.SparkPi \
    --master spark://NX-Test.qmpqivvwwi2uha3zkjyo3baare.bx.internal.cloudapp.net:7077 \
    --deploy-mode client \
    /stat/spark-2.2.0-bin-hadoop2.7/examples/jars/spark-examples_2.11-2.2.0.jar \
    1



#!/usr/bin/env bash
sudo ./bin/spark-submit \
    --class com.netx.StatApp \
    --master spark://127.0.0.1:7077 \
    --deploy-mode client \
    /home/cloudzou/spark-test-assembly-1.0.0.jar



