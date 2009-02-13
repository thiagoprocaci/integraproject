package com.integrareti.integraframework.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.integrareti.integraframework.business.Domain;
import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.controller.adm.group.GroupImportController;
import com.integrareti.integraframework.valueobject.GroupVO;

/**
 * A working thread used to create {@link Group}s and {@link EmailList} both in
 * Integra and Google database
 * 
 * @created 17/02/2008
 * @author Thiago Athouguia Gama *
 * @version 1.0
 */
public class GroupImportWorkingThread extends Thread {

	private final GroupImportController groupImportController;
	private final List<GroupVO> groups;
	private final Domain domain;
	private final String year, semester;
	private final Map<String, GroupVO> errors, success;
	private final TransactionTemplate transactionTemplate;
	private boolean stop;

	public GroupImportWorkingThread(
			GroupImportController groupImportController, List<GroupVO> groups,
			Domain domain, String year, String semester,
			Map<String, GroupVO> success, Map<String, GroupVO> errors,
			TransactionTemplate transactionTemplate) {
		this.groupImportController = groupImportController;
		this.groups = groups;
		this.domain = domain;
		this.year = year;
		this.semester = semester;
		this.success = success;
		this.errors = errors;
		this.transactionTemplate = transactionTemplate;
	}

	public void stopThread() {
		stop = true;
	}

	@Override
	public void run() {
		int count = 1;
		boolean closeConnection = false;
		for (final GroupVO group : groups) {
			if (stop)
				break;
			if (count == groups.size())
				closeConnection = true;
			final boolean clConn = closeConnection;
			final Map<String, GroupVO> error = new HashMap<String, GroupVO>();
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(
						TransactionStatus status) {
					try {
						errors.putAll(groupImportController.createGroup(group,
								domain, year, semester, clConn));
					} catch (Exception ex) {
						error.put("Falha ao criar grupo", group);
						status.setRollbackOnly();
						ex.printStackTrace();
					}
				}
			});
			if (error.isEmpty())
				success.put("Grupo criado com sucesso.", group);
			else
				errors.putAll(error);
			count++;
		}
	}
}
