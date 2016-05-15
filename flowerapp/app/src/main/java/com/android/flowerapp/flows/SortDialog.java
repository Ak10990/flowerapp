package com.android.flowerapp.flows;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.flowerapp.R;

public class SortDialog {

    private final SortDialogCallback callback;
    private AppCompatDialog mDialog;
    private RadioGroup sortChooserGroup;

    public SortDialog(Context context, SortDialogCallback callback) {
        this.callback = callback;
        mDialog = new AppCompatDialog(context);
        mDialog.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_sort);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        initViews();
    }

    private void initViews() {
        sortChooserGroup = (RadioGroup) mDialog.findViewById(R.id.sort_chooser_group);

        sortChooserGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onSelected(checkedId);
            }
        });
    }

    public void showDialog() {
        if (mDialog == null) {
            return;
        }
        if (!mDialog.isShowing())
            mDialog.show();
    }

    public void dismissDialog() {
        onDismissDialog();
        if (mDialog == null || !mDialog.isShowing())
            return;
        mDialog.dismiss();
    }

    private void onDismissDialog() {
        sortChooserGroup.clearCheck();
    }


    private void onSelected(int selectedSortId) {
        if (selectedSortId > 0) {
            RadioButton radioPickUpButton = (RadioButton) mDialog.findViewById(selectedSortId);
            if (radioPickUpButton != null) {
                int selectedPickup = Integer.parseInt(radioPickUpButton.getTag().toString());
                callback.onSortButtonClick(selectedPickup);
            }
            dismissDialog();
        }
    }
}