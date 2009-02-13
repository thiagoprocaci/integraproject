package com.integrareti.integraframework.business.task;

import java.util.HashSet;
import java.util.Set;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.business.error.SystemGroupError;
import com.integrareti.integraframework.business.log.GroupLog;

/**
 * This entity describes a task that system have to do with group
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class SystemGroupTask extends SystemTask {

	// tasks names
	public static final String SAVE_GROUP = "Salvar grupo";
	public static final String DELETE_GROUP = "Excluir grupo";
	public static final String ADD_PARTICIPANTS_TO_GROUP = "Adicionar participante ao grupo";
	public static final String REMOVE_PARTICIPANT_FROM_GROUP = "Remover participante do groupo";
	public static final String ADD_EMAIL_LIST_TO_GROUP = "Adicionar/criar lista de email ao grupo";
	public static final String DELETE_EMAIL_LIST_FROM_GROUP = "Excluir lista de email do grupo ";
	public static final String SAVE_GROUP_OWNERS = "Definindo os responsaveis do grupo";

	private Set<SystemGroupError> errors;
	private GroupLog groupLog;

	/**
	 * Creates a new SystemGroupTask
	 * 
	 * @param name
	 * @param description
	 * @param errors
	 * @param groupLog
	 */
	public SystemGroupTask(String name, String description,
			Set<SystemGroupError> errors, GroupLog groupLog) {
		super(name, description);
		this.errors = errors;
		this.groupLog = groupLog;

	}

	/**
	 * Creates a new SystemGroupTask
	 * 
	 * @param name
	 * @param description
	 * @param errors
	 * @param groupLog
	 */
	public SystemGroupTask(String name, String description, GroupLog groupLog) {
		super(name, description);
		this.groupLog = groupLog;
		this.errors = new HashSet<SystemGroupError>(0);
	}

	/**
	 * Creates a new SystemGroupTask
	 */
	public SystemGroupTask() {
		errors = new HashSet<SystemGroupError>(0);
	}

	/**
	 * 
	 * @return Returns groupLog
	 */
	public GroupLog getGroupLog() {
		return groupLog;
	}

	/**
	 * Sets groupLog
	 * 
	 * @param groupLog
	 */
	public void setGroupLog(GroupLog groupLog) {
		this.groupLog = groupLog;
	}

	/**
	 * 
	 * @return Returns errors
	 */
	public Set<SystemGroupError> getErrors() {
		return errors;
	}

	/**
	 * Sets erros
	 * 
	 * @param errors
	 */
	public void setErrors(Set<SystemGroupError> errors) {
		this.errors = errors;
	}

	/**
	 * Adds a new systemError
	 * 
	 * @param error
	 */
	public void addError(SystemGroupError error) {
		this.errors.add(error);
	}

	/**
	 * Removes an systemError
	 * 
	 * @param error
	 */
	public void removeError(SystemGroupError error) {
		this.errors.remove(error);
	}

	/**
	 * Checks if has errors in this system task
	 * 
	 * @return true if has errors. Otherwise false
	 */
	public boolean hasSystemErrors() {
		if (this.errors == null)
			return false;
		if (this.errors.isEmpty())
			return false;
		return true;
	}

	/**
	 * 
	 * @param group
	 * @return Returns a description for task SAVE_GROUP
	 */
	public static String getSaveGroupDescription(Group group) {
		return "Salvar grupo " + group.getName();
	}

	/**
	 * 
	 * @param group
	 * @return Returns a description for task DELETE_GROUP
	 */
	public static String getDeleteGroupDescription(Group group) {
		return "Excluir grupo " + group.getName();
	}

	/**
	 * 
	 * @param group
	 * @param emailList
	 * @param person
	 * @return Returns a description for task ADD_PARTICIPANTS_TO_GROUP
	 */
	public static String getAddParticipantsToGroupDescription(Group group,
			EmailList emailList, Person person) {
		return "Adicionar participante " + person.getRegistry()
				+ " na lista de email " + emailList.getName() + " do grupo "
				+ group.getName();
	}
	
	/**
	 * 
	 * @param group
	 * @param emailList
	 * @param person
	 * @return Returns a description for task REMOVE_PARTICIPANT_FROM_GROUP
	 */
	public static String getRemoveParticipantFromGroupDescription(Group group,
			EmailList emailList, Person person){
		return "Remover participante "+person.getRegistry()+ " da lista de email "+emailList.getName()+" do grupo "+group.getName();	
	}
	
	/**
	 * 
	 * @param group
	 * @param emailList
	 * @return Returns a description for task ADD_EMAIL_LIST_TO_GROUP
	 */
	public static String getAddEmailListToGroupDescription(Group group, EmailList emailList){
		return "Adicionar a lista de email "+emailList.getName()+ " no grupo "+group.getName();
	}
	
	/**
	 * 
	 * @param group
	 * @param emailList
	 * @return Returns a description for task DELETE_EMAIL_LIST_FROM_GROUP
	 */
	public static String getDeleteListFromGroupDescription(Group group, EmailList emailList){
		return "Excluir a lista de email "+emailList.getName()+ " do grupo "+group.getName();
	}

	/**
	 * 
	 * @param group
	 * @return Returns a description for task DEFINE_GROUP_OWNERS
	 */
	public static String getSaveGroupOwnersDescription(Group group){
		return "Salvar os responsaveis do grupo "+group.getName();
	}
	
}
