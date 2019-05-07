package com.bitbar.testdroid;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CorrectAnswerActivity extends Activity {

	protected LayoutInflater mInflater;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		
		View view = mInflater.inflate(R.layout.correct, null);
		setContentView(view);
		
		Bundle b = getIntent().getExtras();
		String name = b.getString("name");
		TextView textView = (TextView) findViewById(R.id.textView2);
		if(name.length() > 0)
			textView.setText(String.format("Congratulations %1$s!", name));
	}
}
