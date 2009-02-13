package com.integrareti.integraframework.ui.zk.window.adm.log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.integrareti.integraframework.business.error.SystemGroupError;
import com.integrareti.integraframework.business.log.GroupLog;
import com.integrareti.integraframework.business.task.SystemGroupTask;
import com.integrareti.integraframework.controller.adm.group.log.GroupLogController;
import com.integrareti.integraframework.ui.zk.html.HtmlWarning;
import com.integrareti.integraframework.ui.zk.window.AbstractWindow;

/**
 * Class that manipulates groupLog.zul
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class GroupLogWindow extends AbstractWindow {
	// controller
	private GroupLogController groupLogController;
	// view components
	private Datebox dbBegin;
	private Datebox dbEnd;
	private Checkbox cbxError;
	private Tree tree;

	/**
	 * Executed on create window
	 */
	public void onCreate() {
		this.dbBegin = (Datebox) getFellow("dbBegin");
		this.dbEnd = (Datebox) getFellow("dbEnd");
		this.cbxError = (Checkbox) getFellow("cbxError");
		this.groupLogController = (GroupLogController) SpringUtil.getBean("groupLogController");
	}

	/**
	 * Searches a group log
	 */
	public void searchLog() {
		clearHtmlWarnings("warning");
		Date beginDate = dbBegin.getValue();
		Date endDate = dbEnd.getValue();
		// checking dates
		if (beginDate == null || endDate == null) {
			addHtmlWarning("warning", "Defina corretamente as datas", "", HtmlWarning.WARNING);
			return;
		}
		if (beginDate.after(endDate)) {
			addHtmlWarning("warning", "A data de inicio deve anterior a data de fim", "", HtmlWarning.WARNING);
			return;
		}
		List<GroupLog> groupsLog = new ArrayList<GroupLog>(0);
		try {
			// needs filter?
			if (cbxError.isChecked())
				// search only logs with errors
				groupsLog = groupLogController.getGroupLogByPeriodWithErrors(beginDate, endDate);
			else
				// search all logs
				groupsLog = groupLogController.getGroupLogByPeriod(beginDate, endDate);
		} catch (Exception e) {
			e.printStackTrace();
			addHtmlWarning("warning", "O sistema identificou uma falha de banco de dados. Tente novamente mais tarde ou aguarde por reparos", "", HtmlWarning.ERROR);
		}
		if (groupsLog.isEmpty())
			addHtmlWarning("warning", "Nenhum resultado encontrado", "", HtmlWarning.WARNING);
		else
			addHtmlWarning("warning", groupsLog.size() + " resultado(s) encontrado(s)", "", HtmlWarning.INFORMATION);
		buildTree(groupsLog);
	}

	/**
	 * Builds the tree
	 * 
	 * @param groupsLog
	 */
	@SuppressWarnings("unchecked")
	private void buildTree(List<GroupLog> groupsLog) {
		// clear tree
		List<AbstractComponent> components = this.getChildren();
		for (Iterator<AbstractComponent> iterator = components.iterator(); iterator.hasNext();)
			if (iterator.next() instanceof Tree) {
				iterator.remove();
				break;
			}
		if (!groupsLog.isEmpty()) {
			tree = new Tree();
			if (groupsLog.size() > 25)
				tree.setRows(25);
			Treecols treecols = new Treecols();
			treecols.appendChild(new Treecol("Logs de erros - Grupos"));
			tree.appendChild(treecols);
			Treechildren treechildrenRoot = new Treechildren();
			for (GroupLog groupLog : groupsLog) {
				// building log descriptor
				Treeitem treeitemRoot = new Treeitem();
				treeitemRoot.setOpen(false);
				treechildrenRoot.appendChild(treeitemRoot);
				Treerow treerowDescriptor = new Treerow();
				Treecell treecellDescriptor = new Treecell(groupLog.getGroupName() + " - " + groupLog.getGroupDescription());
				treerowDescriptor.appendChild(treecellDescriptor);
				treeitemRoot.appendChild(treerowDescriptor);
				Treechildren treechildren = new Treechildren();
				treeitemRoot.appendChild(treechildren);
				// build date of log
				for (int i = 0; i < 2; i++) {
					Treeitem treeitem = new Treeitem();
					treeitem.setOpen(false);
					Treerow treerowLogDate = new Treerow();
					String aux = null;
					if (i == 0)
						aux = "Data inicio: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(groupLog.getBeginTime().getTime());
					else
						aux = "Data fim: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(groupLog.getEndTime().getTime());
					Treecell treecellLogDate = new Treecell(aux);
					treerowLogDate.appendChild(treecellLogDate);
					treeitem.appendChild(treerowLogDate);
					treechildren.appendChild(treeitem);
				}
				// build tasks
				for (SystemGroupTask task : groupLog.getTasks()) {
					Treeitem treeitem = new Treeitem();
					treeitem.setOpen(false);
					Treerow treerowTask = new Treerow();
					Treecell treecellTask = new Treecell("Tarefa - Data: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(task.getCreateTime().getTime()));
					treerowTask.appendChild(treecellTask);
					treeitem.appendChild(treerowTask);
					treechildren.appendChild(treeitem);
					Treechildren treechildrenTask = new Treechildren();
					treeitem.appendChild(treechildrenTask);
					Treeitem treeitemTaskDescriptor = new Treeitem();
					treeitemTaskDescriptor.setOpen(false);
					Treerow treerowTaskDescription = new Treerow();
					Treecell treecellTaskDescriptor = new Treecell(task.getName() + " - " + task.getDescription());
					treerowTaskDescription.appendChild(treecellTaskDescriptor);
					treeitemTaskDescriptor.appendChild(treerowTaskDescription);
					treechildrenTask.appendChild(treeitemTaskDescriptor);
					// building errors
					if (!task.getErrors().isEmpty()) {
						Treechildren treechildrenError = new Treechildren();
						treeitemTaskDescriptor.appendChild(treechildrenError);
						for (SystemGroupError error : task.getErrors()) {
							Treeitem treeitemError = new Treeitem();
							treeitemError.setOpen(false);
							Treerow treerowError = new Treerow();
							Treecell treecellError = new Treecell("Erro - Data: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(error.getTime().getTime()));
							treerowError.appendChild(treecellError);
							treeitemError.appendChild(treerowError);
							treechildrenError.appendChild(treeitemError);
							Treechildren treechildrenErrorDescriptor = new Treechildren();
							treeitemError.appendChild(treechildrenErrorDescriptor);
							Treeitem treeitemErrorDescriptor = new Treeitem();
							treeitemErrorDescriptor.setOpen(false);
							Treerow treerowErrorDescriptor = new Treerow();
							Treecell treecellErrorDescriptor = new Treecell(error.getDescription());
							treerowErrorDescriptor.appendChild(treecellErrorDescriptor);
							treeitemErrorDescriptor.appendChild(treerowErrorDescriptor);
							treechildrenErrorDescriptor.appendChild(treeitemErrorDescriptor);
						}
					}
				}
			}
			tree.appendChild(treechildrenRoot);
			this.appendChild(tree);
		} else
			tree = null;
	}
}
