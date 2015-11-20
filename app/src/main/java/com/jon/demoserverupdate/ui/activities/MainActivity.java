package com.jon.demoserverupdate.ui.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jon.demoserverupdate.R;
import com.jon.demoserverupdate.app.MainApplication;
import com.jon.demoserverupdate.utils.LogUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity
		extends AppCompatActivity {

	//==============================================
	//              Constants
	//==============================================

	private static final String JSON_KEY_COLOR = "color";
	private static final String JSON_KEY_DATE  = "date";
	private static final String JSON_URL       = "https://dl.dropboxusercontent.com/u/54228215/color_config.json?dl=1";

	//==============================================
	//              Fields
	//==============================================

	private View     mBackgroundView;
	private View     mGetColorFromServerButton;
	private TextView mDateTV;

	//==============================================
	//              Activity Lifecycle
	//==============================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		LogUtils.LogDebug();

		mBackgroundView = findViewById(R.id.mainActivity_background);
		mGetColorFromServerButton = findViewById(R.id.mainActivity_getColorFromServerButton);
		mDateTV = (TextView) findViewById(R.id.mainActivity_dateTV);

		mGetColorFromServerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				LogUtils.LogDebug();

				getJSONWithColorFromServer();
			}
		});
	}

	//==============================================
	//              Private Methods
	//==============================================

	public void getJSONWithColorFromServer() {
		LogUtils.LogDebug();

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(JSON_URL)
		                                       .build();
		client.newCall(request)
		      .enqueue(new Callback() {

			      @Override
			      public void onFailure(Request request, IOException e) { LogUtils.LogDebug();}

			      @Override
			      public void onResponse(Response response) throws
					      IOException {
				      if (!response.isSuccessful()) {
					      throw new IOException("Unexpected code " + response);
				      }
				      handleOnResponse(response);
			      }
		      });
	}

	private void handleOnResponse(Response response) throws
			IOException {

		String body = response.body()
		                      .string();

		LogUtils.LogDebug(body);

		JSONObject jsonObject = null;

		try {
			jsonObject = new JSONObject(body);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (jsonObject != null) {
			extractColorFromJSONAndSetBG(jsonObject);
		}
		else {
			LogUtils.LogDebug("JSON is null");
		}
	}

	private void extractColorFromJSONAndSetBG(JSONObject jsonObject) {

		try {

			final String color = jsonObject.getString(JSON_KEY_COLOR);
			final String date = jsonObject.getString(JSON_KEY_DATE);

			LogUtils.LogDebug("color: " + color + " | " + " date: " + date);

			if (!TextUtils.isEmpty(color) && !TextUtils.isEmpty(date)) {

				MainApplication.getUIhanlder()
				               .post(new Runnable() {
					               @Override
					               public void run() {
						               if (mBackgroundView != null && mDateTV != null) {
							               mBackgroundView.setBackgroundColor(Color.parseColor(color));
							               mDateTV.setText(date);
						               }
					               }
				               });
			}
			else {
				LogUtils.LogDebug("JSON got invalid data");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	//==============================================
	//              Options Menu
	//==============================================

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
