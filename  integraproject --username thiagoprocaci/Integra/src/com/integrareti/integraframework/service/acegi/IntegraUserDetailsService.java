package com.integrareti.integraframework.service.acegi;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.service.google.GoogleUserAccountServiceInterface;
import com.integrareti.integraframework.service.siga.SigaService;

/**
 * Custom {@link UserDetailsService} to get {@link UserDetails} from the legacy
 * system
 * 
 * @created 10/12/2007
 * @author Thiago Athouguia Gama
 * @version 1.0
 * 
 */
public class IntegraUserDetailsService implements UserDetailsService {
	private SigaService sigaService;
	private GoogleUserAccountServiceInterface personService;

	public IntegraUserDetailsService(SigaService sigaService, GoogleUserAccountServiceInterface personService) {
		this.sigaService = sigaService;
		this.personService = personService;
	}

	@Override
	public UserDetails loadUserByUsername(String registry) throws UsernameNotFoundException, DataAccessException {
		Person person = null;
		try {
			person = personService.getByRegistry(registry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (person == null)
			throw new UsernameNotFoundException("Username " + registry + " not found");
		try {
			person.setPassword(sigaService.getPersonPassword(registry));
		} catch (Exception e) {
			throw new UsernameNotFoundException("Ocorreu uma exceção ao se verificar a senha do usuário");
		}
		return person;
	}
}
