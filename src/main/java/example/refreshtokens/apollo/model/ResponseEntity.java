package example.refreshtokens.apollo.model;

public class ResponseEntity {

    private String message;
    private int statusCode;

    public ResponseEntity () {

    }

    public ResponseEntity (String message, int statusCode) {
        this.message=message;
        this.statusCode=statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
