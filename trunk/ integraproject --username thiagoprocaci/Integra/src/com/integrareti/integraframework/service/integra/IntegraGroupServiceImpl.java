package com.integrareti.integraframework.service.integra;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.integrareti.integraframework.business.EmailList;
import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.business.error.SystemGroupError;
import com.integrareti.integraframework.business.log.GroupLog;
import com.integrareti.integraframework.business.task.SystemGroupTask;
import com.integrareti.integraframework.dao.integra.GroupDao;
import com.integrareti.integraframework.dao.integra.GroupDaoJDBC;
import com.integrareti.integraframework.dao.integra.GroupLogDao;
import com.integrareti.integraframework.exceptions.ExceptionUtil;
import com.integrareti.integraframework.service.google.GoogleEmailListServiceInterface;

/**
 * This class offers services to manipulates groups at integra database
 * 
 * @author Thiago Baesso Procaci
 * 
 */
public class IntegraGroupServiceImpl extends IntegraServiceImpl<Group, Integer>
		implements IntegraGroupServiceInterface {

	private GroupDao groupDao;
	private GroupDao groupDaoJDBC;
	private GoogleEmailListServiceInterface googleEmailListServiceInterface;
	private TransactionTemplate transactionTemplate;
	private GroupLogDao groupLogDao;

	/**
	 * Creates a new IntegraGroupServiceImpl
	 * 
	 * @param groupDao
	 */
	public IntegraGroupServiceImpl(GroupDao groupDao,
			GoogleEmailListServiceInterface googleEmailListServiceInterface) {
		super(groupDao);
		this.groupDao = groupDao;
		this.googleEmailListServiceInterface = googleEmailListServiceInterface;
	}

	/**
	 * 
	 * @return Returns transactionTemplate
	 */
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	/**
	 * Sets transactionTemplate
	 * 
	 * @param transactionTemplate
	 */
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	/**
	 * 
	 * @return Returns groupLogDao
	 */
	public GroupLogDao getGroupLogDao() {
		return groupLogDao;
	}

	/**
	 * Sets groupLogDao
	 * 
	 * @param groupLogDao
	 */
	public void setGroupLogDao(GroupLogDao groupLogDao) {
		this.groupLogDao = groupLogDao;
	}

	/**
	 * Adds participants to group and email list
	 * 
	 * @param group
	 * @param addedPeople
	 * @return Returns a map of errors <String (error description),Group
	 *         (object)>
	 */
	@Override
	public Map<String, Group> addParticipantToGroupAndEmailList(Group group,
			Set<Person> addedPeople) {
		Map<String, Group> map = new HashMap<String, Group>(0);
		// log
		final GroupLog groupLog = new GroupLog(group.getName(), group
				.getDescription(), group.getDomain());
		transactionTemplate
				.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		Map<String, Group> m = addParticipantToGroupAndEmailList(group,
				addedPeople, groupLog);
		if (!m.isEmpty())
			map.putAll(m);
		transactionTemplate
				.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		try {
			// saving log
			groupLogDao.save(groupLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Creates more email lists to a group
	 * 
	 * @param group
	 * @param addedPeople
	 * @return Returns a map of errors <String (error description),Group
	 *         (object)>
	 */
	@Override
	public Map<String, Group> createEmailListToGroup(Group group,
			Set<Person> addedPeople) {
		Map<String, Group> map = new HashMap<String, Group>(0);
		// log
		final GroupLog groupLog = new GroupLog(group.getName(), group
				.getDescription(), group.getDomain());
		transactionTemplate
				.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		Map<String, Group> m = createEmailListToGroup(group, addedPeople,
				groupLog);
		if (!m.isEmpty())
			map.putAll(m);
		transactionTemplate
				.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		try {
			// saving log
			groupLogDao.save(groupLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Removes participants from group and email list
	 * 
	 * @param group
	 * @param removedPeople
	 * @return Returns a map of errors <String (error description),Group
	 *         (object)>
	 */
	@Override
	public Map<String, Group> removeParticipantFromGroupAndEmailList(
			Group group, Set<Person> removedPeople) {
		Map<String, Group> map = new HashMap<String, Group>(0);
		// log
		final GroupLog groupLog = new GroupLog(group.getName(), group
				.getDescription(), group.getDomain());
		transactionTemplate
				.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRED);
		Map<String, Group> m = removeParticipantFromGroupAndEmailList(group,
				removedPeople, groupLog);
		if (!m.isEmpty())
			map.putAll(m);
		transactionTemplate
				.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		try {
			// saving log
			groupLogDao.save(groupLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Merge group
	 * 
	 * @return merged group
	 */
	@Override
	public Group mergeGroup(Group group) throws Exception {
		group = (Group) groupDao.getHibernateSession().merge(
				groupDao.getById(group.getId()));
		group.getParticipants();
		return group;
	}

	/**
	 * 
	 * @param groupName
	 * @return Returns a list of groups by name
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupByName(String groupName) throws Exception {
		return groupDao.getGroupByName(groupName);
	}

	/**
	 * 
	 * @param groupName
	 * @param domainName
	 * @return Returns a group by a name and domain name
	 * @throws Exception
	 */
	@Override
	public Group getGroupByNameAndDomainName(String groupName, String domainName)
			throws Exception {
		return groupDao.getGroupByNameAndDomainName(groupName, domainName);
	}

	/**
	 * 
	 * @param groupName
	 * @return Returns the group id by name - using hibernate
	 * @throws Exception
	 */
	@Override
	public Integer getGroupIdByName(String groupName) throws Exception {
		return groupDao.getGroupIdByName(groupName);
	}

	/**
	 * 
	 * @param owner
	 * @return Returns the groups of a owner
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByOwner(Person owner) throws Exception {
		return groupDao.getGroupsByOwner(owner);
	}

	/**
	 * 
	 * @param participant
	 * @return Returns the groups of a person (participant)
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByParticipant(Person participant)
			throws Exception {
		return groupDao.getGroupsByParticipant(participant);
	}
	
	/**
	 * 
	 * @param groupName
	 * @return Returns the group id by name - using JDBC
	 * @throws Exception
	 */
	@Override
	public Integer getGroupIdByName(String groupName,
			boolean stillOpenConnection) throws Exception {
		if (!isConnectionOpenJDBC())
			openConnection();
		Integer id = groupDaoJDBC.getGroupIdByName(groupName);
		if (!stillOpenConnection)
			closeConnection();
		return id;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns all groups without email list by domain name
	 * @throws Exception
	 * @throws DataAccessException
	 */
	@Override
	public List<Group> getAllGroupsWithoutEmailListByDomainName(
			String domainName) throws Exception {
		return groupDao.getAllGroupsWithoutEmailListByDomainName(domainName);
	}

	/**
	 * 
	 * @param description
	 * @return Returns groups by description
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByDescription(String description)
			throws Exception {
		return groupDao.getGroupsByDescription(description);
	}

	/**
	 * 
	 * @param names
	 * @return Returns groups by pieces of name - using like primitive
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByPiecesOfNames(List<String> names)
			throws Exception {
		return groupDao.getGroupsByPiecesOfNames(names);
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @return Returns groups by description end name (using like)
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByDescriptionAndName(String description,
			String name) throws Exception {
		return groupDao.getGroupsByDescriptionAndName(description, name);
	}

	/**
	 * 
	 * @param description
	 * @param name
	 * @return Returns groups by description end name (using like)
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByDescriptionAndName(String description,
			List<String> names) throws Exception {
		return groupDao.getGroupsByDescriptionAndName(description, names);
	}

	/**
	 * Deletes a group
	 * 
	 * @param group
	 * @return Returns a map of errors <String (error description),Group (object
	 *         value)>
	 */
	@Override
	public Map<String, Group> deleteGroupWithEmailLists(final Group group) {
		// log
		final GroupLog groupLog = new GroupLog(group.getName(), group
				.getDescription(), group.getDomain());
		// return type
		final Map<String, Group> map = new HashMap<String, Group>();
		for (final EmailList emailList : group.getEmailLists()) {
			// defining the current task
			final SystemGroupTask task = new SystemGroupTask(
					SystemGroupTask.DELETE_EMAIL_LIST_FROM_GROUP,
					SystemGroupTask.getDeleteListFromGroupDescription(group,
							emailList), groupLog);
			groupLog.addTask(task);
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(
						TransactionStatus status) {
					try {
						googleEmailListServiceInterface
								.deleteEmailListOnlyAtIntegra(emailList);
						googleEmailListServiceInterface
								.deleteEmailListOnlyAtGoogle(emailList);
					} catch (Exception ex) {
						map.put("Group: " + group.getName()
								+ " - can not delete group.emailList : "
								+ emailList.getName(), group);
						// error log
						task.addError(new SystemGroupError(ExceptionUtil
								.formatMessage(ex), SystemGroupError
								.getDeleteEmailListFromGroupErrorDescription(
										group, emailList), task));
						status.setRollbackOnly();
						ex.printStackTrace();
					}
				}
			});
		}
		if (map.isEmpty()) {
			// defining task
			SystemGroupTask task = new SystemGroupTask(
					SystemGroupTask.DELETE_GROUP, SystemGroupTask
							.getDeleteGroupDescription(group), groupLog);
			groupLog.addTask(task);
			try {
				super.delete(group);
			} catch (Exception e) {
				map
						.put("Group: " + group.getName()
								+ " - can not delete group : "
								+ group.getName(), group);
				// error log
				task.addError(new SystemGroupError(ExceptionUtil
						.formatMessage(e), SystemGroupError
						.getDeleteGroupErrorDescription(group), task));
				e.printStackTrace();
			}
		}
		try {
			groupLogDao.save(groupLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 
	 * @param domainName
	 * @param first
	 * @param offset
	 * @return Returns a page of groups by domain name
	 * @throws Exception
	 */
	@Override
	public List<Group> getPageByDomainName(String domainName, int first,
			int offset) throws Exception {
		return groupDao.getPageByDomainName(domainName, first, offset);
	}

	/**
	 * 
	 * @param domainName
	 * @param first
	 * @param offset
	 * @return Returns a page of groups without emaillist by domain
	 * @throws Exception
	 */
	@Override
	public List<Group> getPageOfGroupsWithoutEmailListByDomainName(
			String domainName, int first, int offset) throws Exception {
		return groupDao.getPageOfGroupsWithoutEmailListByDomainName(domainName,
				first, offset);
	}

	/**
	 * 
	 * @param domain
	 * @return Returns the number of groups by domain name
	 * @throws Exception
	 */
	@Override
	public Long countGroupsByDomainName(String domain) throws Exception {
		return groupDao.countGroupsByDomainName(domain);
	}

	/**
	 * 
	 * 
	 * @param clue
	 * @return Returns groups by any clue (using like)
	 * @throws Exception
	 */
	@Override
	public List<Group> getGroupsByClue(String clue) throws Exception {
		return groupDao.getGroupsByClue(clue);
	}

	/**
	 * 
	 * @param domain
	 * @return Returns the number of groups without emaillist by domain name
	 * @throws Exception
	 */
	@Override
	public Long countGroupsWithoutEmailListByDomainName(String domain)
			throws Exception {
		return groupDao.countGroupsWithoutEmailListByDomainName(domain);
	}

	/**
	 * 
	 * @return Returns groupDaoJDBC
	 */
	public GroupDao getGroupDaoJDBC() {
		return groupDaoJDBC;
	}

	/**
	 * Sets groupDaoJDBC
	 * 
	 * @param groupDaoJDBC
	 */
	public void setGroupDaoJDBC(GroupDao groupDaoJDBC) {
		this.groupDaoJDBC = groupDaoJDBC;
	}

	/**
	 * Open integra database connection -using JDBC
	 * 
	 * @throws Exception
	 */
	public void openConnection() throws Exception {
		((GroupDaoJDBC) groupDaoJDBC).openConnection();
	}

	/**
	 * Close integra database connection - -using JDBC
	 * 
	 * @throws Exception
	 */
	public void closeConnection() throws Exception {
		((GroupDaoJDBC) groupDaoJDBC).closeConnection();
	}

	/**
	 * Saves a group and updates the email lists if necessary
	 * 
	 * @param group
	 * @throws Exception
	 */
	@Override
	public Map<String, Group> saveGroupAndUpDateEmailLists(final Group group,
			final Set<Person> addedPeople, final Set<Person> removedPeople,
			final Set<Person> addOwners, final Set<Person> removedOwners)
			throws Exception {
		// log
		final GroupLog groupLog = new GroupLog(group.getName(), group
				.getDescription(), group.getDomain());
		transactionTemplate
				.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		// this variables will help to build the method`s return
		List<Map<String, Group>> listMapErros = new ArrayList<Map<String, Group>>();
		Map<String, Group> map = new HashMap<String, Group>();

		// checking basics datas
		if (group == null)
			throw new Exception("The group must be not null");
		if (addedPeople == null || removedPeople == null || addOwners == null
				|| removedOwners == null)
			throw new Exception("All sets must be not null");

		// when is a new group without any emailList or recipients
		if (addedPeople.isEmpty() && removedPeople.isEmpty()
				&& addOwners.isEmpty() && removedOwners.isEmpty()) {
			// adding a new task for log
			SystemGroupTask task = new SystemGroupTask(
					SystemGroupTask.SAVE_GROUP, SystemGroupTask
							.getSaveGroupDescription(group), groupLog);
			groupLog.addTask(task);
			try {
				groupDao.save(group);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("The Group " + group.getName() + " could not be saved",
						group);
				// getting the error log for the task
				task.addError(new SystemGroupError(ExceptionUtil
						.formatMessage(e), SystemGroupError
						.getSaveGroupErrorDescription(group), task));
			}
			try {
				// saving log
				groupLogDao.save(groupLog);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return map;
		}
		if (!addOwners.isEmpty())
			addedPeople.addAll(addOwners);
		Map<String, Group> mapAux = null;
		// removing participants
		mapAux = removeParticipantFromGroupAndEmailList(group, removedPeople,
				groupLog);
		if (!mapAux.isEmpty())
			listMapErros.add(mapAux);
		// creating email lists if necessary
		if (listMapErros.isEmpty()) {
			mapAux = createEmailListToGroup(group, addedPeople, groupLog);
			if (!mapAux.isEmpty())
				listMapErros.add(mapAux);
		}
		// adding new participants
		if (listMapErros.isEmpty()) {
			mapAux = addParticipantToGroupAndEmailList(group, addedPeople,
					groupLog);
			if (!mapAux.isEmpty())
				listMapErros.add(mapAux);
		}
		// defining owners
		if (listMapErros.isEmpty()) {
			for (Person person : addOwners)
				group.addOwner(person);
			for (Person person : removedOwners)
				group.removerOwner(person);
			SystemGroupTask task = new SystemGroupTask(
					SystemGroupTask.SAVE_GROUP_OWNERS, SystemGroupTask
							.getSaveGroupDescription(group), groupLog);
			groupLog.addTask(task);
			try {
				groupDao.getHibernateSession().merge(group);
			} catch (Exception e) {
				Map<String, Group> m = new HashMap<String, Group>();
				m.put(
						"The group owners could not be saved: "
								+ group.getName(), group);
				task.addError(new SystemGroupError(ExceptionUtil
						.formatMessage(e), SystemGroupError
						.getSaveGroupOwnersErrorDescription(group), task));
				listMapErros.add(m);
			}
		}

		try {
			// saving log
			groupLogDao.save(groupLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Map<String, Group> map2 : listMapErros)
			if (!map2.isEmpty())
				map.putAll(map2);
		return map;
	}

	/**
	 * Creates more email lists to a group
	 * 
	 * @param group
	 * @param addedPeople
	 * @return Returns a map of errors <String (error description),Group
	 *         (object)>
	 */
	private Map<String, Group> createEmailListToGroup(final Group group,
			final Set<Person> addedPeople, final GroupLog groupLog) {
		final Map<String, Group> errors = new HashMap<String, Group>();
		int aux = group.getEmailLists().size();
		int numberOfEmailLists = group.getNumberOfNeededEmailLists(addedPeople
				.size());
		for (int i = 0; i < numberOfEmailLists; i++) {
			final EmailList emailList = new EmailList();
			emailList.setDomain(group.getDomain());
			emailList.setGroup(group);
			aux++;
			if (aux == 1)
				emailList.setName(group.getName());
			else
				emailList.setName(group.getName() + aux);
			group.addEmailList(emailList);
			final SystemGroupTask task = new SystemGroupTask(
					SystemGroupTask.ADD_EMAIL_LIST_TO_GROUP,
					SystemGroupTask.getAddEmailListToGroupDescription(group,
							emailList), groupLog);
			groupLog.addTask(task);
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				protected void doInTransactionWithoutResult(
						TransactionStatus status) {
					try { // saving at both database
						googleEmailListServiceInterface
								.saveOnlyAtIntegra(emailList);
						groupDao.save(group);
						googleEmailListServiceInterface
								.saveEmailListOnlyAtGoogle(emailList);
					} catch (Exception ex) {
						errors.put("Group: " + group.getName()
								+ " - Emaillist  " + emailList.getName()
								+ " could not be created", group);
						task.addError(new SystemGroupError(ExceptionUtil
								.formatMessage(ex), SystemGroupError
								.getAddEmailListToGroupErrorDescription(group,
										emailList), task));
						status.setRollbackOnly();
						ex.printStackTrace();
					}
				}
			});
		}
		return errors;
	}

	/**
	 * Removes participants from group and email list
	 * 
	 * @param group
	 * @param removedPeople
	 * @return Returns a map of errors <String (error description),Group
	 *         (object)>
	 */
	private Map<String, Group> removeParticipantFromGroupAndEmailList(
			final Group group, final Set<Person> removedPeople,
			final GroupLog groupLog) {
		final Map<String, Group> errors = new HashMap<String, Group>();
		if (removedPeople != null && !removedPeople.isEmpty()) {
			for (Iterator<Person> iterator = removedPeople.iterator(); iterator
					.hasNext();) {
				final Person person = (Person) iterator.next();
				if (group.getParticipants().contains(person)) {
					group.getParticipants().remove(person);
					group.getOwners().remove(person);
					for (final EmailList emailList : group.getEmailLists()) {
						if (emailList.getRecipients().contains(person)) {
							final SystemGroupTask task = new SystemGroupTask(
									SystemGroupTask.REMOVE_PARTICIPANT_FROM_GROUP,
									SystemGroupTask
											.getRemoveParticipantFromGroupDescription(
													group, emailList, person),
									groupLog);
							groupLog.addTask(task);
							transactionTemplate
									.execute(new TransactionCallbackWithoutResult() {
										protected void doInTransactionWithoutResult(
												TransactionStatus status) {
											try { // saving at both database
												googleEmailListServiceInterface
														.saveOnlyAtIntegra(emailList);
												groupDao.getHibernateSession()
														.merge(group);
												googleEmailListServiceInterface
														.removeRecipient(
																emailList,
																person);
											} catch (Exception ex) {
												errors
														.put(
																"Group: "
																		+ group
																				.getName()
																		+ "- remove participant error - EmailList: "
																		+ emailList
																				.getName()
																		+ " Recipient: "
																		+ person
																				.getRegistry(),
																group);
												task
														.addError(new SystemGroupError(
																ExceptionUtil
																		.formatMessage(ex),
																SystemGroupError
																		.getRemoveParticipantFromGroupErrorDescription(
																				group,
																				emailList,
																				person),
																task));
												status.setRollbackOnly();
												ex.printStackTrace();
											}
										}
									});
							iterator.remove();
							break;
						}
					}
				}
			}
		}
		return errors;
	}

	/**
	 * Adds participants to group and email list
	 * 
	 * @param group
	 * @param addedPeople
	 * @return Returns a map of errors <String (error description),Group
	 *         (object)>
	 */
	private Map<String, Group> addParticipantToGroupAndEmailList(
			final Group group, final Set<Person> addedPeople,
			final GroupLog groupLog) {
		final Map<String, Group> errors = new HashMap<String, Group>();
		if (addedPeople != null && !addedPeople.isEmpty()) {
			for (Iterator<Person> iterator = addedPeople.iterator(); iterator
					.hasNext();) {
				final Person person = (Person) iterator.next();
				int sizeBefore = group.getParticipants().size();
				group.getParticipants().add(person);
				int sizeAfter = group.getParticipants().size();
				if (sizeBefore == sizeAfter)
					iterator.remove();
				else { // saving at both database
					for (final EmailList emailList : group.getEmailLists()) {
						if (emailList.getRecipients().size() < EmailList.LIMIT_RECIPIENTS) {
							final SystemGroupTask task = new SystemGroupTask(
									SystemGroupTask.ADD_PARTICIPANTS_TO_GROUP,
									SystemGroupTask
											.getAddParticipantsToGroupDescription(
													group, emailList, person),
									groupLog);
							groupLog.addTask(task);
							transactionTemplate
									.execute(new TransactionCallbackWithoutResult() {
										protected void doInTransactionWithoutResult(
												TransactionStatus status) {
											try { // saving at both database
												googleEmailListServiceInterface
														.saveOnlyAtIntegra(emailList);
												groupDao.getHibernateSession()
														.merge(group);
												googleEmailListServiceInterface
														.addRecipient(
																emailList,
																person);
											} catch (Exception ex) {
												errors
														.put(
																"Group: "
																		+ group
																				.getName()
																		+ " add participant error - EmailList: "
																		+ emailList
																				.getName()
																		+ " Recipient: "
																		+ person
																				.getRegistry(),
																group);
												task
														.addError(new SystemGroupError(
																ExceptionUtil
																		.formatMessage(ex),
																SystemGroupError
																		.getAddParticipantToGroupErrorDescription(
																				group,
																				emailList,
																				person),
																task));
												status.setRollbackOnly();
												ex.printStackTrace();
											}
										}
									});
							iterator.remove();
							break;
						}
					}
				}
			}
		}
		return errors;
	}

	/**
	 * Checks if siga database connection is open
	 * 
	 * @return True if open. False if close
	 * @throws SQLException
	 */
	private boolean isConnectionOpenJDBC() throws SQLException {
		return ((GroupDaoJDBC) groupDaoJDBC).isConnectionOpen();
	}

}