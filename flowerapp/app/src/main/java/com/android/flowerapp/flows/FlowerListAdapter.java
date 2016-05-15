package com.android.flowerapp.flows;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.flowerapp.FlowerApp;
import com.android.flowerapp.R;
import com.android.flowerapp.models.Flower;
import com.android.flowerapp.models.FlowerDb;
import com.android.flowerapp.utils.ShareUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import io.realm.Realm;

public class FlowerListAdapter extends RecyclerView.Adapter<FlowerListAdapter.ViewHolder> {

    private Context mContext;
    private List<Flower> mList;
    private final Object objectLock = new Object();

    public FlowerListAdapter(Context context, List<Flower> flowerList) {
        this.mContext = context;
        this.mList = flowerList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flower, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Flower flower = mList.get(position);
        String name = flower.getName();
        if (TextUtils.isEmpty(name)) {
            name = "";
        }
        holder.itemText.setText(name);

        String url = flower.getUrl();
        if (TextUtils.isEmpty(url)) {
            url = "";
        }
        holder.itemImage.setImageURI(Uri.parse(url));

        holder.favButton.setChecked(flower.isFavorite());
        holder.itemView.setTag(position);
        holder.favButton.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        TextView itemText;
        SimpleDraweeView itemImage;
        CheckBox favButton;
        ImageButton shareButton;

        public ViewHolder(View view) {
            super(view);
            itemImage = (SimpleDraweeView) view.findViewById(R.id.item_image);
            itemText = (TextView) view.findViewById(R.id.item_text);
            favButton = (CheckBox) view.findViewById(R.id.fav_button);
            shareButton = (ImageButton) view.findViewById(R.id.share_button);
            favButton.setOnClickListener(this);
            itemImage.setOnClickListener(this);
            shareButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = (int) itemView.getTag();
            Flower flower = mList.get(position);
            switch (view.getId()) {
                case R.id.share_button:
                    ShareUtils.message(mContext, "", "Name : " + flower.getName() + "\nImage Url : " + flower.getUrl());
                    break;
                case R.id.item_image:
                    ImageDialog dialog = new ImageDialog(mContext);
                    dialog.showDialog(flower.getUrl());
                    break;
                case R.id.fav_button:
                    boolean isChecked = !mList.get(position).isFavorite();
                    synchronized (objectLock) {
                        mList.get(position).setFavorite(isChecked);
                        FlowerDb flowerDb = mList.get(position).getFlowerDb();
                        Realm realm = FlowerApp.getRealm();
                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(flowerDb);
                        realm.commitTransaction();
                        notifyDataSetChanged();
                        Log.d("Printing", "hererererere" + FlowerApp.getRealm().allObjects(FlowerDb.class).get(0));
                    }
                    break;
            }

        }
    }

}

