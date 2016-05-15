package com.android.flowerapp.helpers;

import android.text.TextUtils;

import com.android.flowerapp.FlowerApp;
import com.android.flowerapp.models.Data;
import com.android.flowerapp.models.Flower;
import com.android.flowerapp.models.FlowerDb;
import com.android.flowerapp.utils.AlertToastUtils;
import com.android.flowerapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by akanksha on 14/5/16.
 */
public class FlowerModelHelper {

    public static ArrayList<Flower> loadAlbumsFromDb() {
        RealmResults<FlowerDb> list = FlowerApp.getRealm().allObjects(FlowerDb.class);
        ArrayList<Flower> flowerList = new ArrayList<>();
        for (FlowerDb albumDb : list) {
            flowerList.add(new Flower().getFlowerFromDb(albumDb));
        }

        AlertToastUtils.stopProgressDialog();
        return flowerList;
    }

    public static ArrayList<Flower> getFlowersBySearchQuery(String query) {
        if (TextUtils.isEmpty(query)) {
            return null;
        }
        RealmResults<FlowerDb> list = FlowerApp.getRealm()
                .where(FlowerDb.class).contains("name", query)
                .findAll();
        ArrayList<Flower> flowerList = new ArrayList<>();
        for (FlowerDb albumDb : list) {
            flowerList.add(new Flower().getFlowerFromDb(albumDb));
        }
        return flowerList;
    }

    public static ArrayList<Flower> getFlowersBySortQuery(int type) {
        ArrayList<Flower> flowers = new ArrayList<>();
        RealmResults<FlowerDb> list = FlowerApp.getRealm().allObjects(FlowerDb.class);
        if (type == 1) {
            list.sort("name", RealmResults.SORT_ORDER_ASCENDING);
        } else if (type == 2) {
            list.sort("name", RealmResults.SORT_ORDER_DESCENDING);
        }
        Flower flower;
        for (FlowerDb flowerDb : list) {
            flower = new Flower();
            flower.getFlowerFromDb(flowerDb);
            flowers.add(flower);
        }
        return flowers;
    }

    public static ArrayList<Flower> getFlowersFromData(ArrayList<Data> flowersData) {
        ArrayList<Flower> flowerList = new ArrayList<>();
        Flower flower;
        int i = -1;
        for (Data data : flowersData) {
            if (data != null) {
                flower = new Flower(++i, data.getName(), data.getUrl());
                flowerList.add(flower);
            }
        }
        feedIntoDb(flowerList);
        return flowerList;
    }

    private static void feedIntoDb(List<Flower> albumList) {
        Realm realm = FlowerApp.getRealm();
        ArrayList<FlowerDb> list = new ArrayList<>();
        for (Flower album : albumList) {
            list.add(album.getFlowerDb());
        }

        realm.beginTransaction();
        realm.copyToRealm(list);
        realm.commitTransaction();

        FlowerApp.getprefs().edit().putBoolean(Constants.IS_FIRST_LOAD, true).apply();
    }
}
