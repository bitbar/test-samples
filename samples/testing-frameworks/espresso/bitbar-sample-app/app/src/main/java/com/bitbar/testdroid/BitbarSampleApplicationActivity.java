package com.bitbar.testdroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class BitbarSampleApplicationActivity extends Activity {
	
	protected LayoutInflater mInflater;
	RadioButton radioButton;
	EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		
		OnClickListener clickListener = new OnClickListener() {			
			public void onClick(View v) {
				if(radioButton.isChecked()){
					editText = (EditText) findViewById(R.id.editText1);
					String name = editText.getText().toString();
					Intent intent = new Intent(getApplicationContext(), CorrectAnswerActivity.class);
					Bundle b = new Bundle();
					b.putString("name", name);
					intent.putExtras(b);
					startActivity(intent);
				}
				else{
					startActivity(new Intent(getApplicationContext(), WrongAnswerActivity.class));
				}
			}
		};
		
		View view = mInflater.inflate(R.layout.question, null);
		setContentView(view);
		
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(clickListener);
		button.setEnabled(false);
		radioButton = (RadioButton) view.findViewById(R.id.radio1);
		
		final EditText editText = (EditText) view.findViewById(R.id.editText1);
		editText.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {				
				if(editText.getText().length() > 0){
					button.setEnabled(true);
				}
				else{
					button.setEnabled(false);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {								
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
	}
}
