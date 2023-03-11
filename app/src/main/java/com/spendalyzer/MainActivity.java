package com.spendalyzer;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    private static boolean hasReadPermission;
    private final static int READ_SMS_PERMISSION = 0;

    private final static int SPLASH_LAYOUT = R.layout.activity_splash;
    private final static int MAIN_LAYOUT = R.layout.activity_main;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(SPLASH_LAYOUT);


        MaterialTextView splashText = findViewById(R.id.splashText);
        hasReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;

        splashText.animate().rotationBy(20).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {}
            @Override
            public void onAnimationEnd(@NonNull Animator animator) {

                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.READ_SMS }, READ_SMS_PERMISSION);
                }
                if(hasReadPermission) loadContent();

            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {}
            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {}


        }).start();

    }

    private void loadContent() {

        setContentView(MAIN_LAYOUT);
        drawerLayout = findViewById(R.id.navDrawer);
        navigationView = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.menu);


        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        loadFragment(new HomeFragment());
        navigationView.setNavigationItemSelectedListener(item -> {
            int selectedId = item.getItemId();
            if(selectedId == R.id.option_spending){
                getSupportActionBar().setTitle("Spendings");
                loadFragment(new SpendingFragment());
            } else if(selectedId == R.id.option_analyze) {
                getSupportActionBar().setTitle("Analytics");
                loadFragment(new AnalyzeFragment());
            } else if (selectedId == R.id.option_help) {
                getSupportActionBar().setTitle("Help");
                loadFragment(new HelpFragment());
            } else if (selectedId == R.id.option_date) {
                getSupportActionBar().setTitle("Transactions by date");
                loadFragment(new DateFragment());
            } else {
                getSupportActionBar().setTitle("Spendalyzer");
                loadFragment(new HomeFragment());
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == READ_SMS_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                hasReadPermission = true;
                navigateUpTo(new Intent(MainActivity.this, MainActivity.class));
                startActivity(getIntent());
            }else{
                finishAffinity();
                System.exit(0);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void loadFragment(Fragment fragmentToLoad) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trx = manager.beginTransaction();
        trx.add(R.id.frame_main, fragmentToLoad);
        trx.commit();

    }

}