package io.dodn.springboot.storage.kafka.core;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class KafkaConsumerService {

    private final List<String> receivedMessages = new ArrayList<>();
    private final AtomicReference<CountDownLatch> latch = new AtomicReference<>(new CountDownLatch(1));

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(String message) {
        System.out.println("Received Message: " + message);
        receivedMessages.add(message);
        latch.get().countDown();
    }

    @KafkaListener(topics = "test-topic-with-key", groupId = "test-group")
    public void listenWithKey(ConsumerRecord<String, String> record) {
        System.out.println("Received Message with Key: " + record.key() + ", Value: " + record.value());
        receivedMessages.add(record.key() + ":" + record.value());
        latch.get().countDown();
    }

    public List<String> getReceivedMessages() {
        return new ArrayList<>(receivedMessages);
    }

    public void clearMessages() {
        receivedMessages.clear();
        latch.set(new CountDownLatch(1));
    }

    public boolean waitForMessage(long timeout, TimeUnit unit) throws InterruptedException {
        return latch.get().await(timeout, unit);
    }
}
