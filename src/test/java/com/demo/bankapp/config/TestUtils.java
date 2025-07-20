package com.demo.bankapp.config;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

public class TestUtils {

	public static RequestBuilder getPostRequestBuilderWithToken(String path, String requestBody, String token) {
		return MockMvcRequestBuilders
				.post(path)
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody)
				.header("Authorization", "Bearer " + token)
				.with(csrf());
	}

	public static RequestBuilder getGetRequestBuilder(String url) {
		String token = JwtTestUtils.generateTestToken("Mert");
		return MockMvcRequestBuilders
				.get(url)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + token);
	}

}
