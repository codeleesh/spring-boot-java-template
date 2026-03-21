package io.dodn.springboot.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Tag("restdocs")
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocsTest {

    protected MockMvc mockMvc;

    protected final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);

    private RestDocumentationContextProvider restDocumentation;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.restDocumentation = restDocumentation;
    }

    protected MockMvc mockController(Object controller) {
        return MockMvcBuilders.standaloneSetup(controller)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .setMessageConverters(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(objectMapper))
            .build();
    }

}
