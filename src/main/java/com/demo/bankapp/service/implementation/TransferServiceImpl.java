package com.demo.bankapp.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.bankapp.model.Transfer;
import com.demo.bankapp.repository.TransferRepository;
import com.demo.bankapp.service.abstractions.TransferService;

@Service
public class TransferServiceImpl implements TransferService {

	private TransferRepository repository;

	@Autowired
	public TransferServiceImpl(TransferRepository repository) {
		this.repository = repository;
	}

	@Override
	public Transfer createNewTransfer(Transfer transfer) {
		return repository.save(transfer);
	}

	@Override
	public List<Transfer> findAllTransfersFrom24Hours(Long userId) {
		return repository.findAllTransfersFrom24Hours(userId);
	}

}
