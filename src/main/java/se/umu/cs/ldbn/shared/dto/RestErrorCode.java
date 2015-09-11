package se.umu.cs.ldbn.shared.dto;

/**
 * This enumeration represents the different HTTP status and REST error codes, which the API can return.
 * The first 3 digits of a status codes must be the same as in the error code.
 */
public enum RestErrorCode {

    BAD_REQUEST(400, 400),
    BAD_REQUEST_MISSING_JSON(400, 400001),
    BAD_REQUEST_INVALID_JSON_PARAM(400, 400002),
    BAD_REQUEST_INVALID_URL_PARAM(400, 400003),
    UNAUTHORIZED(401, 401),
    NOT_FOUND(404, 404),
    CONFLICT(409, 409),
    SERVER_ERROR(500, 500);


    /** The HTTP status code. */
    private int statusCode;

    /** The LDBN error code. The first 3 digits must match the status code. Max length is 6 digits. */
    private int errorCode;

    RestErrorCode(int statusCode, int errorCode) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
