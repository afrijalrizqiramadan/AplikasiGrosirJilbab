package com.asyabab.egj.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.asyabab.egj.R;


public class DialogHelper {

    public static AlertDialog loading(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.dp_dialog_loading);
        builder.setCancelable(false);
        return builder.create();

    }

    public static AlertDialog confirm(Context context, String message,
                                      DialogInterface.OnClickListener yes) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.warning);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.y, yes);
        builder.setNegativeButton(R.string.btl, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();

    }

    public static Dialog error(Context context, final DialogCallback callback) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dp_dialog_error_tryagain);
        final Button btnTryAgain = dialog.findViewById(R.id.btn_tryagain);
        final ImageView imgNoInet = dialog.findViewById(R.id.img_noinet);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                callback.onClick();
            }
        });
        return dialog;

    }


    public interface DialogCallback {
        void onClick();
    }

}
