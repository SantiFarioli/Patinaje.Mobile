package com.santisoft.patinajemobile.util;

import android.content.Context;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.santisoft.patinajemobile.R; // Asegúrate de importar tu R

public class DialogUtils {

    public static SweetAlertDialog showLoading(Context context, String title) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.md_primary));
        pDialog.setTitleText(title);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    public static void showSuccess(Context context, String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    public static void showError(Context context, String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }

    public static void showWarning(Context context, String title, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .show();
    }
}