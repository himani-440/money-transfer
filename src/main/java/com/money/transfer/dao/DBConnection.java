package com.money.transfer.dao;

import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.ThreadLocalTransactionProvider;

public class DBConnection {

	public static DSLContext getDSLContext() throws Exception {
		final BasicDataSource ds = new BasicDataSource();
		final Properties properties = new Properties();
		properties.load(DBConnection.class.getResourceAsStream("/config.properties"));

		ds.setDriverClassName(properties.getProperty("db.driver"));
		ds.setUrl(properties.getProperty("db.url"));
		ds.setUsername(properties.getProperty("db.username"));
		ds.setPassword(properties.getProperty("db.password"));

		final ConnectionProvider cp = new DataSourceConnectionProvider(ds);
		final Configuration configuration = new DefaultConfiguration()
				.set(cp).set(SQLDialect.H2)
				.set(new Settings().withExecuteWithOptimisticLocking(true))
				.set(new ThreadLocalTransactionProvider(cp, true));
		return DSL.using(configuration);
	}

}
