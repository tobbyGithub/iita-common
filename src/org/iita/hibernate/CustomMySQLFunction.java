/**
 * 
 */
package org.iita.hibernate;

/**
 * @author ken
 *
 */
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.StandardSQLFunction;

public class CustomMySQLFunction extends MySQL5InnoDBDialect{
       public CustomMySQLFunction()
       {
              super();
              registerFunction("puredigits", new StandardSQLFunction("puredigits"));
       }
}