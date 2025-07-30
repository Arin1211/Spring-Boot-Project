package com.demo.bankapp.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "app_user", schema = "bankschema")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;
	private String tcno;

	public User(String username, String password, String tcno) {
		this.username = username;
		this.password = password;
		this.tcno = tcno;
	}
}
