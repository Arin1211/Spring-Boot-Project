package com.demo.bankapp.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.demo.bankapp.config.JwtTestUtils;
import com.demo.bankapp.config.TestSecurityConfig;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.demo.bankapp.config.TestUtils;
import com.demo.bankapp.model.User;
import com.demo.bankapp.model.Wealth;
import com.demo.bankapp.request.RetrieveWealthRequest;
import com.demo.bankapp.service.abstractions.UserService;
import com.demo.bankapp.service.abstractions.WealthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Import(TestSecurityConfig.class)
@WebMvcTest(value = WealthController.class)
public class WealthControllerTest {

	@MockBean
	private UserService userService;

	@MockBean
	private WealthService wealthService;

	@Autowired
	private MockMvc mockMvc;

	static Stream<RetrieveWealthRequest> retrieveWealthRequestProvider() {
		RetrieveWealthRequest request1 = new RetrieveWealthRequest();
		RetrieveWealthRequest request2 = new RetrieveWealthRequest();
		request2.setUsername("");
		RetrieveWealthRequest request3 = new RetrieveWealthRequest();
		request3.setUsername("Mert");

		return Stream.of(request1, request2, request3);
	}

	@ParameterizedTest
	@MethodSource("retrieveWealthRequestProvider")
	public void retrieveWealth(RetrieveWealthRequest request) throws Exception {

		boolean shouldThrowBadRequest = request.getUsername() == null || request.getUsername().equals("");

		Map<String, BigDecimal> mockedWealthMap = new HashMap<>();
		mockedWealthMap.put("TRY", BigDecimal.valueOf(58212));
		mockedWealthMap.put("EUR", BigDecimal.valueOf(5000));
		mockedWealthMap.put("USD", BigDecimal.valueOf(1000));

		Mockito.when(userService.findByUserName(Mockito.anyString())).thenReturn(new User(request.getUsername(), "mert123", "52812576921"));
		Mockito.when(wealthService.findWealth(Mockito.any())).thenReturn(new Wealth(5125L, mockedWealthMap));
		String token = JwtTestUtils.generateTestToken("Mert");
		String requestAsJson = new ObjectMapper().writeValueAsString(request);
		RequestBuilder requestBuilder = TestUtils.getPostRequestBuilderWithToken("/wealth/retrieve", requestAsJson, token);

		ResultActions resultActions = mockMvc.perform(requestBuilder);
		if(shouldThrowBadRequest) {
			resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
		} else {
			resultActions.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(jsonPath("$.wealth.userId", equalTo(5125)))
					.andExpect(jsonPath("$.wealth.wealthMap.TRY", equalTo(58212)))
					.andExpect(jsonPath("$.wealth.wealthMap.EUR", equalTo(5000)))
					.andExpect(jsonPath("$.wealth.wealthMap.USD", equalTo(1000)));
		}
	}

}
