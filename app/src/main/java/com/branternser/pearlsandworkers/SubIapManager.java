package com.branternser.pearlsandworkers0906;


import android.util.Log;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.Product;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserData;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SubIapManager {
    private static final String TAG = "SampleIAPManager";
    private final SubActivity subActivity;
    private final SubscriptionDataSource dataSource;

    private boolean magazineSubsAvailable;
    private SubUserIapData subUserIapData;

    public SubIapManager(final SubActivity subActivity) {
        this.subActivity = subActivity;
        this.dataSource = new SubscriptionDataSource(subActivity.getApplicationContext());
    }

    public void setAmazonUserId(final String newAmazonUserId, final String newAmazonMarketplace) {
        // Reload everything if the Amazon user has changed.
        if (newAmazonUserId == null) {
            if (subUserIapData != null) {
                subUserIapData = null;
                refreshMagazineSubsAvailability();
            }
        } else if (subUserIapData == null || !newAmazonUserId.equals(subUserIapData.getAmazonUserId())) {
            subUserIapData = new SubUserIapData(newAmazonUserId, newAmazonMarketplace);
            refreshMagazineSubsAvailability();
        }
    }

    public void enablePurchaseForSkus(final Map<String, Product> productData) {
        if (productData.containsKey(SubSku.MY_ONE_SUBS.getSku())) {
            magazineSubsAvailable = true;
        }
        if (productData.containsKey(SubSku.MY_FIVE_SUBS.getSku())) {
            magazineSubsAvailable = true;
        }
        if (productData.containsKey(SubSku.MY_TEN_SUBS.getSku())) {
            magazineSubsAvailable = true;
        }
        if (productData.containsKey(SubSku.MY_TWENTY_SUBS.getSku())) {
            magazineSubsAvailable = true;
        }
    }

    public void disablePurchaseForSkus(final Set<String> unavailableSkus) {
        if (unavailableSkus.contains(SubSku.MY_ONE_SUBS.toString())) {
            magazineSubsAvailable = false;
            subActivity.showMessage("the magazine subscription product isn't available now! ");
        }
        if (unavailableSkus.contains(SubSku.MY_FIVE_SUBS.toString())) {
            magazineSubsAvailable = false;
            subActivity.showMessage("the magazine subscription product isn't available now! ");
        }
        if (unavailableSkus.contains(SubSku.MY_TEN_SUBS.toString())) {
            magazineSubsAvailable = false;
            subActivity.showMessage("the magazine subscription product isn't available now! ");
        }
        if (unavailableSkus.contains(SubSku.MY_TWENTY_SUBS.toString())) {
            magazineSubsAvailable = false;
            subActivity.showMessage("the magazine subscription product isn't available now! ");
        }
    }

    public void handleSubscriptionPurchase(final Receipt receipt, final UserData userData) {
        try {
            if (receipt.isCanceled()) {
                revokeSubscription(receipt, userData.getUserId());
            } else {
                if (!verifyReceiptFromYourService(receipt.getReceiptId(), userData)) {
                    subActivity.showMessage("Purchase cannot be verified, please retry later.");
                    return;
                }
                grantSubscriptionPurchase(receipt, userData);
            }
            return;
        } catch (final Throwable e) {
            subActivity.showMessage("Purchase cannot be completed, please retry");
        }

    }

    private void grantSubscriptionPurchase(final Receipt receipt, final UserData userData) {

        final SubSku subSku = SubSku.fromSku(receipt.getSku(), subUserIapData.getAmazonMarketplace());
        if (subSku != SubSku.MY_ONE_SUBS) {
            Log.w(TAG, "The SKU [" + receipt.getSku() + "] in the receipt is not valid anymore ");
            PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.UNAVAILABLE);
            return;
        }
        if (subSku != SubSku.MY_FIVE_SUBS) {
            Log.w(TAG, "The SKU [" + receipt.getSku() + "] in the receipt is not valid anymore ");
            PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.UNAVAILABLE);
            return;
        }
        if (subSku != SubSku.MY_TEN_SUBS) {
            Log.w(TAG, "The SKU [" + receipt.getSku() + "] in the receipt is not valid anymore ");
            PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.UNAVAILABLE);
            return;
        }
        if (subSku != SubSku.MY_TWENTY_SUBS) {
            Log.w(TAG, "The SKU [" + receipt.getSku() + "] in the receipt is not valid anymore ");
            PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.UNAVAILABLE);
            return;
        }
        try {
            saveSubscriptionRecord(receipt, userData.getUserId());
            PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.FULFILLED);

        } catch (final Throwable e) {
            Log.e(TAG, "Failed to grant entitlement purchase, with error " + e.getMessage());
        }

    }

    public void handleReceipt(final String requestId, final Receipt receipt, final UserData userData) {
        switch (receipt.getProductType()) {
            case CONSUMABLE:
                // check consumable sample for how to handle consumable purchases
                break;
            case ENTITLED:
                // check entitlement sample for how to handle consumable purchases
                break;
            case SUBSCRIPTION:
                handleSubscriptionPurchase(receipt, userData);
                break;
        }
    }

    public void purchaseFailed(final String subSku) {
        subActivity.showMessage("Purchase failed!");
    }

    public SubUserIapData getUserIapData() {
        return this.subUserIapData;
    }

    public boolean isMagazineSubsAvailable() {
        return magazineSubsAvailable;
    }

    public void setMagazineSubsAvailable(final boolean magazineSubsAvailable) {
        this.magazineSubsAvailable = magazineSubsAvailable;
    }

    public void disableAllPurchases() {
        this.setMagazineSubsAvailable(false);
        refreshMagazineSubsAvailability();
    }

    public void refreshMagazineSubsAvailability() {
        final boolean available = magazineSubsAvailable && subUserIapData!=null;
        subActivity.setMagazineSubsAvail(available,
                subUserIapData != null && !subUserIapData.isSubsActiveCurrently());
    }

    public void deactivate() {
        dataSource.close();

    }

    public void activate() {
        dataSource.open();

    }

    public void reloadSubscriptionStatus() {
        final List<SubscriptionRecord> subsRecords = dataSource.getSubscriptionRecords(subUserIapData.getAmazonUserId());
        subUserIapData.setSubscriptionRecords(subsRecords);
        subUserIapData.reloadSubscriptionStatus();
        refreshMagazineSubsAvailability();
    }

    private void saveSubscriptionRecord(final Receipt receipt, final String userId) {
        // TODO replace with your own implementation

        dataSource
                .insertOrUpdateSubscriptionRecord(receipt.getReceiptId(),
                        userId,
                        receipt.getPurchaseDate().getTime(),
                        receipt.getCancelDate() == null ? SubscriptionRecord.TO_DATE_NOT_SET
                                : receipt.getCancelDate().getTime(),
                        receipt.getSku());

    }

    private boolean verifyReceiptFromYourService(final String receiptId, final UserData userData) {
        // TODO Add your own server side accessing and verification code
        return true;
    }

    private void revokeSubscription(final Receipt receipt, final String userId) {
        final String receiptId = receipt.getReceiptId();
        dataSource.cancelSubscription(receiptId, receipt.getCancelDate().getTime());

    }

}
