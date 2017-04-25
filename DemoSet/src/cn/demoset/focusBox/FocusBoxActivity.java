package cn.demoset.focusBox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import cn.demoset.R;

public class FocusBoxActivity extends Activity implements OnClickListener,
		OnFocusChangeListener, OnItemSelectedListener,OnItemClickListener {

	private Button btn3;
	private Button btn2;
	private Button btn1;
	private Button btn4;
	private Button btn5;
	private FocusBoxView bv;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.focus_box);

		findView();
		init();
	}

	private void init() {
		bv.setBox(R.drawable.box);
		String[] objects = new String[20];
		for (int i = 0; i < 20; i++) {
			objects[i] = "haha";
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, objects);
		lv.setAdapter(adapter);
		lv.setOnItemSelectedListener(this);
		lv.setOnItemClickListener(this);
	}

	private void findView() {
		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		btn3 = (Button) findViewById(R.id.button3);
		btn4 = (Button) findViewById(R.id.button4);
		btn5 = (Button) findViewById(R.id.button5);
		bv = (FocusBoxView) findViewById(R.id.box);
		lv = (ListView) findViewById(R.id.lv);

		btn1.setOnFocusChangeListener(this);
		btn2.setOnFocusChangeListener(this);
		btn3.setOnFocusChangeListener(this);
		btn4.setOnFocusChangeListener(this);
		btn5.setOnFocusChangeListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			bv.goToTargetView(v);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			break;
		case R.id.button2:
			break;
		case R.id.button3:

			break;
		default:
			break;
		}
		bv.goToTargetView(v);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		bv.goToTargetView(view);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		bv.goToTargetView(view);
	}

}
