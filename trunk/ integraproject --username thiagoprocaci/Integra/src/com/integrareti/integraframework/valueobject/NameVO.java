package com.integrareti.integraframework.valueobject;

import java.io.Serializable;

/**
 * This class describes a VO object
 * @author Thiago
 *
 */
@SuppressWarnings("serial")
public class NameVO implements Serializable{

	// do not modify the methods equal and hashCode
	
	private String name;
	private String registry;
	
	/**
	 * Creates a new NameVO
	 */
	public NameVO(){
		
	}
	
	/**
	 * Creates a new NameVO
	 * @param name
	 * @param registry
	 */
	public NameVO(String name,String registry){
		this.name = name;
		this.registry = registry;
	}

	/**
	 * 
	 * @return Returns name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return Returns registry
	 */
	public String getRegistry() {
		return registry;
	}

	/**
	 * Sets registry
	 * @param registry
	 */
	public void setRegistry(String registry) {
		this.registry = registry;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((registry == null) ? 0 : registry.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final NameVO other = (NameVO) obj;
		if (registry == null) {
			if (other.registry != null)
				return false;
		} else if (!registry.equals(other.registry))
			return false;
		return true;
	}
	
}
