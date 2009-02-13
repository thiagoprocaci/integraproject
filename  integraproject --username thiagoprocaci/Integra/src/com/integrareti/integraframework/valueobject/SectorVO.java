package com.integrareti.integraframework.valueobject;

import java.io.Serializable;

/**
 * This class describes a value object of Sector
 * 
 * @author Thiago
 * 
 */
@SuppressWarnings("serial")
public class SectorVO implements Serializable{

	private String fatherSector;
	private String sector;

	/**
	 * Creates a new sectorVO
	 * 
	 * @param fatherSector
	 * @param sector
	 */
	public SectorVO(String fatherSector, String sector) {
		this.fatherSector = fatherSector;
		this.sector = sector;
	}

	/**
	 * Creates a new sectorVO
	 */
	public SectorVO() {

	}

	/**
	 * 
	 * @return Returns fatherSector
	 */
	public String getFatherSector() {
		return fatherSector;
	}

	/**
	 * Sets fatherSector
	 * 
	 * @param fatherSector
	 */
	public void setFatherSector(String fatherSector) {
		this.fatherSector = fatherSector;
	}

	/**
	 * 
	 * @return Returns sector
	 */
	public String getSector() {
		return sector;
	}

	/**
	 * Sets sector
	 * 
	 * @param sector
	 */
	public void setSector(String sector) {
		this.sector = sector;
	}

}
