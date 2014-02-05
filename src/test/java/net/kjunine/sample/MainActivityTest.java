package net.kjunine.sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import android.widget.Button;
import android.widget.EditText;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

	@Test
	public void test() {
		MainActivity_ activity = Robolectric.buildActivity(MainActivity_.class)
				.create().get();
		assertThat(activity, notNullValue());

		Button button = (Button) activity.findViewById(R.id.myButton);
		assertThat(button, notNullValue());

		EditText edit = (EditText) activity.findViewById(R.id.myEditText);
		assertThat(edit, notNullValue());

		assertThat(edit.getText().toString(), is(""));
		button.performClick();
		assertThat(edit.getText().toString(), is("Hello"));
	}

}
