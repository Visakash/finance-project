package com.example.customer.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

	private Integer id;

	private String fname;
	private String lname;
	private String address;

	private String username;
	private String email;
	private String mobileNo;

	private LocalDate dob;

	private String country;
	private String state;
	private String city;
	private String zipcode;

	private boolean status;

	private LoginRequest loginDTO;
}