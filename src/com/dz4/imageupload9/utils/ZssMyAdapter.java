package com.dz4.imageupload9.utils;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;
 


import com.dz4.ishopping.R;
public class ZssMyAdapter extends CommonAdapter<String>{

	/** 
     * �ļ���·�� 
     */
	
	public ZssMyAdapter(Context context, List<String> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, String item) {
		 int i = helper.getPosition();
		//    /storage/sdcard0/DCIM/Camera
		 // ����ͼƬ  
		helper.setImageByUrl(R.id.id_show_image,item);
		
		final ImageView mImageView = helper.getView(R.id.id_show_image);
	}

}
