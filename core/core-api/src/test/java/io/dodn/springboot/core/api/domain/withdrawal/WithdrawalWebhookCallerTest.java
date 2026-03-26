package io.dodn.springboot.core.api.domain.withdrawal;

import static org.assertj.core.api.Assertions.assertThat;

import io.dodn.springboot.core.api.domain.withdrawal.dto.WebhookCallResult;
import io.dodn.springboot.core.api.domain.withdrawal.dto.WebhookValidationResponse;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalWebhookEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class WithdrawalWebhookCallerTest {

	private WithdrawalWebhookCaller caller;

	private MockRestServiceServer mockServer;

	private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules()
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	@BeforeEach
	public void setUp() {
		RestClient.Builder builder = RestClient.builder();
		mockServer = MockRestServiceServer.bindTo(builder).build();
		RestClient restClient = builder.build();
		caller = new WithdrawalWebhookCaller(restClient);
	}

	private WithdrawalWebhookEntity webhook(String systemName, String url) {
		return new WithdrawalWebhookEntity(systemName, url, "test", 5, 1);
	}

	@Test
	public void shouldReturnPassedWhenEligibleTrue() throws JsonProcessingException {
		String url = "http://test-system/validate";
		mockServer.expect(MockRestRequestMatchers.requestTo(url))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
			.andRespond(MockRestResponseCreators.withSuccess(
					objectMapper.writeValueAsString(new WebhookValidationResponse(true, null)),
					MediaType.APPLICATION_JSON));

		WebhookCallResult result = caller.call(webhook("TestSystem", url), 1L);

		assertThat(result.isPassed()).isTrue();
		assertThat(result.systemName()).isEqualTo("TestSystem");
		mockServer.verify();
	}

	@Test
	public void shouldReturnRejectedWhenEligibleFalse() throws JsonProcessingException {
		String url = "http://test-system/validate";
		mockServer.expect(MockRestRequestMatchers.requestTo(url))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
			.andRespond(MockRestResponseCreators.withSuccess(
					objectMapper.writeValueAsString(new WebhookValidationResponse(false, "Active subscription")),
					MediaType.APPLICATION_JSON));

		WebhookCallResult result = caller.call(webhook("TestSystem", url), 1L);

		assertThat(result.isPassed()).isFalse();
		assertThat(result.status()).isEqualTo(WebhookCallResult.Status.REJECTED);
		assertThat(result.reason()).isEqualTo("Active subscription");
		mockServer.verify();
	}

	@Test
	public void shouldReturnErrorWhenServerError() {
		String url = "http://test-system/validate";
		mockServer.expect(MockRestRequestMatchers.requestTo(url))
			.andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
			.andRespond(MockRestResponseCreators.withServerError());

		WebhookCallResult result = caller.call(webhook("TestSystem", url), 1L);

		assertThat(result.isPassed()).isFalse();
		assertThat(result.status()).isEqualTo(WebhookCallResult.Status.ERROR);
		mockServer.verify();
	}

	@Test
	public void shouldRetryAndSucceedOnSecondAttempt() throws JsonProcessingException {
		String url = "http://test-system/validate";
		WithdrawalWebhookEntity webhookWith2Retries = new WithdrawalWebhookEntity("TestSystem", url, "test", 5, 2);

		mockServer.expect(MockRestRequestMatchers.requestTo(url))
			.andRespond(MockRestResponseCreators.withServerError());
		mockServer.expect(MockRestRequestMatchers.requestTo(url))
			.andRespond(MockRestResponseCreators.withSuccess(
					objectMapper.writeValueAsString(new WebhookValidationResponse(true, null)),
					MediaType.APPLICATION_JSON));

		WebhookCallResult result = caller.call(webhookWith2Retries, 1L);

		assertThat(result.isPassed()).isTrue();
		mockServer.verify();
	}

	@Test
	public void shouldReturnErrorAfterAllRetriesFail() {
		String url = "http://test-system/validate";
		WithdrawalWebhookEntity webhookWith3Retries = new WithdrawalWebhookEntity("TestSystem", url, "test", 5, 3);

		mockServer.expect(MockRestRequestMatchers.requestTo(url))
			.andRespond(MockRestResponseCreators.withServerError());
		mockServer.expect(MockRestRequestMatchers.requestTo(url))
			.andRespond(MockRestResponseCreators.withServerError());
		mockServer.expect(MockRestRequestMatchers.requestTo(url))
			.andRespond(MockRestResponseCreators.withServerError());

		WebhookCallResult result = caller.call(webhookWith3Retries, 1L);

		assertThat(result.isPassed()).isFalse();
		assertThat(result.status()).isEqualTo(WebhookCallResult.Status.ERROR);
		mockServer.verify();
	}

}
