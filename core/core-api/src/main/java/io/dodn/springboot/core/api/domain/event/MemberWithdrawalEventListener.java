package io.dodn.springboot.core.api.domain.event;

import io.dodn.springboot.storage.kafka.core.KafkaProducerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MemberWithdrawalEventListener {

	private static final Logger log = LoggerFactory.getLogger(MemberWithdrawalEventListener.class);

	private static final String TOPIC = "member-withdrawal";

	private final KafkaProducerService kafkaProducerService;

	public MemberWithdrawalEventListener(KafkaProducerService kafkaProducerService) {
		this.kafkaProducerService = kafkaProducerService;
	}

	@EventListener
	public void handleMemberWithdrawal(MemberWithdrawalEvent event) {
		String message = "{\"memberId\":" + event.memberId() + ",\"withdrawnAt\":\"" + event.withdrawnAt()
				+ "\"}";
		kafkaProducerService.sendMessage(TOPIC, String.valueOf(event.memberId()), message);
		log.info("Withdrawal event published to Kafka for member [{}].", event.memberId());
	}

}
