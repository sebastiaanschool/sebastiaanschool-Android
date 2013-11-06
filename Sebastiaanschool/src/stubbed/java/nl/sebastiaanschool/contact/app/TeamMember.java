package nl.sebastiaanschool.contact.app;

/**
 * Created by barend on 3-11-13.
 */
public class TeamMember {
    private String displayName;
    private String detailText;
    private String email;

    public TeamMember() {
        // no-arg constructor for Parse.
    }

    public TeamMember(String displayName, String detailText, String email) {
        this.displayName = displayName;
        this.detailText = detailText;
        this.email = email;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getDetailText() {
        return this.detailText;
    }

    public String getEmail() {
        return this.email;
    }
}
