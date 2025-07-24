package com.demo.bankapp.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demo.bankapp.exception.BadRequestException;
import com.demo.bankapp.exception.InsufficientFundsException;
import com.demo.bankapp.model.Wealth;
import com.demo.bankapp.repository.WealthRepository;
import com.demo.bankapp.service.implementation.WealthServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WealthServiceImplTest {

	@Mock
	private WealthRepository repository;

	private WealthServiceImpl service;

	private Wealth mockedWealth;
	private Long mockedUserId;

	@BeforeEach
	public void setUp() {
		// Create a fixed currency rates map.
		Map<String, Double> fixedRates = new HashMap<>();
		fixedRates.put("USD", 0.5);
		fixedRates.put("EUR", 0.8);
		fixedRates.put("TRY", 1.0);
		fixedRates.put("AUD", 1.1);

		// Instantiate service as a spy and stub getCurrencyRates()
		service = Mockito.spy(new WealthServiceImpl(repository));
		Mockito.doReturn(fixedRates).when(service).getCurrencyRates();

		Map<String, BigDecimal> mockedWealthMap = new HashMap<>();
		mockedWealthMap.put("USD", BigDecimal.valueOf(2500));
		mockedWealthMap.put("TRY", BigDecimal.valueOf(2000));
		mockedWealthMap.put("EUR", BigDecimal.valueOf(3000));
		mockedWealthMap.put("AUD", BigDecimal.ZERO);

		this.mockedUserId = 5125L;
		this.mockedWealth = new Wealth(mockedUserId, mockedWealthMap);

		when(repository.findById(mockedUserId)).thenReturn(Optional.of(mockedWealth));
	}

	@Test
	public void newWealthRecord() {
		// Add assertion or verification
		service.newWealthRecord(25161L);
		verify(repository, times(1)).save(any(Wealth.class));
	}

	@Test
	public void makeWealthExchange() {
		service.makeWealthExchange(mockedUserId, "USD", BigDecimal.valueOf(150), true);
		service.makeWealthExchange(mockedUserId, "USD", BigDecimal.valueOf(250), false);

		// Add verifications
		verify(repository, times(2)).save(any(Wealth.class));
	}

	@Test
	public void makeWealthExchange_InsufficientFunds_Sell() {
		assertThrows(InsufficientFundsException.class, () -> {
			service.makeWealthExchange(mockedUserId, "USD", BigDecimal.valueOf(3000), false);
		});
	}

	@Test
	public void makeWealthExchange_InsufficientFunds_Buy() {
		assertThrows(InsufficientFundsException.class, () -> {
			service.makeWealthExchange(mockedUserId, "USD", BigDecimal.valueOf(3000), true);
		});
	}

	@Test
	public void makeWealthExchange_InvalidCurrency() {
		assertThrows(BadRequestException.class, () -> {
			service.makeWealthExchange(mockedUserId, "XSD", BigDecimal.valueOf(250), false);
		});
	}

	@Test
	public void makeWealthTransaction() {
		service.makeWealthTransaction(mockedUserId, "EUR", BigDecimal.valueOf(2516), true);
		service.makeWealthTransaction(mockedUserId, "TRY", BigDecimal.valueOf(1000), false);

		// Add verifications
		verify(repository, times(2)).save(any(Wealth.class));
	}

	@Test
	public void makeWealthTransaction_InsufficientFunds() {
		assertThrows(InsufficientFundsException.class, () -> {
			service.makeWealthTransaction(mockedUserId, "TRY", BigDecimal.valueOf(5000), false);
		});
	}

	@Test
	public void makeWealthTransaction_InvalidCurrency() {
		assertThrows(BadRequestException.class, () -> {
			service.makeWealthTransaction(mockedUserId, "DTD", BigDecimal.valueOf(250), false);
		});
	}

	@Test
	public void findWealth() {
		Wealth result = service.findWealth(mockedUserId);

		assertNotNull(result);
		assertEquals(mockedUserId, result.getUserId());
		verify(repository, times(1)).findById(mockedUserId);
	}
}