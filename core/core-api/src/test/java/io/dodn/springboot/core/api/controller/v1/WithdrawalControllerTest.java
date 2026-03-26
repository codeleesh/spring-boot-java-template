package io.dodn.springboot.core.api.controller.v1;

import static io.dodn.springboot.test.api.RestDocsUtils.requestPreprocessor;
import static io.dodn.springboot.test.api.RestDocsUtils.responsePreprocessor;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.dodn.springboot.core.api.domain.withdrawal.WithdrawalService;
import io.dodn.springboot.core.enums.WithdrawalStatus;
import io.dodn.springboot.storage.db.core.withdrawal.WithdrawalRequestLogEntity;
import io.dodn.springboot.test.api.RestDocsTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

public class WithdrawalControllerTest extends RestDocsTest {

	private WithdrawalService withdrawalService;

	private WithdrawalController controller;

	@BeforeEach
	public void setUp() {
		withdrawalService = mock(WithdrawalService.class);
		controller = new WithdrawalController(withdrawalService);
		mockMvc = mockController(controller);
	}

	@Test
	public void withdrawSuccess() throws Exception {
		WithdrawalRequestLogEntity log = new WithdrawalRequestLogEntity(1L, WithdrawalStatus.COMPLETED.name());
		when(withdrawalService.withdraw(eq(1L))).thenReturn(log);

		mockMvc
			.perform(post("/api/v1/members/{memberId}/withdraw", 1L).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("withdrawal-success", requestPreprocessor(), responsePreprocessor(),
					pathParameters(parameterWithName("memberId").description("Member ID")),
					responseFields(fieldWithPath("result").type(JsonFieldType.STRING).description("ResultType"),
							fieldWithPath("data.requestId").type(JsonFieldType.NULL)
								.description("Request Log ID"),
							fieldWithPath("data.status").type(JsonFieldType.STRING)
								.description("Withdrawal Status"),
							fieldWithPath("data.message").type(JsonFieldType.NULL)
								.description("Rejection reason if rejected"),
							fieldWithPath("error").type(JsonFieldType.NULL).ignored())));
	}

	@Test
	public void withdrawRejected() throws Exception {
		WithdrawalRequestLogEntity log = new WithdrawalRequestLogEntity(1L, WithdrawalStatus.REQUESTED.name());
		log.reject("OrderService: Active order exists");
		when(withdrawalService.withdraw(eq(1L))).thenReturn(log);

		mockMvc
			.perform(post("/api/v1/members/{memberId}/withdraw", 1L).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("withdrawal-rejected", requestPreprocessor(), responsePreprocessor(),
					pathParameters(parameterWithName("memberId").description("Member ID")),
					responseFields(fieldWithPath("result").type(JsonFieldType.STRING).description("ResultType"),
							fieldWithPath("data.requestId").type(JsonFieldType.NULL)
								.description("Request Log ID"),
							fieldWithPath("data.status").type(JsonFieldType.STRING)
								.description("Withdrawal Status"),
							fieldWithPath("data.message").type(JsonFieldType.STRING)
								.description("Rejection reason"),
							fieldWithPath("error").type(JsonFieldType.NULL).ignored())));
	}

}
