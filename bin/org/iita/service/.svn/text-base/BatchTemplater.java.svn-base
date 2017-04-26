/**
 * iita-common-web.struts Sep 17, 2010
 */
package org.iita.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.util.StringUtil;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author mobreza
 */
public class BatchTemplater {
	private static final Log LOG = LogFactory.getLog(BatchTemplater.class);
	private Template template;
	private Properties templateProperties;
	private HashMap<String, Object> extensions;
	private OutputStream outputStream;
	private String header;
	private String footer;
	private String[] headings = null;

	/**
	 * @param footer Footer
	 * @param header header
	 * @param ftl
	 * @param templateProperties
	 * @param extensions
	 */
	public BatchTemplater(String header, String footer, Template ftl, Properties templateProperties, Map<String, Object> extensions) {
		this.header = header;
		this.footer = footer;
		this.template = ftl;
		this.templateProperties = templateProperties;
		this.extensions = new HashMap<String, Object>();
		this.extensions.putAll(extensions);
	}

	/**
	 * @param outputStream the outputStream to set
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * @param headings the headings to set
	 */
	public void setHeadings(String[] headings) {
		this.headings = headings;
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run(BatchDataProvider dataProvider) {
		OutputStreamWriter osw = new OutputStreamWriter(this.outputStream);

		try {
			// append header
			if (this.header != null) {
				LOG.debug("Writing header");
				osw.write(this.header);
				osw.flush();
			}
			//... body
			List<Object[]> dataRows = null;

			while ((dataRows = dataProvider.fetchData()) != null && dataRows.size() > 0) {

				for (Object row : dataRows) {
					Map<String, Object> data = new Hashtable<String, Object>();
					data.put("config", templateProperties);
					// add extension services
					data.putAll(this.extensions);

					data.put("raw", row);

					if (this.headings != null) {
						if (row instanceof List<?>) {
							List<?> rowList = (List<?>) row;
							for (int i = 0; i < this.headings.length; i++) {
								LOG.debug("Putting list data '" + StringUtil.getCamel(this.headings[i]) + "' = " + rowList.get(i));
								data.put(StringUtil.getCamel(this.headings[i]), rowList.get(i).toString().replaceAll("&", "&amp;"));
							}
						} else {
							if (this.headings.length > 0) {
								LOG.debug("Putting data '" + StringUtil.getCamel(this.headings[0]) + "' = " + row);
								data.put(StringUtil.getCamel(this.headings[0]), row);
							} else {
								LOG.debug("Type " + row.getClass() + " not supported fully. Crosscheck");
							}
						}
					}

					try {
						template.process(data, osw);
						osw.flush();
					} catch (TemplateException e) {
						LOG.error("Error processing template: " + e.getMessage());
						throw new RuntimeException("Error processing template: " + e.getMessage());
					} catch (IOException e) {
						LOG.error("IO exception: " + e.getMessage());
						return;
					}
				}
			}

			if (this.footer != null) {
				LOG.debug("Writing footer");
				osw.write(this.footer);
				osw.flush();
			}

			osw.close();
		} catch (IOException e) {
			LOG.error(e, e);
		}
	}
}
