package com.android.flowerapp.flows;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.flowerapp.R;
import com.android.flowerapp.models.Flower;

import java.util.ArrayList;
import java.util.List;

public class FlowerListFragment extends Fragment {

    private static final String DATA_LIST_ARGS = "DATA_LIST_ARGS";
    private List<Flower> flowerList = new ArrayList<>();

    public static Fragment getInstance(ArrayList<Flower> flowerList) {
        FlowerListFragment fragment = new FlowerListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(DATA_LIST_ARGS, flowerList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initValues();
    }

    private void initValues() {
        if (getArguments() != null && getArguments().containsKey(DATA_LIST_ARGS)) {
            flowerList = getArguments().getParcelableArrayList(DATA_LIST_ARGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flower_list, container, false);
        initViews(view);
        return view;
    }

    protected void initViews(View rootView) {
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.flowers_list);
        TextView noFlowersPlaceholder = (TextView) rootView.findViewById(R.id.no_flowers_placeholder);

        if (recyclerView != null) {
            FlowerListAdapter adapter = new FlowerListAdapter(getActivity(), flowerList);
            recyclerView.setAdapter(adapter);
        }
        noFlowersPlaceholder.setVisibility(View.GONE);
    }
}
