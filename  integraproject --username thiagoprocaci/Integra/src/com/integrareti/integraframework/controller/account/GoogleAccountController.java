package com.integrareti.integraframework.controller.account;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.integrareti.integraframework.business.Domain;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.exceptions.UsernameException;
import com.integrareti.integraframework.service.google.GoogleUserAccountServiceInterface;
import com.integrareti.integraframework.service.integra.IntegraDeletedGoogleUserInterface;
import com.integrareti.integraframework.service.integra.IntegraDomainServiceInterface;
import com.integrareti.integraframework.service.integra.IntegraUserGroupServiceInterface;
import com.integrareti.integraframework.service.siga.SigaService;
import com.integrareti.integraframework.util.StringUtil;
import com.integrareti.integraframework.valueobject.PersonVO;
import com.integrareti.integraframework.valueobject.SectorVO;

/**
 * This class gives to googleAccount.zul (view) the access to business
 * components
 * 
 * @author Thiago
 * 
 */
public class GoogleAccountController {
	protected GoogleUserAccountServiceInterface personService;
	protected SigaService sigaService;
	protected IntegraDomainServiceInterface domainService;
	protected IntegraUserGroupServiceInterface userGroupService;
	protected IntegraDeletedGoogleUserInterface deletedGoogleUserService;
	private String username;

	/**
	 * Creates a new GoogleAccountController
	 */
	public GoogleAccountController(SigaService sigaService, GoogleUserAccountServiceInterface personService, IntegraDomainServiceInterface domainService, IntegraUserGroupServiceInterface userGroupService, IntegraDeletedGoogleUserInterface deletedGoogleUserService) {
		this.sigaService = sigaService;
		this.personService = personService;
		this.domainService = domainService;
		this.userGroupService = userGroupService;
		this.deletedGoogleUserService = deletedGoogleUserService;
	}

	/**
	 * Saves a person
	 * 
	 * @return saved person
	 */
	public void save(PersonVO personVO) throws AppsForYourDomainException, Exception {
		Person person = getPerson(personVO);
		personService.save(person);
	}

	/**
	 * Deletes a person
	 * 
	 * @param person
	 * @throws Exception
	 */
	public void delete(Person person) throws Exception {
		personService.delete(person);
	}

	/**
	 * 
	 * @param personVO
	 * @return Returns the possibles usernames
	 */
	public List<String> getPossiblesUsernames(PersonVO personVO) {
		try {
			Person p = new Person();
			p.setGivenName(personVO.getGivenName());
			p.setFamilyName(personVO.getFamilyName());
			return p.getPossiblesUsernames();
		} catch (UsernameException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param usernames
	 * @return Returns the valid usernames
	 * @throws Exception
	 * @throws DataAccessException
	 */
	public List<String> validUsernames(List<String> usernames, String domainName) throws Exception {
		List<String> aux = new ArrayList<String>(usernames.size());
		List<String> list = deletedGoogleUserService.getDeletedGoogleAccountUntilFiveDaysAgoByDomainName(domainName);
		int count = 1;
		for (Iterator<String> it = usernames.iterator(); it.hasNext();) {
			String string = it.next();
			String auxS = string;
			while (personService.getByGoogleAccount(auxS, domainName) != null || list.contains(auxS)) {
				auxS = string + count;
				if (personService.getByGoogleAccount(auxS, domainName) == null && !list.contains(auxS))
					break;
				count++;
			}
			count = 1;
			aux.add(auxS);
		}
		return aux;
	}

	/**
	 * 
	 * 
	 * @param registry
	 * @return Returns a personVO with the google account
	 * @throws Exception
	 * @throws DataAccessException
	 */
	public String getGoogleAccount(String registry) throws Exception {
		Person p = personService.getByRegistry(registry);
		if (p != null)
			return p.getGoogleAccount();
		return null;
	}

	/**
	 * Checks if a person exists in integra database - using JBDC to improve
	 * performance
	 * 
	 * @param registry
	 * @return Returns id if person exist
	 * @throws Exception
	 */
	public Integer isPersonSaved(String registry) throws Exception {
		return personService.isPersonSaved(registry, false);
	}

	/**
	 * 
	 * @param sectorVO
	 * @return Returns the domain of a sector
	 * @throws Exception
	 */
	public Domain getDomain(SectorVO sectorVO) throws Exception {
		return domainService.getDomain(sectorVO);
	}

	/**
	 * 
	 * @return Returns all domain
	 * @throws Exception
	 */
	public List<Domain> getAllDomains() throws Exception {
		return domainService.getAll();
	}

	/**
	 * 
	 * @param registry
	 * @return Returns the basics data of a person
	 *         (givenName,familyName,course/departament,userGroups)
	 * @throws Exception
	 */
	public PersonVO getPersonBasicsData(String registry) throws Exception {
		return sigaService.getPersonBasicsData(registry);
	}

	/**
	 * 
	 * @return Returns username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets username
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @param personVO
	 * @return Returns a person with same atributtes of a personVO
	 * @throws Exception
	 */
	private Person getPerson(PersonVO personVO) throws Exception {
		Person person = new Person();
		person.setId(personVO.getId());
		person.setGivenName(personVO.getGivenName());
		person.getUserGroups().addAll(userGroupService.getByName(personVO.getUserPositionGroups().toArray(new String[0])));
		person.setPassword(StringUtil.SHA1Encrypt(personVO.getPassword()));
		person.setFamilyName(personVO.getFamilyName());
		person.setRegistry(personVO.getRegistry());
		person.setGoogleAccount(personVO.getGoogleAccount());
		if (personVO.getDomain() == null)
			person.setDomain(domainService.getDomain(personVO.getSector()));
		else
			person.setDomain(personVO.getDomain());
		return person;
	}

	/**
	 * 
	 * @param domainName
	 * @return Returns Domain by name
	 * @throws Exception
	 */
	public Domain getDomainByName(String domainName) throws Exception {
		return domainService.getDomainByName(domainName);
	}
}
