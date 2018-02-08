package se.umu.cs.ldbn.server;

import com.google.inject.Module;
import com.squarespace.jersey2.guice.JerseyGuiceServletContextListener;

import java.util.Collections;
import java.util.List;

public class LdbnServletContextListener extends JerseyGuiceServletContextListener {

    @Override
    protected List<? extends Module> modules() {
        return Collections.singletonList(new LdbnMainModule());
    }
}