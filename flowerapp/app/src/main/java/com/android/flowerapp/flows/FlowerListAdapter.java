package com.android.flowerapp.flows;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.flowerapp.FlowerApp;
import com.android.flowerapp.R;
import com.android.flowerapp.models.Flower;
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
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageDialog dialog = new ImageDialog(mContext);
                dialog.showDialog(flower.getUrl());
            }
        });

        holder.favButton.setChecked(flower.isFavorite());
        holder.favButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mList.get(holder.getAdapterPosition()).setFavorite(isChecked);
                synchronized (objectLock) {
                    Realm realm = FlowerApp.getRealm();
                    realm.beginTransaction();
                    flower.setFavorite(isChecked);
                    realm.commitTransaction();
//                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.message(mContext, "", "Name : " + flower.getName() + "\nImage Url : " + flower.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
        }
    }

}

