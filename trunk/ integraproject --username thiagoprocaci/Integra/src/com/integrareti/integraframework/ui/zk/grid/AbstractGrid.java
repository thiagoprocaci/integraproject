package com.integrareti.integraframework.ui.zk.grid;


import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Grid;

/**
 * Abstract Grid
 * @author Thiago
 *
 */
public abstract class AbstractGrid extends Grid {
	
	/**
	 * Shows a warnning at a specified component 
	 * @param componentId
	 * @param warnning
	 */
	public void setWarnning(String componentId, String warnning){
		throw new WrongValueException(getFellow(componentId), warnning);
	}
	
	
}
