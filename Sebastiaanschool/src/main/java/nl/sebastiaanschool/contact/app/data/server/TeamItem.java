package nl.sebastiaanschool.contact.app.data.server;

import java.util.Comparator;

/**
 * Represents items shown on the contacts tab.
 */
@SuppressWarnings("WeakerAccess")
public class TeamItem {

    public final String displayName;
    public final String detailText;
    public final String email;
    public final int order;

    /** Canonical ID. */
    public final String url;

    public TeamItem(String displayName, String detailText, String email, int order, String url) {
        this.displayName = displayName;
        this.detailText = detailText;
        this.email = email;
        this.order = order;
        this.url = url;
    }

    public static final Comparator<TeamItem> DEFAULT_ORDER = new Comparator<TeamItem>() {
        @Override
        public int compare(TeamItem lhs, TeamItem rhs) {
            return lhs.order < rhs.order ? -1 : (lhs.order == rhs.order ? 0 : 1);
        }
    };
}
