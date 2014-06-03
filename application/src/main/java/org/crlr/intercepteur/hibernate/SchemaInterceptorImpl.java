package org.crlr.intercepteur.hibernate;

import java.util.regex.Pattern;

import org.apache.oro.text.PatternCacheFIFO;
import org.crlr.metier.utils.SchemaUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;

public class SchemaInterceptorImpl extends EmptyInterceptor implements
		Interceptor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 343527089711398834L;
	
	final static private ThreadLocal<String>  SCHEMA = new ThreadLocal<String>();

	
	
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

		
		return newSql;
	}
	
	
	
}
