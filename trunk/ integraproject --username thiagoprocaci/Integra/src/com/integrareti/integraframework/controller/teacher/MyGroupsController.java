package com.integrareti.integraframework.controller.teacher;

import java.util.List;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.service.integra.IntegraGroupServiceInterface;
import com.integrareti.integraframework.service.siga.SigaService;

public class MyGroupsController {
	
	private SigaService sigaService;
	private IntegraGroupServiceInterface integraGroupService;	
	
	/**
	 * Creates a new instance of GroupImportController
	 * 
	 * @param sigaService
	 * @param integraGroupService
	 */
	public MyGroupsController(
			SigaService sigaService,
			IntegraGroupServiceInterface integraGroupService) {
		this.sigaService = sigaService;
		this.integraGroupService = integraGroupService;
	}
	
	/**
	 * Reurns all groups that has an owner with the specified id
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Group getMyGroupById(Integer id) throws Exception{
		Group g =  integraGroupService.getById(id);
		sigaService.initPeopleName(g.getParticipants());
		return g; 
	}
	
	/**
	 * 
	 * @param owner
	 * @return Returns the groups of a owner
	 * @throws Exception
	 */
	public List<Group> getGroupsByOwner(Person p) throws Exception {
		return integraGroupService.getGroupsByOwner(p);
	}

	/**
	 * 
	 * @param participant
	 * @return Returns the groups of a person (participant)
	 * @throws Exception
	 */
	public List<Group> getGroupsByParticipant(Person participant)
			throws Exception {
		return integraGroupService.getGroupsByParticipant(participant);
	}
	
}
