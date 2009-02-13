package com.integrareti.integraframework.thread;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.integrareti.integraframework.business.Domain;
import com.integrareti.integraframework.controller.adm.group.GroupImportController;
import com.integrareti.integraframework.valueobject.GroupVO;

/**
 * 
 * @author Thiago Athouguia Gama
 * 
 */
public class SingleGroupImportWorkingThread extends Thread {
	private final GroupImportController groupImportController;
	private final GroupVO group;
	private final Domain domain;
	private final String year, semester;
	private final Map<String, GroupVO> errors, success;
	private final TransactionTemplate transactionTemplate;
	private int count;

	/**
	 * 
	 * @param groupImportController
	 * @param group
	 * @param domain
	 * @param year
	 * @param semester
	 * @param success
	 * @param errors
	 * @param transactionTemplate
	 */
	public SingleGroupImportWorkingThread(GroupImportController groupImportController, GroupVO group, Domain domain, String year, String semester, Map<String, GroupVO> success, Map<String, GroupVO> errors, TransactionTemplate transactionTemplate) {
		this.groupImportController = groupImportController;
		this.group = group;
		this.domain = domain;
		this.year = year;
		this.semester = semester;
		this.success = success;
		this.errors = errors;
		this.transactionTemplate = transactionTemplate;
	}

	/**
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		final Map<String, GroupVO> error = new HashMap<String, GroupVO>();
		final Map<String, GroupVO> ex = new HashMap<String, GroupVO>();
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					ex.clear();
					ex.putAll(groupImportController.createGroup(group, domain, year, semester, true));
					errors.putAll(ex);
				} catch (Exception ex) {
					error.put("Falha ao criar grupo " + group.getSubjectName(), group);
					status.setRollbackOnly();
					ex.printStackTrace();
				}
			}
		});
		if (error.isEmpty() && ex.isEmpty())
			success.put("Grupo" + group.getSubjectName() + " criado com sucesso.", group);
		else
			errors.putAll(error);
		count++;
	}
}
