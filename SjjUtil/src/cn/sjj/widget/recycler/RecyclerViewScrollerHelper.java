package cn.sjj.widget.recycler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * 给RecyclerView提供在快速滑动时，不加载图片的功能
 *
 * @author 宋疆疆
 * @since 2017/7/5.
 */
public class RecyclerViewScrollerHelper {

    private RecyclerView       mRv;
    private INotLoadOnScroll   mController;
    private onLoadMoreListener loadMoreListener;     //加载更多回调
    private boolean            isLoadingMore;        //是否加载更多

    public RecyclerViewScrollerHelper(RecyclerView mRv, INotLoadOnScroll adapter) {
        this.mRv = mRv;
        mController = adapter;
        init();
    }

    private void init() {
        isLoadingMore = false;    //默认无需加载更多
        mRv.addOnScrollListener(new AutoLoadScrollListener(false, true));
    }

    /**
     * 配置显示图片，需要设置这几个参数，快速滑动时，暂停图片加载
     *
     * @param pauseOnScroll
     * @param pauseOnFling
     */
    public void setOnPauseListenerParams(boolean pauseOnScroll, boolean pauseOnFling) {
        mRv.addOnScrollListener(new AutoLoadScrollListener(pauseOnScroll, pauseOnFling));
    }

    public void setLoadMoreListener(onLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void loadMoreComplete() {
        isLoadingMore = false;
    }


    //加载更多的回调接口
    public interface onLoadMoreListener {
        void loadMore();
    }

    /**
     * 滑动自动加载监听器
     */
    private class AutoLoadScrollListener extends RecyclerView.OnScrollListener {

        private final boolean pauseOnScroll;
        private final boolean pauseOnFling;

        public AutoLoadScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
            super();
            this.pauseOnScroll = pauseOnScroll;
            this.pauseOnFling = pauseOnFling;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            //由于GridLayoutManager是LinearLayoutManager子类，所以也适用
            if (mRv.getLayoutManager() instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) mRv.getLayoutManager()).findLastVisibleItemPosition();
                int totalItemCount = mRv.getAdapter().getItemCount();

                //有回调接口，且不是加载状态，且计算后剩下2个item，且处于向下滑动，则自动加载
                if (loadMoreListener != null && !isLoadingMore && lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                    loadMoreListener.loadMore();
                    isLoadingMore = true;
                }
            }
        }

        //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；由于用户的操作，屏幕产生惯性滑动时为2
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            //根据newState状态做处理
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    mController.setCanLoad(true);
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    if (pauseOnScroll) {
                        mController.setCanLoad(false);
                    } else {
                        mController.setCanLoad(true);
                    }
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    if (pauseOnFling) {
                        mController.setCanLoad(false);
                    } else {
                        mController.setCanLoad(true);
                    }
                    break;
            }
        }
    }

    public interface INotLoadOnScroll {
        void setCanLoad(boolean canLoad);
    }
}
