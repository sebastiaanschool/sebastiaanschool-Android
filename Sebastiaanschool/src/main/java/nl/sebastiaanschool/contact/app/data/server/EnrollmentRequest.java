package nl.sebastiaanschool.contact.app.data.server;

public class EnrollmentRequest {
    public final String username;
    public final String password;

    public EnrollmentRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
