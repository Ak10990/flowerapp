package com.android.flowerapp.flows;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.android.flowerapp.R;
import com.android.flowerapp.helpers.FlowerModelHelper;
import com.android.flowerapp.models.Flower;

import java.util.ArrayList;

public class FlowerSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        ArrayList<Flower> flowerList = FlowerModelHelper.getFlowersBySearchQuery(query);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && flowerList != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(flowerList.size() + " results found for \"" + query + "\"");
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, FlowerListFragment.getInstance(flowerList)).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}