package com.common.base;


import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

/**
 * Created by IntelliJ IDEA.
 * User: ritchrs
 * Date: 11-7-20
 * Time: 下午7:41
 * desc:
 */
public class Oracle10gDialect extends org.hibernate.dialect.Oracle10gDialect {
    public Oracle10gDialect() {
        super();
        registerHibernateType(Types.CHAR, StandardBasicTypes.STRING.getName());
    }
}
