package com.dz4.ishop.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.bmob.BmobPro;
import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.db.DatabaseUtil;
import com.dz4.ishop.domain.Comment;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.sns.TencentShare;
import com.dz4.ishop.sns.TencentShareEntity;
import com.dz4.ishop.ui.EditQiangActivity;
import com.dz4.ishop.ui.GoodsDetailActivity;
import com.dz4.ishop.ui.LoginActivity;
import com.dz4.ishop.ui.PersonalActivity;
import com.dz4.ishop.ui.ShowImageActivity;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.ImageUtils;
import com.dz4.ishop.view.innerGridView;
import com.dz4.ishopping.R;
import com.dz4.support.proxy.ActivityProxy;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 *  ǽ��ListView��������
 *  
 * @author MZone
 *
 */
public class PersonalQiangListAdapter extends BaseAdapter {
	private final String TAG = "QiangListAdapter";
	private ArrayList<QiangItem> datalist;
	private Context mContext;
	private BmobRelation focusRelation;
	private GridViewAdapter mGridViewAdapter;
	private ScaleAnimation scaleAnimation_b,scaleAnimation_s;
	private User user;
	private User cUser;
	private Activity activity;
	
	public PersonalQiangListAdapter(Activity activity,ArrayList datalist,User user){
		this.datalist=datalist;
		this.activity = activity;
		this.mContext=activity.getApplicationContext();
		this.user = user;
		
		initAnimation();
	}
	
	private void initAnimation() {
		 
		//ǰ�ĸ�������ʾ��ԭ����С��100%��С��10%�����ĸ�������Ϊȷ�������ĵ㡱  
        scaleAnimation_s = new ScaleAnimation(1.5f, 0.8f, 1.5f,  
                0.8f, Animation.RELATIVE_TO_SELF, 0.5f,  
                Animation.RELATIVE_TO_SELF, 0.5f); 
        scaleAnimation_s.setFillBefore(false);
        scaleAnimation_s.setDuration(800);
		
	}
	@Override
	public int getCount() {
		return datalist.size();
	}

