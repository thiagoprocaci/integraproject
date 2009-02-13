package com.integrareti.integraframework.ui.zk.html;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;
import org.zkoss.zul.Html;

/**
 * Html message manager
 * 
 * @author Thiago
 * 
 */
@SuppressWarnings("serial")
public class HtmlWarning extends Html {
	// STATIC CONSTANTS
	public static final int WARNING = 0;
	public static final int INFORMATION = 1;
	public static final int ERROR = 2;
	private List<String> warnings = new ArrayList<String>(10);
	private String title;
	private int type;

	/**
	 * @param warning
	 *            the String warning to be added
	 * @param type
	 *            defining the style of the warning box
	 */
	public void addWarning(String warning, int type) {
		this.type = type;
		warnings.clear();
		if (StringUtils.hasText(warning))
			warnings.add(warning);
		refresh();
	}

	/**
	 * 
	 * @return Returns title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return Returns warnings
	 */
	public List<String> getWarnings() {
		return warnings;
	}

	/**
	 * Sets warnings
	 * 
	 * @param warnings
	 *            list of string warnings to be displayed in the html list
	 * @param type
	 *            defining the style of the warning box
	 */
	public void setWarnings(List<String> warnings, int type) {
		this.type = type;
		this.warnings = warnings;
		refresh();
	}

	/**
	 * 
	 * @return one of the folowing HtmlWarning.WARNING, HtmlWarning.INFORMATION,
	 *         HtmlWarning.ERROR;
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets type
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Refreshes the content of the Html component
	 * 
	 */
	private void refresh() {
		// sets the style
		switch (this.type) {
		case WARNING:
			this.setContent("<div id=\"warningContainer\">");
			break;
		case ERROR:
			this.setContent("<div id=\"errorContainer\">");
			break;
		case INFORMATION:
			this.setContent("<div id=\"infoContainer\">");
			break;
		}
		// sets the title if not null
		if (title != null)
			this.setContent(getContent() + "<h5>" + title + "</h5>");
		if (warnings.size() > 0)
			this.setContent(getContent() + "<ul>");
		// sets the list of warnnings
		for (String warning : warnings)
			this.setContent(getContent() + "<li>" + warning + "</li>");
		if (warnings.size() > 0)
			this.setContent(getContent() + "</ul>");
		this.setContent(getContent() + "</div>");
	}
}
