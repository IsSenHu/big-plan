package rocketmq.demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

/**
 * @author HuSen
 * @since 2020/7/16 11:30 上午
 */
public class Producer {

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("demo_Producer_group");
        producer.setNamesrvAddr("118.24.38.46:9876");
        producer.start();
        // keys 消息的唯一键
        Message message = new Message("Topic_Demo", "Tags", "Keys_1", "hello!".getBytes(RemotingHelper.DEFAULT_CHARSET));
        System.out.println(producer.send(message));
    }
}
