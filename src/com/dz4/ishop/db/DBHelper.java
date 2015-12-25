package com.dz4.ishop.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	public static final String DATA_BASE_NAME = "qiang_db";
	public static final int DATA_BASE_VERSION = 1;
	public static final String TABLE_NAME = "locallove";

	private SQLiteDatabase mDb;
	
	public DBHelper(Context context) {
		super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		onCreateLoveTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	interface LoveTable{
		String _ID = "_id";
		String USER_ID = "userid";
		String OBJECT_ID = "objectid";
		String IS_LOVE = "islove";
	}
	private void onCreateLoveTable(SQLiteDatabase db){
		  StringBuilder favStr=new StringBuilder();
	      favStr.append("CREATE TABLE IF NOT EXISTS ")
	      		.append(DBHelper.TABLE_NAME)
	      		.append(" ( ").append(LoveTable._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
	      		.append(LoveTable.USER_ID).append(" varchar(100),")
	      		.append(LoveTable.OBJECT_ID).append(" varchar(20),")
	      		.append(LoveTable.IS_LOVE).append(" Integer);");
	      db.execSQL(favStr.toString());
	}
	
    /**
     * @param isWrite 
     * @return
     */
    public synchronized SQLiteDatabase getDatabase(boolean isWrite) {

        if(mDb == null || !mDb.isOpen()) {
            if(isWrite) {
                try {
                    mDb=getWritableDatabase();
                } catch(Exception e) {
                    mDb=getReadableDatabase();
                    return mDb;
                }
            } else {
                mDb=getReadableDatabase();
            }
        }
        return mDb;
    }
    
    public int delete(String table, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return mDb.delete(table, whereClause, whereArgs);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        getDatabase(true);
        return mDb.insertOrThrow(table, nullColumnHack, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return mDb.update(table, values, whereClause, whereArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        getDatabase(false);
        return mDb.rawQuery(sql, selectionArgs);
    }

    public void execSQL(String sql) {
        getDatabase(true);
        mDb.execSQL(sql);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
    // final
        String orderBy) {
        getDatabase(false);
        return mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

}
