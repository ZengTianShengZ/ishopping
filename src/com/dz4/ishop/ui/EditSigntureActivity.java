package com.dz4.ishop.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.listener.UpdateListener;

import com.dz4.ishop.app.IshopApplication;
import com.dz4.ishop.domain.User;
import com.dz4.ishop.utils.Constant;
import com.dz4.ishop.utils.LogUtils;
import com.dz4.ishopping.R;
import com.dz4.support.activity.BaseUIActivity;

public class EditSigntureActivity extends BaseUIActivity implements
		OnClickListener {
	private Button btn_commit;
	private Button btn_cancel;
	private EditText signature_edittext;
	
	private User mUser;

	private final String TAG ="EditSigntureActivity";
	@Override
	public void initView() {
		// TODO �Զ����ɵķ������
		setContentView(R.layout.activity_editsignature);
		btn_commit = (Button) findViewById(R.id.signature_submit);
		btn_cancel = (Button) findViewById(R.id.signature_cancel);
		signature_edittext = (EditText) findViewById(R.id.signature_edittext);
	}

	@Override
	public void initData() {
		// TODO �Զ����ɵķ������
		CharSequence oldcontent =getIntent().getCharSequenceExtra(Constant.BUNDLE_KEY_SIGNATURE);
		signature_edittext.getEditableText().append(oldcontent);
		mUser = (User)getIntent().getSerializableExtra(Constant.BUNDLE_KEY_USER);
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
		case R.id.signature_submit:
			String lastsignature = signature_edittext.getEditableText().toString();	
			mUser.setSignature(lastsignature);
			
			mUser.update(getApplicationContext(), new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO �Զ����ɵķ������
					showToast("�ύ�ɹ�");
					finish();
					LogUtils.i(TAG, "signature is update success!");
					((IshopApplication)getApplication()).notifyDataChange();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO �Զ����ɵķ������
					showToast("�ύʧ��");
					LogUtils.i(TAG, "signature is update failure!");
				}
			});
			break;
		case R.id.signature_cancel:
			finish();
			break;
		default:
			break;
		}
	}
}
