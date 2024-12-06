package com.branternser.pearlsandworkers0906;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class AlertDialog extends Dialog {
    private final AddPlayerActivity addPlayerActivity;
    private Integer turn;

    public AlertDialog(@NonNull Context context, Integer turn, AddPlayerActivity addPlayerActivity) {
        super(context);
        this.addPlayerActivity = addPlayerActivity;
        this.turn = turn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.alert_dialog_layout);

        final Button playGameBtn = findViewById(R.id.playGameBtn);


        playGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayerActivity.playGame(turn);
            }
        });
    }
}