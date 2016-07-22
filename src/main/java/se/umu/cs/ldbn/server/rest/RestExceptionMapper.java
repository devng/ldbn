package se.umu.cs.ldbn.server.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.umu.cs.ldbn.shared.dto.ErrorDto;
import se.umu.cs.ldbn.shared.dto.RestErrorCode;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
public class RestExceptionMapper implements ExceptionMapper<Exception> {

	private static final Logger log = LoggerFactory.getLogger(RestExceptionMapper.class);

    private final ObjectMapper objectMapper;

    public RestExceptionMapper() {
        objectMapper = ObjectMapperProvider.newObjectMapper();
    }

    @Override
    public Response toResponse(final Exception ex) {
        if (ex instanceof IllegalArgumentException) {
            return errorResponse(400, "Malformed REST request. " + ex.getMessage());
        }

        if (ex instanceof WebApplicationException) {
            WebApplicationException waex = (WebApplicationException) ex;
            return errorResponse(waex.getResponse().getStatus(), waex.getMessage());
        }

        log.error("Unexpected exception in REST interface.", ex);
        return errorResponse(RestErrorCode.SERVER_ERROR, null, "Unknown error in REST interface.");
    }

    /**
     * Build a JSON error response based on a HTTP status code.
     *
     * @param statusCode
     *            HTTP status code, which is going to be the error code too.
     * @param errorMessage
     *            optional human readable message.
     *
     * @return
     */
    private Response errorResponse(final int statusCode, final String errorMessage) {
        final ErrorDto errorDto = new ErrorDto(statusCode, null, errorMessage);

        return Response.status(statusCode).entity(toJson(errorDto)).type(MediaType.APPLICATION_JSON_TYPE).build();
    }


    /**
     * Build a JSON error response based on a HTTP status code.
     *
     * @param errorCode
     *            Ldbn error and status code as an enum.
     * @param errorValue
     *            optional machine readable message.
     * @param errorMessage
     *            optional human readable message.
     *
     * @return
     */
    private Response errorResponse(final RestErrorCode errorCode, final String errorValue, final String errorMessage) {
        final ErrorDto errorDto = new ErrorDto(errorCode.getErrorCode(), errorValue, errorMessage);

        return Response.status(errorCode.getStatusCode()).entity(toJson(errorDto)).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    private String toJson(ErrorDto errorDto) {
        String errorJson = null;
        try {
            errorJson = objectMapper.writeValueAsString(errorDto);
        } catch (Exception e) {
            log.error("Could not create error JSON:", e);
        }
        return errorJson;
    }
}
