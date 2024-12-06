package com.branternser.pearlsandworkers0906;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.branternser.pearlsandworkers0906.IapManager.PurchaseRecord;

public class PurchaseDataSource {
    public static enum PurchaseStatus {
        PAID, FULFILLED, UNAVAILABLE, UNKNOWN
    }

    private static final String TAG = "SampleIAPManager";

    private SQLiteDatabase database;
    private final SQLiteHelper dbHelper;

    private final String[] allColumns = { SQLiteHelper.COLUMN_RECEIPT_ID, SQLiteHelper.COLUMN_USER_ID,
            SQLiteHelper.COLUMN_STATUS };

    public PurchaseDataSource(final Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createPurchase(final String receiptId, final String userId, final PurchaseStatus status) {
        Log.d(TAG, "createPurchase: receiptId (" + receiptId + "),userId (" + userId + "), status (" + status + ")");

        final ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_RECEIPT_ID, receiptId);
        values.put(SQLiteHelper.COLUMN_USER_ID, userId);
        values.put(SQLiteHelper.COLUMN_STATUS, status.toString());
        try {
            database.insertOrThrow(SQLiteHelper.TABLE_PURCHASES, null, values);
        } catch (final SQLException e) {
            Log.w(TAG, "A purchase with given receipt id already exists, simply discard the new purchase record");
        }
    }

    @SuppressLint("Range")
    private PurchaseRecord cursorToPurchaseRecord(final Cursor cursor) {
        final PurchaseRecord purchaseRecord = new PurchaseRecord();
        purchaseRecord.setReceiptId(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_RECEIPT_ID)));
        purchaseRecord.setUserId(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_USER_ID)));
        try {
            purchaseRecord.setStatus( PurchaseStatus.valueOf(cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_STATUS))));
        } catch (final Exception e) {
            purchaseRecord.setStatus( PurchaseStatus.UNKNOWN);
        }
        return purchaseRecord;
    }

    public final PurchaseRecord getPurchaseRecord(final String receiptId, final String userId) {
        Log.d(TAG, "getPurchaseRecord: receiptId (" + receiptId + "), userId (" + userId + ")");

        final String where = SQLiteHelper.COLUMN_RECEIPT_ID + " = ?";
        final Cursor cursor = database.query(SQLiteHelper.TABLE_PURCHASES,
                allColumns,
                where,
                new String[] { receiptId },
                null,
                null,
                null);
        cursor.moveToFirst();
        // no record found for the given receipt id
        if (cursor.isAfterLast()) {
            Log.d(TAG, "getPurchaseRecord: no record found for receipt id (" + receiptId + ")");
            cursor.close();
            return null;
        }
        final PurchaseRecord purchaseRecord = cursorToPurchaseRecord(cursor);
        cursor.close();
        if (purchaseRecord.getUserId() != null && purchaseRecord.getUserId().equalsIgnoreCase(userId)) {
            Log.d(TAG, "getPurchaseRecord: record found for receipt id (" + receiptId + ")");
            return purchaseRecord;
        } else {
            Log.d(TAG, "getPurchaseRecord: user id not match, receipt id (" + receiptId + "), userId (" + userId + ")");
            // cannot verify the purchase is for the correct user;
            return null;
        }

    }

    public boolean updatePurchaseStatus(final String receiptId,
                                        final PurchaseStatus fromStatus,
                                        final PurchaseStatus toStatus) {
        Log.d(TAG, "updatePurchaseStatus: receiptId (" + receiptId + "), status:(" + fromStatus + "->" + toStatus + ")");

        String where = SQLiteHelper.COLUMN_RECEIPT_ID + " = ?";
        String[] whereArgs = new String[] { receiptId };

        if (fromStatus != null) {
            where = where + " and " + SQLiteHelper.COLUMN_STATUS + " = ?";
            whereArgs = new String[] { receiptId, fromStatus.toString() };
        }
        final ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_STATUS, toStatus.toString());
        final int updated = database.update(SQLiteHelper.TABLE_PURCHASES, values, where, whereArgs);
        Log.d(TAG, "updatePurchaseStatus: updated " + updated);
        return updated > 0;

    }
}

