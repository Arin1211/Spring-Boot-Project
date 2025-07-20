package com.demo.bankapp.model;

import java.math.BigDecimal;
import java.util.Map;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Wealth {

	private @Id
	@GeneratedValue Long userId;

	@ElementCollection
	private Map<String, BigDecimal> wealthMap;

	public Wealth(Long userId, Map<String, BigDecimal> wealthMap) {
		this.userId = userId;
		this.wealthMap = wealthMap;
	}

}
