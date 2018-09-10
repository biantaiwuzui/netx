package pers.yawn;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.common.utils.AddressUtils;
import org.junit.Test;
import com.netx.extrator.kafka.CanalKafkaProducer;
import com.netx.extrator.client.CilentSimple;

import java.net.InetSocketAddress;

/**
 * Created by Yawn on 2018/7/18 0018.
 */
public class CanalKafkaProducerTest {
    private CilentSimple cilentSimple;

    private void init() {
        CanalKafkaProducer canalKafkaProducer = new CanalKafkaProducer("config.properties");
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(AddressUtils.getHostIp(),
                11111), "example", "", "");
        cilentSimple = new CilentSimple(canalKafkaProducer, connector);
    }

    @Test
    public void work() {
        init();
        cilentSimple.work();
    }





}
