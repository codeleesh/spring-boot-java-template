package io.dodn.springboot.core.api.controller.admin.v1;

import io.dodn.springboot.core.api.controller.admin.v1.request.WebhookActiveRequest;
import io.dodn.springboot.core.api.controller.admin.v1.request.WebhookCreateRequest;
import io.dodn.springboot.core.api.controller.admin.v1.request.WebhookUpdateRequest;
import io.dodn.springboot.core.api.controller.admin.v1.response.WebhookResponse;
import io.dodn.springboot.core.api.domain.withdrawal.WithdrawalWebhookService;
import io.dodn.springboot.core.api.support.response.ApiResponse;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalWebhookEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/v1/withdrawal-webhooks")
public class WithdrawalWebhookAdminController {

	private final WithdrawalWebhookService webhookService;

	public WithdrawalWebhookAdminController(WithdrawalWebhookService webhookService) {
		this.webhookService = webhookService;
	}

	@PostMapping
	public ApiResponse<WebhookResponse> create(@RequestBody WebhookCreateRequest request) {
		WithdrawalWebhookEntity entity = webhookService.create(request.systemName(), request.webhookUrl(),
				request.description(), request.timeoutSecondsOrDefault(), request.maxRetryCountOrDefault());
		return ApiResponse.success(WebhookResponse.from(entity));
	}

	@GetMapping
	public ApiResponse<List<WebhookResponse>> list() {
		List<WebhookResponse> responses = webhookService.findAll()
			.stream()
			.map(WebhookResponse::from)
			.toList();
		return ApiResponse.success(responses);
	}

	@GetMapping("/{id}")
	public ApiResponse<WebhookResponse> get(@PathVariable Long id) {
		return ApiResponse.success(WebhookResponse.from(webhookService.findById(id)));
	}

	@PutMapping("/{id}")
	public ApiResponse<WebhookResponse> update(@PathVariable Long id,
			@RequestBody WebhookUpdateRequest request) {
		WithdrawalWebhookEntity entity = webhookService.update(id, request.systemName(), request.webhookUrl(),
				request.description(), request.timeoutSeconds(), request.maxRetryCount());
		return ApiResponse.success(WebhookResponse.from(entity));
	}

	@DeleteMapping("/{id}")
	public ApiResponse<?> delete(@PathVariable Long id) {
		webhookService.delete(id);
		return ApiResponse.success();
	}

	@PatchMapping("/{id}/active")
	public ApiResponse<WebhookResponse> changeActive(@PathVariable Long id,
			@RequestBody WebhookActiveRequest request) {
		WithdrawalWebhookEntity entity = webhookService.changeActive(id, request.active());
		return ApiResponse.success(WebhookResponse.from(entity));
	}

}
