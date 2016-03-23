package org.crlr.metier.utils;

import java.sql.Types;

public class HsqlDialect extends org.hibernate.dialect.HSQLDialect {

    public HsqlDialect() {
        super();
       // registerColumnType(Types.BIT, "boolean");
        registerHibernateType(Types.BOOLEAN, "boolean");

        // Assert that the new type is registered correctly.
        
    }

}
