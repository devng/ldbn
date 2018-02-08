package se.umu.cs.ldbn.shared.dto;

/**
 * This DTO is used to pass error codes and messages via the REST interface from the server to the client.
 */
public class ErrorDto {

    /** The error status code. */
    private int errorCode;

    /** (Optional) A machine readable error value. */
    private String errorValue;

    /** (Optional) A human readable error text. */
    private String errorText;

    /** Used for serialization only. */
    ErrorDto() {
        super();
    }

    public ErrorDto(int errorCode, String errorValue, String errorText) {
        this.errorCode = errorCode;
        this.errorText = errorText;
        this.errorValue = errorValue;
    }

    public int getErrorCode() {
        return errorCode;
    }

    void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorValue() {
        return errorValue;
    }

    void setErrorValue(String errorValue) {
        this.errorValue = errorValue;
    }

    public String getErrorText() {
        return errorText;
    }

    void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorDto)) return false;

        ErrorDto errorDto = (ErrorDto) o;

        if (getErrorCode() != errorDto.getErrorCode()) return false;
        if (getErrorValue() != null ? !getErrorValue().equals(errorDto.getErrorValue()) : errorDto.getErrorValue() != null)
            return false;
        return !(getErrorText() != null ? !getErrorText().equals(errorDto.getErrorText()) : errorDto.getErrorText() != null);

    }

    @Override
    public int hashCode() {
        int result = getErrorCode();
        result = 31 * result + (getErrorValue() != null ? getErrorValue().hashCode() : 0);
        result = 31 * result + (getErrorText() != null ? getErrorText().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorDto{");
        sb.append("errorCode=").append(errorCode);
        sb.append(", errorValue='").append(errorValue).append('\'');
        sb.append(", errorText='").append(errorText).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
