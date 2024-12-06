package com.branternser.pearlsandworkers0906;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<int[]> combinationList = new ArrayList<>();

    private int [] boxPositions = {0,0,0,0,0,0,0,0,0};

    private int playerTurn = 1;

    private int totalSelectedBoxes = 1;

    private LinearLayout playerOneLayout, playerTwoLayout;
    private TextView playerOneName, playerTwoName;
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9;
    public TextView scoreTurnTv;
    public int turn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        scoreTurnTv = findViewById(R.id.scoreTurn);
//        turn = Integer.parseInt(getIntent().getStringExtra("turn"));
//        scoreTurnTv.setText ( turn );

        playerOneName = findViewById(R.id.playerOneName);
        playerTwoName = findViewById(R.id.playerTwoName);

        playerOneLayout = findViewById(R.id.playerOneLayout);
        playerTwoLayout = findViewById(R.id.playerTwoLayout);

        image1 = findViewById(R.id.iamge1);
        image2 = findViewById(R.id.iamge2);
        image3 = findViewById(R.id.iamge3);
        image4 = findViewById(R.id.iamge4);
        image5 = findViewById(R.id.iamge5);
        image6 = findViewById(R.id.iamge6);
        image7 = findViewById(R.id.iamge7);
        image8 = findViewById(R.id.iamge8);
        image9 = findViewById(R.id.iamge9);

        combinationList.add(new int[]{0, 1, 2});
        combinationList.add(new int[]{3, 4, 5});
        combinationList.add(new int[]{6, 7, 8});
        combinationList.add(new int[]{0, 3, 6});
        combinationList.add(new int[]{1, 4, 7});
        combinationList.add(new int[]{2, 5, 8});
        combinationList.add(new int[]{0, 1, 2});
        combinationList.add(new int[]{2, 4, 6});
        combinationList.add(new int[]{0, 4, 8});

        final String getPlayerOneName = getIntent().getStringExtra("playerOne");
        final String getPlayerTwoName = getIntent().getStringExtra("playerTwo");

        playerOneName.setText(getPlayerOneName);
        playerTwoName.setText(getPlayerTwoName);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(0)){
                    performAction((ImageView)v, 0);
                }
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(1)){
                    performAction((ImageView)v, 1);
                }
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(2)){
                    performAction((ImageView)v, 2);
                }
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(3)){
                    performAction((ImageView)v, 3);
                }
            }
        });
        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(4)){
                    performAction((ImageView)v, 4);
                }
            }
        });
        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(5)){
                    performAction((ImageView)v, 5);
                }
            }
        });
        image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(6)){
                    performAction((ImageView)v, 6);
                }
            }
        });
        image8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(7)){
                    performAction((ImageView)v, 7);
                }
            }
        });
        image9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBoxSelectable(8)){
                    performAction((ImageView)v, 8);
                }
            }
        });
    }

    private void performAction(ImageView imageView, int selectedBoxPosition) {
        boxPositions[selectedBoxPosition] = playerTurn;
        if(playerTurn == 1){
            imageView.setImageResource(R.drawable.dart);
            if(checkPlayerWin()){
                WinDialog winDialog = new WinDialog( MainActivity.this, playerOneName.getText().toString() + " Won this game",  MainActivity.this);
                winDialog.setCancelable(false);
                winDialog.show();
            } else if(totalSelectedBoxes == 9) {
                WinDialog winDialog = new WinDialog( MainActivity.this, "Draw!",  MainActivity.this);
                winDialog.setCancelable(false);
                winDialog.show();
            } else {
                changePlayerTurn(2);
                totalSelectedBoxes++;
            }
        } else {
            imageView.setImageResource(R.drawable.fottball);
            if(checkPlayerWin()){
                WinDialog winDialog = new WinDialog( MainActivity.this, playerTwoName.getText().toString() + " Won this game", MainActivity.this);
                winDialog.setCancelable(false);
                winDialog.show();
            } else  if(totalSelectedBoxes == 9) {
                WinDialog winDialog = new WinDialog( MainActivity.this, "Draw!", MainActivity.this);
                winDialog.setCancelable(false);
                winDialog.show();
            } else {
                changePlayerTurn(1);
                totalSelectedBoxes++;
            }
        }
    }

    private void changePlayerTurn(int currentPlayerTurn){
        playerTurn = currentPlayerTurn;

        if(playerTurn == 1){
            playerOneLayout.setBackgroundResource(R.drawable.round_back_blue_border);
            playerTwoLayout.setBackgroundResource(R.drawable.round_back_dark_blue);

        } else {
            playerTwoLayout.setBackgroundResource(R.drawable.round_back_blue_border);
            playerOneLayout.setBackgroundResource(R.drawable.round_back_dark_blue);

        }
    }

    private boolean checkPlayerWin() {

        boolean response = false;

        for(int i=0; i<combinationList.size(); i++){
            final  int[] combination = combinationList.get(i);

            if(boxPositions[combination[0]] == playerTurn && boxPositions[combination[1]] == playerTurn && boxPositions[combination[2]] == playerTurn){
                response = true;
            }
        }

        return  response;
    }

    private boolean isBoxSelectable(int boxPosition){

        boolean response = false;

        if(boxPositions[boxPosition] == 0) {
            response = true;
        }

        return response;
    }

    public void restartMatch(){
        boxPositions = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
        playerTurn = 1;

        image1.setImageResource(R.drawable.transparent_back);
        image2.setImageResource(R.drawable.transparent_back);
        image3.setImageResource(R.drawable.transparent_back);
        image4.setImageResource(R.drawable.transparent_back);
        image5.setImageResource(R.drawable.transparent_back);
        image6.setImageResource(R.drawable.transparent_back);
        image7.setImageResource(R.drawable.transparent_back);
        image8.setImageResource(R.drawable.transparent_back);
        image9.setImageResource(R.drawable.transparent_back);

        totalSelectedBoxes = 0;
    }
}