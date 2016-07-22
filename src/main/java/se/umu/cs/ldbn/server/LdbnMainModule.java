package se.umu.cs.ldbn.server;

import com.squarespace.jersey2.guice.JerseyModule;
import se.umu.cs.ldbn.server.dao.LdbnDaoModule;
import se.umu.cs.ldbn.server.rest.LdbnRestModule;

import java.util.Properties;

public class LdbnMainModule extends JerseyModule {
	
	private static final String LDBN_PROPERTIES = "/ldbn.properties";

    @Override
    protected void configure() {
    	install(new LdbnDaoModule(loadLdbnProperties()));
        install(new LdbnRestModule());
    }
    
    private Properties loadLdbnProperties() {
    	final Properties properties = new Properties();
        try {
        	properties.load(this.getClass().getResourceAsStream(LDBN_PROPERTIES));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
