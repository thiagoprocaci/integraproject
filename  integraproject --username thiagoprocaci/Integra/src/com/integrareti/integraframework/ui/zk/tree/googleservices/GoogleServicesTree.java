package com.integrareti.integraframework.ui.zk.tree.googleservices;

import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

/**
 * The Table-of-Content tree on the left.
 * 
 * @author jumperchen
 */
@SuppressWarnings("serial")
public class GoogleServicesTree extends Tree implements AfterCompose {

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

	/**
	 * After compose action
	 */
	public void afterCompose() {
		final Execution exec = Executions.getCurrent();
		String id = exec.getParameter("id");
		Treeitem item = null;
		if (id != null) {
			try {
				item = (Treeitem) getSpaceOwner().getFellow(id);
			} catch (ComponentNotFoundException ex) { // ignore
			}
		}

		if (item == null)
			item = (Treeitem) getSpaceOwner().getFellow("googleServices");
		// so index.zul know which page to load based on the id parameter
		exec.setAttribute("contentSrc", (String) item.getValue());
		selectItem(item);
	}

}
