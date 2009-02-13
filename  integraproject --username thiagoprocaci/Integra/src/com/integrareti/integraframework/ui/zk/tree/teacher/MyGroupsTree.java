package com.integrareti.integraframework.ui.zk.tree.teacher;

import java.util.List;

import org.acegisecurity.context.SecurityContextHolder;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.integrareti.integraframework.business.Group;
import com.integrareti.integraframework.business.Person;
import com.integrareti.integraframework.controller.teacher.MyGroupsController;

/**
 * The Table-of-Content tree on the left.
 * 
 * @author jumperchen
 */
@SuppressWarnings("serial")
public class MyGroupsTree extends Tree {
	private static final String TEACHER_WINDOW_URL = "/zul/secure/teacher/myGroupsWindow.zul";
	private MyGroupsController myGroupsController;

	public MyGroupsTree() {
		myGroupsController = (MyGroupsController) SpringUtil.getBean("myGroupsController");
		initMyGroups();
	}

	/**
	 * On select action
	 */
	public void onSelectItem() {
		Treeitem item = getSelectedItem();
		if (item != null) {
			Include inc = (Include) getRoot().getFellow("include");
			inc.setSrc((String) item.getValue());
		}
	}

	public void initMyGroups() {
		boolean newChildren = false;
		List<Group> myGroups = null;
		try {
			myGroups = myGroupsController.getGroupsByParticipant((Person) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Treechildren children = getTreechildren();
		if (children == null) {
			children = new Treechildren();
			newChildren = true;
		} else {
			children.getChildren().clear();
		}
		for (Group group : myGroups) {
			Treeitem item = new Treeitem();
			item.setValue(TEACHER_WINDOW_URL + "?groupId=" + group.getId());
			Treerow row = new Treerow();
			Treecell cell = new Treecell(group.toString());
			cell.addEventListener("onClick", new EventListener() {
				@Override
				public void onEvent(Event arg0) throws Exception {
					onSelectItem();
				}
			});
			row.appendChild(cell);
			item.appendChild(row);
			item.setImage("/img/group.png");
			children.appendChild(item);
		}
		if (newChildren)
			appendChild(children);
	}

	public void onGroupUpdate(Event e) {
		initMyGroups();
	}
}