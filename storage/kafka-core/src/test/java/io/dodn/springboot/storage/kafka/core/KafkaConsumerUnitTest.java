package io.dodn.springboot.storage.kafka.core;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = {"unit-test-topic"}
)
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"
})
public class KafkaConsumerUnitTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    private org.apache.kafka.clients.consumer.Consumer<String, String> consumer;
    private Producer<String, String> producer;

    @BeforeEach
    void setUp() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getBrokersAsString());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-consumer-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(Collections.singletonList("unit-test-topic"));

        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, embeddedKafka.getBrokersAsString());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        producer = new DefaultKafkaProducerFactory<String, String>(producerProps).createProducer();
    }

    @AfterEach
    void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
        if (producer != null) {
            producer.close();
        }
    }

    @Test
    @DisplayName("저수준 Consumer/Producer 테스트")
    void testLowLevelConsumerProducer() {
        // Given
        String topic = "unit-test-topic";
        String key = "test-key";
        String value = "test-value";

        // When
        producer.send(new ProducerRecord<>(topic, key, value));
        producer.flush();

        // Then
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
        assertThat(records).isNotEmpty();

        ConsumerRecord<String, String> record = records.iterator().next();
        assertThat(record.topic()).isEqualTo(topic);
        assertThat(record.key()).isEqualTo(key);
        assertThat(record.value()).isEqualTo(value);
    }
}
