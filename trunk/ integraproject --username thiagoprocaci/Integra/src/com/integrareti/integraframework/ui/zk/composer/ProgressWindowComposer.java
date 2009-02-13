package com.integrareti.integraframework.ui.zk.composer;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.integrareti.integraframework.valueobject.GroupVO;
import com.integrareti.integraframework.valueobject.ImportDetailsVO;

/**
 * Handles the events of progressWindow.zul
 * 
 * @created 17/02/2008
 * @author Thiago Athouguia Gama *
 * @version 1.0
 */
public class ProgressWindowComposer extends GenericComposer {
	private ExecutorService executorService;
	private Map<String, GroupVO> importSuccess, importErrors;
	private Integer importSize;
	private boolean cancel;

	/**
	 * Sets itself if the session to be accessed by components in other
	 * {@link Desktop}
	 * 
	 * @param e
	 */
	public void onCreate(Event e) {
		Executions.getCurrent().getDesktop().getSession().setAttribute("progressWindow", e.getTarget());
	}

	/**
	 * Hides the window
	 * 
	 * @param e
	 */
	public void onHide(Event e) {
		Window win = (Window) e.getTarget();
		win.setSclass("");
		win.getFellow("progress").setVisible(false);
		win.getFellow("lblInstrucao").setVisible(false);
		win.getFellow("btnEsconder").setVisible(false);
		win.getFellow("btnCancel").setVisible(false);
	}

	/**
	 * Shows the window
	 * 
	 * @param e
	 */
	public void onShow(Event e) {
		Window win = (Window) e.getTarget();
		ImportDetailsVO importDetails = (ImportDetailsVO) e.getData();
		cancel = false;
		this.importErrors = importDetails.getErrorImport();
		this.importSuccess = importDetails.getSuccessImport();
		this.importSize = importDetails.getImportSize();
		this.executorService = importDetails.getExecutorService();
		win.setSclass("progress-panel");
		win.getFellow("btnEsconder").setVisible(false);
		Progressmeter progressmeter = (Progressmeter) win.getFellow("progress");
		progressmeter.setValue(0);
		progressmeter.setVisible(true);
		Label progressLabel = (Label) win.getFellow("lblInstrucao");
		progressLabel.setVisible(true);
		progressLabel.setValue("Preparando para criar grupos...");
		win.getFellow("btnCancel").setVisible(true);
		((Timer) win.getFellow("importInfoTimer")).start();
	}

	/**
	 * Show the result of the operation
	 * 
	 * @param e
	 */
	public void onFinishTask(Event e) {
		Window win = (Window) e.getTarget();
		Label lblInstrucao = (Label) win.getFellow("lblInstrucao");
		win.getFellow("btnEsconder").setVisible(true);
		win.getFellow("btnCancel").setVisible(false);
		((Timer) win.getFellow("importInfoTimer")).stop();
		StringBuilder instrucaoString;
		if (cancel) {
			instrucaoString = new StringBuilder("Criação cancelada - " + importSuccess.size() + "/" + (new Double(importSize).intValue() - importErrors.size()) + " grupo(s) criado(s)");
			if (!importErrors.isEmpty()) {
				instrucaoString.append(" (" + importErrors.size() + " Erros)");
			}
			lblInstrucao.setValue(instrucaoString.toString());
		}
	}

	/**
	 * Cancel the operation
	 * 
	 * @param e
	 */
	public void onCancel(Event e) {
		cancel = true;
		executorService.shutdownNow();
		try {
			executorService.awaitTermination(3, TimeUnit.MINUTES);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Events.sendEvent(e.getTarget(), new Event("onFinishTask", e.getTarget()));
	}

	/**
	 * Updates the operation details
	 * 
	 * @param e
	 */
	public void onUpdateImportInfo(Event e) {
		Window progressWindow = (Window) e.getTarget();
		Label lblInstrucao = (Label) progressWindow.getFellow("lblInstrucao");
		Progressmeter pmProgress = (Progressmeter) progressWindow.getFellow("progress");
		StringBuilder instrucaoString;
		if (cancel) {
			lblInstrucao.setValue("Cancelando criação...");
			Events.sendEvent(new Event("onFinishTask", progressWindow));
			return;
		}
		double numberDone = importSuccess.size() + importErrors.size();
		if (numberDone == importSize) {
			pmProgress.setValue(100);
			instrucaoString = new StringBuilder(importSuccess.size() + " Grupo(s) criado(s)");
			if (!importErrors.isEmpty()) {
				instrucaoString.append(" - Erros " + importErrors.size());
			}
			lblInstrucao.setValue(instrucaoString.toString());
			Events.sendEvent(new Event("onFinishTask", progressWindow));
		} else {
			int progressCount = (int) (numberDone / importSize * 100);
			pmProgress.setValue(progressCount);
			instrucaoString = new StringBuilder("Criando grupo " + new Double(numberDone).intValue() + "/" + new Double(importSize).intValue());
			if (!importErrors.isEmpty()) {
				instrucaoString.append(" - Erros " + importErrors.size());
			}
			lblInstrucao.setValue(instrucaoString.toString());
		}
	}
}
