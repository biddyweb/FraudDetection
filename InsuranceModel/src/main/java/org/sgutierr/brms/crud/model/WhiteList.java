package org.sgutierr.brms.crud.model;


import java.util.ArrayList;
import java.util.List;

public class WhiteList {
	private List <String> accountNumbers = new ArrayList<String>();

	public List<String> getAccountNumbers() {
		return accountNumbers;
	}
	public void setAccountNumbers(List<String> accountNumbers) {
		this.accountNumbers = accountNumbers;
	}
}
