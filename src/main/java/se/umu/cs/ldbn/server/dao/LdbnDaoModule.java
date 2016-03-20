package se.umu.cs.ldbn.server.dao;

import java.util.Properties;

import javax.inject.Singleton;

import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.UnpooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import se.umu.cs.ldbn.server.dao.typehandlers.DateTypeHandler;
import se.umu.cs.ldbn.server.dao.typehandlers.LocalDateTimeTypeHandler;

import com.google.inject.name.Names;

public class LdbnDaoModule extends MyBatisModule {

	private final Properties ldbnProperties;
	
	public LdbnDaoModule(Properties ldbnProperties) {
		super();
		// MyBatis helper links:
		// https://www.javacodegeeks.com/2012/11/mybatis-tutorial-crud-operations-and-mapping-relationships-part-1.html
		// http://www.slideshare.net/simonetripodi/mybatis-googleguice
		// http://www.concretepage.com/mybatis-3/mybatis-3-annotation-example-with-select-insert-update-and-delete
		this.ldbnProperties = ldbnProperties;
	}
	
	
    @Override
    protected void initialize() {
    	if (Boolean.valueOf(ldbnProperties.getProperty("ldbn.useMySQL", "false"))) {
    		install(JdbcHelper.MySQL);
    	} else {
    		install(JdbcHelper.SQLITE_FILE);
    	}
    	
    	Names.bindProperties(binder(), ldbnProperties);
    	
    	bindDataSourceProviderType(UnpooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        addMapperClass(AssignmentMapper.class);
    	addTypeHandlerClass(LocalDateTimeTypeHandler.class);
    	addTypeHandlerClass(DateTypeHandler.class);
        
        bind(AssignmentDao.class).to(AssignmentDaoImpl.class).in(Singleton.class);
        
    }

}
