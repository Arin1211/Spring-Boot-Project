package com.demo.bankapp.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.demo.bankapp.config.JwtTestUtils;
import com.demo.bankapp.config.TestSecurityConfig;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
import com.demo.bankapp.model.Transaction;
import com.demo.bankapp.model.User;
import com.demo.bankapp.request.CreateTransactionRequest;
import com.demo.bankapp.request.FindAllTransactionsByUserRequest;
import com.demo.bankapp.service.abstractions.TransactionService;
import com.demo.bankapp.service.abstractions.UserService;
import com.demo.bankapp.service.abstractions.WealthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Import(TestSecurityConfig.class)
@WebMvcTest(value = TransactionController.class)
public class TransactionControllerTest {

	@MockBean
	private UserService userService;

	@MockBean
	private TransactionService transactionService;

	@MockBean
	private WealthService wealthService;

	@Autowired
	private MockMvc mockMvc;

	static Stream<Arguments> transactionTestDataProvider() {
		CreateTransactionRequest request2 = new CreateTransactionRequest();
		CreateTransactionRequest request3 = new CreateTransactionRequest();
		request3.setUsername("Mert");
		CreateTransactionRequest request4 = new CreateTransactionRequest();
		request4.setUsername("Mert");
		request4.setCurrency("TRY");
		CreateTransactionRequest request5 = new CreateTransactionRequest();
		request5.setUsername("Mert");
		request5.setCurrency("EUR");
		request5.setAmount(BigDecimal.TEN);
		CreateTransactionRequest request6 = new CreateTransactionRequest();
		request6.setUsername("Mert");
		request6.setCurrency("USD");
		request6.setAmount(BigDecimal.valueOf(1250));
		CreateTransactionRequest request7 = new CreateTransactionRequest();
		request7.setUsername("Mert");
		request7.setCurrency("TRY");
		request7.setAmount(BigDecimal.valueOf(1250));
		CreateTransactionRequest request8 = new CreateTransactionRequest();
		request8.setUsername("Mert");
		request8.setCurrency("EUR");
		request8.setAmount(BigDecimal.ZERO);
		CreateTransactionRequest request9 = new CreateTransactionRequest();
		request9.setUsername("Mert");
		request9.setCurrency("USD");
		request9.setAmount(BigDecimal.valueOf(-1));
		CreateTransactionRequest request10 = new CreateTransactionRequest();
		request10.setUsername("Mert");
		request10.setCurrency("");
		request10.setAmount(BigDecimal.valueOf(-1));
		CreateTransactionRequest request11 = new CreateTransactionRequest();
		request11.setUsername("");
		request11.setCurrency("XSD");

		FindAllTransactionsByUserRequest fatbuRequest1 = new FindAllTransactionsByUserRequest();
		FindAllTransactionsByUserRequest fatbuRequest2 = new FindAllTransactionsByUserRequest();
		fatbuRequest2.setUsername("");
		FindAllTransactionsByUserRequest fatbuRequest3 = new FindAllTransactionsByUserRequest();
		fatbuRequest3.setUsername("Mert");

		return Stream.of(
				Arguments.of(request2, fatbuRequest1),
				Arguments.of(request3, fatbuRequest2),
				Arguments.of(request4, fatbuRequest3),
				Arguments.of(request5, fatbuRequest3),
				Arguments.of(request6, fatbuRequest2),
				Arguments.of(request7, fatbuRequest1),
				Arguments.of(request8, fatbuRequest3),
				Arguments.of(request9, fatbuRequest3),
				Arguments.of(request10, fatbuRequest2),
				Arguments.of(request11, fatbuRequest1)
		);
	}

	@ParameterizedTest
	@MethodSource("transactionTestDataProvider")
	public void createTransaction(CreateTransactionRequest ctRequest, FindAllTransactionsByUserRequest fatbuRequest) throws Exception {
		boolean shouldThrowBadRequest = ctRequest == null || ctRequest.getUsername() == null || ctRequest.getUsername().equals("") ||
				ctRequest.getCurrency() == null || ctRequest.getCurrency().equals("") || ctRequest.getCurrency().equals("TRY") || ctRequest.getAmount() == null ||
				ctRequest.getAmount().signum() == 0 || ctRequest.getAmount().signum() == -1;

		boolean shouldThrowDailyOperationLimitExceeded = false;

		Transaction mockTransaction = new Transaction(250L, true, "USD", BigDecimal.TEN);

		int mockedOperationCount = ctRequest != null && ctRequest.getCurrency() != null && ctRequest.getCurrency().equals("EUR") ? 15 : 5;
		if(mockedOperationCount == 15) {
			shouldThrowDailyOperationLimitExceeded = true;
		}

		Mockito.when(userService.findByUserName(Mockito.anyString())).thenReturn(new User("Mert", "mert123", "22512567125"));
		Mockito.when(transactionService.getOperationCountFromLast24Hours(Mockito.any())).thenReturn(mockedOperationCount);
		Mockito.when(transactionService.createNewTransaction(Mockito.any(), Mockito.anyBoolean(), Mockito.anyString(), Mockito.any())).thenReturn(mockTransaction);

		String token = JwtTestUtils.generateTestToken("Mert");
		String requestAsJson = new ObjectMapper().writeValueAsString(ctRequest);
		RequestBuilder requestBuilder = TestUtils.getPostRequestBuilderWithToken("/transaction/create", requestAsJson, token);

		ResultActions resultActions = mockMvc.perform(requestBuilder);
		if (shouldThrowBadRequest) {
			resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
		} else if (shouldThrowDailyOperationLimitExceeded) {
			resultActions.andExpect(MockMvcResultMatchers.status().isUnauthorized());
		} else {
			resultActions.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(jsonPath("$.transaction.amount", equalTo(mockTransaction.getAmount().intValue())))
					.andExpect(jsonPath("$.transaction.currency", equalTo(mockTransaction.getCurrency())));
		}
	}

	@ParameterizedTest
	@MethodSource("transactionTestDataProvider")
	public void findAllTransactions(CreateTransactionRequest ctRequest, FindAllTransactionsByUserRequest fatbuRequest) throws Exception {
		List<Transaction> transactionList = new ArrayList<>();
		Transaction mockTransaction = new Transaction(63L, false, "EUR", BigDecimal.TEN);
		transactionList.add(mockTransaction);

		Mockito.when(userService.findByUserName(Mockito.anyString())).thenReturn(new User("Mert", "mert123", "22512567125"));
		Mockito.when(transactionService.findAllByUserId(Mockito.any())).thenReturn(transactionList);

		boolean shouldThrowBadRequest = fatbuRequest.getUsername() == null || fatbuRequest.getUsername().equals("");
		String token = JwtTestUtils.generateTestToken("Mert");
		String requestAsJson = new ObjectMapper().writeValueAsString(fatbuRequest);
		RequestBuilder requestBuilder = TestUtils.getPostRequestBuilderWithToken("/transaction/find/all", requestAsJson, token);

		ResultActions resultActions = mockMvc.perform(requestBuilder);
		if (shouldThrowBadRequest) {
			resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
		} else {
			resultActions.andExpect(MockMvcResultMatchers.status().isOk())
					.andExpect(jsonPath("$.transactionList", hasSize(1)))
					.andExpect(jsonPath("$.transactionList[0].currency", equalTo(mockTransaction.getCurrency())))
					.andExpect(jsonPath("$.transactionList[0].amount", equalTo(mockTransaction.getAmount().intValue())));
		}
	}
}
