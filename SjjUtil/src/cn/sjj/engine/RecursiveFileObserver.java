package cn.sjj.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.os.FileObserver;
import android.util.Log;

/**
 * 循环递归检测文件系统的变化
 * @author 宋疆疆
 * @date 2014-2-19 下午5:51:12
 */
@SuppressWarnings(value = { "rawtypes", "unchecked" })
public class RecursiveFileObserver extends FileObserver {

	/** Only modification events */
	public static int CHANGES_ONLY = CREATE | DELETE | CLOSE_WRITE
			| DELETE_SELF | MOVE_SELF | MOVED_FROM | MOVED_TO;

	List mObservers;
	String mPath;
	int mMask;

	public RecursiveFileObserver(String path) {
		this(path, ALL_EVENTS);
	}

	public RecursiveFileObserver(String path, int mask) {
		super(path, mask);
		mPath = path;
		mMask = mask;
	}

	@Override
	public void startWatching() {
		if (mObservers != null)
			return;

		mObservers = new ArrayList();
		Stack stack = new Stack();
		stack.push(mPath);

		while (!stack.isEmpty()) {
			String parent = (String) stack.pop();
			mObservers.add(new SingleFileObserver(parent, mMask));
			File path = new File(parent);
			File[] files = path.listFiles();
			if (null == files)
				continue;
			for (File f : files) {
				if (f.isDirectory() && !f.getName().equals(".")
						&& !f.getName().equals("..")) {
					stack.push(f.getPath());
				}
			}
		}

		for (int i = 0; i < mObservers.size(); i++) {
			SingleFileObserver sfo = (SingleFileObserver) mObservers.get(i);
			sfo.startWatching();
		}
	};

	@Override
	public void stopWatching() {
		if (mObservers == null)
			return;

		for (int i = 0; i < mObservers.size(); i++) {
			SingleFileObserver sfo = (SingleFileObserver) mObservers.get(i);
			sfo.stopWatching();
		}

		mObservers.clear();
		mObservers = null;
	};

	@Override
	public void onEvent(int event, String path) {
		switch (event) {
		case FileObserver.ACCESS:
			Log.i("sjj", "ACCESS: " + path);
			break;
		case FileObserver.ATTRIB:
			Log.i("sjj", "ATTRIB: " + path);
			break;
		case FileObserver.CLOSE_NOWRITE:
			Log.i("sjj", "CLOSE_NOWRITE: " + path);
			break;
		case FileObserver.CLOSE_WRITE:
			Log.i("sjj", "CLOSE_WRITE: " + path);
			break;
		case FileObserver.CREATE:
			Log.i("sjj", "CREATE: " + path);
			break;
		case FileObserver.DELETE:
			Log.i("sjj", "DELETE: " + path);
			break;
		case FileObserver.DELETE_SELF:
			Log.i("sjj", "DELETE_SELF: " + path);
			break;
		case FileObserver.MODIFY:
			Log.i("sjj", "MODIFY: " + path);
			break;
		case FileObserver.MOVE_SELF:
			Log.i("sjj", "MOVE_SELF: " + path);
			break;
		case FileObserver.MOVED_FROM:
			Log.i("sjj", "MOVED_FROM: " + path);
			break;
		case FileObserver.MOVED_TO:
			Log.i("sjj", "MOVED_TO: " + path);
			break;
		case FileObserver.OPEN:
			Log.i("sjj", "OPEN: " + path);
			break;
		default:
			Log.i("sjj", "DEFAULT(" + event + " : " + path);
			break;
		}
	}

	/**
	 * Monitor single directory and dispatch all events to its parent, with full
	 * path.
	 */
	class SingleFileObserver extends FileObserver {
		String mPath;

		public SingleFileObserver(String path) {
			this(path, ALL_EVENTS);
			mPath = path;
		}

		public SingleFileObserver(String path, int mask) {
			super(path, mask);
			mPath = path;
		}

		@Override
		public void onEvent(int event, String path) {
			String newPath = mPath + "/" + path;
			RecursiveFileObserver.this.onEvent(event, newPath);
		}
	}
}
