package nl.sebastiaanschool.contact.app;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Secondary activity launched to show stuff off the navigation page.
 */
public class DetailActivity extends AppCompatActivity implements DataLoadingCallback, NewsletterFragment.Callback {
    private static final String ARG_MODE = "mode";
    public static final int MODE_AGENDA = 945;
    public static final int MODE_BULLETIN = 784;
    public static final int MODE_NEWSLETTER = 959;
    public static final int MODE_SETTINGS = 260;
    public static final int MODE_TEAM = 636;

    private NewsletterDownloadHelper newsletterDownloadHelper;
    private ProgressBar progressBar;

    public static void start(Context context, @Mode int mode) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra(ARG_MODE, mode);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        setSupportActionBar((Toolbar) findViewById(R.id.action_bar));
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            int mode = getIntent().getIntExtra(ARG_MODE, -1);
            Fragment fragment;
            switch (mode) {
                case MODE_AGENDA:
                    fragment = AgendaFragment.newInstance();
                    break;
                case MODE_BULLETIN:
                    fragment = BulletinFragment.newInstance();
                    break;
                case MODE_NEWSLETTER:
                    fragment = NewsletterFragment.newInstance();
                    break;
                case MODE_SETTINGS:
                    fragment = SettingsFragment.newInstance();
                    break;
                case MODE_TEAM:
                    fragment = TeamFragment.newInstance();
                    break;
                default:
                    throw new IllegalStateException();
            }
            setTitle(((SebFragment) fragment).getTitleResId());
            getFragmentManager().beginTransaction().add(R.id.detail__content_container, fragment).commit();
        }
    }

    @Override
    public void onStartLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStopLoading(Exception e) {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void downloadNewsletterFromUri(Uri uri) {
        if (newsletterDownloadHelper == null) {
            newsletterDownloadHelper = new NewsletterDownloadHelper(this);
        }
        newsletterDownloadHelper.downloadNewsletterFromUri(uri);
    }

    @IntDef({MODE_AGENDA, MODE_BULLETIN, MODE_NEWSLETTER, MODE_SETTINGS, MODE_TEAM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }
}
