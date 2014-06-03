package org.crlr.intercepteur.hibernate;

import java.util.regex.Pattern;

import org.apache.oro.text.PatternCacheFIFO;
import org.crlr.metier.utils.SchemaUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaInterceptorImpl extends EmptyInterceptor implements
		Interceptor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 343527089711398834L;
	
	final static private ThreadLocal<String>  SCHEMA = new ThreadLocal<String>();

	protected final Logger log = LoggerFactory.getLogger(SchemaInterceptorImpl.class);
	
	private final static String REGEX = "\"?" + SchemaUtils.getDefaultSchema() + "\"?" ; 
	
	static public String getSchema(){
		return  SCHEMA.get();
	}
	
	static public void setSchema(String schema){
		SCHEMA.set(schema);
	}
	
	
	
	public SchemaInterceptorImpl() {
		super();
		SCHEMA.set(null);
	}

	@Override
	public String onPrepareStatement(String sql) {
		String schema = getSchema();
		String defautSchema = SchemaUtils.getDefaultSchema();
		if (schema == null || schema.equals(defautSchema)) {
			return sql;
		}
		
		if (! schema.contains("\"")) {
			schema = '"' + schema + '"';
		}
		String newSql = sql.replaceAll(REGEX, schema);

		log.debug("réecriture requette: [{}]" , newSql);
		return newSql;
	}
	
	
	
}
