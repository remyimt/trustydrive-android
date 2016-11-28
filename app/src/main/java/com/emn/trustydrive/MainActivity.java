package com.emn.trustydrive;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
    }

    /**
     * Called when the user clicks the " Access documents " button
     */
    public void accessDocuments(View v) {
        Intent intent = new Intent(this, AccessDocumentsActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the " Manage registered accounts " button
     */
    public void manageAccounts(View v) {
        Intent intent = new Intent(this, ManageCloudAccounts.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the " Add cloud accounts  " button
     */
    public void addCloudAccounts(View v) {
        Intent intent = new Intent(this, AddCloudAccount.class);
        startActivity(intent);
    }

}
