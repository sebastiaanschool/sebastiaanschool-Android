package nl.sebastiaanschool.contact.app.gui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.sebastiaanschool.contact.app.GrabBag;
import nl.sebastiaanschool.contact.app.R;

public class ContactFragment extends Fragment implements View.OnClickListener {

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    private View callButton;
    private View twitterButton;
    private View yurlsButton;
    private View homepageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contact, container, false);
        callButton = view.findViewById(R.id.contact_phone);
        callButton.setOnClickListener(this);
        twitterButton = view.findViewById(R.id.contact_twitter);
        twitterButton.setOnClickListener(this);
        yurlsButton = view.findViewById(R.id.contact_yurls);
        yurlsButton.setOnClickListener(this);
        homepageButton = view.findViewById(R.id.contact_homepage);
        homepageButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == callButton) {
            String phoneUrl = getString(R.string.contact__call_url);
            GrabBag.openUri(getContext(), phoneUrl);
        } else if (v == twitterButton) {
            String twitterUrl = getString(R.string.contact__twitter_url);
            GrabBag.openUri(getContext(), twitterUrl);
        } else if (v == yurlsButton) {
            String yurlsUrl = getString(R.string.contact__yurls_url);
            GrabBag.openUri(getContext(), yurlsUrl);
        } else if (v == homepageButton) {
            String homepageUrl = getString(R.string.contact__homepage_url);
            GrabBag.openUri(getContext(), homepageUrl);
        }
    }
}
