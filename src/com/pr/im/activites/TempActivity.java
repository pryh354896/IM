/**
 * 
 */
package com.pr.im.activites;

import com.pr.im.R;
import com.pr.im.d3View.D3View;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @author MZH
 *
 */
public class TempActivity extends BaseActivity {
	@D3View TextView test;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.acti_register);
		initTitle();
	}
	
	
	public void onClick(View v){
		
		
	}
}
