package com.celeste.celestedaylightapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.celeste.celestedaylightapp.R;
import com.celeste.celestedaylightapp.fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationShifting extends AppCompatActivity {

    boolean isNavigationHide = false;
    boolean isSearchBarHide = false;
    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private View search_bar;
  //  final Fragment modes = new ModesFragment();
    final Fragment settingsFragment = new SettingsFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   initComponent();
    }

//    private void initComponent() {
//        search_bar = (View) findViewById(R.id.search_bar);
////        mTextMessage = (TextView) findViewById(R.id.search_text);
//        navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment = null;
//                Bundle bundle = new Bundle();
//                switch (item.getItemId()) {
//                    case R.id.navigation_dash:
//                        fragment = new Dashboard();
//                        break;
//                    case R.id.navigation_modes:
//                        fragment = new ModesFragment();
//                        break;
//                    case R.id.nav_settings:
//                        fragment = new SettingsFragment();
//                        break;
//                }
//                assert fragment != null;
//                fragment.setArguments(bundle);
//                if (fragment != null) {
//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.frame_content, fragment);
//                    fragmentTransaction.commit();
//                }
//                return false;
//            }
//        });
//
//        NestedScrollView nested_content = (NestedScrollView) findViewById(R.id.nested_scroll_view);
//        nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY < oldScrollY) { // up
//                    animateNavigation(false);
////                    animateSearchBar(false);
//                }
//                if (scrollY > oldScrollY) { // down
//                    animateNavigation(true);
////                    animateSearchBar(true);
//                }
//            }
//        });
//
//        Tools.setSystemBarColor(this, R.color.grey_5);
//        Tools.setSystemBarLight(this);
//    }

    private void animateNavigation(final boolean hide) {
        if (isNavigationHide && hide || !isNavigationHide && !hide) return;
        isNavigationHide = hide;
        int moveY = hide ? (2 * navigation.getHeight()) : 0;
        navigation.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    private void animateSearchBar(final boolean hide) {
        if (isSearchBarHide && hide || !isSearchBarHide && !hide) return;
        isSearchBarHide = hide;
        int moveY = hide ? -(2 * search_bar.getHeight()) : 0;
        search_bar.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

}
