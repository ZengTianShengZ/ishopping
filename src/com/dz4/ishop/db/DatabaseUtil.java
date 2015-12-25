package com.dz4.ishop.db;

import java.util.Iterator;
import java.util.List;

import com.dz4.ishop.db.DBHelper.LoveTable;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.utils.LogUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DatabaseUtil {
	 private static final String TAG="DatabaseUtil";
	    private static DatabaseUtil instance;

	    /** 鏁版嵁搴撳府鍔╃被 **/
	    private DBHelper dbHelper;

	    public synchronized static DatabaseUtil getInstance(Context context) {
	        if(instance == null) {
	            instance=new DatabaseUtil(context);
	        }
	        return instance;
	    }

	    /**
	     * 鍒濆鍖�
	     * @param context
	     */
	    private DatabaseUtil(Context context) {
	        dbHelper=new DBHelper(context);
	    }

	    /**
	     * 閿�姣�
	     */
	    public static void destory() {
	        if(instance != null) {
	            instance.onDestory();
	        }
	    }

	    /**
	     * 閿�姣�
	     */
	    public void onDestory() {
	        instance=null;
	        if(dbHelper != null) {
	            dbHelper.close();
	            dbHelper=null;
	        }
	    }
	    
	    
	    public void deleteLove(QiangItem entity,User user){
	    	Cursor cursor=null;
	    	String where = LoveTable.USER_ID+" = '"+user.getObjectId()
	    			+"' AND "+LoveTable.OBJECT_ID+" = '"+entity.getObjectId()+"'";
	    	cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
	    	if(cursor != null && cursor.getCount() > 0) {
            	cursor.moveToFirst();
            	int isLove = cursor.getInt(cursor.getColumnIndex(LoveTable.IS_LOVE));
            	if(isLove==0){
            		dbHelper.delete(DBHelper.TABLE_NAME, where, null);
            	}else{
            		ContentValues cv = new ContentValues();
            		cv.put(LoveTable.IS_LOVE, 0);
            		dbHelper.update(DBHelper.TABLE_NAME, cv, where, null);
            	}
	    	}
	    	if(cursor != null) {
	            cursor.close();
	            dbHelper.close();
	        }
	    }
	    
	    
	    public boolean isLoved(QiangItem qy,User user){
	    	Cursor cursor = null;
	    	String where = LoveTable.USER_ID+" = '"+user.getObjectId()
	    			+"' AND "+LoveTable.OBJECT_ID+" = '"+qy.getObjectId()+"'";
	    	cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
	    	if(cursor!=null && cursor.getCount()>0){
	    		cursor.moveToFirst();
	    		 if(cursor.getInt(cursor.getColumnIndex(LoveTable.IS_LOVE))==1){
	    			 return true;
	    		 }
	    	}
	    	return false;
	    }
	    
	    public long insertLove(QiangItem entity,User user){
	    	long uri = 0;
	    	Cursor cursor=null;
	    	String where = LoveTable.USER_ID+" = '"+user.getObjectId()
	    			+"' AND "+LoveTable.OBJECT_ID+" = '"+entity.getObjectId()+"'";
            cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
            if(cursor != null && cursor.getCount() > 0) {
            	cursor.moveToFirst();
            	ContentValues conv = new ContentValues();
            	conv.put(LoveTable.IS_LOVE, 1);
            	dbHelper.update(DBHelper.TABLE_NAME, conv, where, null);
            }else{
		    	ContentValues cv = new ContentValues();
		    	cv.put(LoveTable.USER_ID, user.getObjectId());
		    	cv.put(LoveTable.OBJECT_ID, entity.getObjectId());
		    	cv.put(LoveTable.IS_LOVE, entity.isMyLove()==true?1:0);
		    	uri = dbHelper.insert(DBHelper.TABLE_NAME, null, cv);
            }
	    	 if(cursor != null) {
		            cursor.close();
		            dbHelper.close();
		        }
	    	return uri;
	    }

	    
	    /**
	     * 璁剧疆鍐呭鐨勬敹钘忕姸鎬�
	     * @param context
	     * @param lists
	     */
	    public List<QiangItem> setLove(List<QiangItem> lists,User user) {
	        Cursor cursor=null;
	        if(lists != null && lists.size() > 0) {
	            for(Iterator iterator=lists.iterator(); iterator.hasNext();) {
	        	    QiangItem content=(QiangItem)iterator.next();
	            	String where = LoveTable.USER_ID+" = '"+user.getObjectId()//content.getAuthor().getObjectId()
	    	    			+"' AND "+LoveTable.OBJECT_ID+" = '"+content.getObjectId()+"'";
	                cursor=dbHelper.query(DBHelper.TABLE_NAME, null, where, null, null, null, null);
	                if(cursor != null && cursor.getCount() > 0) {
	                	cursor.moveToFirst();
	                    if(cursor.getInt(cursor.getColumnIndex(LoveTable.IS_LOVE))==1){
	                    	content.setMyLove(true);
	                    }else{
	                    	content.setMyLove(false);
	                    }
	                }else{
	                	content.setMyLove(false);
	                }
	                LogUtils.i(TAG,content.isMyLove()+"");
	            }
	        }
	        if(cursor != null) {
	            cursor.close();
	            dbHelper.close();
	        }
		return lists;
	}

}
