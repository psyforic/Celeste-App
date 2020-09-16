package com.celeste.celestedaylightapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.UARTInterface;
import com.celeste.celestedaylightapp.UARTService;
import com.celeste.celestedaylightapp.adapter.ModeListAdapter;
import com.celeste.celestedaylightapp.data.Constants;
import com.celeste.celestedaylightapp.data.Tools;
import com.celeste.celestedaylightapp.database.DatabaseHelper;
import com.celeste.celestedaylightapp.domain.Command;
import com.celeste.celestedaylightapp.domain.UartConfiguration;
import com.celeste.celestedaylightapp.fragment.Frag_Dashboard;
import com.celeste.celestedaylightapp.model.Mode;
import com.celeste.celestedaylightapp.profile.BleProfileService;
import com.celeste.celestedaylightapp.profile.BleProfileServiceReadyActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;

import java.util.List;
import java.util.UUID;

public class AutomaticModesActivity  extends BleProfileServiceReadyActivity<UARTService.UARTBinder> implements UARTInterface, MainActivity.ConfigurationListener {
    private View parenView;
    private RecyclerView recyclerView;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private List<Mode> modes;
    private LinearLayout lyt_not_found;
    private ModeListAdapter mAdapter;
    private FloatingActionButton fab;
    private UARTService.UARTBinder mServiceBinder;
    private UartConfiguration mConfiguration;
    private SharedPreferences mPreferences;
    private final static String PREFS_CONFIGURATION = "configuration_id";

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {

    }*/

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
    protected void onCreateView(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatic_modes);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initToolbar();
        setup();
        addMode();
        initComponent();
      //  setDefaultFragment();
    }
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Automatic Modes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Tools.setSystemBarColor(this);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//
//    }
    private void initComponent() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
     //   recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);
        modes = Constants.getDefaultModesData(this);
        try {
            DatabaseHelper mDatabaseHelper = new DatabaseHelper(this);
            long id = mPreferences.getLong(PREFS_CONFIGURATION, 0);
            final String xml = mDatabaseHelper.getConfiguration(id);
            final Format format = new Format(new HyphenStyle());
            final Serializer serializer = new Persister(format);
            mConfiguration = serializer.read(UartConfiguration.class, xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdapter = new ModeListAdapter(mConfiguration, this, new ModeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Command obj, int position) {
                final Command.Eol eol = obj.getEol();
                String text = obj.getCommand();
                if (text == null)
                    text = "";
                switch (eol) {
                    case CR_LF:
                        text = text.replaceAll("\n", "\r\n");
                        break;
                    case CR:
                        text = text.replaceAll("\n", "\r");
                        break;
                }
                final UARTInterface uart = (UARTInterface) AutomaticModesActivity.this;
                // Toast.makeText(AutomaticModesActivity.this, "Clicked " + obj.getCommand(), Toast.LENGTH_LONG).show();
                uart.send(text);
            }
        });

        recyclerView.setAdapter(mAdapter);
        actionModeCallback = new ActionModeCallback();
        if (modes.size() == 0) {
            lyt_not_found.setVisibility(View.VISIBLE);
        } else {
            lyt_not_found.setVisibility(View.GONE);
        }
    }

    private void setup() {
        parenView = findViewById(android.R.id.content);
        lyt_not_found = findViewById(R.id.lyt_not_found);
        fab = findViewById(R.id.fab);
    }

    @Override
    public void send(String text) {
        if (mServiceBinder != null)
            mServiceBinder.send(text);
    }

    @Override
    public void onConfigurationModified() {

    }

    @Override
    public void onConfigurationChanged(UartConfiguration configuration) {
        mConfiguration = configuration;
        mAdapter.setConfiguration(configuration);
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            Tools.setSystemBarColor(AutomaticModesActivity.this, R.color.blue_grey_700);
            mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_delete) {
                mode.finish();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //mAdapter.clearSelections();
            actionMode = null;
            Tools.setSystemBarColor(AutomaticModesActivity.this, R.color.colorPrimaryDark);
        }
    }
    private void setDefaultFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content,new Frag_Dashboard());
        transaction.commit();

    }
    private void addMode() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddModeActivity.class));
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
}
