package com.branternser.pearlsandworkers0906;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

public class LoadingDialog extends Dialog {

    private ProgressBar spinner;

    private final AddPlayerActivity addPlayerActivity;

    public LoadingDialog(@NonNull Context context, AddPlayerActivity addPlayerActivity) {
        super(context);

        this.addPlayerActivity = addPlayerActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loading_dialog_layout);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);

    }
}