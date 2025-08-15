package io.dodn.springboot.core.api.domain;

import io.dodn.springboot.storage.kafka.core.KafkaProducerService;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {

    private final KafkaProducerService kafkaProduce;

    public ExampleService(KafkaProducerService kafkaProduce) {
        this.kafkaProduce = kafkaProduce;
    }

    public ExampleResult processExample(ExampleData exampleData) {
        return new ExampleResult(exampleData.value());
    }

    public void produceExample() {
        kafkaProduce.sendMessage("test-topic", "test");
    }
}