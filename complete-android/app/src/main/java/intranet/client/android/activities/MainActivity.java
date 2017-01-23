package intranet.client.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import org.grails.springsecurityrest.GrailsApi;

import java.util.ArrayList;
import java.util.List;

import intranet.client.R;
import intranet.client.android.adapters.AnnouncementAdapter;
import intranet.client.android.asynctasks.CreateAnnouncementTask;
import intranet.client.android.asynctasks.RetrieveAnnouncementsTask;
import intranet.client.android.delegates.AnnouncementAdapterDelegate;
import intranet.client.android.delegates.RetrieveAnnouncementsDelegate;
import intranet.client.network.Constants;
import intranet.client.network.model.Announcement;

public class MainActivity extends AppCompatActivity
        implements RetrieveAnnouncementsDelegate, AnnouncementAdapterDelegate {

    final private static String TAG = MainActivity.class.getSimpleName();

    public static final String EXTRA_ID = "id";
    private AnnouncementAdapter adapter;

    private static final int MENU_ADD = 0;
    private static final int MENU_LOGOUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView announcementsListView = (ListView) findViewById(R.id.announcementsListView);
        adapter = new AnnouncementAdapter(this, new ArrayList<Announcement>(), this);
        announcementsListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUi();

        String username = GrailsApi.getLoggedUsername(this);
        if ( username != null ) {
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.action_bar_title));
            sb.append(username);
            this.getSupportActionBar().setTitle(sb.toString());
        }

    }

    void refreshUi() {
        if ( shouldUserLogin() ) {
            segueToLoginActivity();
            return;
        }

        new RetrieveAnnouncementsTask(this, this).execute();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

//        if ( GrailsApi.hasRole(this, Constants.ROLE_BOSS) ) {
            menu.add(0, MENU_ADD, 0, getString(R.string.menu_add));
            menu.add(0, MENU_LOGOUT, 0, getString(R.string.menu_logout));
//        } else {
//            menu.add(0, MENU_LOGOUT, 0, getString(R.string.menu_logout));
//        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case MENU_LOGOUT:
                logout();
                break;

            case MENU_ADD:
                onAddMenuOptionTapped();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        GrailsApi.logout(this);
        refreshUi();
    }

    private boolean shouldUserLogin() {
        return !(GrailsApi.hasRole(this, Constants.ROLE_EMPLOYEE) || GrailsApi.hasRole(this, Constants.ROLE_BOSS));
    }

    private void segueToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    private void onAddMenuOptionTapped() {
        segueToCreateAnnouncementActivity();
    }

    private void segueToCreateAnnouncementActivity() {
        Intent i = new Intent(this, CreateAnnouncementActivity.class);
        startActivity(i);
    }

    @Override
    public void onAnnouncementsFetched(final List<Announcement> announcements) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.addAll(announcements);
            }
        });
    }

    @Override
    public void onAnnouncementTapped(Announcement announcement) {
        segueToAnnouncementActivity(announcement);
    }

    private void segueToAnnouncementActivity(Announcement announcement) {
        Intent i = new Intent(this, AnnouncementActivity.class);
        i.putExtra(EXTRA_ID, announcement.getId());
        startActivity(i);
    }
}
