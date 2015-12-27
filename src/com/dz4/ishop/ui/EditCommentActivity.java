package com.dz4.ishop.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.domain.Comment;
import com.dz4.ishop.domain.QiangItem;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;

public class EditCommentActivity extends BaseUIActivity implements
		OnClickListener {
	private Button btn_commit;
	private Button btn_cancel;
	private EditText comment_edittext;
	
	private User mUser;

	private final String TAG ="EditCommentActivity";
	private QiangItem mQiangItem;
	private String commentEdit;
	private Context mContext;
	private User mReplyTo;
	@Override
	public void initView() {
		// TODO �Զ����ɵķ������
		setContentView(R.layout.activity_editcomment);
		btn_commit = (Button) findViewById(R.id.comment_submit);
		btn_cancel = (Button) findViewById(R.id.comment_cancel);
		comment_edittext = (EditText) findViewById(R.id.comment_edittext);
	}

	@Override
	public void initData() {
		// TODO �Զ����ɵķ������
		mContext = getApplicationContext();
		mUser = ((IshopApplication)getApplication()).getCurrentUser();
		mReplyTo = (User)getIntent().getSerializableExtra(Constant.BUNDLE_KEY_REPLYTO);
		mQiangItem = (QiangItem)getIntent().getSerializableExtra(Constant.BUNDLE_KEY_QIANGITEM);
		
	}

	@Override
	public void initEvent() {
		// TODO �Զ����ɵķ������
		btn_cancel.setOnClickListener(this);
		btn_commit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO �Զ����ɵķ������
		switch (v.getId()) {
		case R.id.comment_submit:
			if (mUser != null) {// �ѵ�¼
				commentEdit = comment_edittext.getText().toString().trim();
				if (TextUtils.isEmpty(commentEdit)) {
					showToast("�������ݲ���Ϊ�ա�");
					return;
				}
				showProgressDialog("wait..");
				// comment now
				publishComment(mUser, commentEdit);
			} else {// δ��¼
				showToast("��������ǰ���ȵ�¼��");
				Intent intent = new Intent();
				intent.setClass(this, RegisterActivity.class);
				startActivityForResult(intent, Constant.PUBLISH_COMMENT);
			}
			break;
		case R.id.comment_cancel:
			finish();
			break;
		default:
			break;
		}
	}
	private void publishComment(User user, String content) {

		final Comment comment = new Comment();
		comment.setUser(user);
		if(mReplyTo!=null){
			comment.setReplyTo(mReplyTo.getNickname());
			LogUtils.i(TAG,mReplyTo.getNickname());
		}else{
			comment.setReplyTo(null);
		}
		comment.setCommentContent(content);
		comment.setQiang(mQiangItem);
		comment.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				showToast("���۳ɹ���");
				LogUtils.i(TAG, "�������۳ɹ���");
				setResult(RESULT_OK);
				finish();
				cancelProgressDialog();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				showToast("����ʧ�ܡ���������~");
				LogUtils.i(TAG, "��������ʧ�ܡ�" + arg1);
				cancelProgressDialog();
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constant.PUBLISH_COMMENT:
				// ��¼���
				btn_commit.performClick();
				break;
			default:
				break;
			}
		}

	}
}
