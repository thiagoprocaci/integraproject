package com.integrareti.integraframework.ui.zk.window.adm.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.integrareti.integraframework.business.Domain;
import com.integrareti.integraframework.business.Unit;
import com.integrareti.integraframework.controller.adm.group.GroupImportController;
import com.integrareti.integraframework.thread.SingleGroupImportWorkingThread;
import com.integrareti.integraframework.ui.zk.html.HtmlWarning;
import com.integrareti.integraframework.ui.zk.window.AnnotateDataBinderWindow;
import com.integrareti.integraframework.valueobject.GroupVO;
import com.integrareti.integraframework.valueobject.ImportDetailsVO;

/**
 * Class that manipulates groupImportWindon.zul
 * 
 * @author Thiago
 * 
 */
@SuppressWarnings("serial")
public class GroupImportWindow extends AnnotateDataBinderWindow {

	private static final int MAX_NON_BACKGROUND_ALLOWED_SIZE = 0;

	// Threadpool
	private ExecutorService executorService;

	// page grid size
	private static Integer DEFAULT_PAGE_SIZE = 25;

	// controller
	private GroupImportController groupImportController;

	// bind
	private List<GroupVO> groupsVO;

	private List<Unit> units;

	private List<String> unitFilters;

	// view components
	private Listbox groupsListBox;

	// others
	private List<Domain> domains;

	private Unit selectedUnit;

	private String selectedSemester;

	private String selectedYear;

	private Map<String,GroupVO> importErrors, importSuccess;

	private List<GroupVO> selectedGroupsVO;

	private Window progressWindow;

	private TransactionTemplate template;

	@SuppressWarnings("unchecked")
	@Override
	public void doOnCreate() {
		progressWindow = (Window) Executions.getCurrent().getDesktop().getPage(
				"progressWindow").getFellow("progressWindow");

		importErrors = Collections.synchronizedMap(new HashMap<String, GroupVO>());
		importSuccess = Collections.synchronizedMap(new HashMap<String, GroupVO>());
		groupImportController = (GroupImportController) SpringUtil
				.getBean("groupImportController");
		template = (TransactionTemplate) SpringUtil
				.getBean("sharedTransactionTemplate");
		try {
			domains = groupImportController.getDomains();
		} catch (Exception e) {
			showDataBaseMessageError();
			e.printStackTrace();
		}
		groupsListBox = (Listbox) getFellow("lbxGroups");
		units = new ArrayList<Unit>();
		units.add(null);
		unitFilters = new ArrayList<String>();
		unitFilters.add(null);
		groupsVO = null;
		groupsListBox.setPageSize(DEFAULT_PAGE_SIZE);
		if (domains != null)
			for (Domain domain : domains)
				units.addAll(domain.getUnits());
		((Listbox) getFellow("units")).setSelectedIndex(0);
		((Listbox) getFellow("semester")).setSelectedIndex(0);
		selectedUnit = null;
		selectedSemester = null;
		selectedYear = null;
	}

	@Override
	public void doBeforeBind() {
		getBindObjects().put("groups", groupsVO);
		getBindObjects().put("units", units);
		getBindObjects().put("filters", unitFilters);
	}

	/**
	 * Gets the groupsVO from siga and display
	 */
	public void getGroupsVO() {
		selectedUnit = getSelectedUnit();
		selectedSemester = getSelectedSemester();
		selectedYear = getSelectedYear();
		if (selectedUnit == null || selectedSemester == null
				|| !StringUtils.hasText(selectedYear)) {
			addHtmlWarning(
					"warning",
					"Atenção",
					"Defina os campos unidade, semestre e ano para a importação",
					HtmlWarning.WARNING);
			groupsVO = null;
			updateBoundComponent(groupsListBox);
			return;
		}
		if (((Listbox) getFellow("filterDepto")).isVisible()
				&& getSelectFilter() != null) {
			// department filter
			try {
				groupsVO = groupImportController
						.getSubjectByPeriodAndSectorAndDepartment(selectedYear,
								selectedSemester, selectedUnit.getName(),
								getSelectFilter());
			} catch (Exception e) {
				e.printStackTrace();
				showDataBaseMessageError();
				return;
			}
		} else {
			if (((Textbox) getFellow("filterCode")).isVisible()
					&& StringUtils.hasText(((Textbox) getFellow("filterCode"))
							.getText())) {
				// subject code filter
				String filter = ((Textbox) getFellow("filterCode")).getText()
						.trim();
				try {
					groupsVO = groupImportController
							.getSubjectByPeriodAndSectorAndSubjectCode(
									selectedYear, selectedSemester, filter,
									selectedUnit.getName());
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
					return;
				}
			} else {
				// no filter
				try {
					groupsVO = groupImportController.getSubjectByPeriodAndSector(
							selectedYear, selectedSemester, selectedUnit
									.getName());
				} catch (Exception e) {
					e.printStackTrace();
					showDataBaseMessageError();
					return;
				}
			}

		}
		if (groupsVO.isEmpty())
			addHtmlWarning("warning", "Nenhum resultado encontrado", "",
					HtmlWarning.WARNING);
		else
			addHtmlWarning("warning", "Foram encontrados " + groupsVO.size()
					+ " resultados", "", HtmlWarning.INFORMATION);
		updateBoundComponent(groupsListBox);
	}

