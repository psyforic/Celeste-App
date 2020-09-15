package com.celeste.celestedaylightapp.activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.UARTInterface;
import com.celeste.celestedaylightapp.UARTService;
import com.celeste.celestedaylightapp.data.Tools;
import com.celeste.celestedaylightapp.database.DatabaseHelper;
import com.celeste.celestedaylightapp.domain.Command;
import com.celeste.celestedaylightapp.domain.UartConfiguration;
import com.celeste.celestedaylightapp.fragment.Frag_Dashboard;
import com.celeste.celestedaylightapp.fragment.FragmentAbout;
import com.celeste.celestedaylightapp.fragment.SettingsFragment;
import com.celeste.celestedaylightapp.fragment.UsersFragment;
import com.celeste.celestedaylightapp.model.User;
import com.celeste.celestedaylightapp.profile.BleProfileService;
import com.celeste.celestedaylightapp.profile.BleProfileServiceReadyActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Visitor;
import org.simpleframework.xml.strategy.VisitorStrategy;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

import java.io.StringWriter;
import java.util.UUID;

public class MainActivity extends BleProfileServiceReadyActivity<UARTService.UARTBinder> implements UARTInterface {
    private final static String TAG = "MainActivity";
    /**
     * This preference keeps the ID of the selected configuration.
     */
    private final static String PREFS_CONFIGURATION = "configuration_id";
    private final static String PREFS_BUTTON_ENABLED = "prefs_uart_enabled_";
    private final static String PREFS_BUTTON_COMMAND = "prefs_uart_command_";
    private final static String PREFS_BUTTON_ICON = "prefs_uart_icon_";
    private static final String UTILS_CATEGORY = "com.celeste.celestedaylightapp.UTILS";
    // final ModesFragment modes = new ModesFragment();
    public ActionBar actionBar;
    boolean isNavigationHide = false;
    boolean isSearchBarHide = false;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private View search_bar;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private AppBarLayout appBarLayout;
    private View mContainer;
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("users");
    private UartConfiguration mConfiguration;
    private DatabaseHelper mDatabaseHelper;
    private SharedPreferences mPreferences;
    private UARTService.UARTBinder mServiceBinder;
    private ConfigurationListener mConfigurationListener;

    public void setConfigurationListener(final ConfigurationListener listener) {
        mConfigurationListener = listener;
    }

    @Override
    public void onDeviceSelected(BluetoothDevice device, String name) {
        super.onDeviceSelected(device, name);
    }

    @Override
    public void onServicesDiscovered(BluetoothDevice device, boolean optionalServicesFound) {
        // do nothing
    }

    // final Fragment settingsFragment = new SettingsFragment();
    @Override
    protected void onInitialize(Bundle savedInstanceState) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mDatabaseHelper = new DatabaseHelper(this);
        ensureFirstConfiguration(mDatabaseHelper);
        saveDefaultConfiguration(mDatabaseHelper);

        initializeDefaultConfiguration();

