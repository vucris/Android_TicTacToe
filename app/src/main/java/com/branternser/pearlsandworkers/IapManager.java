package com.branternser.pearlsandworkers0906;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.Product;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserData;
import com.branternser.pearlsandworkers0906.PurchaseDataSource.PurchaseStatus;

import java.util.Map;
import java.util.Set;

public class IapManager {
    private static final String CONSUMED = "CONSUMED";

    private static final String REMAINING = "REMAINING";

    private static final String TAG = "PearlsandWorkersIAPConsumablesApp";

    final private Context context;
    final private BuyTurnActivity buyTurnActivity;

    private UserIapData userIapData;
    final private PurchaseDataSource dataSource;

    public static class PurchaseRecord {
        private PurchaseStatus status;
        private String receiptId;
        private String userId;

        public PurchaseStatus getStatus() {
            return status;
        }

        public void setStatus(final PurchaseStatus status) {
            this.status = status;
        }

        public String getReceiptId() {
            return receiptId;
        }

        public void setReceiptId(final String receiptId) {
            this.receiptId = receiptId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(final String userId) {
            this.userId = userId;
        }

    }


    public IapManager(final BuyTurnActivity buyTurnActivity) {
        this.buyTurnActivity = buyTurnActivity;
        this.context = buyTurnActivity.getApplicationContext();
        this.dataSource = new PurchaseDataSource(context);
    }

    public void setAmazonUserId(final String newAmazonUserId, final String newAmazonMarketplace) {
        if (newAmazonUserId == null) {
            if (userIapData != null) {
                userIapData = null;
                buyTurnActivity.updateTurnInView(0, 0);
                AddPlayerActivity.updateTurnInView(0, 0);
            }
        } else if (userIapData == null || !newAmazonUserId.equals(userIapData.getAmazonUserId())) {
            userIapData = reloadUserData(newAmazonUserId, newAmazonMarketplace);
            buyTurnActivity.updateTurnInView(userIapData.getRemainingOneTurn(), userIapData.getConsumedOneTurn());
            AddPlayerActivity.updateTurnInView(userIapData.getRemainingOneTurn(), userIapData.getConsumedOneTurn());
        }
    }

    public void enablePurchaseForSkus(final Map<String, Product> productData) {
        if (productData.containsKey(MySku.ONETURN.toString())) {
            buyTurnActivity.enableOneTurnButton();
        }
        if (productData.containsKey(MySku.FIVETURN.toString())) {
            buyTurnActivity.enableFiveOrangeButton();
        }
        if (productData.containsKey(MySku.TENTURN.toString())) {
            buyTurnActivity.enableTenOrangeButton();
        }
        if (productData.containsKey(MySku.FIFTEENTURN.toString())) {
            buyTurnActivity.enableFifteenOrangeButton();
        }
        if (productData.containsKey(MySku.TWELTYTURN.toString())) {
            buyTurnActivity.enableTweltyOrangeButton();
        }
    }
    public void disablePurchaseForSkus(final Set<String> unavailableSkus) {
        if (unavailableSkus.contains(MySku.ONETURN.toString())) {
            buyTurnActivity.disableOneTurnButton();
        }
        if (unavailableSkus.contains(MySku.FIVETURN.toString())) {
            buyTurnActivity.disableFiveTurnButton();
        }
        if (unavailableSkus.contains(MySku.TENTURN.toString())) {
            buyTurnActivity.disableTenTurnButton();
        }
        if (unavailableSkus.contains(MySku.FIFTEENTURN.toString())) {
            buyTurnActivity.disableFifteenTurnButton();
        }
        if (unavailableSkus.contains(MySku.TWELTYTURN.toString())) {
            buyTurnActivity.disableTweltyTurnButton();
        }
    }

    public void handleConsumablePurchase(final Receipt receipt, final UserData userData) {
        try {
            if (receipt.isCanceled()) {
                revokeConsumablePurchase(receipt, userData);
            } else {
                if (!verifyReceiptFromYourService(receipt.getReceiptId(), userData)) {
                    buyTurnActivity.showMessage("Purchase cannot be verified, please retry later.");
                    return;
                }
                if (receiptAlreadyFulfilled(receipt.getReceiptId(), userData)) {
                    PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.FULFILLED);
                    return;
                }

                grantConsumablePurchase(receipt, userData);
            }
            return;
        } catch (final Throwable e) {
            buyTurnActivity.showMessage("Purchase cannot be completed, please retry");
        }
    }

