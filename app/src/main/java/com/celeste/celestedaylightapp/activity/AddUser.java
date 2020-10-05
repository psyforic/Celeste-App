package com.celeste.celestedaylightapp.activity;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.fragment.addUser.FragmentAddModes;
import com.celeste.celestedaylightapp.fragment.addUser.FragmentAddUser;
import com.celeste.celestedaylightapp.fragment.addUser.FragmentUserAddress;
import com.celeste.celestedaylightapp.fragment.addUser.FragmentUserRoles;
import com.celeste.celestedaylightapp.utils.Tools;
import com.celeste.celestedaylightapp.utils.ViewAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddUser extends AppCompatActivity {

    private static final int MAX_STEP = 4;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private ViewPager view_pager;
    private int current_step = 1;
    private ProgressBar progressBar;
    private TextView status;
    private SectionsPagerAdapter viewPagerAdapter;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        initComponent();
        initToolbar();

    }


    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Add User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Tools.setSystemBarColor(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initComponent() {
        findViewById(R.id.lyt_back).setVisibility(View.INVISIBLE);
        status = (TextView) findViewById(R.id.status);
        mFragmentList.add(0, new FragmentAddUser());
        mFragmentList.add(1, new FragmentUserAddress());
        mFragmentList.add(2, new FragmentUserRoles());
        mFragmentList.add(3, new FragmentAddModes());
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setMax(MAX_STEP);
        progressBar.setProgress(current_step);
        progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        chooseFragment(mFragmentList.get(current_step - 1));
        ((LinearLayout) findViewById(R.id.lyt_back)).setOnClickListener((View.OnClickListener) view -> backStep(current_step));

        ((LinearLayout) findViewById(R.id.lyt_next)).setOnClickListener((View.OnClickListener) view -> {
            nextStep(current_step);
            if (current_step == MAX_STEP) {
                findViewById(R.id.lyt_next).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.btn_next)).setText(R.string.str_done);
            }
        });
        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        status.setText(str_progress);
    }

    private void nextStep(int progress) {
        if (progress < MAX_STEP) {
            progress++;
            current_step = progress;
            ViewAnimation.fadeOutIn(status);
        }
        if (current_step == MAX_STEP) {
            findViewById(R.id.lyt_next).setVisibility(View.INVISIBLE);
            if (((TextView) findViewById(R.id.btn_next)).getText().equals("DONE")) {
                ((LinearLayout) findViewById(R.id.lyt_back)).setVisibility(View.VISIBLE);
            }
        } else {
            findViewById(R.id.lyt_next).setVisibility(View.VISIBLE);
        }
        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        status.setText(str_progress);
        progressBar.setProgress(current_step);
        chooseFragment(mFragmentList.get(current_step - 1));
    }

    private void backStep(int progress) {
        if (progress > 1) {
            progress--;
            current_step = progress;
            ViewAnimation.fadeOutIn(status);
        }
        if (progress == 1) {
            findViewById(R.id.lyt_back).setVisibility(View.INVISIBLE);
        }
        else
        {
            findViewById(R.id.lyt_back).setVisibility(View.VISIBLE);
        }
        if (current_step == MAX_STEP - 1) {
            findViewById(R.id.lyt_next).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.btn_next)).setText(getString(R.string.str_next));
        }
        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
        status.setText(str_progress);
        progressBar.setProgress(current_step);
        chooseFragment(mFragmentList.get(current_step - 1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
//
//    private void initComponent() {
//        view_pager = (ViewPager) findViewById(R.id.view_pager);
//        setupViewPager(view_pager);
//        // status = (TextView) findViewById(R.id.status);
//        progressBar = (ProgressBar) findViewById(R.id.progress);
//        progressBar.setMax(MAX_STEP);
//        progressBar.setProgress(current_step);
//        progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
//
//        ((LinearLayout) findViewById(R.id.lyt_back)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                backStep(current_step);
//            }
//        });
//
//        ((LinearLayout) findViewById(R.id.lyt_next)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                nextStep(current_step);
//            }
//        });
//        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
//        //  status.setText(str_progress);
//    }
//
//    private void nextStep(int progress) {
//        if (progress < MAX_STEP) {
//            progress++;
//            current_step = progress;
//            switch (current_step) {
//                case 1:
//                    FragmentAddUser fragmentAddUser = new FragmentAddUser();
//                    chooseFragment(fragmentAddUser);
//                    break;
//                case 2:
//                    FragmentUserAddress fragmentUserAddress = new FragmentUserAddress();
//                    chooseFragment(fragmentUserAddress);
//                    break;
//
//            }
//            view_pager.setAdapter(viewPagerAdapter);
//            setupViewPager(view_pager);
//            //   ViewAnimation.fadeOutIn(status);
//        }
//        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
//        //   status.setText(str_progress);
//        progressBar.setProgress(current_step);
//    }
//
//    private void backStep(int progress) {
//        if (progress > 1) {
//            progress--;
//            current_step = progress;
//            //   ViewAnimation.fadeOutIn(status);
//        }
//        String str_progress = String.format(getString(R.string.step_of), current_step, MAX_STEP);
//        //   status.setText(str_progress);
//        progressBar.setProgress(current_step);
//    }
//
//    private void setupViewPager(ViewPager viewPager) {
//        viewPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        viewPagerAdapter.addFragment(FragmentAddUser.newInstance(), "User");    // index 0
//        viewPagerAdapter.addFragment(FragmentUserAddress.newInstance(), "Address");   // index 1
//        viewPagerAdapter.addFragment(FragmentUserRoles.newInstance(), "Roles");
//        viewPagerAdapter.addFragment(FragmentAddModes.newInstance(), "Modes");    // index 2
//        viewPager.setAdapter(viewPagerAdapter);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // getMenuInflater().inflate(R.menu.menu_search_setting, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        } else {
//            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void chooseFragment(Fragment fragment) {
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(R.id.content_frame, fragment).addToBackStack(null).commit();
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
