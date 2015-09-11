package se.umu.cs.ldbn.server;

import java.util.Collections;
import java.util.List;

import com.google.inject.Module;
import com.squarespace.jersey2.guice.JerseyGuiceServletContextListener;

public class LdbnServletContextListener extends JerseyGuiceServletContextListener {

    @Override
    protected List<? extends Module> modules() {
        return Collections.singletonList(new LdbnMainModule());
    }
}