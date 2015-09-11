package se.umu.cs.ldbn.shared.dto;

public class HelloDto {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HelloDto)) return false;

        HelloDto hello = (HelloDto) o;

        return !(getMessage() != null ? !getMessage().equals(hello.getMessage()) : hello.getMessage() != null);

    }

    @Override
    public int hashCode() {
        return getMessage() != null ? getMessage().hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HelloDto{");
        sb.append("message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
