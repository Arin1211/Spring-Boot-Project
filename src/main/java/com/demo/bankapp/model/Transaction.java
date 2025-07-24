package com.demo.bankapp.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Transaction {

	@Id @GeneratedValue
	private Long id;

	private Long userId;
	private boolean isBought;
	private String currency;
	private BigDecimal amount;
	private Date transactionTime;

	public Transaction(Long userId, boolean isBought, String currency, BigDecimal amount) {
		this.userId = userId;
		this.isBought = isBought;
		this.currency = currency;
		this.amount = amount;
		this.transactionTime = new Date();
	}
}