	@Override
	public Object getItem(int position) {
		return datalist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_personal_qiang, null);
			viewHolder.nickName = (TextView) convertView
					.findViewById(R.id.user_name);
			viewHolder.userLogo = (ImageView) convertView
					.findViewById(R.id.user_logo);
			viewHolder.qiangtime = (TextView) convertView
					.findViewById(R.id.qiang_time);
			viewHolder.contentText = (TextView) convertView
					.findViewById(R.id.content_text);
			viewHolder.deleteBtn = (TextView) convertView
					.findViewById(R.id.delete);
			viewHolder.contentImage = (innerGridView) convertView
					.findViewById(R.id.content_image_gridView);
			viewHolder.lin_love =  (LinearLayout) convertView
					.findViewById(R.id.item_action_lin_love);
			viewHolder.img_love =   (ImageView) convertView
					.findViewById(R.id.item_action_img_love);
			viewHolder.love = (TextView) convertView
					.findViewById(R.id.item_action_love);
			viewHolder.hate = (TextView) convertView
					.findViewById(R.id.item_action_hate);
			viewHolder.share = (TextView) convertView
					.findViewById(R.id.item_action_share);
			convertView.setTag(viewHolder);
		}else{
			viewHolder =(ViewHolder)convertView.getTag();
		}
		//TODO
		final QiangItem mQiangItem= (QiangItem) datalist.get(position);
		viewHolder.contentText.setText(mQiangItem.getContent()+"");
		viewHolder.hate.setText(mQiangItem.getHate()+"");
		viewHolder.love.setText(mQiangItem.getLove()+"");
		viewHolder.nickName.setText(mQiangItem.getAuthor().getNickname());
		viewHolder.qiangtime.setText(mQiangItem.getCreatedAt());
		
		
		//�����ͷ��
		viewHolder.userLogo.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if( mQiangItem.getAuthor()==null) return;
				if( user==null) {
					Intent intent = new Intent(mContext,PersonalActivity.class);
					intent.putExtra(Constant.BUNDLE_KEY_AUTHOR, mQiangItem.getAuthor());
					activity.startActivity(intent);
				}else{
					if(user.getObjectId().equalsIgnoreCase(mQiangItem.getAuthor().getObjectId()))
						return;
					Intent intent = new Intent(mContext,PersonalActivity.class);
					intent.putExtra(Constant.BUNDLE_KEY_AUTHOR, mQiangItem.getAuthor());
					activity.startActivity(intent);
				}
			}
			
		});
		
		

		cUser = BmobUser.getCurrentUser(mContext, User.class);
		viewHolder.love.setText(mQiangItem.getLove()+"");
		if(!cUser.getObjectId().equals(user.getObjectId())){
			viewHolder.deleteBtn.setVisibility(View.GONE);
		}

		if(null != cUser){
			if(mQiangItem.isMyLove()){
				viewHolder.love.setTextColor(Color.parseColor("#D95555"));
				viewHolder.img_love.setImageResource(R.drawable.ic_action_love_b);
			}else{
				viewHolder.love.setTextColor(Color.parseColor("#000000"));
				viewHolder.img_love.setImageResource(R.drawable.ic_action_love_a);
			} 
		}else{
			viewHolder.love.setTextColor(Color.parseColor("#000000"));
			viewHolder.img_love.setImageResource(R.drawable.ic_action_love_a);
		}
		/**
		 * ����
		 */
		viewHolder.lin_love.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(null == cUser){
					Toast.makeText(mContext, "����û��¼�����ȵ�¼", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(mContext,LoginActivity.class);
					mContext.startActivity(intent);
					return ;
				}
				if(mQiangItem.isMyLove()){
					Toast.makeText(mContext, "�����޹�", Toast.LENGTH_SHORT).show();
					return;
				}
				if (DatabaseUtil.getInstance(mContext).isLoved(mQiangItem,cUser)) {
					Toast.makeText(mContext, "�����޹�", Toast.LENGTH_SHORT).show();
					return;
				}
				mQiangItem.setLove(mQiangItem.getLove()+1);
				mQiangItem.update(mContext, new UpdateListener(){

					@Override
					public void onFailure(int arg0, String arg1) {
						
						Toast.makeText(mContext, "����ʧ��", Toast.LENGTH_SHORT).show();
						
					}

					@Override
					public void onSuccess() {
						mQiangItem.setMyLove(true);
						DatabaseUtil.getInstance(mContext).insertLove(mQiangItem, cUser);
						viewHolder.love.setText(mQiangItem.getLove()+"");
						viewHolder.love.setTextColor(Color.parseColor("#D95555"));
						viewHolder.img_love.setImageResource(R.drawable.ic_action_love_b);
						viewHolder.img_love.startAnimation(scaleAnimation_s);
			 
					}
					
				});
			}
		});
		viewHolder.deleteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			final ActivityProxy mActivityProxy = new ActivityProxy(activity);
			mActivityProxy.showMsgDialog("ɾ��","�Ƿ�ɾ��?", "ȷ��", "ȡ��", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mActivityProxy.showProgressDialog();
 						mQiangItem.delete(mContext, new DeleteListener() {
							@Override
							public void onSuccess() {
								//ɾ���������
								BmobQuery<Comment> query = new BmobQuery<Comment>();
								query.addWhereEqualTo("qiang", mQiangItem);
								query.findObjects(mContext, new FindListener<Comment>() {
									
									@Override
									public void onSuccess(List<Comment> arg0) {
										List<BmobObject> objects =  new ArrayList<BmobObject>();
										for(Comment c:arg0){
											objects.add(c);
										}
										new BmobObject().deleteBatch(mContext,objects, new DeleteListener() {
											
											@Override
											public void onSuccess() {
												mActivityProxy.showToast("ɾ���ɹ�");
												((IshopApplication)activity.getApplication()).notifyDataChange();
												datalist.remove(mQiangItem);
												notifyDataSetChanged();
												mActivityProxy.cancelProgressDialog();
											}
											
											@Override
											public void onFailure(int arg0, String arg1) {
												mActivityProxy.showToast("ɾ��ʧ��");
												mActivityProxy.cancelProgressDialog();
											}
										});
									}
									
									@Override
									public void onError(int arg0, String arg1) {
										// TODO �Զ����ɵķ������
										
									}
								});
								
							}
							
							@Override
							public void onFailure(int arg0, String arg1) {
								// TODO �Զ����ɵķ������
								mActivityProxy.showToast("ɾ��ʧ�ܣ�");
								mActivityProxy.cancelProgressDialog();
							}
						});
					}
				}, new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mActivityProxy.cancelMsgDialog();
					}
				});
			}
		});
		viewHolder.share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO �Զ����ɵķ������
				Toast.makeText(mContext, "��������ѿ�Ŷ~", Toast.LENGTH_SHORT).show();
				final TencentShare tencentShare = new TencentShare(
						activity,
						getQQShareEntity(mQiangItem));
				tencentShare.shareToQQ();
			}
		});
		viewHolder.contentImage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO �Զ����ɵķ������
				Intent intent = new Intent(mContext,GoodsDetailActivity.class);
				intent.putExtra(Constant.BUNDLE_KEY_QIANGITEM, mQiangItem);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,GoodsDetailActivity.class);
				intent.putExtra(Constant.BUNDLE_KEY_QIANGITEM, mQiangItem);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
			}
			
		});
		if (null == mQiangItem.getContentfigureurl()) {
			viewHolder.contentImage.setVisibility(View.GONE);
		} else {
			viewHolder.contentImage.setVisibility(View.VISIBLE);
 
			ArrayList<String> paths = new ArrayList<String>();
			if(mQiangItem.getContentfigureurl()!=null)
				paths.add(mQiangItem.getContentfigureurl().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl1()!=null)
				paths.add(mQiangItem.getContentfigureurl1().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl2()!=null)
				paths.add(mQiangItem.getContentfigureurl2().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl3()!=null)
				paths.add(mQiangItem.getContentfigureurl3().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl4()!=null)
				paths.add(mQiangItem.getContentfigureurl4().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl5()!=null)
				paths.add(mQiangItem.getContentfigureurl5().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl6()!=null)
				paths.add(mQiangItem.getContentfigureurl6().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl7()!=null)
				paths.add(mQiangItem.getContentfigureurl7().getFileUrl(mContext));
			if(mQiangItem.getContentfigureurl8()!=null)
				paths.add(mQiangItem.getContentfigureurl8().getFileUrl(mContext));
			mGridViewAdapter=new GridViewAdapter(mContext, paths);
			viewHolder.contentImage.setAdapter(mGridViewAdapter);
		}
		
		
		String Imageurl = null;
		if (mQiangItem.getAuthor().getAvatar()!=null) {
			Imageurl = mQiangItem.getAuthor().getAvatar().getFileUrl(mContext);
		}
		ImageLoader.getInstance().displayImage(Imageurl,viewHolder.userLogo, ImageUtils.getOptions(R.drawable.user_icon_default_main),new SimpleImageLoadingListener(){
					public void onLoadingComplete(String imageUri, View view, android.graphics.Bitmap loadedImage) {
						
					};
				} );
	 
		return convertView;
	}
	private TencentShareEntity getQQShareEntity(QiangItem qy) {
		String title = "���ڰ���Ʒ�Ͽ����˺ö�����";
		String comment = "������";
		String img = null;
		if (qy.getContentfigureurl() != null) {
			img = qy.getContentfigureurl().getFileUrl(mContext);
		} else {
			img = "http://sdisa.bmob.cn/uploads/567d91eaa8876.png";
		}
		String summary = qy.getContent();

		String targetUrl = "http://sdisa.bmob.cn";
		TencentShareEntity entity = new TencentShareEntity(title, img,
				targetUrl, summary, comment);
		return entity;
	}
	public  class ViewHolder {
		public ImageView userLogo;
		public TextView nickName;
		public TextView qiangtime;
		public TextView contentText;
		public TextView deleteBtn;
		public innerGridView contentImage;

		public LinearLayout lin_love;
		public ImageView img_love;
		public TextView love;
		public TextView hate;
		public TextView share;
		public TextView comment;
	}
}