        // mConfigurationsAdapter = new UARTConfigurationsAdapter(this, this, mDatabaseHelper.getConfigurationsNames());

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.str_Dashboard);
        actionBar.setHomeButtonEnabled(true);
        Tools.setSystemBarColor(this);
    }

    @Override
    protected void onCreateView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        initToolbar();
        mAuth = FirebaseAuth.getInstance();
        setDefaultFragment();
        initComponent();
        //  displayContentView(R.id.navigation_dashboard);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {
                    //startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            }
        };
        // loadUserInfo();
        // saveConfigs();
        Tools.systemBarLolipop(this);
    }

    private void setDefaultFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, new Frag_Dashboard()).commit();
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        final View headerView = navigationView.getHeaderView(0);
        if (user != null) {
            if (user.getDisplayName() != null) {
                String displayName = user.getDisplayName();
                //  TextView textView = headerView.findViewById(R.id.header_title_name);
                //  textView.setText(displayName);
            } else {

                userReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        String displayName = user.getFirstName() + " " + user.getLastName();
                        //      TextView textView = headerView.findViewById(R.id.header_title_name);
                        //     textView.setText(displayName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    @Override
    protected void onServiceBound(UARTService.UARTBinder binder) {
        mServiceBinder = binder;
    }

    @Override
    protected void onServiceUnbound() {
        mServiceBinder = null;
    }

    @Override
    protected Class<? extends BleProfileService> getServiceClass() {
        return UARTService.class;
    }

    @Override
    protected void setDefaultUI() {

    }

    @Override
    protected int getDefaultDeviceName() {
        return 0;
    }

    @Override
    protected int getAboutTextId() {
        return 0;
    }

    @Override
    protected UUID getFilterUUID() {
        return null;
    }

    private void ensureFirstConfiguration(final DatabaseHelper mDatabaseHelper) {
        // This method ensures that the "old", single configuration has been saved to the database.
        if (mDatabaseHelper.getConfigurationsCount() == 0) {
            final UartConfiguration configuration = new UartConfiguration();
            configuration.setName("First configuration");
            final Command[] commands = configuration.getCommands();

            for (int i = 0; i < 9; ++i) {
                final String cmd = mPreferences.getString(PREFS_BUTTON_COMMAND + i, null);
                if (cmd != null) {
                    final Command command = new Command();
                    command.setCommand(cmd);
                    command.setActive(mPreferences.getBoolean(PREFS_BUTTON_ENABLED + i, false));
                    command.setEol(0); // default one
                    command.setIconIndex(mPreferences.getInt(PREFS_BUTTON_ICON + i, 0));
                    commands[i] = command;
                }
            }

            try {
                final Format format = new Format(new HyphenStyle());
                final Strategy strategy = new VisitorStrategy(new CommentVisitor());
                final Serializer serializer = new Persister(strategy, format);
                final StringWriter writer = new StringWriter();
                serializer.write(configuration, writer);
                final String xml = writer.toString();

                mDatabaseHelper.addConfiguration(configuration.getName(), xml);
            } catch (final Exception e) {
                Log.e(TAG, "Error while creating default configuration", e);
            }
        }
    }

    private void saveDefaultConfiguration(final DatabaseHelper mDatabaseHelper) {
        if (!mDatabaseHelper.configurationExists("2 Config")) {
            final UartConfiguration configuration = new UartConfiguration();
            configuration.setName("2 Config");
            final Command[] commands = configuration.getCommands();
            int eol = Command.Eol.LF.index;
            String[] cmd = {"<<100", "<<200", "<<300", "<<400", "<<c00", "<<600"};
            String[] names = {"Sunrise", "Mid-Morning", "Mid-Day", "Sun Set", "Therapy", "Off"};
            TypedArray m_icons = getApplicationContext().getResources().obtainTypedArray(R.array.default_modes_icons);
            for (int i = 0; i < 9; ++i) {
                if (i < 6) {
                    if (cmd[i] != null) {
                        final Command command = new Command();
                        command.setCommand(cmd[i]);
                        command.setActive(true);
                        command.setEol(0);
                        command.setIconIndex(i);
                        command.setCommandName(names[i]);
                        commands[i] = command;
                    }
                } else {
                    final String cmds = mPreferences.getString(PREFS_BUTTON_COMMAND + i, null);
                    if (cmd != null) {
                        final Command command = new Command();
                        command.setCommand(cmds);
                        command.setActive(mPreferences.getBoolean(PREFS_BUTTON_ENABLED + i, false));
                        command.setEol(0); // default one
                        command.setIconIndex(mPreferences.getInt(PREFS_BUTTON_ICON + i, 0));
                        command.setCommandName("Any Name");
                        commands[i] = command;
                    }
                }
            }

            try {
                final Format format = new Format(new HyphenStyle());
                final Strategy strategy = new VisitorStrategy(new CommentVisitor());
                final Serializer serializer = new Persister(strategy, format);
                final StringWriter writer = new StringWriter();
                serializer.write(configuration, writer);
                final String xml = writer.toString();

                final long id = mDatabaseHelper.addConfiguration(configuration.getName(), xml);
                mPreferences.edit().putLong(PREFS_CONFIGURATION, id).apply();
            } catch (final Exception e) {
                Log.e(TAG, "Error while creating default configuration", e);
            }
        }

    }

    private void initializeDefaultConfiguration() {
        long id = mPreferences.getLong(PREFS_CONFIGURATION, 0);
        try {
            final String xml = mDatabaseHelper.getConfiguration(id);
            final Format format = new Format(new HyphenStyle());
            final Serializer serializer = new Persister(format);
            mConfiguration = serializer.read(UartConfiguration.class, xml);
            //mConfigurationListener.onConfigurationChanged(mConfiguration);
        } catch (final Exception e) {
            Log.e(TAG, "Selecting configuration failed", e);

            String message;
            if (e.getLocalizedMessage() != null)
                message = e.getLocalizedMessage();
            else if (e.getCause() != null && e.getCause().getLocalizedMessage() != null)
                message = e.getCause().getLocalizedMessage();
            else
                message = "Unknown error";
            final String msg = message;

            return;
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.logout_message);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initComponent() {
        search_bar = (View) findViewById(R.id.search_bar);
//        mTextMessage = (TextView) findViewById(R.id.search_text);
        navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
        navigation.setItemRippleColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        navigation.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    fragment = new Frag_Dashboard();
                    actionBar.setTitle("Dashboard");
                    break;

                case R.id.navigation_settings:
                    fragment = new SettingsFragment();
                    actionBar.setTitle("Settings");
                    break;
                case R.id.navigation_modes:
                    fragment = new UsersFragment();
                    actionBar.setTitle("Users");
                    break;
                case R.id.navigation_profile:
                    fragment = new FragmentAbout();
                    actionBar.setTitle("About");
                    break;
                default:
                    fragment = new Frag_Dashboard();
                    break;
            }
            assert fragment != null;
            fragment.setArguments(bundle);
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_content, fragment);
                fragmentTransaction.commit();
            }
            return true;
        });

        NestedScrollView nested_content = (NestedScrollView) findViewById(R.id.nested_scroll_view);
        nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) { // up
                    animateNavigation(false);
//                    animateSearchBar(false);
                }
                if (scrollY > oldScrollY) { // down
                    animateNavigation(true);
//                    animateSearchBar(true);
                }
            }
        });

        Tools.setSystemBarColor(this, R.color.grey_5);
        Tools.setSystemBarLight(this);
    }


    private void animateNavigation(final boolean hide) {
        if (isNavigationHide && hide || !isNavigationHide && !hide) return;
        isNavigationHide = hide;
        int moveY = hide ? (2 * navigation.getHeight()) : 0;
        navigation.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }


    @Override
    public void send(String text) {
        if (mServiceBinder != null)
            mServiceBinder.send(text);
    }

    public interface ConfigurationListener {
        void onConfigurationModified();

        void onConfigurationChanged(final UartConfiguration configuration);

        //void setEditMode(final boolean editMode);
    }

    private class CommentVisitor implements Visitor {
        @Override
        public void read(final Type type, final NodeMap<InputNode> node) throws Exception {
            // do nothing
        }

        @Override
        public void write(final Type type, final NodeMap<OutputNode> node) throws Exception {
            if (type.getType().equals(Command[].class)) {
                OutputNode element = node.getNode();

                StringBuilder builder = new StringBuilder("A configuration must have 9 commands, one for each button.\n        Possible icons are:");
                for (Command.Icon icon : Command.Icon.values())
                    builder.append("\n          - ").append(icon.toString());
                element.setComment(builder.toString());
            }
        }
    }

}