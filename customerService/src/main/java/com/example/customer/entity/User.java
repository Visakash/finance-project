package com.example.customer.entity;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "First name is required")
	@Column(nullable = false)
	private String fname;

	@NotBlank(message = "Last name is required")
	@Column(nullable = false)
	private String lname;

	@NotBlank(message = "Username is required")
	@Column(nullable = false, unique = true)
	private String username;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	@Column(nullable = false)
	private String password;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	@Column(nullable = false, unique = true)
	private String email;

	private String mobileNo;
	private LocalDate dob;

	private String address;
	private String country;
	private String state;
	private String city;
	private String zipcode;

	@Column(nullable = false)
	private boolean status = true;

	@Column(nullable = false)
	private Boolean deleted = false;

	private String createdBy;
	private String createdDate;
	private String updatedBy;
	private String updateDate;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "login_id")
	private Login login;
}