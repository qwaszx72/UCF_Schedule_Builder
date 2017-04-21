package com.poos.cop.schedulebuilder;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mContent;
    public final static String FRAGMENT_NAME = "myFragment";

    public static final String TAG = "ScheduleBuilder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ClassUtils.initCart(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState != null) {
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_NAME);
        }

        if (savedInstanceState == null) {
            SearchFragment fragment = new SearchFragment();
            switchFragment(fragment);

        }

        verifyClassDatabase();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        return;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //if(mContent != null) {
        //    getSupportFragmentManager().putFragment(outState, FRAGMENT_NAME, mContent);
        //}
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.update_class_database) {
            updateClassDatabase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_find_classes) {
            findClasses();
        } else if (id == R.id.nav_view_cart) {
            viewCart();
        } else if (id == R.id.nav_create_schedules) {
            createSchedules();
        } else if (id == R.id.nav_help) {
            HelpFragment fragment = new HelpFragment();
            switchFragment(fragment);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void switchFragment(Fragment fragment, boolean addToBackStack) {
        if(addToBackStack) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, FRAGMENT_NAME)
                    .addToBackStack("back")
                    .commit();
        } else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            for(int i = 0; i < count; i++)
                getSupportFragmentManager().popBackStackImmediate();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, FRAGMENT_NAME)
                    .commit();
        }
        mContent = fragment;
    }

    public void switchFragment(Fragment fragment) {
        switchFragment(fragment, false);
    }

    public void executeSearch(final SearchQuery searchQuery) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Searching...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                ArrayList<Section> results = new ArrayList<>();
                ArrayList<Section> sections = ClassUtils.getSections(getApplicationContext());
                for(Section s : sections)
                    if(s.matches(searchQuery))
                        results.add(s);
                Collections.sort(results);

                final SearchResultsFragment fragment = new SearchResultsFragment();
                fragment.setResults(results);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switchFragment(fragment, true);
                    }
                });

                dialog.dismiss();
            }
        };
        mThread.start();
    }

    public void executeScheduleSearch(final ScheduleQuery scheduleQuery) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Finding schedules...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                ArrayList<Section> validSections = new ArrayList<>();
                ArrayList<Section> sections = ClassUtils.getSections(getApplicationContext());
                for(Section s : sections)
                    if(s.inCart)
                        validSections.add(s);

                final ArrayList<Schedule> schedules = Schedule.buildSchedules(scheduleQuery, validSections);

                //System.out.println(schedules);

                final ScheduleResultsFragment fragment = new ScheduleResultsFragment();
                fragment.setSchedules(schedules);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switchFragment(fragment, true);
                    }
                });

                dialog.dismiss();
            }
        };
        mThread.start();
    }

    public void viewCart() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Loading cart...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                ArrayList<Section> cartContents = new ArrayList<>();
                ArrayList<Section> sections = ClassUtils.getSections(getApplicationContext());
                for(Section s : sections)
                    if(s.inCart)
                        cartContents.add(s);
                Collections.sort(cartContents);

                final ViewCartFragment fragment = new ViewCartFragment();
                fragment.setCartContents(cartContents);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switchFragment(fragment, false);
                    }
                });

                dialog.dismiss();
            }
        };
        mThread.start();
    }

    public void createSchedules() {
        final CreateSchedulesFragment fragment = new CreateSchedulesFragment();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switchFragment(fragment, false);
            }
        });
    }

    public void findClasses() {
        final SearchFragment fragment = new SearchFragment();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switchFragment(fragment, false);
            }
        });
    }

    public void viewClassDetails(Section section) {
        ViewClassFragment fragment = new ViewClassFragment();
        fragment.setSection(section);
        switchFragment(fragment, true);
    }

    public void viewSchedule(Schedule schedule) {
        ViewScheduleFragment fragment = new ViewScheduleFragment();
        fragment.setSchedule(schedule);
        switchFragment(fragment, true);
    }

    public void displayToastLong(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void displayToastShort(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void toggleSectionInCart(Section section) {
        section.inCart = !section.inCart;

        if(section.inCart && ClassUtils.CART != null) {
            ClassUtils.CART.add(new SectionPair(section));
            ClassUtils.saveCart(this);
        } else if(ClassUtils.CART != null) {
            ClassUtils.CART.remove(new SectionPair(section));
            ClassUtils.saveCart(this);
        }

        if(section.inCart)
            displayToastShort("Added class to cart");
        else
            displayToastShort("Removed class from cart");
    }

    public void clearCart() {
        if(ClassUtils.CART != null) {
            ClassUtils.CART.clear();
            ClassUtils.saveCart(this);
        }
        displayToastShort("Cleared cart successfully");
    }

    public void exportNbrs(Schedule schedule) {
        if(schedule == null || schedule.getNumSections() == 0)
            return;
        ClipboardManager clipMan = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipMan.setPrimaryClip(ClipData.newPlainText("Schedule NBRs", schedule.getNbrsString()));
        displayToastShort("Class NBRs copied to clipboard");
    }

    public void verifyClassDatabase() {
        long lastUpdateTime = ClassUtils.getLastDatabaseUpdateTime(this);
        if(lastUpdateTime == -1)
            updateClassDatabase();
    }

    public void updateClassDatabase() {
        long lastUpdateTime = ClassUtils.getLastDatabaseUpdateTime(this);

        if(System.currentTimeMillis() - lastUpdateTime < 5 * 60 * 1000) {
            displayToastShort("You have already updated the database in the last 5 minutes!");
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Updating class database...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://mcpeshare.com/schedule_builder/public/courses.php?key=70299BD27DCDF5DAF563068F5F82895E6FE377CD8C5CF724E98D6BF7B14D0A70");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    while(true) {
                        String line = br.readLine();
                        if(line == null)
                            break;
                        sb.append(line);
                        sb.append('\n');
                    }

                    // Only allow responses that are long enough to be what we want
                    if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK && sb.length() > 10000) {
                        boolean status = ClassUtils.updateClassDatabase(MainActivity.super.getApplicationContext(), sb.toString());
                        if(status)
                            onUpdateDatabaseSuccess();
                        else
                            onUpdateDatabaseError();
                    } else {
                        onUpdateDatabaseError();
                    }
                } catch(Exception e) {
                    onUpdateDatabaseError();
                }
                dialog.dismiss();
            }
        };
        mThread.start();

    }

    public void onUpdateDatabaseError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayToastShort("Failed to update class database!");
            }
        });
    }

    public void onUpdateDatabaseSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayToastShort("Successfully updated class database!");
            }
        });

    }
}
