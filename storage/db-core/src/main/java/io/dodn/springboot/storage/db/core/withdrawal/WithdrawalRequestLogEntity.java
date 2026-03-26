package io.dodn.springboot.storage.db.core.withdrawal;

import io.dodn.springboot.storage.db.core.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "withdrawal_request_log")
public class WithdrawalRequestLogEntity extends BaseEntity {

	@Column(nullable = false)
	private Long memberId;

	@Column(nullable = false, length = 20)
	private String status;

	@Column(length = 1000)
	private String rejectReason;

	protected WithdrawalRequestLogEntity() {
	}

	public WithdrawalRequestLogEntity(Long memberId, String status) {
		this.memberId = memberId;
		this.status = status;
	}

	public Long getMemberId() {
		return memberId;
	}

	public String getStatus() {
		return status;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void updateStatus(String status) {
		this.status = status;
	}

	public void reject(String reason) {
		this.status = "REJECTED";
		this.rejectReason = reason;
	}

	public void fail(String reason) {
		this.status = "FAILED";
		this.rejectReason = reason;
	}

}
