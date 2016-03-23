/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: OperationLdap.java,v 1.11 2010/06/04 14:40:15 jerome.carriere Exp $
 */

package org.crlr.alimentation;


import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
 
public final class MyPagedResultsDirContextProcessor extends
  PagedResultsDirContextProcessor {
  public MyPagedResultsDirContextProcessor(int pageSize,
    PagedResultsCookie cookie) {
    super(pageSize, cookie);
  }
 
  @Override
  public void preProcess(DirContext ctx) throws NamingException {
    if (ctx instanceof LdapContext) {
      final LdapContext ldapContext = (LdapContext) ctx;
      ldapContext.setRequestControls(new Control[] { createRequestControl() });
    } else {
      throw new IllegalArgumentException("Request Control operations require "
        + "LDAPv3 - Context must be of type LdapContext");
    }
  }
 
  @Override
  public void postProcess(DirContext ctx) throws NamingException {
    final LdapContext ldapContext = (LdapContext) ctx;
    ldapContext.setRequestControls(null);
    super.postProcess(ldapContext);
  }
}