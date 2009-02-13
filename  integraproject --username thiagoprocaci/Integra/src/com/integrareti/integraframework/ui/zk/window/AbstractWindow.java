package com.integrareti.integraframework.ui.zk.window;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Label;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.integrareti.integraframework.ui.zk.html.HtmlWarning;

/**
 * Describes an abstract Window
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@SuppressWarnings("serial")
public class AbstractWindow extends Window {

	/**
	 * Shows a warnning at a specified component
	 * 
	 * @param componentId
	 * @param warnning
	 */
	public void setBoxWarnning(String componentId, String warnning) {
		throw new WrongValueException(getFellow(componentId), warnning);
	}

	/**
	 * Shows a warnning at any label with id warnning
	 * 
	 * @param warnning
	 * @param delaySeconds
	 */
	public void setTemporaryWarnning(String warnning, int delaySeconds) {
		final Label l = (Label) getFellowIfAny("warnning");
		if (l != null) {
			l.setValue(warnning);
			Timer t = new Timer();
			t.setRepeats(false);
			t.setDelay(delaySeconds * 1000);
			t.addEventListener("onTimer", new EventListener() {
				public void onEvent(Event arg0) {
					l.setValue("");
				}
			});
			appendChild(t);
			t.start();
		}
	}

	/**
	 * Shows a warnning at any label with id warnning
	 * 
	 * @param warnning
	 */
	public void setPermanentWarnning(String warnning) {
		final Label l = (Label) getFellowIfAny("warnning");
		if (l != null)
			l.setValue(warnning);
	}

	/**
	 * Shows a warning
	 * 
	 * @param title
	 * @param warnnings
	 * @param type
	 */
	public void addHtmlWarning(String componentId, String title,
			List<String> warnnings, int type) {
		HtmlWarning l = (HtmlWarning) getFellowIfAny(componentId);
		if (l != null) {
			l.setType(type);
			l.setTitle(title);
			l.setWarnings(warnnings, type);
		}
	}

	/**
	 * Shows a warnning at any label with id warnning
	 * 
	 * @param warnning
	 */
	public void addHtmlWarning(String componentId, String title,
			String warning, int type) {
		HtmlWarning l = (HtmlWarning) getFellowIfAny(componentId);
		if (l != null) {
			l.setType(type);
			l.setTitle(title);
			l.addWarning(warning, type);
		}
	}

	/**
	 * Clears all default <code>HtmlWarning</code>s from the window
	 * 
	 */
	public void clearHtmlWarnings(String componentId) {
		HtmlWarning w = (HtmlWarning) getFellowIfAny(componentId);
		if (w != null)
			w.setContent(null);
	}
	
	
}
