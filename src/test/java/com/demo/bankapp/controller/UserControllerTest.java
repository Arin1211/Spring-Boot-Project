package com.demo.bankapp.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.demo.bankapp.config.JwtTestUtils;
import com.demo.bankapp.config.TestSecurityConfig;
import com.demo.bankapp.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.demo.bankapp.config.TestUtils;
import com.demo.bankapp.request.CreateUserRequest;
import com.demo.bankapp.service.abstractions.UserService;
import com.demo.bankapp.service.abstractions.WealthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Import(TestSecurityConfig.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

	@MockBean
	private UserService userService;

	@MockBean
	private WealthService wealthService;

	@Autowired
	private MockMvc mockMvc;

	static Stream<CreateUserRequest> createUserRequestProvider() {
		CreateUserRequest request1 = new CreateUserRequest();
		CreateUserRequest request2 = new CreateUserRequest();
		request2.setUsername("");
		CreateUserRequest request3 = new CreateUserRequest();
		request3.setUsername("Mert");
		CreateUserRequest request4 = new CreateUserRequest();
		request4.setUsername("Mert");
		request4.setPassword("");
		CreateUserRequest request5 = new CreateUserRequest();
		request5.setUsername("Mert");
		request5.setPassword("mert123");
		CreateUserRequest request6 = new CreateUserRequest();
		request6.setUsername("Mert");
		request6.setPassword("mert123");
		request6.setTcno("");
		CreateUserRequest request7 = new CreateUserRequest();
		request7.setUsername("Mert");
		request7.setPassword("mert123");
		request7.setTcno("52126");
		CreateUserRequest request8 = new CreateUserRequest();
		request8.setUsername("Mert");
		request8.setPassword("mert123");
		request8.setTcno("sfhashg");
		CreateUserRequest request9 = new CreateUserRequest();
		request9.setUsername("Mert");
		request9.setPassword("mert123");
		request9.setTcno("sfhashg");
		CreateUserRequest request10 = new CreateUserRequest();
		request10.setUsername("exist");
		request10.setPassword("mert123");
		request10.setTcno("12512262539");
		CreateUserRequest request11 = new CreateUserRequest();
		request11.setUsername("Mert");
		request11.setPassword("mert123");
		request11.setTcno("55555555555");
		CreateUserRequest request12 = new CreateUserRequest();
		request12.setUsername("Mert");
		request12.setPassword("mert123");
		request12.setTcno("51258693850");

		return Stream.of(request1, request2, request3, request4, request5, request6,
				request7, request8, request9, request10, request11, request12);
	}

	@ParameterizedTest
	@MethodSource("createUserRequestProvider")
	public void createUser(CreateUserRequest request) throws Exception {

		boolean shouldThrowBadRequest = request.getUsername() == null || request.getUsername().equals("") || request.getPassword() == null || request.getPassword().equals("") ||
				request.getTcno() == null || request.getTcno().length() != 11 || !Pattern.matches("[0-9]+", request.getTcno());
		boolean shouldThrowBadCredentials = false;

		if (request.getUsername() != null && request.getUsername().equals("exist")) {
			Mockito.when(userService.isUsernameExist(Mockito.anyString())).thenReturn(Boolean.TRUE);
			shouldThrowBadCredentials = true;
		}

		if (request.getTcno() != null && request.getTcno().equals("55555555555")) {
			Mockito.when(userService.isTcnoExist(Mockito.anyString())).thenReturn(Boolean.TRUE);
			shouldThrowBadCredentials = true;
		}

		User mockedUser = new User("Mert6", "12356", "52535128592");
		Mockito.when(userService.createNewUser(Mockito.any())).thenReturn(mockedUser);
		String requestAsJson = new ObjectMapper().writeValueAsString(request);
		String token = JwtTestUtils.generateTestToken("Mert");
		RequestBuilder requestBuilder = TestUtils.getPostRequestBuilderWithToken("/user/create", requestAsJson, token);


		ResultActions resultActions = mockMvc.perform(requestBuilder);
		if(shouldThrowBadRequest) {
			resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
		} else if (shouldThrowBadCredentials) {
			resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
		} else {
			resultActions.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(jsonPath("$.username", equalTo(mockedUser.getUsername())))
					.andExpect(jsonPath("$.tcno", equalTo(mockedUser.getTcno())));
		}
	}

	@Test
	public void findAll() throws Exception {

		List<User> userList = new ArrayList<>();
		User mockedUser = new User("Mert", "mert123", "51258692052");
		userList.add(mockedUser);

		Mockito.when(userService.findAll()).thenReturn(userList);

		RequestBuilder requestBuilder = TestUtils.getGetRequestBuilder("/user/find/all");
		mockMvc.perform(requestBuilder)
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.userList", hasSize(1)))
				.andExpect(jsonPath("$.userList.[0].username", equalTo(mockedUser.getUsername())));

	}

}
