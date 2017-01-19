/**
 * 
 */
package com.pr.im.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pr.im.R;
import com.pr.im.d3View.D3Fragment;
import com.pr.im.d3View.D3View;

/**
 * @author MZH
 *
 */
public class TempFragment extends D3Fragment{
	@D3View TextView test;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.acti_register, null);
	
		return view;
	}
	
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registBtn:
			
			break;

		default:
			break;
		}
	}
}
