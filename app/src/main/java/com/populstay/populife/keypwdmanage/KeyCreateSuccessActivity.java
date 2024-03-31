package com.populstay.populife.keypwdmanage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.populstay.populife.R;
import com.populstay.populife.base.BaseActivity;

public class KeyCreateSuccessActivity extends BaseActivity {

	private static final String KEY_FROM = "KEY_FROM";

	private int from;
	public static final int FROM_SHARE_EDIT = 1;
	public static final int FROM_KEY_CREATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_create_success);
		getIntentData();
        initView();
    }

	public static void actionStart(Context context, int from) {
		Intent intent = new Intent(context, KeyCreateSuccessActivity.class);
		intent.putExtra(KEY_FROM, from);
		context.startActivity(intent);
	}

	private void getIntentData(){
		from = getIntent().getIntExtra(KEY_FROM, FROM_SHARE_EDIT);
	}

    private void initView() {
		findViewById(R.id.page_action).setVisibility(View.GONE);
    	TextView title = findViewById(R.id.page_title);
    	if (FROM_SHARE_EDIT == from){
			title.setText(R.string.share_digital_key);
		}else {
			title.setText(R.string.create_bluetooth_key);
		}
    }

}