    public void handleReceipt(final Receipt receipt, final UserData userData) {
        switch (receipt.getProductType()) {
            case CONSUMABLE:
                handleConsumablePurchase(receipt, userData);
                break;
            case ENTITLED:
                break;
            case SUBSCRIPTION:
                break;
        }

    }

    public void purchaseFailed(final String sku) {
        buyTurnActivity.showMessage("Purchase failed!");
    }

    public void disableAllPurchases() {
        buyTurnActivity.disableOneTurnButton ();
        buyTurnActivity.disableFiveTurnButton ();
        buyTurnActivity.disableTenTurnButton ();
        buyTurnActivity.disableFifteenTurnButton ();
        buyTurnActivity.disableTweltyTurnButton ();

    }
    public void deactivate() {
        dataSource.close();

    }
    public void activate() {
        dataSource.open();

    }
    public void refreshTurn() {
        buyTurnActivity.updateTurnInView(userIapData.getRemainingOneTurn(), userIapData.getConsumedOneTurn());
    }
    private void revokeConsumablePurchase(final Receipt receipt, final UserData userData) {
        // TODO: implement your application-specific logic to handle the
        // consumable purchase.

    }
    private UserIapData reloadUserData(final String amazonUserId, final String amazonMarketplace) {
        final UserIapData userIapData = new UserIapData(amazonUserId, amazonMarketplace);

        final SharedPreferences oneTurnSharedPreference = context.getSharedPreferences("ONETURN_" + amazonUserId, Context.MODE_PRIVATE);
        final SharedPreferences fiveTurnSharedPreference = context.getSharedPreferences("FIVETURN_" + amazonUserId, Context.MODE_PRIVATE);
        final SharedPreferences tenTurnSharedPreference = context.getSharedPreferences("TENTURN_" + amazonUserId, Context.MODE_PRIVATE);
        final SharedPreferences fifteenTurnSharedPreference = context.getSharedPreferences("FIFTEENTURN_" + amazonUserId, Context.MODE_PRIVATE);
        final SharedPreferences twentyTurnSharedPreference = context.getSharedPreferences("TWENTYTURN_" + amazonUserId, Context.MODE_PRIVATE);

        userIapData.setRemainingOneTurn(oneTurnSharedPreference.getInt(REMAINING, 1));
        userIapData.setRemainingFiveTurn(fiveTurnSharedPreference.getInt(REMAINING, 5));
        userIapData.setRemainingTenTurn(tenTurnSharedPreference.getInt(REMAINING, 10));
        userIapData.setRemainingFifteenTurn(fifteenTurnSharedPreference.getInt(REMAINING, 15));
        userIapData.setRemainingTwentyTurn(twentyTurnSharedPreference.getInt(REMAINING, 20));

        userIapData.setConsumedOneTurn(oneTurnSharedPreference.getInt(CONSUMED, 1));
        userIapData.setConsumedFiveTurn(fiveTurnSharedPreference.getInt(CONSUMED, 5));
        userIapData.setConsumedTenTurn(tenTurnSharedPreference.getInt(CONSUMED, 10));
        userIapData.setConsumedFifteenTurn(fifteenTurnSharedPreference.getInt(CONSUMED, 15));
        userIapData.setConsumedTwentyTurn(twentyTurnSharedPreference.getInt(CONSUMED, 20));

        return userIapData;

    }
    private void saveUserIapData() {
        if (userIapData == null || userIapData.getAmazonUserId() == null) {
            return;
        }
        try {
            final SharedPreferences oneTurnSharedPreference = context.getSharedPreferences("ONETURN_" + userIapData.getAmazonUserId(), Context.MODE_PRIVATE);
            final SharedPreferences fiveTurnSharedPreference = context.getSharedPreferences("FIVETURN_" + userIapData.getAmazonUserId(), Context.MODE_PRIVATE);
            final SharedPreferences tenTurnSharedPreference = context.getSharedPreferences("TENTURN_" + userIapData.getAmazonUserId(), Context.MODE_PRIVATE);
            final SharedPreferences fifteenTurnSharedPreference = context.getSharedPreferences("FIFTEENTURN_" + userIapData.getAmazonUserId(), Context.MODE_PRIVATE);
            final SharedPreferences twentyTurnSharedPreference = context.getSharedPreferences("TWENTYTURN_" + userIapData.getAmazonUserId(), Context.MODE_PRIVATE);

            final SharedPreferences.Editor editor1 = oneTurnSharedPreference.edit();
            final SharedPreferences.Editor editor5 = fiveTurnSharedPreference.edit();
            final SharedPreferences.Editor editor10 = tenTurnSharedPreference.edit();
            final SharedPreferences.Editor editor15 = fifteenTurnSharedPreference.edit();
            final SharedPreferences.Editor editor20 = twentyTurnSharedPreference.edit();

            editor1.putInt(REMAINING, userIapData.getRemainingOneTurn());
            editor5.putInt(REMAINING, userIapData.getRemainingFiveTurn());
            editor10.putInt(REMAINING, userIapData.getRemainingTenTurn());
            editor15.putInt(REMAINING, userIapData.getRemainingFifteenTurn());
            editor20.putInt(REMAINING, userIapData.getRemainingTwentyTurn());

            editor1.putInt(CONSUMED, userIapData.getConsumedOneTurn());
            editor5.putInt(CONSUMED, userIapData.getConsumedFiveTurn());
            editor10.putInt(CONSUMED, userIapData.getConsumedTenTurn());
            editor15.putInt(CONSUMED, userIapData.getConsumedFifteenTurn());
            editor20.putInt(CONSUMED, userIapData.getConsumedTwentyTurn());

            editor1.commit();
            editor5.commit();
            editor10.commit();
            editor15.commit();
            editor20.commit();
        } catch (final Throwable e) {
            Log.e(TAG, "failed to save user iap data:");
        }

    }
    private void grantConsumablePurchase(final Receipt receipt, final UserData userData) {
        try {
            createPurchase(receipt.getReceiptId(), userData.getUserId());
            final MySku mySku = MySku.fromSku(receipt.getSku(), userIapData.getAmazonMarketplace());
            if (mySku == null) {
                Log.w(TAG, "The SKU [" + receipt.getSku() + "] in the receipt is not valid anymore ");
                updatePurchaseStatus(receipt.getReceiptId(), null, PurchaseStatus.UNAVAILABLE);
                PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.UNAVAILABLE);
                return;
            }

            if (updatePurchaseStatus(receipt.getReceiptId(), PurchaseStatus.PAID, PurchaseStatus.FULFILLED)) {
                userIapData.setRemainingOneTurn(userIapData.getRemainingOneTurn() + 1);
                userIapData.setRemainingFiveTurn(userIapData.getRemainingFiveTurn() + 5);
                userIapData.setRemainingTenTurn(userIapData.getRemainingTenTurn() + 10);
                userIapData.setRemainingFifteenTurn(userIapData.getRemainingFifteenTurn() + 15);
                userIapData.setRemainingTwentyTurn(userIapData.getRemainingTwentyTurn() + 20);
                saveUserIapData();
                Log.i(TAG, "Successfuly update purchase from PAID->FULFILLED for receipt id " + receipt.getReceiptId());
                PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.FULFILLED);
            } else {
                Log.w(TAG, "Failed to update purchase from PAID->FULFILLED for receipt id " + receipt.getReceiptId()
                        + ", Status already changed.");
            }

        } catch (final Throwable e) {
            Log.e(TAG, "Failed to grant consumable purchase, with error " + e.getMessage());
        }

    }
    private boolean verifyReceiptFromYourService(final String receiptId, final UserData userData) {
        return true;
    }
    private boolean receiptAlreadyFulfilled(final String receiptId, final UserData userData) {
        final PurchaseRecord receiptRecord = dataSource.getPurchaseRecord(receiptId, userData.getUserId());
        if (receiptRecord == null) {
            return false;
        }
        return !(PurchaseStatus.FULFILLED == receiptRecord.getStatus() || PurchaseStatus.UNAVAILABLE == receiptRecord.getStatus());

    }
    private boolean updatePurchaseStatus(final String receiptId,
                                         final PurchaseStatus fromStatus,
                                         final PurchaseStatus toStatus) {
        return dataSource.updatePurchaseStatus(receiptId, fromStatus, toStatus);
    }
    private void createPurchase(final String receiptId, final String userId) {
        dataSource.createPurchase(receiptId, userId, PurchaseStatus.PAID);
    }
    public boolean eatOrange() {
        if (userIapData == null) {
            buyTurnActivity.showMessage("user not logged in to amazon marketplace!");
        }
        if (userIapData.getRemainingOneTurn () <= 0) {
            buyTurnActivity.showMessage("You don't have anymore Oranges remaining, buy more before eating");
            return false;
        }
        userIapData.setConsumedOneTurn (userIapData.getConsumedOneTurn() + 1);
        userIapData.setRemainingFiveTurn (userIapData.getRemainingOneTurn() - 1);
        saveUserIapData();
        return true;
    }
    public UserIapData getUserIapData() {
        return this.userIapData;
    }

}