	/**
	 * 
	 * @return Returns selected year
	 */
	public String getSelectedYear() {
		return ((Textbox) getFellow("year")).getText();
	}

	/**
	 * 
	 * @return Returns selected semester
	 */
	public String getSelectedSemester() {
		return ((Listbox) getFellow("semester")).getSelectedItem().getLabel();
	}

	/**
	 * 
	 * @return Returns selected Unit
	 */
	public Unit getSelectedUnit() {
		Listitem listitem = ((Listbox) getFellow("units")).getSelectedItem();
		if (listitem == null)
			return null;
		return (Unit) listitem.getValue();
	}

	/**
	 * 
	 * @return Returns the selected filter
	 */
	public String getSelectFilter() {
		Listitem listitem = ((Listbox) getFellow("filterDepto"))
				.getSelectedItem();
		if (listitem == null || listitem.getValue() == null)
			return null;
		return ((String) listitem.getValue()).trim();
	}

	/**
	 * Creates groups
	 * 
	 * @return Groups that could not be created
	 */
	@SuppressWarnings("unchecked")
	public void save() {
		importErrors.clear();
		importSuccess.clear();

		Set<Listitem> listItens = groupsListBox.getSelectedItems();
		if (listItens == null || listItens.isEmpty()) {
			addHtmlWarning("warning", "Atenção",
					"Selecione pelo menos 1 (um) grupo a ser criado",
					HtmlWarning.WARNING);
			return;
		}

		selectedGroupsVO = new ArrayList<GroupVO>();
		// getting the selectedGroupsVO checked
		for (Listitem listItem : listItens) {
			GroupVO g = (GroupVO) listItem.getValue();
			selectedGroupsVO.add(g);
		}
		Domain selectedDomain = null;
		// getting the domain
		for (Domain domain : domains)
			if (domain.getUnits().contains(selectedUnit)) {
				selectedDomain = domain;
				break;
			}
		// too long operation
		if (listItens.size() > MAX_NON_BACKGROUND_ALLOWED_SIZE) {
			executorService = Executors.newFixedThreadPool(5);
			for (GroupVO groupVO : selectedGroupsVO) {
				executorService
						.execute(new SingleGroupImportWorkingThread(
								groupImportController, groupVO, selectedDomain,
								selectedYear, selectedSemester, importSuccess,
								importErrors, template));
			}
			executorService.shutdown();

			Events.sendEvent(new Event("onShow", progressWindow,
					new ImportDetailsVO(this.importSuccess, this.importErrors,
							selectedGroupsVO.size(), executorService)));

			doAfterSave(selectedGroupsVO);

		} else {
			Map<String, GroupVO> mapErrors = groupImportController.createGroups(
					selectedGroupsVO, selectedDomain, selectedYear,
					selectedSemester);
			if (!mapErrors.isEmpty()) {
				List<String> errorlist = new ArrayList<String>();
				Iterator<String> keyIterator = mapErrors.keySet().iterator();
				while (keyIterator.hasNext()) {
					String key = (String) keyIterator.next();
					GroupVO g = mapErrors.get(key);
					selectedGroupsVO.remove(g);
					errorlist.add("Não foi possível salvar o grupo "
							+ g.getSubjectName() + " Turma " + g.getClassroom()
							+ ". Violação de restrições do banco de dados");
				}
				addHtmlWarning("warning", "Erro ao salvar os grupo(s):",
						errorlist, HtmlWarning.ERROR);
			} else
				addHtmlWarning("warning", "Grupo(s) salvo(s) com sucesso", "",
						HtmlWarning.INFORMATION);
			doAfterSave(selectedGroupsVO);
		}
	}

	/**
	 * Executed on select unit - Update the filters
	 */
	public void onSelectUnit() {
		Unit u = getSelectedUnit();
		if (u != null)
			try {
				unitFilters.addAll(groupImportController.getDepartamentsBySector(u
						.getName()));
			} catch (Exception e) {
				e.printStackTrace();
				showDataBaseMessageError();
				return;
			}
		else {
			unitFilters.clear();
			unitFilters.add(null);
		}
		updateBoundComponent((Listbox) getFellow("filterDepto"));
	}

	/**
	 * Executed on select result - updates the size of the list of groups
	 */
	public void onSelectResult() {
		String size = ((Listbox) getFellow("result")).getSelectedItem()
				.getLabel().trim();
		if (StringUtils.hasText(size))
			groupsListBox.setPageSize(Integer.parseInt(size));
	}

	/**
	 * After save action - updates the groupsListBox (removes the saved
	 * successful group).
	 */
	private void doAfterSave(List<GroupVO> savedGroups) {
		for (Iterator<GroupVO> iterator = groupsVO.iterator(); iterator
				.hasNext();) {
			GroupVO g = (GroupVO) iterator.next();
			for (GroupVO groupVO : savedGroups) {
				if (g.equals(groupVO)) {
					iterator.remove();
					break;
				}
			}
		}
		updateBoundComponent(groupsListBox);
		Events.sendEvent(new Event("onGroupUpdate", Path.getComponent("//main/menuWindow/myGroups")));
	}

	/**
	 * Shows a dataBase message error
	 */
	private void showDataBaseMessageError() {
		addHtmlWarning(
				"warning",
				"O sistema identificou uma falha de banco de dados. Tente novamente mais tarde ou aguarde por reparos",
				"", HtmlWarning.ERROR);
	}

}
