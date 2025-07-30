package com.demo.bankapp.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "wealth", schema = "bankschema")
@NoArgsConstructor
public class Wealth {

	@Id
	private Long userId; // Remove @GeneratedValue

	@ElementCollection
	@CollectionTable(
			name = "wealth_map",
			schema = "bankschema",
			joinColumns = @JoinColumn(name = "user_id")
	)
	@MapKeyColumn(name = "wealth_type")
	@Column(name = "amount", precision = 19, scale = 2)
	private Map<String, BigDecimal> wealthMap = new HashMap<>();

	public Wealth(Long userId, Map<String, BigDecimal> wealthMap) {
		this.userId = userId;
		this.wealthMap = wealthMap != null ? wealthMap : new HashMap<>();
	}

	public Wealth(Long userId) {
		this.userId = userId;
		this.wealthMap = new HashMap<>();
		this.wealthMap.put("balance", BigDecimal.ZERO);
		this.wealthMap.put("savings", BigDecimal.ZERO);
		this.wealthMap.put("investments", BigDecimal.ZERO);
	}
}
