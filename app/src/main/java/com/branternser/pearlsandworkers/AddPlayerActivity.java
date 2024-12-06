package com.branternser.pearlsandworkers0906;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.Window;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class AddPlayerActivity extends AppCompatActivity {

    public static TextView scoreTurnTv;
    public static Handler guiThreadHandler;
    public EditText playerOne, playerTwo;
    public int turn;
    public final int SPLASH_DISPLAY_LENGTH = 3450;
    public static final String TAG = "PearlsandWorkersIAPConsumablesApp";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean sub, inapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadingDialog loadingDialog = new LoadingDialog ( AddPlayerActivity.this, AddPlayerActivity.this);
        loadingDialog.show();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_player);

        new Handler ().postDelayed( new Runnable(){
            @Override
            public void run() {
                loadingDialog.dismiss ();
            }
        }, SPLASH_DISPLAY_LENGTH);
        playerOne = findViewById(R.id.playerOneName);
        playerTwo = findViewById(R.id.playerTwoName);
        scoreTurnTv = findViewById(R.id.scoreTurn);
        try {
            turn = Integer.parseInt(scoreTurnTv.getText().toString());
        } catch (Exception e) {
            turn = 0;
        }

        final Button startGameBtn = findViewById(R.id.startGame);
        final Button buyTurnBtn = findViewById(R.id.buyTurnBtn);
        final Button subBtn = findViewById(R.id.SubscriptionBtn);

        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog( AddPlayerActivity.this, turn, AddPlayerActivity.this);
                alertDialog.show();
            }
        });
        db.collection("com.branternser.pearlsandworkers0906")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot> () {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                sub = (Boolean) Objects.requireNonNull(document.get("sub"));
                                inapp = (Boolean) Objects.requireNonNull(document.get("inapp"));
                                if(sub){
                                    subBtn.setVisibility ( View.VISIBLE );
                                }else {
                                    subBtn.setVisibility ( View.GONE );
                                }
                                if(inapp){
                                    buyTurnBtn.setVisibility ( View.VISIBLE );
                                }else {
                                    buyTurnBtn.setVisibility ( View.GONE );
                                }
                            }
                        } else {
                            Intent intent = new Intent(AddPlayerActivity.this, SplashActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener ( new OnFailureListener () {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Intent intent = new Intent(AddPlayerActivity.this, SplashActivity.class);
                        startActivity(intent);
                    }
                } );

        buyTurnBtn.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPlayerActivity.this, BuyTurnActivity.class);
                startActivity(intent);
            }
        } );

        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPlayerActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });
    }

    public void playGame(Integer turn){
        final String getPlayerOneName = playerOne.getText().toString();
        final String getPlayerTwoName = playerTwo.getText().toString();

        if(getPlayerOneName.isEmpty() || getPlayerTwoName.isEmpty()){
            Toast.makeText(AddPlayerActivity.this, "Pleas enter player names", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(AddPlayerActivity.this, MainActivity.class);
            intent.putExtra("playerOne", getPlayerOneName);
            intent.putExtra("playerTwo", getPlayerTwoName);
            intent.putExtra("turn", turn);
            startActivity(intent);
        }
    }

    public static void updateTurnInView(final int haveQuantity, final int consumedQuantity) {
        Log.d(TAG, "updateOrangesInView with haveQuantity (" + haveQuantity
                + ") and consumedQuantity ("
                + consumedQuantity
                + ")");
        scoreTurnTv.setText(String.valueOf(haveQuantity));

        guiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                scoreTurnTv.setText(String.valueOf(haveQuantity).toString()); }
        });
    }
}