package com.markzhai.lyrichere.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.markzhai.lyrichere.Constants;
import com.markzhai.lyrichere.R;
import com.markzhai.lyrichere.prefs.SettingsActivity;
import com.markzhai.lyrichere.utils.LogHelper;
import com.markzhai.lyrichere.workers.Finder;
import com.markzhai.lyrichere.workers.LyricOpener;

public class LyricExplorerActivity extends ActionBarCastActivity implements  LyricExplorerFragment
        .OnFragmentInteractionListener, DownloadFragment.OnFragmentInteractionListener {
    private static final String TAG = LyricExplorerActivity.class.getSimpleName();

    @Override
    public void onResume() {
        super.onResume();
        LogHelper.i(TAG, "onResume");

        if (getIntent() != null) {
            String title = getIntent().getStringExtra(Constants.Column.TITLE);
            if (!TextUtils.isEmpty(title)) {
                String artist = getIntent().getStringExtra(Constants.Column.ARTIST);
                String album = getIntent().getStringExtra(Constants.Column.ALBUM);
                LogHelper.i(TAG, title);
                new LyricOpener(this).execute(title, artist, album);
            }
            setIntent(null);
        }
    }

    /**
     * Override onNewIntent to get new intent when re-entering
    @Override
    protected void onNewIntent(Intent intent) {
        LogHelper.i(TAG, "onNewIntent");
        if (intent.getStringExtra(Constants.Column.TITLE) != null)
            LogHelper.i(TAG, intent.getStringExtra(Constants.Column.TITLE));
        super.onNewIntent(intent);
        setIntent(intent);
    }
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogHelper.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric_explorer);
        initializeToolbar();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsFragment fragment = new SlidingTabsFragment();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lyric_explorer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                new Finder(this).execute(Environment.getExternalStorageDirectory());
                break;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(String text) {
        if (!TextUtils.isEmpty(text)) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }
}