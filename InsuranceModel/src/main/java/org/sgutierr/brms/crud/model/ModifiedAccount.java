package org.sgutierr.brms.crud.model;


public class ModifiedAccount {
	public static final Integer ACTION_REMOVE = new Integer(0);
	public static final Integer ACTION_ADD = new Integer(1);
	public static final Integer LIST_BLACK = new Integer(0);
	public static final Integer LIST_WHITE = new Integer(1);
	
	private String accountNumber;
	private Integer action; /* 0-> Remove | 1-> Add */
	private Integer list; /* 0-> BlackList | 1-> WhiteList */

	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public Integer getAction() {
		return action;
	}
	public void setAction(Integer action) {
		this.action = action;
	}
	public Integer getList() {
		return list;
	}
	public void setList(Integer list) {
		this.list = list;
	}	
}
