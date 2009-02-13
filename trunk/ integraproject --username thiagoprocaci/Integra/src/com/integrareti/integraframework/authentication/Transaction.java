package com.integrareti.integraframework.authentication;

/**
 * 
 * Describes a trasaction that a person can execute
 * @version 1.0
 * @created 02-Oct-2007 15:27:38
 */
public class Transaction {

	private Integer id;
	private String name;

	/**
	 * Creates a new transaction
	 */
	public Transaction(){

	}
	
	/**
	 * Creates a new transaction
	 * @param name
	 */
	public Transaction(String name){
		this.name = name;
	}

	/**
	 * 
	 * @return returns the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @returnreturns the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	

}