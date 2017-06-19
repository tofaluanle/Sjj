package cn.sjj.ktv.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.sjj.ktv.Logger;
import cn.sjj.ktv.util.SystemTool;

/**
 * RecyclerView的Adapter和ViewHolder的抽象基类，默认提供item点击事件监听和ButterKnife注解的使用；同时ListAdapter提供一个拥有mDataList的成员变量<BR>
 * 第二版针对LayoutManager引起的IndexOutOfBoundsException进行处理，在调用mDataList的相关方法时，直接notify，并且判断是否在主进程
 *
 * @auther 宋疆疆
 * @since 2016/12/26.
 */
public class BaseRecyclerView2 extends RecyclerView {

    //    protected static final boolean DEBUG = Config.DEBUG;
    protected static final boolean DEBUG = false;

    public BaseRecyclerView2(Context context) {
        super(context);
    }

    public BaseRecyclerView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRecyclerView2(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static abstract class Adapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

        protected OnItemClickListener mListener;
        protected boolean             canLoad;

        public void setCanLoad(boolean canLoad) {
            this.canLoad = canLoad;
        }

        public void setListener(OnItemClickListener mListener) {
            this.mListener = mListener;
        }

        public interface OnItemClickListener {

            void onRvItemClick(View v, int position);

            boolean onRvItemLongClick(View v, int position);

        }

    }

    public static abstract class ListAdapter<T, VH extends RecyclerView.ViewHolder> extends Adapter<VH> {

        protected List<T> mDataList = new ArrayList<>();

        public List<T> getDataList() {
            return mDataList;
        }

        public void setData(List<T> dataList) {
            SystemTool.assertMainThread();
            this.mDataList = dataList;
            if (mDataList == null) {
                mDataList = new ArrayList<>();
            }
            notifyDataSetChanged();
        }

        public T setData(int position, T data) {
            SystemTool.assertMainThread();
            if (position >= mDataList.size() || position < 0) {
                Logger.e("setData position: " + position);
                return null;
            }
            T remove = this.mDataList.set(position, data);
            notifyItemChanged(position);
            return remove;
        }

        public T getData(int position) {
            SystemTool.assertMainThread();
            if (position >= mDataList.size() || position < 0) {
                Logger.e("getData position: " + position);
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
            if (position > mDataList.size() || position < 0) {
                Logger.e("addData position: " + position);
                return;
            }
            mDataList.add(position, data);
            notifyItemInserted(position);
        }

        public boolean addAllData(List<T> list) {
            SystemTool.assertMainThread();
            boolean add = mDataList.addAll(list);
            if (add) {
                notifyItemRangeInserted(getItemCount(), list.size());
            }
            return add;
        }

        public boolean addAllData(int position, List<T> list) {
            SystemTool.assertMainThread();
            if (position > mDataList.size() || position < 0) {
                Logger.e("addAllData position: " + position + ", list size: " + list.size());
                return false;
            }
            boolean add = mDataList.addAll(position, list);
            if (add) {
                notifyItemRangeInserted(position, list.size());
            }
            return add;
        }

        public T removeData(int position) {
            SystemTool.assertMainThread();
            if (position > mDataList.size() || position < 0) {
                Logger.e("removeData position: " + position);
                return null;
            }
            T remove = mDataList.remove(position);
            notifyItemRemoved(position);
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

    public static abstract class LinkedListAdapter<T, VH extends RecyclerView.ViewHolder> extends Adapter<VH> {

        protected LinkedList<T> mDataList = new LinkedList<>();

        public LinkedList<T> getDataList() {
            return mDataList;
        }

        public void setData(LinkedList<T> dataList) {
            SystemTool.assertMainThread();
            this.mDataList = dataList;
            if (mDataList == null) {
                mDataList = new LinkedList<>();
            }
            notifyDataSetChanged();
        }

        public T setData(int position, T data) {
            Logger.v(DEBUG, "setData: " + position);
            if (position >= mDataList.size() || position < 0) {
                Logger.e("setData position: " + position);
                return null;
            }
            T remove = this.mDataList.set(position, data);
            notifyItemChanged(position);
            return remove;
        }

        public T getData(int position) {
            SystemTool.assertMainThread();
            if (position >= mDataList.size() || position < 0) {
                Logger.e("getData position: " + position);
                return null;
            }
            return mDataList.get(position);
        }

        public void addData(T data) {
            SystemTool.assertMainThread();
            Logger.v(DEBUG, "addData: " + mDataList.size());
            mDataList.add(data);
            notifyItemInserted(getItemCount());
        }

        public void addData(int position, T data) {
            SystemTool.assertMainThread();
            Logger.v(DEBUG, "addData: " + position);
            if (position > mDataList.size() || position < 0) {
                Logger.e("addData position: " + position);
                return;
            }
            mDataList.add(position, data);
            notifyItemInserted(position);
        }

        public boolean addAllData(List<T> list) {
            SystemTool.assertMainThread();
            Logger.v(DEBUG, "addAllData: " + mDataList.size());
            boolean add = mDataList.addAll(list);
            if (add) {
                notifyItemRangeInserted(getItemCount(), list.size());
            }
            return add;
        }

        public boolean addAllData(int position, List<T> list) {
            SystemTool.assertMainThread();
            Logger.v(DEBUG, "addAllData: " + position);
            if (position > mDataList.size() || position < 0) {
                Logger.e("addAllData position: " + position + ", list size: " + list.size());
                return false;
            }
            boolean add = mDataList.addAll(position, list);
            if (add) {
                notifyItemRangeInserted(position, list.size());
            }
            return add;
        }

        public T removeData(int position) {
            SystemTool.assertMainThread();
            Logger.v(DEBUG, "removeData: " + position);
            if (position > mDataList.size() || position < 0) {
                Logger.e("removeData position: " + position);
                return null;
            }
            T remove = mDataList.remove(position);
            notifyItemRemoved(position);
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

    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        private Adapter.OnItemClickListener mListener;
        private T                           mBean;

        public T getBean() {
            return mBean;
        }

        public void setBean(T bean) {
            mBean = bean;
        }

        public ViewHolder(View v) {
            super(v);
        }

        public ViewHolder(View v, Adapter.OnItemClickListener listener) {
            super(v);
            mListener = listener;
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return;
                    }
                    mListener.onRvItemClick(itemView, position);
                }
            });
            v.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getLayoutPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return false;
                    }
                    return mListener.onRvItemLongClick(v, position);
                }
            });
        }
    }

}
