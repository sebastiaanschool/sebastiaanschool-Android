/**
 Copyright (c) 2013 Barend Garvelink

 This program code can be used subject to the MIT license. See the LICENSE file for details.
 */
package nl.sebastiaanschool.contact.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by barend on 1-11-13.
 */
public class NavigationFragment extends Fragment implements View.OnClickListener {

    public NavigationFragment() {
        // Mandatory no-arg constructor.
    }

    private Callback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.navigate__agenda).setOnClickListener(this);
        view.findViewById(R.id.navigate__bulletin).setOnClickListener(this);
        view.findViewById(R.id.navigate__call).setOnClickListener(this);
        view.findViewById(R.id.navigate__home).setOnClickListener(this);
        view.findViewById(R.id.navigate__newsletter).setOnClickListener(this);
        view.findViewById(R.id.navigate__team).setOnClickListener(this);
        view.findViewById(R.id.navigate__twitter).setOnClickListener(this);
        view.findViewById(R.id.navigate__yurl).setOnClickListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        this.callback = (Callback) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        this.callback = null;
        super.onDetach();
    }

    void setVisible(boolean visible) {
        View view = getView();
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (callback != null) {
            switch (view.getId()) {
                case R.id.navigate__agenda:
                    callback.onItemSelected(Callback.ITEM_AGENDA);
                    break;
                case R.id.navigate__bulletin:
                    callback.onItemSelected(Callback.ITEM_BULLETIN);
                    break;
                case R.id.navigate__call:
                    callback.onItemSelected(Callback.ITEM_CALL);
                    break;
                case R.id.navigate__home:
                    callback.onItemSelected(Callback.ITEM_HOME);
                    break;
                case R.id.navigate__newsletter:
                    callback.onItemSelected(Callback.ITEM_NEWSLETTER);
                    break;
                case R.id.navigate__team:
                    callback.onItemSelected(Callback.ITEM_TEAM);
                    break;
                case R.id.navigate__twitter:
                    callback.onItemSelected(Callback.ITEM_TWITTER);
                    break;
                case R.id.navigate__yurl:
                    callback.onItemSelected(Callback.ITEM_YURLS);
                    break;
            }
        }
    }

    public interface Callback {
        final int ITEM_AGENDA = 1;
        final int ITEM_BULLETIN = 2;
        final int ITEM_CALL = 3;
        final int ITEM_HOME = 4;
        final int ITEM_NEWSLETTER = 5;
        final int ITEM_TEAM = 6;
        final int ITEM_TWITTER = 7;
        final int ITEM_YURLS = 8;

        void onItemSelected(int item);
    }
}
