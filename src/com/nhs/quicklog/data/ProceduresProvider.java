package com.nhs.quicklog.data;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.nhs.quicklog.data.ProceduresDatabase.ProcedureOpenHelper;

public class ProceduresProvider extends ContentProvider {
    String TAG = "ProceduresProvider";

    private ProcedureOpenHelper procedureOpenHelper;
    private static HashMap<String, String> ProceduresProjectionMap;

    private static final int PROCEDURES = 1;

    private static final int PROCEDURE_ID = 2;

    private static final int PROCEDURE_NAME = 3;

    private static final UriMatcher sUriMatcher;

    static {

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(QuickLog.AUTHORITY, "procedures", PROCEDURES);

        sUriMatcher.addURI(QuickLog.AUTHORITY, "procedures/#", PROCEDURE_ID);

        sUriMatcher.addURI(QuickLog.AUTHORITY, "procedures/*", PROCEDURE_NAME);

        ProceduresProjectionMap = new HashMap<String, String>();

        ProceduresProjectionMap.put(QuickLog.Procedures._ID,
                QuickLog.Procedures._ID);

        ProceduresProjectionMap.put(QuickLog.Procedures.COLUMN_NAME_NAME,
                QuickLog.Procedures.COLUMN_NAME_NAME);

        ProceduresProjectionMap.put(
                QuickLog.Procedures.COLUMN_NAME_CREATE_DATE,
                QuickLog.Procedures.COLUMN_NAME_CREATE_DATE);

        ProceduresProjectionMap.put(QuickLog.Procedures.COLUMN_NAME_HITS,
                QuickLog.Procedures.COLUMN_NAME_HITS);
    }

    @Override
    public boolean onCreate() {
        procedureOpenHelper = new ProcedureOpenHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {

            case PROCEDURES:
                return QuickLog.Procedures.CONTENT_TYPE;

            case PROCEDURE_ID:
                return QuickLog.Procedures.CONTENT_ITEM_TYPE;

            case PROCEDURE_NAME:
                return QuickLog.Procedures.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(QuickLog.Procedures.TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
            case PROCEDURES:
                qb.setProjectionMap(ProceduresProjectionMap);
                break;

            case PROCEDURE_ID:
                qb.setProjectionMap(ProceduresProjectionMap);
                qb.appendWhere(QuickLog.Procedures._ID
                        + "="
                        + uri.getPathSegments().get(
                        QuickLog.Procedures.PROCEDURE_ID_PATH_POSITION));
                break;

            case PROCEDURE_NAME:
                qb.setProjectionMap(ProceduresProjectionMap);
                qb.appendWhere(QuickLog.Procedures.COLUMN_NAME_NAME
                        + " like '"
                        + uri.getPathSegments().get(
                        QuickLog.Procedures.PROCEDURE_ID_PATH_POSITION) + "%'");
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = QuickLog.Procedures.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = procedureOpenHelper.getReadableDatabase();

        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, orderBy);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    public Uri insert(Uri uri, ContentValues initialValues) {

        if (sUriMatcher.match(uri) != PROCEDURES) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        ContentValues values;

        if (initialValues != null) {
            values = new ContentValues(initialValues);

        } else {
            throw new IllegalArgumentException("Invalid values");
        }

        if (values.containsKey(QuickLog.Procedures.COLUMN_NAME_NAME) == false) {
            throw new IllegalArgumentException("Invalid procedure name");
        }

        Long now = Long.valueOf(System.currentTimeMillis());

        if (values.containsKey(QuickLog.Procedures.COLUMN_NAME_CREATE_DATE) == false) {
            values.put(QuickLog.Procedures.COLUMN_NAME_CREATE_DATE, now);
        }

        if (values.containsKey(QuickLog.Procedures.COLUMN_NAME_HITS) == false) {
            values.put(QuickLog.Procedures.COLUMN_NAME_HITS, 0);
        }

        SQLiteDatabase db = procedureOpenHelper.getWritableDatabase();

        long rowId = db.insert(QuickLog.Procedures.TABLE_NAME,
                QuickLog.Procedures.COLUMN_NAME_NAME, values);

        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(
                    QuickLog.Procedures.CONTENT_ID_URI_BASE, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);

            return noteUri;
        }

        Log.e(TAG, "Failed to insert row into " + uri);

        return null;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {

        SQLiteDatabase db = procedureOpenHelper.getWritableDatabase();
        String finalWhere;

        int count;
        switch (sUriMatcher.match(uri)) {

            case PROCEDURES:
                count = db.delete(QuickLog.Procedures.TABLE_NAME, where, whereArgs);
                break;

            case PROCEDURE_ID:
                finalWhere = QuickLog.Procedures._ID
                        + " = "
                        + uri.getPathSegments().get(
                        QuickLog.Procedures.PROCEDURE_ID_PATH_POSITION);

                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }

                count = db.delete(QuickLog.Procedures.TABLE_NAME, finalWhere,
                        whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where,
                      String[] whereArgs) {

        SQLiteDatabase db = procedureOpenHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (sUriMatcher.match(uri)) {

            case PROCEDURES:

                count = db.update(QuickLog.Procedures.TABLE_NAME, values, where,
                        whereArgs);
                break;

            case PROCEDURE_ID:
                finalWhere = QuickLog.Procedures._ID
                        + " = "
                        + uri.getPathSegments().get(
                        QuickLog.Procedures.PROCEDURE_ID_PATH_POSITION);

                if (where != null) {
                    finalWhere = finalWhere + " AND " + where;
                }

                count = db.update(QuickLog.Procedures.TABLE_NAME, values,
                        finalWhere, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
