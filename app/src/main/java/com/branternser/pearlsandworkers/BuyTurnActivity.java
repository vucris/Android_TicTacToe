package com.branternser.pearlsandworkers0906;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.RequestId;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class BuyTurnActivity extends AppCompatActivity {
    private IapManager iapManager;
    private static final String TAG = "PearlsandWorkersIAPConsumablesApp";
    public Handler guiThreadHandler;
    public ImageView buy1Turn, buy5Turn, buy10Turn, buy15Turn, buy20Turn, closeButton;
    public TextView scoreTurnTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView ( R.layout.activity_buy_turn );


        buy1Turn = (ImageView) findViewById(R.id.buy1TurnBtn);
        buy5Turn = (ImageView) findViewById(R.id.buy5TurnBtn);
        buy10Turn = (ImageView) findViewById(R.id.buy10TurnBtn);
        buy15Turn = (ImageView) findViewById(R.id.buy15TurnBtn);
        buy20Turn = (ImageView) findViewById(R.id.buy20TurnBtn);
        closeButton = (ImageView) findViewById(R.id.close);
        scoreTurnTv = findViewById(R.id.scoreTurn);

        closeButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyTurnActivity.this, AddPlayerActivity.class);
                startActivity(intent);
            }
        } );


        guiThreadHandler = new Handler();

        iapManager = new IapManager(this);
        iapManager.activate();
        final PurchasingListener purchasingListener = new PurchasingListener(iapManager);
        Log.d(TAG, "onCreate: registering PurchasingListener");
        PurchasingService.registerListener(this.getApplicationContext(), purchasingListener);
        Log.d(TAG, "IS_SANDBOX_MODE:" + PurchasingService.ACCESSIBILITY_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: call getProductData for skus: " + Arrays.toString ( MySku.values () ) );
        final Set<String> productSkus = new HashSet<String> ();
        for (final MySku mySku : MySku.values()) {
            productSkus.add(mySku.getSku());
        }
        PurchasingService.getProductData(productSkus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        iapManager.activate();
        Log.d(TAG, "onResume: call getUserData");
        PurchasingService.getUserData();
        Log.d(TAG, "onResume: getPurchaseUpdates");
        PurchasingService.getPurchaseUpdates(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        iapManager.deactivate();
    }

    public void onBuyOneTurnClick(final View view) {
        final RequestId requestId = PurchasingService.purchase(MySku.ONETURN.getSku());
        Log.d(TAG, "onBuyOneTurnClick: requestId (" + requestId + ")");
    }

    public void onBuyFiveTurnClick(final View view) {
        final RequestId requestId = PurchasingService.purchase(MySku.FIVETURN.getSku());
        Log.d(TAG, "onBuyFiveTurnClick: requestId (" + requestId + ")");
    }
    public void onBuyTenTurnClick(final View view) {
        final RequestId requestId = PurchasingService.purchase(MySku.TENTURN.getSku());
        Log.d(TAG, "onBuyTenTurnClick: requestId (" + requestId + ")");
    }
    public void onBuyFifteenTurnClick(final View view) {
        final RequestId requestId = PurchasingService.purchase(MySku.FIFTEENTURN.getSku());

        Log.d(TAG, "onBuyFifteenTurnClick: requestId (" + requestId + ")");
    }
    public void onBuyTweltyTurnClick(final View view) {

        final RequestId requestId = PurchasingService.purchase(MySku.TWELTYTURN.getSku());
        Log.d(TAG, "onBuyTweltyTurnClick: requestId (" + requestId + ")");
    }


    protected void disableButtonsForUnavailableSkus(final Set<String> unavailableSkus) {
        for (final String unavailableSku : unavailableSkus) {
            if (MySku.ONETURN.getSku().equals(unavailableSku)) {
                disableOneTurnButton();
            }
            if (MySku.FIVETURN.getSku().equals(unavailableSku)) {
                disableFiveTurnButton();
            }
            if (MySku.TENTURN.getSku().equals(unavailableSku)) {
                disableTenTurnButton ();
            }
            if (MySku.FIFTEENTURN.getSku().equals(unavailableSku)) {
                disableFifteenTurnButton ();
            }
            if (MySku.TWELTYTURN.getSku().equals(unavailableSku)) {
                disableTweltyTurnButton();
            }
        }
    }

    void disableOneTurnButton() {
        buy1Turn.setEnabled(false);
    }
    void disableFiveTurnButton() {
        buy5Turn.setEnabled(false);
    }
    void disableTenTurnButton() {
        buy10Turn.setEnabled(false);
    }
    void disableFifteenTurnButton() {
        buy15Turn.setEnabled(false);
    }
    void disableTweltyTurnButton() {
        buy20Turn.setEnabled(false);
    }

    void enableOneTurnButton() {
        buy1Turn.setEnabled(true);
    }
    void enableFiveOrangeButton() {
        buy5Turn.setEnabled(true);
    }
    void enableTenOrangeButton() {
        buy10Turn.setEnabled(true);
    }
    void enableFifteenOrangeButton() {
        buy15Turn.setEnabled(true);
    }
    void enableTweltyOrangeButton() {
        buy20Turn.setEnabled(true);
    }

    public void updateTurnInView(final int haveQuantity, final int consumedQuantity) {
        Log.d(TAG, "updateOrangesInView with haveQuantity (" + haveQuantity
                + ") and consumedQuantity ("
                + consumedQuantity
                + ")");
        scoreTurnTv.setText(String.valueOf(haveQuantity));

        guiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                scoreTurnTv.setText(String.valueOf(haveQuantity).toString());
            }
        });
    }
    public void showMessage(final String message) {
        Toast.makeText(BuyTurnActivity.this, message, Toast.LENGTH_LONG).show();
    }

}

