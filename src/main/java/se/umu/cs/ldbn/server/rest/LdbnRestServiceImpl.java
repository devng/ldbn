package se.umu.cs.ldbn.server.rest;


import se.umu.cs.ldbn.shared.dto.HelloDto;

import com.google.inject.Singleton;

@Singleton
public class LdbnRestServiceImpl implements LdbnRestService {

    @Override
    public HelloDto sayHello() {
        final HelloDto hello = new HelloDto();
        hello.setMessage("I just want to say hello.");
        return hello;
    }
}
