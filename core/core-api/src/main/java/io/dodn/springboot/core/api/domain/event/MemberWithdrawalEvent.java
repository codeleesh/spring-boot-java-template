package io.dodn.springboot.core.api.domain.event;

import java.time.LocalDateTime;

public record MemberWithdrawalEvent(Long memberId, LocalDateTime withdrawnAt) {

}
