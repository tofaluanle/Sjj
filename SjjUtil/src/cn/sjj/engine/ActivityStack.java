package cn.sjj.engine;

import java.util.Stack;

import android.app.Activity;

public class ActivityStack {

	private static ActivityStack mInstance = new ActivityStack();
	private Stack<Activity> stack;

	public static ActivityStack getInstance() {
		return mInstance;
	}

	private ActivityStack() {
		super();
		init();
	}

	private void init() {
		stack = new Stack<Activity>();
	}

	public void push(Activity activity) {
		stack.push(activity);
	}

	public Activity pop() {
		return stack.pop();
	}
	
}
