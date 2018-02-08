package se.umu.cs.ldbn.server.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.inject.Singleton;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.text.SimpleDateFormat;

@Provider
@Singleton
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    public static final String DATEFORMAT="yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    static ObjectMapper newObjectMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        result.configure(SerializationFeature.INDENT_OUTPUT, true);
        result.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        result.setDateFormat(new SimpleDateFormat(DATEFORMAT));
        return result;
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return newObjectMapper();
    }
}
