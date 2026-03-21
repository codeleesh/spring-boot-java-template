package io.dodn.springboot.storage.kafka.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("context")
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@EmbeddedKafka(
        partitions = 1,
        topics = {"test-topic", "test-topic-with-key", "error-topic"}
)
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.auto-offset-reset=earliest"
})
public class KafkaIntegrationTest {

    @Autowired
    private KafkaProducerService producerService;

    @Autowired
    private KafkaConsumerService consumerService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setUp() {
        consumerService.clearMessages();
    }

    @Test
    @DisplayName("기본 메시지 송수신 테스트")
    void testBasicProduceAndConsume() throws InterruptedException {
        // Given
        String testMessage = "Hello Kafka!";
        String topic = "test-topic";

        // When
        producerService.sendMessage(topic, testMessage);

        // Then
        boolean messageReceived = consumerService.waitForMessage(5, TimeUnit.SECONDS);
        assertThat(messageReceived).isTrue();

        List<String> receivedMessages = consumerService.getReceivedMessages();
        assertThat(receivedMessages.size()).isEqualTo(1);
        assertThat(receivedMessages.get(0)).isEqualTo(testMessage);
    }

    @Test
    @DisplayName("키-값 쌍 메시지 송수신 테스트")
    void testProduceAndConsumeWithKey() throws InterruptedException {
        // Given
        String key = "user-123";
        String value = "User data";
        String topic = "test-topic-with-key";

        // When
        producerService.sendMessage(topic, key, value);

        // Then
        boolean messageReceived = consumerService.waitForMessage(5, TimeUnit.SECONDS);
        assertThat(messageReceived).isTrue();

        List<String> receivedMessages = consumerService.getReceivedMessages();
        assertThat(receivedMessages.size()).isEqualTo(1);
        assertThat(receivedMessages.get(0)).isEqualTo(key + ":" + value);
    }

    @Test
    @DisplayName("비동기 메시지 전송 테스트")
    void testAsyncProduceAndConsume() throws Exception {
        // Given
        String testMessage = "Async message";
        String topic = "test-topic";

        // When
        CompletableFuture<SendResult<String, String>> future =
                producerService.sendMessageAsync(topic, testMessage);

        // Then
        SendResult<String, String> result = future.get(5, TimeUnit.SECONDS);
        assertThat(result).isNotNull();
        assertThat(result.getRecordMetadata().topic()).isEqualTo(topic);

        boolean messageReceived = consumerService.waitForMessage(5, TimeUnit.SECONDS);
        assertThat(messageReceived).isTrue();

        List<String> receivedMessages = consumerService.getReceivedMessages();
//        assertThat(receivedMessages).contains(testMessage);
    }

    @Test
    @DisplayName("여러 메시지 순차 전송 테스트")
    void testMultipleMessages() throws InterruptedException {
        // Given
        String topic = "test-topic";
        List<String> testMessages = Arrays.asList("Message 1", "Message 2", "Message 3");

        // When
        testMessages.forEach(message -> producerService.sendMessage(topic, message));

        // Then
        Thread.sleep(2000); // 모든 메시지가 처리될 때까지 대기

        List<String> receivedMessages = consumerService.getReceivedMessages();
        assertThat(receivedMessages.size()).isEqualTo(testMessages.size());
//        assertThat(receivedMessages).containsExactlyInAnyOrderElementsOf(testMessages);
    }

    @Test
    @DisplayName("KafkaTemplate 직접 사용 테스트")
    void testDirectKafkaTemplate() throws Exception {
        // Given
        String topic = "test-topic";
        String message = "Direct template message";

        // When
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);

        // Then
        SendResult<String, String> result = future.get(5, TimeUnit.SECONDS);
        assertThat(result.getRecordMetadata().topic()).isEqualTo(topic);

        boolean messageReceived = consumerService.waitForMessage(5, TimeUnit.SECONDS);
        assertThat(messageReceived).isTrue();
    }
}
