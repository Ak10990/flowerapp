package com.android.flowerapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Akanksha on 14/5/16.
 */
public class ShareUtils {

    public static void message(Context mContext, String phoneNumber, String message) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", message);
        mContext.startActivity(intent);
    }

}
