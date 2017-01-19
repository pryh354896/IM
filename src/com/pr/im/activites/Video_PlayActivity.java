package com.pr.im.activites;

import com.pr.im.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class Video_PlayActivity extends Activity{
	 /** Called when the activity is first created. */  
	private String path;
	 MediaController mediaController;
	    @Override  
	    public void onCreate(Bundle savedInstanceState)  
	    {  
	        super.onCreate(savedInstanceState);  
	  
	        setContentView(R.layout.video_play);  
	 
 
        final VideoView videoView = (VideoView) findViewById(R.id.VideoView01);  
//      
//	       Button PauseButton = (Button) this.findViewById(R.id.PauseButton);  
//          Button LoadButton = (Button) this.findViewById(R.id.LoadButton);  
//	        Button PlayButton = (Button) this.findViewById(R.id.PlayButton);  
	        path = getIntent().getStringExtra("videoPath");
	        
	        Uri uri = Uri.parse(path);   
	        //调用系统自带的播放器  
	            Intent intent = new Intent(Intent.ACTION_VIEW);  
	            Log.v("URI:::::::::", uri.toString());  
	            intent.setDataAndType(uri, "video/3gp"); 
	            startActivity(intent);  
//	        mediaController = new MediaController(this);
//	        videoView.setMediaController(mediaController);
//	        mediaController.setMediaPlayer(videoView);
//	        videoView.setVideoPath(path);   
//            videoView.start();
//            videoView.requestFocus(); 
//            videoView.setOnErrorListener(new OnErrorListener() {
//				
//				@Override
//				public boolean onError(MediaPlayer mp, int what, int extra) {
//					System.out.println("播放出错！");
//					return false;
//				}
//			});
            
            
	          
//	       // load  
//	        LoadButton.setOnClickListener(new OnClickListener() {  
//	            public void onClick(View arg0)  
//	           {  
//	             System.out.println(path+ "====================================");
//	            
//	            }  
//	        });  
//	 
//	       // play  
//	       PlayButton.setOnClickListener(new OnClickListener() {  
//	            public void onClick(View arg0)  
//	            {  
//	            	 
//	           }  
//	        });  
//	 
//	        // pause  
//	        PauseButton.setOnClickListener(new OnClickListener() {  
//	            public void onClick(View arg0)  
//	            {  
//                videoView.pause();  
//	            }  
//	        });  
	    }  

}
