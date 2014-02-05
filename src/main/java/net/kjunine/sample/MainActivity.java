package net.kjunine.sample;

import java.util.concurrent.TimeUnit;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.Transactional;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.BooleanRes;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.StringRes;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

	@ViewById
	EditText myEditText;

	@ViewById(R.id.myTextView)
	TextView textView;

	@StringRes(R.string.hello_world)
	String helloFormat;

	@ColorRes
	int androidColor;

	@BooleanRes
	boolean someBoolean;

	@SystemService
	NotificationManager notificationManager;

	@SystemService
	WindowManager windowManager;

	public void onBackPressed() {
		Toast.makeText(this, "Back key pressed!", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		windowManager.getDefaultDisplay();
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}

	@Click
	void myButtonClicked() {
		myEditText.setText("Hello");
	}

	@Background
	void someBackgroundWork(String name, long timeToDoSomeLongComputation) {
		try {
			TimeUnit.SECONDS.sleep(timeToDoSomeLongComputation);
		} catch (InterruptedException e) {
		}

		String message = String.format(helloFormat, name);

		updateUi(message, androidColor);

		showNotificationsDelayed();
	}

	@UiThread
	void updateUi(String message, int color) {
		setProgressBarIndeterminateVisibility(false);
		textView.setText(message);
		textView.setTextColor(color);
	}

	@UiThread(delay = 2000)
	void showNotificationsDelayed() {
		Notification notification = new Notification.Builder(
				getApplicationContext())
				.setSmallIcon(R.drawable.ic_launcher)
				.setTicker("Hello !")
				.setContentTitle("My notification")
				.setContentText("Hello World!")
				.setContentIntent(
						PendingIntent.getActivity(getApplicationContext(), 0,
								new Intent(), 0)).build();

		notificationManager.notify(1, notification);
	}

	@LongClick
	void startExtraActivity() {
		String name = myEditText.getText().toString();
		setProgressBarIndeterminateVisibility(true);
		someBackgroundWork(name, 5);
		// Intent intent = ActivityWithExtra_.intent(this).myDate(new
		// Date()).myMessage("hello !").get();
		// intent.putExtra(ActivityWithExtra.MY_INT_EXTRA, 42);
		// startActivity(intent);
	}

	@Click
	void startListActivity(View v) {
		// startActivity(new Intent(this, MyListActivity_.class));
	}

	@Touch
	void myTextView(MotionEvent event) {
		Log.d("MyActivity", "myTextView was touched!");
	}

	@Transactional
	int transactionalMethod(SQLiteDatabase db, int someParam) {
		return 42;
	}

}
