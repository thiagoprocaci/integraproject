package com.integrareti.integraframework.acegi;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Custom property editor for {@link PageRedirectDefinition} on Spring Acegi's context
 * definition
 * 
 * @created 27/02/2008
 * @author Thiago Athouguia Gama * 
 * @version 1.0
 */
public class PageRedirectDefinitionSourceEditor extends PropertyEditorSupport {
	
	private static final Log logger = LogFactory
			.getLog(PageRedirectDefinitionSourceEditor.class);
	
	/**
	 * <b>Description copied from {@link PropertyEditor#setAsText(String)}</b><br/>
     * Set the property value by parsing a given String.  May raise
     * java.lang.IllegalArgumentException if either the String is
     * badly formatted or if this kind of property can't be expressed
     * as text.
     * @param text  The string to be parsed.
     * 
     */
	public void setAsText(String text) throws IllegalArgumentException {
		PageRedirectDefinitionSource definitionSource = new PageRedirectDefinitionSource();

		List<PageRedirectDefinition> definitionsList = new ArrayList<PageRedirectDefinition>();

		if (StringUtils.isNotEmpty(text)) {
			BufferedReader br = new BufferedReader(new StringReader(text));
			int counter = 0;
			String line;
			while (true) {
				counter++;
				try {
					line = br.readLine();
				} catch (IOException ioe) {
					throw new IllegalArgumentException(ioe.getMessage());
				}
				if (line == null) {
					break;
				}
				line = line.trim();
				if (logger.isDebugEnabled()) {
					logger.debug("Line " + counter + ": " + line);
				}
				if (line.startsWith("//")) {
					continue;
				}
				if (line.lastIndexOf('=') == -1) {
					continue;
				} // Tokenize the line into its name/value tokens
				int equalsPos = line.indexOf("=");
				String roleNamesString = line.substring(0, equalsPos);
				String pageName = line.substring(equalsPos + 1);
				if (StringUtils.isEmpty(pageName)
						|| StringUtils.isEmpty(roleNamesString)) {
					throw new IllegalArgumentException(
							"Failed to parse a valid name/value pair from "
									+ line);
				}

				if (logger.isDebugEnabled()) {
					logger.debug("roleNamesString :" + roleNamesString);
					logger.debug("pageName :" + pageName);
				}

				String[] roleName = org.springframework.util.StringUtils
						.delimitedListToStringArray(roleNamesString, ",");

				for (int i = 0; i < roleName.length; i++) {
					PageRedirectDefinition definition = new PageRedirectDefinition(
							roleName[i], pageName);
					definitionsList.add(definition);
				}

			}
		}
		definitionSource.setDefinitionsList(definitionsList);
		setValue(definitionSource);
	}
}