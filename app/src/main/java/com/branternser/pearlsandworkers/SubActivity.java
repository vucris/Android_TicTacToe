package com.branternser.pearlsandworkers0906;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.RequestId;

import java.util.HashSet;
import java.util.Set;

public class SubActivity extends Activity {
    private static final String TAG = "AppTrickySay";
    private SubIapManager subIapManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setupApplicationSpecificOnCreate();
        setupIAPOnCreate();
    }

    private void setupIAPOnCreate() {
        subIapManager = new SubIapManager(this);
        final SubPurchasingListener purchasingListener = new SubPurchasingListener(subIapManager);
        PurchasingService.registerListener(this.getApplicationContext(), purchasingListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: call getProductData for skus: " + MySku.values());
        final Set<String> productSkus = new HashSet<String> ();
        for (final SubSku subSku : SubSku.values()) {
            productSkus.add(subSku.getSku());
        }
        PurchasingService.getProductData(productSkus);
    }

    @Override
    protected void onResume() {
        super.onResume();
        subIapManager.activate();
        Log.d(TAG, "onResume: call getUserData");
        PurchasingService.getUserData();
        Log.d(TAG, "onResume: getPurchaseUpdates");
        PurchasingService.getPurchaseUpdates(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        subIapManager.deactivate();
    }

    public void onBuyOneClick(final View view) {
        final RequestId requestId = PurchasingService.purchase(SubSku.MY_ONE_SUBS.getSku());
        Log.d(TAG, "onBuyMagazineClick: requestId (" + requestId + ")");
    }
    public void onBuyFiveClick(final View view) {
        final RequestId requestId = PurchasingService.purchase(SubSku.MY_FIVE_SUBS.getSku());
        Log.d(TAG, "onBuyMagazineClick: requestId (" + requestId + ")");
    }
    public void onBuyTenClick(final View view) {
        final RequestId requestId = PurchasingService.purchase(SubSku.MY_TEN_SUBS.getSku());
        Log.d(TAG, "onBuyMagazineClick: requestId (" + requestId + ")");
    }
    public void onBuyTwentyClick(final View view) {
        final RequestId requestId = PurchasingService.purchase(SubSku.MY_TWENTY_SUBS.getSku());
        Log.d(TAG, "onBuyMagazineClick: requestId (" + requestId + ")");
    }

    public void onPurchaseUpdatesResponseFailed(final String requestId) {
        Log.d(TAG, "onPurchaseUpdatesResponseFailed: for requestId (" + requestId + ")");
    }



    private Handler guiThreadHandler;

    private ImageView buyOneButton, buyFiveButton, buyTenButton, buyTwentyButton, closeButton;

    private TextView isSubscriptionEnabled;

    private void setupApplicationSpecificOnCreate() {
        setContentView(R.layout.activity_sub);
        final String TAG = "SampleIAPSubscriptionsApp";
        buyOneButton = (ImageView) findViewById( R.id.buy_one_button);
        buyFiveButton = (ImageView) findViewById(R.id.buy_five_button);
        buyTenButton = (ImageView) findViewById(R.id.buy_ten_button);
        buyTwentyButton = (ImageView) findViewById(R.id.buy_twenty_button);
        closeButton = (ImageView) findViewById(R.id.close);

        closeButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubActivity.this, AddPlayerActivity.class);
                startActivity(intent);
            }
        } );

        resetApplication();

        guiThreadHandler = new Handler ();
    }

    private void resetApplication() {
        isSubscriptionEnabled = (TextView) findViewById(R.id.is_magazine_enabled);
        isSubscriptionEnabled.setText(R.string.subscription_disabled);
        isSubscriptionEnabled.setTextColor( Color.GRAY);
        isSubscriptionEnabled.setBackgroundColor(Color.WHITE);
    }

    private void disableBuyMagazineButton() {
        buyOneButton.setEnabled(false);
        buyFiveButton.setEnabled(false);
        buyTenButton.setEnabled(false);
        buyTwentyButton.setEnabled(false);
    }

    private void enableBuyMagazineButton() {
        buyOneButton.setEnabled(true);
        buyFiveButton.setEnabled(true);
        buyTenButton.setEnabled(true);
        buyTwentyButton.setEnabled(true);
    }

    private void enableMagazineSubscriptionInView() {
        guiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                isSubscriptionEnabled.setText(R.string.subscription_enabled);
                isSubscriptionEnabled.setTextColor(Color.BLUE);
                isSubscriptionEnabled.setBackgroundColor(Color.YELLOW);
            }
        });
    }

    protected void disableMagazineSubscriptionInView() {
        guiThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                isSubscriptionEnabled.setText(R.string.subscription_disabled);
                isSubscriptionEnabled.setTextColor(Color.GRAY);
                isSubscriptionEnabled.setBackgroundColor(Color.WHITE);
            }
        });
    }

    public void showMessage(final String message) {
        Toast.makeText(SubActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void setMagazineSubsAvail(final boolean productAvailable, final boolean userCanSubscribe) {
        if (productAvailable) {
            if (userCanSubscribe) {
                disableMagazineSubscriptionInView();
                enableBuyMagazineButton();
            } else {
                enableMagazineSubscriptionInView();
                disableBuyMagazineButton();
            }
        } else {
            disableMagazineSubscriptionInView();
            disableBuyMagazineButton();
        }

    }
}
