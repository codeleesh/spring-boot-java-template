package io.dodn.springboot.core.api.controller.v1;

import io.dodn.springboot.core.api.controller.v1.response.WithdrawalResponse;
import io.dodn.springboot.core.api.domain.withdrawal.WithdrawalService;
import io.dodn.springboot.core.api.support.response.ApiResponse;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalRequestLogEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WithdrawalController {

	private final WithdrawalService withdrawalService;

	public WithdrawalController(WithdrawalService withdrawalService) {
		this.withdrawalService = withdrawalService;
	}

	@PostMapping("/api/v1/members/{memberId}/withdraw")
	public ApiResponse<WithdrawalResponse> withdraw(@PathVariable Long memberId) {
		WithdrawalRequestLogEntity result = withdrawalService.withdraw(memberId);
		return ApiResponse.success(WithdrawalResponse.from(result));
	}

}
