package com.demo.bankapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.demo.bankapp.model.Transaction;
import com.demo.bankapp.repository.TransactionRepository;
import com.demo.bankapp.service.implementation.TransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

	@Mock
	private TransactionRepository repository;

	private TransactionServiceImpl service;

	@BeforeEach
	public void setUp() {
		service = new TransactionServiceImpl(repository);
	}

	@Test
	public void createNewTransaction() {
		Transaction mockedTransaction = new Transaction(25681L, true, "TRY", BigDecimal.valueOf(61268));
		Mockito.when(repository.save(Mockito.any())).thenReturn(mockedTransaction);

		Transaction newTransaction = service.createNewTransaction(
				mockedTransaction.getUserId(),
				mockedTransaction.isBought(),
				mockedTransaction.getCurrency(),
				mockedTransaction.getAmount());

		assertThat(newTransaction.getAmount()).isEqualTo(mockedTransaction.getAmount());
		assertThat(newTransaction.getCurrency()).isEqualTo(mockedTransaction.getCurrency());
		assertThat(newTransaction.getUserId()).isEqualTo(mockedTransaction.getUserId());
		assertThat(newTransaction.isBought()).isEqualTo(mockedTransaction.isBought());
	}

	@Test
	public void getOperationCountFromLast24Hours() {
		// Given
		Long userId = 12161L;
		LocalDateTime cutoff = LocalDateTime.now().minusHours(24);

		Mockito.when(repository.getOperationCountFromLast24Hours(userId)).thenReturn(20);

		// When
		int operationCount = service.getOperationCountFromLast24Hours(userId);

		// Then
		assertThat(operationCount).isPositive();
		assertThat(operationCount).isEqualTo(20);
	}

	@Test
	public void findAllByUserId() {
		List<Transaction> transactionList = new ArrayList<>();
		Transaction mockedTransaction = new Transaction(61682L, true, "EUR", BigDecimal.valueOf(12661));
		transactionList.add(mockedTransaction);

		Mockito.when(repository.findAllByUserId(Mockito.any())).thenReturn(transactionList);

		List<Transaction> foundTransactionList = service.findAllByUserId(mockedTransaction.getUserId());

		assertThat(foundTransactionList).isEqualTo(transactionList);
	}
}
