package com.integrareti.integraframework.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.acl.basic.SimpleAclEntry;
import org.acegisecurity.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.google.gdata.data.appsforyourdomain.Login;
import com.google.gdata.data.appsforyourdomain.Name;
import com.google.gdata.data.appsforyourdomain.Quota;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;
import com.integrareti.integraframework.authentication.UserGroup;
import com.integrareti.integraframework.exceptions.UsernameException;
import com.integrareti.integraframework.util.StringUtil;

/**
 * Descreve uma entidade que interage com o sistema, administrador, coordenador,
 * professor ou aluno.
 * -------------------------------------------------------------------------------
 * Describes an entity that interacts with the system. This entity can be:
 * administrator, coordinator, professor or student
 * 
 * @version 1.0
 * @created 22-May-2007 14:55:58
 * @author Thiago Athouguia Gama
 */

@SuppressWarnings("serial")
public class Person extends SimpleAclEntry implements Cloneable, UserDetails,
		Identifiable<Integer> {

	private Integer id;
	private Domain domain;
	private String registry;
	private UserEntry userEntry;
	private Set<UserGroup> userGroups;

	/**
	 * Creates a new Person
	 */
	public Person() {
		domain = new Domain();
		userGroups = new HashSet<UserGroup>();
		userEntry = new UserEntry();
		Login login = new Login();
		login.setSuspended(false);
		userEntry.addExtension(login);
		userEntry.addExtension(new Name());
		Quota quota = new Quota();
		quota.setLimit(2000);
		userEntry.addExtension(quota);
		userEntry.getLogin().setHashFunctionName("SHA-1");
	}

	/**
	 * @return Returns google quota
	 */
	public Integer getQuotaLimitInMB() {
		return getUserEntry().getQuota().getLimit();
	}

	/**
	 * Sets google quota
	 * 
	 * @param limit
	 */
	public void setQuotaLimitInMB(Integer limit) {
		getUserEntry().getQuota().setLimit(limit);
	}

	/**
	 * @return Returns the password
	 */
	public String getPassword() {
		return getUserEntry().getLogin().getPassword();
	}

	/**
	 * Sets the password
	 * 
	 * @param pass
	 */
	public void setPassword(String pass) {
		getUserEntry().getLogin().setPassword(pass);
	}

	/**
	 * @return Returns a String representing the username used to identify on
	 *         Google's systems.
	 */
	public String getGoogleAccount() {
		return getUserEntry().getLogin().getUserName();
	}

	/**
	 * Sets the String that represent the username used to identify on Google's
	 * systems.
	 * 
	 * @param googleAccount
	 */
	public void setGoogleAccount(String googleAccount) {
		getUserEntry().getLogin().setUserName(googleAccount);
	}

	/**
	 * @return Returns the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the given name
	 */
	public String getGivenName() {
		return getUserEntry().getName().getGivenName();
	}

	/**
	 * Sets the given name
	 * 
	 * @param name
	 */
	public void setGivenName(String name) {
		getUserEntry().getName().setGivenName(name);
	}

	/**
	 * @return Returns the family name
	 */
	public String getFamilyName() {
		return getUserEntry().getName().getFamilyName();
	}

	/**
	 * Sets the family name
	 * 
	 * @param familyName
	 */
	public void setFamilyName(String familyName) {
		getUserEntry().getName().setFamilyName(familyName);
	}

	/**
	 * 
	 * @return Returns name
	 */
	public String getName() {
		if (getGivenName() != null && getFamilyName() != null)
			return getGivenName() + " " + getFamilyName();
		if (getGivenName() != null && getFamilyName() == null)
			return getGivenName();
		return null;
	}

	/**
	 * Sets name
	 */
	public void setName(String name) {
		Scanner sc = new Scanner(name).useDelimiter("\\s* \\s*");
		int count = 0;
		String familyName = new String();
		while (sc.hasNext()) {
			if (count == 0) {
				setGivenName(sc.next());
				count++;
			} else
				familyName += sc.next() + " ";
		}
		if (StringUtils.hasText(familyName))
			setFamilyName(familyName.trim());
		else
			setFamilyName(null);
	}

	/**
	 * @return Returns a String representing a system specific identification.
	 *         For example school registry or department registry.
	 */
	public String getRegistry() {
		return registry;
	}

	/**
	 * Sets the registry
	 * 
	 * @param registry
	 */
	public void setRegistry(String registry) {
		this.registry = registry;
	}

	/**
	 * @return Returns the domain
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * Sets the domain
	 * 
	 * @param domain
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * @return Returns the email
	 */
	public String getEmail() {
		return getGoogleAccount() + "@" + getDomain().getName();
	}

	/**
	 * Adds a new UserGroup
	 * 
	 * @param userGroup
	 */
	public void addUserGroup(UserGroup userGroup) {
		this.userGroups.add(userGroup);
	}

	/**
	 * Removes a UserGroup
	 * 
	 * @param userGroup
	 */
	public void removeUserGroup(UserGroup userGroup) {
		this.userGroups.remove(userGroup);
	}

	/**
	 * @return Returns the userEntry
	 */
	public UserEntry getUserEntry() {
		return userEntry;
	}

	/**
	 * Sets the userEntry
	 * 
	 * @param userEntry
	 */
	public void setUserEntry(UserEntry userEntry) {
		this.userEntry = userEntry;
	}

	/**
	 * 
	 * @return returns userGroups
	 */
	public Set<UserGroup> getUserGroups() {
		return userGroups;
	}

	/**
	 * Sets the userGroups
	 * 
	 * @param userGroups
	 */
	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	/**
	 * @param groupName
	 * @return true if the <code>Person</code> instance is member of a group
	 *         with the specified name
	 */
	public boolean isInPermissionGroup(String groupName) {
		for (UserGroup pGroup : userGroups)
			if (pGroup.getName().toUpperCase().equals(groupName.toUpperCase()))
				return true;
		return false;
	}

	/**
	 * @return Returns a list of possibles usernames for a person
	 */
	public List<String> getPossiblesUsernames() throws UsernameException {

		// verifies if an user (person) has a google account
		if (StringUtils.hasText(getGoogleAccount())) {
			throw new UsernameException(
					"This user has already defined a username");
		}
		// verifies if an user has a "given name" and "family name"
		if ((!StringUtils.hasText(getGivenName()))
				|| (!StringUtils.hasText(getFamilyName()))) {
			throw new UsernameException(
					"The given name and family name must be specified");
		}
		// get the complete name of a person
		String completeName = StringUtil.changeAccent(getGivenName()
				.toLowerCase())
				+ " " + StringUtil.changeAccent(getFamilyName().toLowerCase());
		completeName = StringUtil.removePoint(completeName);

		// list that will store the possibles usernames
		List<String> usernames = new ArrayList<String>();

		Scanner nameScanner = new Scanner(completeName)
				.useDelimiter("\\s* \\s*");
		List<String> names = new ArrayList<String>();
		List<String> initials = new ArrayList<String>();
		while (nameScanner.hasNext()) {
			String s = nameScanner.next();
			// discards the invalid words
			if (!s.equals("da") && !s.equals("de") && !s.equals("di")
					&& !s.equals("dos") && !s.equals("das") && !s.equals("dus")
					&& !s.equals("dis") && !s.equals("do") && !s.equals("du")
					&& (s.length() > 1)) {
				names.add(s);
				initials.add(s.substring(0, 1));
			}
		}
		String aux = "";
		int i = 1;
		// algorithm that generates the usernames
		for (String n : names) {
			if (!names.get(0).equals(n)) {
				usernames.add(names.get(0) + n);
				usernames.add(names.get(0) + "." + n);
			}
			if (!n.equals(names.get(0))) {
				usernames.add(initials.get(0) + n);
			}
			if ((aux.length() > 1)) {
				usernames.add(aux + n);
			}
			i++;
			aux += n.substring(0, 1);
		}

		if (names.size() > 2) {
			for (int j = 0; j < names.size(); j++) {
				if (!names.get(0).equals(names.get(j))) {
					if (names.size() != j + 1) {
						usernames.add(names.get(0) + names.get(j)
								+ names.get(j + 1));
						usernames.add(names.get(0) + "." + names.get(j) + "."
								+ names.get(j + 1));
					}
				}
			}
		}
		return usernames;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
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
		final Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * ###########################################################################
	 * Adapter Pattern for org.acegisecurity.userdetails.UserDetails Interface
	 * Methods implementations - These methods are used to enable Acegi security
	 * in the application
	 */

	/**
	 * @return an array of <code>GrantedAuthority</code>s, each one described
	 *         as the name of a <code>UserGroup</code>. Obs.: the prefix
	 *         ROLE_ should be added to "feed" the roleVoter,
	 * @see org.acegisecurity.vote.RoleVoter
	 */
	@Override
	public GrantedAuthority[] getAuthorities() {
		GrantedAuthority[] authorities = new GrantedAuthorityImpl[userGroups
				.size()];
		int count = 0;
		for (UserGroup uGroup : userGroups) {
			authorities[count] = new GrantedAuthorityImpl("ROLE_"
					+ uGroup.getName());
			count++;
		}
		return authorities;
	}

	/**
	 * @return the user's registry as username
	 */
	@Override
	public String getUsername() {
		return getRegistry();
	}

	/**
	 * @returns always true, as it's not intended for the application yet.
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * @returns always true, as it's not intended for the application yet.
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * @returns always true, as it's not intended for the application yet.
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return !getUserEntry().getLogin().getSuspended();
	}

	@Override
	public Person clone() throws CloneNotSupportedException {
		Person p = (Person) super.clone();
		UserEntry uEntry = new UserEntry();
		Login login = new Login();
		login.setHashFunctionName(getUserEntry().getLogin()
				.getHashFunctionName());
		login.setUserName(getGoogleAccount());
		login.setPassword(getPassword());
		uEntry.addExtension(login);
		uEntry.addExtension(getUserEntry().getName());
		uEntry.addExtension(getUserEntry().getQuota());
		p.setUserEntry(uEntry);
		return p;
	}

}