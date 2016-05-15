package com.android.flowerapp.flows;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.flowerapp.R;

public class ImageDialog {

    private AppCompatDialog mDialog;
    private ImageView itemImage;

    public ImageDialog(Context context) {
        mDialog = new AppCompatDialog(context);
        mDialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_image);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        initViews();
    }

    private void initViews() {
        itemImage = (ImageView) mDialog.findViewById(R.id.item_image);
    }

    private void populateData(String uri) {
        if (TextUtils.isEmpty(uri)) {
            uri = "";
        }
        itemImage.setImageURI(Uri.parse(uri));
    }

    public void showDialog(String uri) {
        populateData(uri);
        if (mDialog == null) {
            return;
        }
        if (!mDialog.isShowing())
            mDialog.show();
    }

    public void dismissDialog() {
        if (mDialog == null || !mDialog.isShowing())
            return;
        mDialog.dismiss();
    }

}