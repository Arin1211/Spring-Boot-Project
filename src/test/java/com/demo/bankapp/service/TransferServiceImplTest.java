package com.demo.bankapp.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.demo.bankapp.model.Transfer;
import com.demo.bankapp.repository.TransferRepository;
import com.demo.bankapp.service.implementation.TransferServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TransferServiceImplTest {

	@Mock
	private TransferRepository repository;

	private TransferServiceImpl service;

	@BeforeEach
	public void setUp() {
		service = new TransferServiceImpl(repository);
	}

	@Test
	public void createNewTransaction() {
		Transfer mockedTransfer = new Transfer(5816L, 2181L, "AUD", BigDecimal.valueOf(25125));
		Mockito.when(repository.save(Mockito.any())).thenReturn(mockedTransfer);

		Transfer createdTransfer = service.createNewTransfer(mockedTransfer);

		assertThat(createdTransfer.getAmount()).isEqualTo(mockedTransfer.getAmount());
		assertThat(createdTransfer.getCurrency()).isEqualTo(mockedTransfer.getCurrency());
		assertThat(createdTransfer.getFromUserId()).isEqualTo(mockedTransfer.getFromUserId());
		assertThat(createdTransfer.getToUserId()).isEqualTo(mockedTransfer.getToUserId());
	}

	@Test
	public void findAllTransfersFrom24Hours() {
		List<Transfer> mockedTransferList = new ArrayList<>();
		Transfer mockedTransfer = new Transfer(5816L, 2181L, "AUD", BigDecimal.valueOf(25125));
		mockedTransferList.add(mockedTransfer);

		Long userId = mockedTransfer.getFromUserId();
		Date cutoff = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24));

		Mockito.when(repository.findAllTransfersFrom24Hours(userId, cutoff)).thenReturn(mockedTransferList);

		List<Transfer> foundTransferList = service.findAllTransfersFrom24Hours(userId);

		assertThat(foundTransferList).isEqualTo(mockedTransferList);
	}
}
