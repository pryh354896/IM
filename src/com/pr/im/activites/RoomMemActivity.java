/**
 * 
 */
package com.pr.im.activites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.XMPPException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pr.im.R;
import com.pr.im.adapter.SearchAdapter;
import com.pr.im.constant.Constants;
import com.pr.im.constant.MyApplication;
import com.pr.im.d3View.D3View;
import com.pr.im.dao.MsgDbHelper;
import com.pr.im.dao.NewMsgDbHelper;
import com.pr.im.model.ChatItem;
import com.pr.im.model.Room;
import com.pr.im.util.LoadThread;
import com.pr.im.util.Tool;
import com.pr.im.xmpp.XmppConnection;

/**
 * @author MZH
 *
 */
public class RoomMemActivity extends BaseActivity {
	@D3View(click="onClick") ImageView leftBtn;
	@D3View(click="onClick") TextView rightBtn;
	@D3View(click="onClick") Button exitBtn;
	@D3View ListView listView;
	private SearchAdapter adapter;
//	private List<RoomMem> roomMems;
	private String roomName;
	private List<String> members = new ArrayList<String>(); 
	public static boolean isExit = false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.acti_room_mem);
		roomName = getIntent().getStringExtra("roomName");
		adapter = new SearchAdapter(getApplicationContext());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getApplicationContext(), FriendActivity.class);
				intent.putExtra("username", adapter.getItem(position));
				startActivity(intent);
			}
		});
		for (Room room : XmppConnection.getInstance().getMyRoom()) {
			if (room.name.equals(roomName)) {
				members = room.friendList;
				for (String mem : room.friendList) {
					adapter.add(mem);
				}
			}
		}
		
		
//		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isExit) {
			isExit = false;
			finish();
		}
	}
	
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.leftBtn:
			finish();
			break;
		case R.id.rightBtn:
			Intent intent = new Intent(getApplicationContext(), ChoseActivity.class);
			intent.putExtra("roomName", roomName);
			intent.putStringArrayListExtra("members", (ArrayList<String>) members);
			startActivity(intent);
			break;
		case R.id.exitBtn:
			AlertDialog.Builder builder = new Builder(RoomMemActivity.this);
			builder
			.setMessage("确定要退出吗?")
			.setTitle("提示")
			.setPositiveButton("确认", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("jid", XmppConnection.getFullUsername(Constants.USER_NAME));
					for (Room room : XmppConnection.getInstance().getMyRoom()) {
						if (room.equals(new Room(roomName))) {
							map.put("roomId", room.roomid);
							map.put("roomName", roomName);
							for (String mem : room.friendList) {
								try {
									XmppConnection.getInstance().sendMsg(mem,"[RoomChange,"+roomName+","+Constants.USER_NAME, ChatItem.CHAT);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
					
					new LoadThread(RoomMemActivity.this,Constants.URL_EXIT_ROOM,map) {
						
						@Override
						protected void refreshUI(String result) {
							Tool.initToast(getApplicationContext(), "退出成功");
							XmppConnection.getInstance().getMyRoom().remove(new Room(roomName));
							NewMsgDbHelper.getInstance(getApplicationContext()).delNewMsg(roomName);
							MsgDbHelper.getInstance(getApplicationContext()).delChatMsg(roomName);
							MyApplication.getInstance().sendBroadcast(new Intent("ChatNewMsg"));
							MyApplication.getInstance().sendBroadcast(new Intent("LeaveRoom"));
							XmppConnection.leaveRooms.add(new Room(roomName));
							XmppConnection.getInstance().reconnect();
							ChatActivity.isExit = true;
							finish();
						}
					};
				}
			})
			.setNegativeButton("取消", null)
			.show();
			break;

		default:
			break;
		}
		
	}
}
