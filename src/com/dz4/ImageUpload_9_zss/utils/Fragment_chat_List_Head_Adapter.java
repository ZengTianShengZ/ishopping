package com.dz4.ImageUpload_9_zss.utils;

import java.util.ArrayList;
import java.util.List;

import com.dz4.ishop.domain.PushNews;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

public class Fragment_chat_List_Head_Adapter extends PagerAdapter {

	private List<ImageView> imageViews = new ArrayList<ImageView>();
	
	public Fragment_chat_List_Head_Adapter(List<ImageView> imageViews){
		this.imageViews = imageViews;
	}
 


	@Override  
      public int getCount() {  
          return imageViews.size();  
      }  

      @Override  
      public Object instantiateItem(View arg0, int arg1) {  
    	  ((ViewPager) arg0).addView(imageViews.get(arg1));  
    	  
          return imageViews.get(arg1);  
      }  

      @Override  
      public void destroyItem(View arg0, int arg1, Object arg2) {  
          ((ViewPager) arg0).removeView((View) arg2);  
      }  

      @Override  
      public boolean isViewFromObject(View arg0, Object arg1) {  
          return arg0 == arg1;  
      }  

      @Override  
      public void restoreState(Parcelable arg0, ClassLoader arg1) {  

      }  

      @Override  
      public Parcelable saveState() {  
          return null;  
      }  

      @Override  
      public void startUpdate(View arg0) {  

      }  

      @Override  
      public void finishUpdate(View arg0) {  

      }  

}
