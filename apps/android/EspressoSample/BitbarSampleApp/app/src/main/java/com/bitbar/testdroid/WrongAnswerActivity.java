package com.bitbar.testdroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class WrongAnswerActivity extends Activity {

	protected LayoutInflater mInflater;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		
		View view = mInflater.inflate(R.layout.wrong, null);
		setContentView(view);
	}
}
