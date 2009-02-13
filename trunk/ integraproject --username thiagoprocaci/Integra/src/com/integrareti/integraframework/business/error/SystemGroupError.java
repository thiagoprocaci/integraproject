package com.integrareti.integraframework.business.error;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.business.task.SystemGroupTask;

/**
 * This entity describes error that can occur during in any operation with group
 * 
 * @author Thiago Procaci
 * 
 */
@SuppressWarnings("serial")
public class SystemGroupError extends SystemError {

	private SystemGroupTask systemGroupTask;

	/**
	 * Creates a new SystemGroupError
	 */
	public SystemGroupError() {
		super();
	}

	/**
	 * Creates a new SystemGroupError
	 * 
	 * @param time
	 * @param cause
	 * @param systemGroupTask
	 */
	public SystemGroupError(String cause, String description,
			SystemGroupTask systemGroupTask) {
		super(cause, description);
		this.systemGroupTask = systemGroupTask;
	}

	/**
	 * 
	 * @return Returns systemGroupTask
	 */
	public SystemGroupTask getSystemGroupTask() {
		return systemGroupTask;
	}

	/**
	 * Sets systemGroupTask
	 * 
	 * @param systemGroupTask
	 */
	public void setSystemGroupTask(SystemGroupTask systemGroupTask) {
		this.systemGroupTask = systemGroupTask;
	}

	/**
	 * 
	 * @param group
	 * @return Returns a description of save group
	 */
	public static String getSaveGroupErrorDescription(Group group) {
		return "O grupo " + group.getName() + " nao pode ser salvo";
	}

	/**
	 * 
	 * @param group
	 * @return Returns a description of delete group
	 */
	public static String getDeleteGroupErrorDescription(Group group) {
		return "O grupo " + group.getName() + " nao pode ser excluido";
	}

	/**
	 * 
	 * @param group
	 * @param emailList
	 * @return Returns a description of delete an emailList from a group
	 */
	public static String getDeleteEmailListFromGroupErrorDescription(
			Group group, EmailList emailList) {
		return "A lista de email " + emailList.getName()
				+ " nao pode ser excluida do grupo " + group.getName();
	}

	/**
	 * 
	 * @param group
	 * @param emailList
	 * @return Returns a description of add an emailList to a group
	 */
	public static String getAddEmailListToGroupErrorDescription(Group group,
			EmailList emailList) {
		return "A lista de email " + emailList.getName()
				+ " nao pode ser adicionada ao grupo " + group.getName();
	}

	/**
	 * 
	 * @param group
	 * @param emailList
	 * @param person
	 * @return Returns a description of remove participant from an emailList of
	 *         a group
	 */
	public static String getRemoveParticipantFromGroupErrorDescription(
			Group group, EmailList emailList, Person person) {
		return "O participante " + person.getName()
				+ " nao pode ser removido da lista de email "
				+ emailList.getName() + " do grupo " + group.getName();
	}
	
	/**
	 * 
	 * @param group
	 * @param emailList
	 * @param person
	 * @return Returns a description of add participant to an emailList of
	 *         a group
	 */
	public static String getAddParticipantToGroupErrorDescription(
			Group group, EmailList emailList, Person person) {
		return "O participante " + person.getName()
				+ " nao pode ser adicionado da lista de email "
				+ emailList.getName() + " do grupo " + group.getName();
	}
	
	/**
	 * 
	 * @param group
	 * @return Returns a description of define group owners
	 */
	public static String getSaveGroupOwnersErrorDescription(Group group){
		return "Os responsaveis do grupo "+group.getName()+ " nao puderam ser salvos";
	}

}
