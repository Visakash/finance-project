package com.example.customer.enumValue;

public enum EnumData {

	ACTIVE("A"), NONACTIVE("N");

	private final String value;

	EnumData(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
