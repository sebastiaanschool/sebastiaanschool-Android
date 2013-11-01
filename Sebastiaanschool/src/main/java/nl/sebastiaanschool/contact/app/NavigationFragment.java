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

    @Override
    public void onClick(View view) {
        if (callback != null) {
            switch (view.getId()) {
                case R.id.navigate__agenda:
                    callback.onItemSelected(Callback.ITEM_AGENDA);
                    break;
            }
        }
    }

    public interface Callback {
        final int ITEM_AGENDA = 1;
        final int ITEM_BULLETIN = 2;
        final int ITEM_CALL = 3;
        final int ITEM_NEWSLETTER = 4;
        final int ITEM_TEAM = 5;
        final int ITEM_TWITTER = 6;
        final int ITEM_YURL = 7;

        void onItemSelected(int item);
    }
}
