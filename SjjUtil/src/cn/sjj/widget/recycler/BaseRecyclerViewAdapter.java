package cn.sjj.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import cn.sjj.Logger;
import cn.sjj.util.LazyUtil;
import cn.sjj.util.SystemTool;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.internal.functions.Functions;

/**
 * RecyclerView的Adapter和ViewHolder的抽象基类，默认提供item点击事件监听和ButterKnife注解的使用；同时ListAdapter提供一个拥有mDataList的成员变量<BR>
 * 第二版针对LayoutManager引起的IndexOutOfBoundsException进行处理，在调用mDataList的相关方法时，直接notify，并且判断是否在主进程<BR>
 * 第三版统一了两个ListAdapter的代码
 *
 * @author 宋疆疆
 * @since 2016/12/26.
 */
public class BaseRecyclerViewAdapter {

    //    protected static final boolean DEBUG = Config.DEBUG;
    protected static final boolean DEBUG = false;

    public static abstract class Adapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements RecyclerViewScrollerHelper.INotLoadOnScroll {

        protected OnItemClickListener mListener;
        protected boolean             mCanLoad = true;

        @Override
        public void setCanLoad(boolean canLoad) {
            this.mCanLoad = canLoad;
        }

        public void setListener(OnItemClickListener mListener) {
            this.mListener = mListener;
        }

    }

    public static abstract class ListAdapter<T, VH extends RecyclerView.ViewHolder, L extends List<T>> extends Adapter<VH> {

        protected L mDataList;

        protected abstract void createList();

        public ListAdapter() {
            createList();
        }

        public L getDataList() {
            return mDataList;
        }

        public void setData(L dataList) {
            SystemTool.assertMainThread();
            this.mDataList = dataList;
            if (mDataList == null) {
                createList();
            }
            notifyDataSetChanged();
        }

        public T setData(int position, T data) {
            SystemTool.assertMainThread();
            Logger.v(DEBUG, "setData: " + position);
            if (position >= mDataList.size() || position < 0) {
                LazyUtil.sendUnExpect(this, new Exception(), "setData position: " + position + ", mDataList size: " + mDataList.size());
                return null;
            }
            T remove = this.mDataList.set(position, data);
            notifyItemChanged(position);
            return remove;
        }

        public T getData(int position) {
            SystemTool.assertMainThread();
            if (position >= mDataList.size() || position < 0) {
                LazyUtil.sendUnExpect(this, new Exception(), "getData position: " + position + ", mDataList size: " + mDataList.size());
                return null;
            }
            return mDataList.get(position);
        }

        public void addData(T data) {
            SystemTool.assertMainThread();
            mDataList.add(data);
            notifyItemInserted(getItemCount());
        }

        public void addData(int position, T data) {
            SystemTool.assertMainThread();
            Logger.v(DEBUG, "addData: " + position);
            if (position > mDataList.size() || position < 0) {
                LazyUtil.sendUnExpect(this, new Exception(), "addData position: " + position + ", mDataList size: " + mDataList.size());
                return;
            }
            mDataList.add(position, data);
            notifyItemInserted(position);
        }

        public boolean addAllData(L list) {
            SystemTool.assertMainThread();
            Logger.v(DEBUG, "addAllData mDataList size: " + mDataList.size());
            if (list.isEmpty()) {
                return true;
            }
            boolean add = mDataList.addAll(list);
            if (add) {
                notifyItemRangeInserted(getItemCount(), list.size());
            } else {
                LazyUtil.sendUnExpect(this, new Exception(), "addAllData list size: " + list.size() + ", but call addAll method fail" + ", mDataList size: " + mDataList.size());
            }
            return add;
        }

        public boolean addAllData(int position, L list) {
            SystemTool.assertMainThread();
            Logger.v(DEBUG, "addAllData: " + position);
            if (list.isEmpty()) {
                return true;
            }
            if (position > mDataList.size() || position < 0) {
                LazyUtil.sendUnExpect(this, new Exception(), "addAllData position: " + position + ", mDataList size: " + mDataList.size() + ", list size: " + list.size());
                return false;
            }
            boolean add = mDataList.addAll(position, list);
            if (add) {
                notifyItemRangeInserted(position, list.size());
            } else {
                LazyUtil.sendUnExpect(this, new Exception(), "addAllData position: " + position + ", list size: " + list.size() + ", but call addAll method fail" + ", mDataList size: " + mDataList.size());
            }
            return add;
        }

        public T removeData(int position) {
            SystemTool.assertMainThread();
            Logger.v(DEBUG, "removeData: " + position);
            if (position > mDataList.size() || position < 0) {
                LazyUtil.sendUnExpect(this, new Exception(), "removeData position: " + position + ", mDataList size: " + mDataList.size());
                return null;
            }
            T remove = mDataList.remove(position);
            notifyItemRemoved(position);
            return remove;
        }

        public T moveData(int from, int to) {
            SystemTool.assertMainThread();
            Logger.v(DEBUG, String.format("moveData from: %d, to: %d", from, to));
            if (from > mDataList.size() || from < 0 || to > mDataList.size() || to < 0) {
                LazyUtil.sendUnExpect(this, new Exception(), String.format("moveData from: %d, to: %d, mDataList size: %d", from, to, mDataList.size()));
                return null;
            }
            T remove = mDataList.remove(from);
            mDataList.add(to, remove);
            notifyItemMoved(from, to);
            return remove;
        }

        public void clearData() {
            SystemTool.assertMainThread();
            mDataList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

    public static abstract class ArrayListAdapter<T, VH extends RecyclerView.ViewHolder> extends ListAdapter<T, VH, ArrayList<T>> {

        @Override
        protected void createList() {
            mDataList = new ArrayList<>();
        }

    }

    public static abstract class LinkedListAdapter<T, VH extends RecyclerView.ViewHolder> extends ListAdapter<T, VH, LinkedList<T>> {

        @Override
        protected void createList() {
            mDataList = new LinkedList<>();
        }

    }

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        private OnItemClickListener mListener;
        private T                   mBean;

        public T getBean() {
            return mBean;
        }

        public void setBean(T bean) {
            mBean = bean;
        }

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public ViewHolder(View v, OnItemClickListener listener) {
            this(v, listener, true);
        }

        public ViewHolder(View v, OnItemClickListener listener, boolean canThrottle) {
            super(v);
            ButterKnife.bind(this, v);
            mListener = listener;

            if (mListener != null) {
                Observable<Object> observable = RxView.clicks(v);
                if (canThrottle) {
                    observable = observable.throttleFirst(1, TimeUnit.SECONDS);
                }
                Action action = new Action() {
                    @Override
                    public void run() throws Exception {
                        int position = getLayoutPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            return;
                        }
                        mListener.onRvItemClick(itemView, mBean, position);
                    }
                };
                observable.subscribe(Functions.actionConsumer(action));

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = getLayoutPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            return false;
                        }
                        return mListener.onRvItemLongClick(v, mBean, position);
                    }
                });
            }
        }
    }

    public interface OnItemClickListener<T> {

        void onRvItemClick(View v, T bean, int position);

        boolean onRvItemLongClick(View v, T bean, int position);

    }

}
