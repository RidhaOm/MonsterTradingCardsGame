package Server;

public enum HttpStatus {
    OK(200, "OK"),
    FORBIDDEN(403, "Forbidden");

    public final int statusCode;
    public final String statusMessage;

    HttpStatus(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
