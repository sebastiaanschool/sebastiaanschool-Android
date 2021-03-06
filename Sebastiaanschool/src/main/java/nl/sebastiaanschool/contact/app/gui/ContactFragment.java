package nl.sebastiaanschool.contact.app.gui;


import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;

import java.util.List;

import nl.sebastiaanschool.contact.app.R;
import nl.sebastiaanschool.contact.app.data.analytics.AnalyticsInterface;

public class ContactFragment extends Fragment
        implements View.OnClickListener, AnalyticsCapableFragment {

    private AnalyticsInterface analytics;
    private String analyticsCategory;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    private TextView callButton;
    private TextView twitterButton;
    private TextView yurlsButton;
    private TextView homepageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contact, container, false);
        callButton = (TextView) view.findViewById(R.id.contact_phone);
        callButton.setOnClickListener(this);
        GrabBag.applyVectorDrawableLeft(callButton, R.drawable.ic_contact_phone_32dp);
        twitterButton = (TextView) view.findViewById(R.id.contact_twitter);
        twitterButton.setOnClickListener(this);
        GrabBag.applyBitmapDrawableLeft(twitterButton, R.drawable.ic_twitter_blue);
        yurlsButton = (TextView) view.findViewById(R.id.contact_yurls);
        yurlsButton.setOnClickListener(this);
        GrabBag.applyBitmapDrawableLeft(yurlsButton, R.drawable.ic_yurls_blue);
        homepageButton = (TextView) view.findViewById(R.id.contact_homepage);
        homepageButton.setOnClickListener(this);
        GrabBag.applyVectorDrawableLeft(homepageButton, R.drawable.ic_contact_homepage_32dp);
        return view;
    }

    @Override
    public void enableAnalytics(AnalyticsInterface analytics, String category) {
        this.analytics = analytics;
        this.analyticsCategory = category;
    }

    @Override
    public void onClick(View v) {
        if (v == callButton) {
            callSebastiaan();
        } else if (v == twitterButton) {
            if (analytics != null) {
                analytics.itemSelected(analyticsCategory, "twitter", "Twitter Page");
            }
            String twitterUrl = getString(R.string.contact__twitter_url);
            GrabBag.openUri(getContext(), twitterUrl);
        } else if (v == yurlsButton) {
            if (analytics != null) {
                analytics.itemSelected(analyticsCategory, "yurls", "Yurls Page");
            }
            String yurlsUrl = getString(R.string.contact__yurls_url);
            GrabBag.openUri(getContext(), yurlsUrl);
        } else if (v == homepageButton) {
            if (analytics != null) {
                analytics.itemSelected(analyticsCategory, "home", "Sebastiaanschool Homepage");
            }
            String homepageUrl = getString(R.string.contact__homepage_url);
            GrabBag.openUri(getContext(), homepageUrl);
        }
    }

    private void callSebastiaan() {
        final String number = getResources().getString(R.string.contact__call_url);
        final Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
        dial.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        List<ResolveInfo> handlers = getContext().getPackageManager().queryIntentActivities(dial, 0);
        boolean fail = handlers.isEmpty();
        if (!fail) {
            try {
                if (analytics != null) {
                    analytics.itemSelected(analyticsCategory, "call", "Call Sebastiaanschool");
                }
                startActivity(dial);
            } catch (Exception e) {
                fail = true;
            }
        }
        if (fail) {
            // Unlikely to occur. Tablets generally register their contacts app to handle tel: URI's.
            FirebaseCrash.logcat(Log.DEBUG, "CF", "No handler for tel: URI");
            new AlertDialog.Builder(getActivity())
                    .setCancelable(true)
                    .setMessage(R.string.call_failed_dialog_body)
                    .setNegativeButton(R.string.close_button, null)
                    .show();
        }
    }
}
