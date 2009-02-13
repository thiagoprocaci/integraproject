package com.integrareti.integraframework.valueobject;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.thread.GroupImportWorkingThread;

/**
 * Used to encapsulate all the details of the {@link Group}s import process
 * 
 * @created 17/02/2008
 * @author Thiago Athouguia Gama *
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ImportDetailsVO implements Serializable {
	private Map<String, GroupVO> successImport, errorImport;
	private Integer importSize;
	public GroupImportWorkingThread runningThread;
	public ExecutorService executorService;

	public ImportDetailsVO(Map<String, GroupVO> successImport, Map<String, GroupVO> errorImport, Integer importSize, ExecutorService executorService) {
		this.successImport = successImport;
		this.errorImport = errorImport;
		this.importSize = importSize;
		this.executorService = executorService;
	}

	public Map<String, GroupVO> getErrorImport() {
		return errorImport;
	}

	public Integer getImportSize() {
		return importSize;
	}

	public Map<String, GroupVO> getSuccessImport() {
		return successImport;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}
}
