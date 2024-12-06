package com.branternser.pearlsandworkers0906;

import android.util.Log;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.ProductDataResponse;
import com.amazon.device.iap.model.PurchaseResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserDataResponse;

import java.util.HashSet;
import java.util.Set;

public class PurchasingListener implements com.amazon.device.iap.PurchasingListener {

    private static final String TAG = "PearlsandWorkersIAPConsumablesApp";

    private final IapManager iapManager;

    public PurchasingListener(final IapManager iapManager) {
        this.iapManager = iapManager;
    }

    @Override
    public void onUserDataResponse(final UserDataResponse response) {
        Log.d(TAG, "onGetUserDataResponse: requestId (" + response.getRequestId()
                + ") userIdRequestStatus: "
                + response.getRequestStatus()
                + ")");

        final UserDataResponse.RequestStatus status = response.getRequestStatus();
        switch (status) {
            case SUCCESSFUL:
                Log.d(TAG, "onUserDataResponse: get user id (" + response.getUserData().getUserId()
                        + ", marketplace ("
                        + response.getUserData().getMarketplace()
                        + ") ");
                iapManager.setAmazonUserId(response.getUserData().getUserId(), response.getUserData().getMarketplace());
                break;

            case FAILED:
            case NOT_SUPPORTED:
                Log.d(TAG, "onUserDataResponse failed, status code is " + status);
                iapManager.setAmazonUserId(null, null);
                break;
        }
    }

    @Override
    public void onProductDataResponse(final ProductDataResponse response) {
        final ProductDataResponse.RequestStatus status = response.getRequestStatus();
        Log.d(TAG, "onProductDataResponse: RequestStatus (" + status + ")");

        switch (status) {
            case SUCCESSFUL:
                Log.d(TAG, "onProductDataResponse: successful.  The item data map in this response includes the valid SKUs");
                final Set<String> unavailableSkus = response.getUnavailableSkus();
                Log.d(TAG, "onProductDataResponse: " + unavailableSkus.size() + " unavailable skus");
                iapManager.enablePurchaseForSkus(response.getProductData());
                iapManager.disablePurchaseForSkus(response.getUnavailableSkus());
                break;
            case FAILED:
            case NOT_SUPPORTED:
                Log.d(TAG, "onProductDataResponse: failed, should retry request");
                iapManager.disableAllPurchases();
                break;
        }
    }

    @Override
    public void onPurchaseUpdatesResponse(final PurchaseUpdatesResponse response) {
        Log.d(TAG, "onPurchaseUpdatesResponse: requestId (" + response.getRequestId()
                + ") purchaseUpdatesResponseStatus ("
                + response.getRequestStatus()
                + ") userId ("
                + response.getUserData().getUserId()
                + ")");
        final PurchaseUpdatesResponse.RequestStatus status = response.getRequestStatus();
        switch (status) {
            case SUCCESSFUL:
                iapManager.setAmazonUserId(response.getUserData().getUserId(), response.getUserData().getMarketplace());
                for (final Receipt receipt : response.getReceipts()) {
                    iapManager.handleReceipt(receipt, response.getUserData());
                }
                if (response.hasMore()) {
                    PurchasingService.getPurchaseUpdates(false);
                }
                iapManager.refreshTurn();
                break;
            case FAILED:
            case NOT_SUPPORTED:
                Log.d(TAG, "onProductDataResponse: failed, should retry request");
                iapManager.disableAllPurchases();
                break;
        }

    }

    @Override
    public void onPurchaseResponse(final PurchaseResponse response) {
        final String requestId = response.getRequestId().toString();
        final String userId = response.getUserData().getUserId();
        final PurchaseResponse.RequestStatus status = response.getRequestStatus();
        Log.d(TAG, "onPurchaseResponse: requestId (" + requestId
                + ") userId ("
                + userId
                + ") purchaseRequestStatus ("
                + status
                + ")");

        switch (status) {
            case SUCCESSFUL:
                final Receipt receipt = response.getReceipt();
                iapManager.setAmazonUserId(response.getUserData().getUserId(), response.getUserData().getMarketplace());
                Log.d(TAG, "onPurchaseResponse: receipt json:" + receipt.toJSON());
                iapManager.handleReceipt(receipt, response.getUserData());
                iapManager.refreshTurn();
                break;
            case ALREADY_PURCHASED:
                Log.d(TAG, "onPurchaseResponse: already purchased, should never get here for a consumable.");
                break;
            case INVALID_SKU:
                Log.d(TAG,
                        "onPurchaseResponse: invalid SKU!  onProductDataResponse should have disabled buy button already.");
                final Set<String> unavailableSkus = new HashSet<String>();
                unavailableSkus.add(response.getReceipt().getSku());
                iapManager.disablePurchaseForSkus(unavailableSkus);
                break;
            case FAILED:
            case NOT_SUPPORTED:
                Log.d(TAG, "onPurchaseResponse: failed so remove purchase request from local storage");
                iapManager.purchaseFailed(response.getReceipt().getSku());
                break;
        }

    }

}

