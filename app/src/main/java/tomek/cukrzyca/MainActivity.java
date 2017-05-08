package tomek.cukrzyca;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import tomek.cukrzyca.Fragments.AddNewRegistrationFragment;
import tomek.cukrzyca.Fragments.Calculator.CalculatorFragment;
import tomek.cukrzyca.Fragments.HelpFragment;
import tomek.cukrzyca.Fragments.HistoryElement;
import tomek.cukrzyca.Fragments.HistoryFragment;
import tomek.cukrzyca.Fragments.MainFragment;
import tomek.cukrzyca.Fragments.SettingsFragment;
import tomek.cukrzyca.Fragments.StatsFragment;
import tomek.cukrzyca.Fragments.UserDataFragment;
import tomek.cukrzyca.Fragments.WW.WwFragment;
import tomek.cukrzyca.Notifications.NotificationService;
import tomek.cukrzyca.Notifications.NotificationsFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;
    MainFragment fragment = null;

    public static boolean paused = false;

    public static Database db;

    public static String currentFragment;
    public static boolean fromCalc = false;

    private String[] fragmentsToBack = {"wwFragment", "newRegistrationFragment", "calcFragment", "notifsFragment", "settingsFragment",
                "historyFragment", "statsFragment"};

    public boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotificationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new Database(getApplicationContext());

        fragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        if (!isMyServiceRunning()) {
            Intent notificationService = new Intent(this, NotificationService.class);
            startService(notificationService);
        }

        String sQuery = "SELECT * FROM " + Database.USER_TABLE + ";";

        Cursor cursor = MainActivity.db.query(sQuery);

        if (cursor == null || !cursor.moveToFirst()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(Html.fromHtml("Witaj!<br><br>" +
                    "Nie uzupełniłeś jeszcze danych osobistych.<br><br>" +
                    "Jeżeli chcesz to zrobić teraz, naciśnij <b>Idź</b>.<br><br>" +
                    "Jeżeli chcesz to zrobić później, naciśnij <b>Anuluj</b>."))
                    .setCancelable(false)
                    .setPositiveButton("Idź", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            UserDataFragment fragment = new UserDataFragment();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                            fragmentTransaction.replace(R.id.fragment_container, fragment);
                            fragmentTransaction.addToBackStack(MainActivity.currentFragment);
                            fragmentTransaction.commit();
                        }
                    })
                    .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_plus: {
                if (!currentFragment.equals("newRegistrationFragment")) {
                    HistoryFragment.id = -1;

                    AddNewRegistrationFragment fragment = new AddNewRegistrationFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(currentFragment);
                    fragmentTransaction.commit();
                }
                return true;
            }

            case R.id.action_calculator: {
                if (!currentFragment.equals("calcFragment")) {
                    HistoryFragment.id = -1;

                    CalculatorFragment fragmentCalc = new CalculatorFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    fragmentTransaction.replace(R.id.fragment_container, fragmentCalc);
                    fragmentTransaction.addToBackStack(currentFragment);
                    fragmentTransaction.commit();
                }

                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        for (String name : fragmentsToBack) {
            if (currentFragment.equals(name)) {

                if(name.equals("newRegistrationFragment") || name.equals("calcFragment")) {
                    if(HistoryFragment.id == -1) {
                        getSupportFragmentManager().popBackStack("mainFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        return;
                    }
                } else {
                    getSupportFragmentManager().popBackStack("mainFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    HistoryFragment.id = -1;
                    return;
                }
            }
        }

        HistoryFragment.id = -1;
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();

        paused = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        paused = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ww && !currentFragment.equals("wwFragment")) {
            WwFragment fragment = new WwFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(currentFragment);
            fragmentTransaction.commit();
        }

        if (id == R.id.nav_notifications && !currentFragment.equals("notifsFragment")) {
            NotificationsFragment fragmentRegister = new NotificationsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.fragment_container, fragmentRegister);
            fragmentTransaction.addToBackStack(currentFragment);
            fragmentTransaction.commit();
        }

        if (id == R.id.nav_settings && !currentFragment.equals("settingsFragment")) {
            SettingsFragment fragment = new SettingsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(currentFragment);
            fragmentTransaction.commit();
        }

        if (id == R.id.nav_help) {
            //SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE);
            //SharedPreferences.Editor editor;
            //editor = sharedPref.edit();
            //editor.putInt("custID", 0).apply();
            //db.dropTable(Database.MEALS_TABLE);
            //db.dropTable(Database.PRODUCTS_TABLE);
            //db.dropTable(Database.PRODUCTS_IN_MEALS_TABLE);
            //db.dropTable(Database.WW_TABLE);
            //db.dropTable(Database.CALC_SETTINGS_TABLE);
            //db.dropTable(Database.NOTIFICATIONS_TABLE);
            //db.dropTable(Database.USER_TABLE);

            HelpFragment helpFragment = new HelpFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.fragment_container, helpFragment);
            fragmentTransaction.addToBackStack(currentFragment);
            fragmentTransaction.commit();
        }

        if(id == R.id.nav_history && !currentFragment.equals("historyFragment")
                && HistoryFragment.id == -1) {
            HistoryFragment historyFragment = new HistoryFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.fragment_container, historyFragment);
            fragmentTransaction.addToBackStack(currentFragment);
            fragmentTransaction.commit();
        }

        if(id == R.id.nav_stats && !currentFragment.equals("statsFragment")) {
            StatsFragment statsFragment = new StatsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(R.id.fragment_container, statsFragment);
            fragmentTransaction.addToBackStack(currentFragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
