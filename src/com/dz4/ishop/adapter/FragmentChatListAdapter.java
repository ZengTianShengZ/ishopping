package com.dz4.ishop.adapter;

import java.util.List;

import com.dz4.imageupload9.utils.CommonAdapter;
import com.dz4.imageupload9.utils.ViewHolder;
import com.dz4.ishop.domain.PushNews;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishopping.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class FragmentChatListAdapter extends CommonAdapter<PushNews>{

	private Context context;
	private ImageView imageView;
	
	public FragmentChatListAdapter(Context context, List<PushNews> mPushNews,int fragmentChatList) {
		 
		super(context, mPushNews, fragmentChatList);
		
		this.context = context;
	}


	@Override
	public void convert(ViewHolder helper, PushNews item) {
		helper.setText(R.id.fragment_char_list_tvTitle, item.getNewsTitle()+"");
		helper.setText(R.id.fragment_char_list_tvTime, item.getCreatedAt());
		
		String Imageurl = null;
		if(item.getNewsImageUrl()!= null){
			Imageurl = item.getNewsImageUrl();
		}
		imageView = helper.getView(R.id.fragment_char_list_image);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		ImageLoader.getInstance().displayImage(Imageurl,
				imageView, 
				ImageUtils.getOptions(R.drawable.user_icon_default_main),new SimpleImageLoadingListener(){
			public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
				
			};
		} );
		
		helper.setText(R.id.fragment_char_list_tvContent, item.getNewsContent()); 
	}

}
