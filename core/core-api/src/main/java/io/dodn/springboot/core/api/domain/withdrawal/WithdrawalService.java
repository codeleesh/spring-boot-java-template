package io.dodn.springboot.core.api.domain.withdrawal;

import io.dodn.springboot.core.api.domain.event.MemberWithdrawalEvent;
import io.dodn.springboot.core.api.domain.withdrawal.dto.WithdrawalValidationResult;
import io.dodn.springboot.core.enums.WithdrawalStatus;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalRequestLogEntity;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalRequestLogRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WithdrawalService {

	private static final Logger log = LoggerFactory.getLogger(WithdrawalService.class);

	private final WithdrawalValidationService validationService;

	private final WithdrawalRequestLogRepository requestLogRepository;

	private final ApplicationEventPublisher eventPublisher;

	public WithdrawalService(WithdrawalValidationService validationService,
			WithdrawalRequestLogRepository requestLogRepository, ApplicationEventPublisher eventPublisher) {
		this.validationService = validationService;
		this.requestLogRepository = requestLogRepository;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public WithdrawalRequestLogEntity withdraw(Long memberId) {
		WithdrawalRequestLogEntity requestLog = new WithdrawalRequestLogEntity(memberId,
				WithdrawalStatus.REQUESTED.name());
		requestLogRepository.save(requestLog);

		WithdrawalValidationResult validationResult = validationService.validate(memberId);

		if (!validationResult.isEligible()) {
			String reason = validationResult.rejectionSummary();
			log.warn("Withdrawal rejected for member [{}]: {}", memberId, reason);
			requestLog.reject(reason);
			return requestLog;
		}

		requestLog.updateStatus(WithdrawalStatus.VALIDATED.name());

		try {
			eventPublisher.publishEvent(new MemberWithdrawalEvent(memberId, LocalDateTime.now()));
			requestLog.updateStatus(WithdrawalStatus.COMPLETED.name());
			log.info("Withdrawal completed for member [{}].", memberId);
		}
		catch (Exception e) {
			log.error("Withdrawal event publishing failed for member [{}]: {}", memberId, e.getMessage());
			requestLog.fail("Event publishing failed: " + e.getMessage());
		}

		return requestLog;
	}

}
