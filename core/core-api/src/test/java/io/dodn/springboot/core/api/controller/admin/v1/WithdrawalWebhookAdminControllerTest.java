package io.dodn.springboot.core.api.controller.admin.v1;

import static io.dodn.springboot.test.api.RestDocsUtils.requestPreprocessor;
import static io.dodn.springboot.test.api.RestDocsUtils.responsePreprocessor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dodn.springboot.core.api.controller.admin.v1.request.WebhookActiveRequest;
import io.dodn.springboot.core.api.controller.admin.v1.request.WebhookCreateRequest;
import io.dodn.springboot.core.api.controller.admin.v1.request.WebhookUpdateRequest;
import io.dodn.springboot.core.api.domain.withdrawal.WithdrawalWebhookService;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalWebhookEntity;
import io.dodn.springboot.test.api.RestDocsTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.List;

public class WithdrawalWebhookAdminControllerTest extends RestDocsTest {

	private WithdrawalWebhookService webhookService;

	private WithdrawalWebhookAdminController controller;

	@BeforeEach
	public void setUp() {
		webhookService = mock(WithdrawalWebhookService.class);
		controller = new WithdrawalWebhookAdminController(webhookService);
		mockMvc = mockController(controller);
	}

	private WithdrawalWebhookEntity sampleEntity() {
		return new WithdrawalWebhookEntity("OrderService", "http://order-service/api/withdrawal/validate",
				"Order validation", 5, 3);
	}

	@Test
	public void createWebhook() throws Exception {
		when(webhookService.create(any(), any(), any(), eq(5), eq(3))).thenReturn(sampleEntity());

		mockMvc
			.perform(post("/admin/v1/withdrawal-webhooks").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
						new WebhookCreateRequest("OrderService",
								"http://order-service/api/withdrawal/validate", "Order validation", 5, 3))))
			.andExpect(status().isOk())
			.andDo(document("admin-webhook-create", requestPreprocessor(), responsePreprocessor(),
					requestFields(
							fieldWithPath("systemName").type(JsonFieldType.STRING).description("System name"),
							fieldWithPath("webhookUrl").type(JsonFieldType.STRING).description("Webhook URL"),
							fieldWithPath("description").type(JsonFieldType.STRING).description("Description"),
							fieldWithPath("timeoutSeconds").type(JsonFieldType.NUMBER)
								.description("Timeout in seconds"),
							fieldWithPath("maxRetryCount").type(JsonFieldType.NUMBER)
								.description("Max retry count")),
					responseFields(fieldWithPath("result").type(JsonFieldType.STRING).description("ResultType"),
							fieldWithPath("data.id").type(JsonFieldType.NULL).description("Webhook ID"),
							fieldWithPath("data.systemName").type(JsonFieldType.STRING)
								.description("System name"),
							fieldWithPath("data.webhookUrl").type(JsonFieldType.STRING)
								.description("Webhook URL"),
							fieldWithPath("data.description").type(JsonFieldType.STRING)
								.description("Description"),
							fieldWithPath("data.timeoutSeconds").type(JsonFieldType.NUMBER)
								.description("Timeout"),
							fieldWithPath("data.maxRetryCount").type(JsonFieldType.NUMBER)
								.description("Max retry"),
							fieldWithPath("data.active").type(JsonFieldType.BOOLEAN).description("Active"),
							fieldWithPath("data.createdAt").type(JsonFieldType.NULL).description("Created at"),
							fieldWithPath("data.updatedAt").type(JsonFieldType.NULL)
								.description("Updated at"),
							fieldWithPath("error").type(JsonFieldType.NULL).ignored())));
	}

	@Test
	public void listWebhooks() throws Exception {
		when(webhookService.findAll()).thenReturn(List.of(sampleEntity()));

		mockMvc.perform(get("/admin/v1/withdrawal-webhooks").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("admin-webhook-list", requestPreprocessor(), responsePreprocessor(),
					responseFields(fieldWithPath("result").type(JsonFieldType.STRING).description("ResultType"),
							fieldWithPath("data[].id").type(JsonFieldType.NULL).description("Webhook ID"),
							fieldWithPath("data[].systemName").type(JsonFieldType.STRING)
								.description("System name"),
							fieldWithPath("data[].webhookUrl").type(JsonFieldType.STRING)
								.description("Webhook URL"),
							fieldWithPath("data[].description").type(JsonFieldType.STRING)
								.description("Description"),
							fieldWithPath("data[].timeoutSeconds").type(JsonFieldType.NUMBER)
								.description("Timeout"),
							fieldWithPath("data[].maxRetryCount").type(JsonFieldType.NUMBER)
								.description("Max retry"),
							fieldWithPath("data[].active").type(JsonFieldType.BOOLEAN).description("Active"),
							fieldWithPath("data[].createdAt").type(JsonFieldType.NULL)
								.description("Created at"),
							fieldWithPath("data[].updatedAt").type(JsonFieldType.NULL)
								.description("Updated at"),
							fieldWithPath("error").type(JsonFieldType.NULL).ignored())));
	}

	@Test
	public void getWebhook() throws Exception {
		when(webhookService.findById(1L)).thenReturn(sampleEntity());

		mockMvc.perform(get("/admin/v1/withdrawal-webhooks/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("admin-webhook-get", requestPreprocessor(), responsePreprocessor(),
					pathParameters(parameterWithName("id").description("Webhook ID"))));
	}

	@Test
	public void updateWebhook() throws Exception {
		when(webhookService.update(eq(1L), any(), any(), any(), eq(10), eq(2))).thenReturn(sampleEntity());

		mockMvc
			.perform(put("/admin/v1/withdrawal-webhooks/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new WebhookUpdateRequest("OrderService",
						"http://order-service/api/withdrawal/validate", "Updated desc", 10, 2))))
			.andExpect(status().isOk())
			.andDo(document("admin-webhook-update", requestPreprocessor(), responsePreprocessor(),
					pathParameters(parameterWithName("id").description("Webhook ID")),
					requestFields(
							fieldWithPath("systemName").type(JsonFieldType.STRING).description("System name"),
							fieldWithPath("webhookUrl").type(JsonFieldType.STRING).description("Webhook URL"),
							fieldWithPath("description").type(JsonFieldType.STRING).description("Description"),
							fieldWithPath("timeoutSeconds").type(JsonFieldType.NUMBER)
								.description("Timeout in seconds"),
							fieldWithPath("maxRetryCount").type(JsonFieldType.NUMBER)
								.description("Max retry count"))));
	}

	@Test
	public void deleteWebhook() throws Exception {
		mockMvc.perform(delete("/admin/v1/withdrawal-webhooks/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("admin-webhook-delete", requestPreprocessor(), responsePreprocessor(),
					pathParameters(parameterWithName("id").description("Webhook ID"))));
	}

	@Test
	public void changeActive() throws Exception {
		WithdrawalWebhookEntity entity = sampleEntity();
		entity.changeActive(false);
		when(webhookService.changeActive(eq(1L), eq(false))).thenReturn(entity);

		mockMvc
			.perform(patch("/admin/v1/withdrawal-webhooks/{id}/active", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new WebhookActiveRequest(false))))
			.andExpect(status().isOk())
			.andDo(document("admin-webhook-change-active", requestPreprocessor(), responsePreprocessor(),
					pathParameters(parameterWithName("id").description("Webhook ID")),
					requestFields(fieldWithPath("active").type(JsonFieldType.BOOLEAN)
						.description("Active status"))));
	}

}
