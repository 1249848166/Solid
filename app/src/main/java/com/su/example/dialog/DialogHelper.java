package com.su.example.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {

    public static void showSimpleDialog(Context context,String title, String content, DialogInterface.OnClickListener confirm){
        final Dialog dialog=new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("确定",confirm)
                .setCancelable(false)
                .create();
        dialog.show();
    }
}
