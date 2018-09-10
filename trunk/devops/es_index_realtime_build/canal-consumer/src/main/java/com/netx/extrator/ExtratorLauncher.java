package com.netx.extrator;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import com.netx.extrator.client.CilentSimple;
import com.netx.extrator.kafka.CanalKafkaProducer;
import com.netx.extrator.util.PropsUtil;


import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 主程序入口
 * Created by Yawn on 2018/7/18
 */
public class ExtratorLauncher {
    public static void main(String[] args) {
        CilentSimple cilentSimple;
        //构建canal消费者
        CanalKafkaProducer canalKafkaProducer = new CanalKafkaProducer("config.properties");
        //构造canal client
        Properties properties = PropsUtil.loadProps("canal_client.properties");
        String destination = PropsUtil.getString(properties, "destination");
        String username = PropsUtil.getString(properties, "username");
        String password = PropsUtil.getString(properties, "password");

        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(AddressUtils.getHostIp(),
                    11111), destination, username, password);
        cilentSimple = new CilentSimple(canalKafkaProducer, connector);
        cilentSimple.work();

    }

}
