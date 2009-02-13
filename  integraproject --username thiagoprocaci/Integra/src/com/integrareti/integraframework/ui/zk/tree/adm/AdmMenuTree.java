package com.integrareti.integraframework.ui.zk.tree.adm;


import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

/**
 * The Table-of-Content tree on the left.
 * 
 * @author jumperchen
 */
@SuppressWarnings("serial")
public class AdmMenuTree extends Tree {

	/**
	 * Creates a new AdmMenuTree
	 */
	public AdmMenuTree() {

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


}