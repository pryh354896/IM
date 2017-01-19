/**
 * 
 */
package com.pr.im.activites;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.pr.im.R;
import com.pr.im.constant.Constants;
import com.pr.im.constant.MyApplication;
import com.pr.im.d3View.D3View;
import com.pr.im.dao.MsgDbHelper;
import com.pr.im.dao.NewFriendDbHelper;
import com.pr.im.dao.NewMsgDbHelper;
import com.pr.im.util.FileUtil;
import com.pr.im.util.MyAndroidUtil;
import com.pr.im.util.Tool;
import com.pr.im.util.XmppLoadThread;
import com.pr.im.xmpp.XmppConnection;

/**
 * @author PRYH
 *
 */
public class LoginActivity extends BaseActivity implements OnCheckedChangeListener, TextWatcher {
	@D3View(click="onClick") TextView registBtn,changePwdBtn,clearBtn;
	@D3View(click="onClick") Button loginBtn;
	@D3View CheckBox checkBox;
	@D3View TextView nameText,pwdText;
	private String name,pwd;
	private boolean isChecked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acti_login);
		initTitle();
		isChecked = MyApplication.sharedPreferences.getBoolean(Constants.LOGIN_CHECK, false);
		checkBox.setOnCheckedChangeListener(this);
		checkBox.setChecked(isChecked);
		nameText.addTextChangedListener(this);
		
		//已登录过,自动登录
		name = MyApplication.sharedPreferences.getString(Constants.LOGIN_ACCOUNT, null);
		pwd = MyApplication.sharedPreferences.getString(Constants.LOGIN_PWD, null);
		
		if (isChecked) {
			nameText.setText(name);
		} 
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			name = nameText.getText().toString();
			pwd = pwdText.getText().toString();
			if (TextUtils.isEmpty(name)) {
				Tool.initToast(LoginActivity.this, getString(R.string.register_name));
			} else if (TextUtils.isEmpty(pwd)) {
				Tool.initToast(LoginActivity.this, getString(R.string.register_password));
			} else {
				loginAccount(name, pwd);
			}
			break;
			
		case R.id.registBtn:
			Intent intent = new Intent(LoginActivity.this, RegActivity.class);
			startActivity(intent);
			break;
//
////		case R.id.changePwdBtn:
////			startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
////			break;
			
		case R.id.clearBtn:
			new AlertDialog.Builder(this) 
		 	.setTitle("提示")
		 	.setMessage("确认清除痕迹？清除后不可恢复？")
		 	.setPositiveButton("是", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MsgDbHelper.getInstance(getApplicationContext()).clear();
					NewFriendDbHelper.getInstance(getApplicationContext()).clear();
					NewMsgDbHelper.getInstance(getApplicationContext()).clear();
					FileUtil.RecursionDeleteFile(new File(Constants.SAVE_IMG_PATH));
					FileUtil.RecursionDeleteFile(new File(Constants.SAVE_SOUND_PATH));
				}
			})
		 	.setNegativeButton("否", null)
		 	.show();
			
			break;
			
		default:
			break;
		}
	}
	
	
	private void loginAccount(final String userName, final String password) {
		new XmppLoadThread(this) {

			@Override
			protected Object load() {
				boolean isSuccess = XmppConnection.getInstance().login(userName, password);
				if (isSuccess) {
					Constants.USER_NAME = name;
					Constants.PWD = password;
				}
				return isSuccess;
			}

			@Override
			protected void result(Object o) {
				boolean isSuccess = (Boolean) o;
				if (isSuccess) {
					if (isChecked) {
						MyAndroidUtil.editXmlByString(Constants.LOGIN_ACCOUNT, name);
						MyAndroidUtil.editXmlByString(Constants.LOGIN_PWD, pwd);
					}
					else {
						MyAndroidUtil.removeXml(Constants.LOGIN_ACCOUNT);
						MyAndroidUtil.removeXml(Constants.LOGIN_PWD);
					}
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				} else {
					Tool.initToast(LoginActivity.this, getResources().getString(R.string.login_error));
				}
			}

		};
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { 
			if (getIntent().getBooleanExtra("isRelogin", false)){
				return false;
			}
        }
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		if (getIntent().getBooleanExtra("isRelogin", false)) {
			Tool.initToast(getApplicationContext(), "此账号已在别处登录");
		}
		else if (Constants.USER_NAME != null && !Constants.USER_NAME.equals("")) {
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		}
		else {
			if (name!=null && pwd!=null) {  //都不为空,自动登录
				loginAccount(name, pwd);
			}
		}
		super.onResume();
	}
	
	
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		checkBox.setChecked(isChecked);
		this.isChecked = isChecked;
		MyAndroidUtil.editXml(Constants.LOGIN_CHECK, isChecked);
		if (isChecked) {
			checkBox.setButtonDrawable(R.drawable.login_checked);
		} else {
			checkBox.setButtonDrawable(R.drawable.login_check);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
	}

}
