package com.android.flowerapp.flows;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.flowerapp.FlowerApp;
import com.android.flowerapp.R;
import com.android.flowerapp.helpers.FlowerModelHelper;
import com.android.flowerapp.models.Flower;
import com.android.flowerapp.models.FlowerDb;
import com.android.flowerapp.service.FetchDataService;
import com.android.flowerapp.utils.AlertToastUtils;
import com.android.flowerapp.utils.Constants;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by akanksha on 14/5/16.
 */
public class MainActivity extends AppCompatActivity implements SortDialogCallback {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fetchData();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.FETCHED_API_SUCCESS);
        LocalBroadcastManager.getInstance(this).registerReceiver(downloadSuccessReceiver, filter);
        filter = new IntentFilter(Constants.FETCHED_API_FAILURE);
        LocalBroadcastManager.getInstance(this).registerReceiver(downloadFailureReceiver, filter);
    }

    private BroadcastReceiver downloadSuccessReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                ArrayList<Flower> flowerlist = intent
                        .getParcelableArrayListExtra(Constants.FETCHED_API_RESULT);
                if (flowerlist != null) {
                    initFragment(flowerlist);
                }
            }
        }
    };
    private BroadcastReceiver downloadFailureReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String error = intent.getStringExtra(Constants.FETCHED_API_ERROR);
            onError(error);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.action_sort:
                SortDialog sortDialog = new SortDialog(this, this);
                sortDialog.showDialog();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void fetchData() {
        AlertToastUtils.showProgressDialog(this, "Loading...");
        if (FlowerApp.getprefs().getBoolean(Constants.IS_FIRST_LOAD, false)) {
            new FetchDbAsyncTask().execute();
        } else {
            Intent intent = new Intent(this, FetchDataService.class);
            startService(intent);
        }
    }

    private void initFragment(ArrayList<Flower> flowers) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frag_container, FlowerListFragment.getInstance(flowers)).commit();
    }

    private void onError(String errMessage) {
        AlertToastUtils.createToast(this, errMessage);
    }

    @Override
    public void onSortButtonClick(int type) {
        ArrayList<Flower> flowerList = FlowerModelHelper.getFlowersBySortQuery(type);
        replaceFragment(flowerList);
    }

    private void replaceFragment(ArrayList<Flower> flowers) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frag_container, FlowerListFragment.getInstance(flowers)).commit();
    }

    private class FetchDbAsyncTask extends AsyncTask<Void, Void, ArrayList<Flower>> {
        @Override
        protected ArrayList<Flower> doInBackground(Void... params) {
            RealmConfiguration realmConfig = new RealmConfiguration.Builder(getApplicationContext()).build();
            Realm realm = Realm.getInstance(realmConfig);
            RealmResults<FlowerDb> list = realm.allObjects(FlowerDb.class);
            ArrayList<Flower> flowerList = new ArrayList<>();
            for (FlowerDb albumDb : list) {
                flowerList.add(new Flower().getFlowerFromDb(albumDb));
            }
            return flowerList;
        }

        @Override
        protected void onPostExecute(ArrayList<Flower> flowerlist) {
            super.onPostExecute(flowerlist);
            initFragment(flowerlist);
            AlertToastUtils.stopProgressDialog();
        }
    }

}
