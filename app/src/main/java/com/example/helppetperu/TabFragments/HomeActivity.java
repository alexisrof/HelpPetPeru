package com.example.helppetperu.TabFragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.helppetperu.R;
import com.example.helppetperu.TabFragments.Home.HomeFragment;
import com.example.helppetperu.TabFragments.Pet.PetFragment;
import com.example.helppetperu.TabFragments.Profile.ProfileFragment;
import com.example.helppetperu.TabFragments.Help.HelpFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /*
        bottomNavigationView = findViewById(R.id.BottomNavigationView_HomePage);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        */
        openFragment(HomeFragment.newInstance("",""));
        chipNavigationBar = findViewById(R.id.ChipBottomNavigation);
        chipNavigationBar.setOnItemSelectedListener(onItemSelectedListener);
        chipNavigationBar.setItemSelected(R.id.navigation_home,true);

    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.Frame_FragmentsPageContainer, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    ChipNavigationBar.OnItemSelectedListener onItemSelectedListener = new ChipNavigationBar.OnItemSelectedListener() {
        @Override
        public void onItemSelected(int i) {
            switch (i){
                case R.id.navigation_profile:
                    openFragment(ProfileFragment.newInstance("",""));
                    break;
                case R.id.navigation_home:
                    openFragment(HomeFragment.newInstance("",""));
                    break;
                case R.id.navigation_pet:
                    openFragment(PetFragment.newInstance("",""));
                    break;
                case R.id.navigation_help:
                    openFragment(HelpFragment.newInstance("",""));
                    break;
            }
        }
    };

    /*
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_profile:
                    openFragment(ProfileFragment.newInstance("",""));
                    return true;
                case R.id.navigation_home:
                    openFragment(HomeFragment.newInstance("",""));
                    return true;
                case R.id.navigation_pet:
                    openFragment(PetFragment.newInstance("",""));
                    return true;
                case R.id.navigation_help:
                    openFragment(HelpFragment.newInstance("",""));
                    return true;
            }
            return false;
        }
    };


    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/BottomNavigationView_HomePage"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:background="@color/colorPrimary"
        app:itemIconTint="@color/colorWhite"
        app:itemTextColor="@color/colorWhite"
        app:layout_constraintBottom_toBottomOf="parent"
        app:menu="@menu/bottom_navigation_menu" />
    */
}
