/**
 * 
 */
package org.iita.service;

import java.util.Map;
import java.util.Properties;

import org.iita.util.PagedResult;

/**
 * @author mobreza
 * 
 */
public interface TemplatingService {

	/**
	 * @param string
	 * @return
	 */
	String fillTemplate(String string, Map<String, Object> data) throws TemplatingException;

	/**
	 * Fill template using provided data.
	 * 
	 * @param templateName
	 * @param headings
	 * @param testData
	 * @return
	 */
	String fillReport(String templateName, String[] headings, PagedResult<?> testData, Map<String, Object> extraBans);
	
	/**
	 * @return
	 */
	Properties getTemplateProperties();

	/**
	 * @param templateName
	 * @return
	 */
	BatchTemplater createBatchTemplater(String templateName);
}
