package me.lingxiao.exam.ui.widget;


import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.RelativeLayout;


public class TopSlidingLayout extends RelativeLayout implements OnTouchListener {

    public static final int SNAP_VELOCITY = 200;
    public static final int DO_NOTHING = 0;
    public static final int SHOW_TOP_MENU = 1;
    public static final int HIDE_TOP_MENU = 2;
    private int slideState;
    private int screenHeight;
    private int touchSlop;
    private float xDown;
    private float yDown;
    private float xMove;
    private float yMove;
    private float yUp;
    private boolean isTopMenuVisible;
    private boolean isSliding;
    private View topMenuLayout;
    private View contentLayout;
    private View mBindView;
    private MarginLayoutParams topMenuLayoutParams;
    private LayoutParams contentLayoutParams;
    private VelocityTracker mVelocityTracker;

    public TopSlidingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenHeight = wm.getDefaultDisplay().getHeight();
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setScrollEvent(View bindView) {
        mBindView = bindView;
        mBindView.setOnTouchListener(this);
    }

    public void scrollToTopMenu() {
        new topMenuScrollTask().execute(-30);
    }

    public void scrollToContentFromTopMenu() {
        new topMenuScrollTask().execute(30);
    }

    public boolean isTopLayoutVisible() {
        return isTopMenuVisible;
    }

    public void initShowTopState() {
        contentLayoutParams.bottomMargin = 0;
        contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        contentLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        contentLayout.setLayoutParams(contentLayoutParams);
        topMenuLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            topMenuLayout = getChildAt(0);
            topMenuLayoutParams = (MarginLayoutParams) topMenuLayout.getLayoutParams();
            contentLayout = getChildAt(1);
            contentLayoutParams = (LayoutParams) contentLayout.getLayoutParams();
            contentLayoutParams.height = screenHeight;
            contentLayout.setLayoutParams(contentLayoutParams);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        createVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = event.getRawX();
                yDown = event.getRawY();
                slideState = DO_NOTHING;
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = event.getRawX();
                yMove = event.getRawY();
                int moveDistanceX = (int) (xMove - xDown);
                int moveDistanceY = (int) (yMove - yDown);
                checkSlideState(moveDistanceX, moveDistanceY);
                switch (slideState) {
                    case SHOW_TOP_MENU:
                        contentLayoutParams.bottomMargin = -moveDistanceY;
                        checkTopMenuBorder();
                        contentLayout.setLayoutParams(contentLayoutParams);
                        break;
                    case HIDE_TOP_MENU:
                        contentLayoutParams.bottomMargin = -topMenuLayoutParams.height - moveDistanceY;
                        checkTopMenuBorder();
                        contentLayout.setLayoutParams(contentLayoutParams);
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                yUp = event.getRawY();
                int upDistanceY = (int) (yUp - yDown);
                if (isSliding) {
                    switch (slideState) {
                        case SHOW_TOP_MENU:
                            if (shouldScrollToTopMenu()) {
                                scrollToTopMenu();
                            } else {
                                scrollToContentFromTopMenu();
                            }
                            break;
                        case HIDE_TOP_MENU:
                            if (shouldScrollToContentFromTopMenu()) {
                                scrollToContentFromTopMenu();
                            } else {
                                scrollToTopMenu();
                            }
                            break;
                        default:
                            break;
                    }
                } else if (upDistanceY < touchSlop && isTopMenuVisible) {
                    scrollToContentFromTopMenu();
                }
                recycleVelocityTracker();
                break;
        }
        if (v.isEnabled()) {
            if (isSliding) {
                unFocusBindView();
                return true;
            }
            if (isTopMenuVisible) {
                return true;
            }
            return false;
        }
        return true;
    }

    private void checkSlideState(int moveDistanceX, int moveDistanceY) {
        if (isTopMenuVisible) {
            if (!isSliding && Math.abs(moveDistanceY) >= touchSlop && moveDistanceY < 0) {
                isSliding = true;
                slideState = HIDE_TOP_MENU;
            }
        } else {
            if (!isSliding && Math.abs(moveDistanceY) >= touchSlop && moveDistanceY > 0
                    && Math.abs(moveDistanceX) < touchSlop) {
                isSliding = true;
                slideState = SHOW_TOP_MENU;
                initShowTopState();
            }
        }
    }

    private void checkTopMenuBorder() {
        if (contentLayoutParams.bottomMargin > 0) {
            contentLayoutParams.bottomMargin = 0;
        } else if (contentLayoutParams.bottomMargin < -topMenuLayoutParams.height) {
            contentLayoutParams.bottomMargin = -topMenuLayoutParams.height;
        }
    }

    private boolean shouldScrollToTopMenu() {
        return yUp - yDown > topMenuLayoutParams.height / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    private boolean shouldScrollToContentFromTopMenu() {
        return yDown - yUp > topMenuLayoutParams.height / 2 || getScrollVelocity() > SNAP_VELOCITY;
    }

    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }

    private void unFocusBindView() {
        if (mBindView != null) {
            mBindView.setPressed(false);
            mBindView.setFocusable(false);
            mBindView.setFocusableInTouchMode(false);
        }
    }

    class topMenuScrollTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... speed) {
            int bottomMargin = contentLayoutParams.bottomMargin;
            while (true) {
                bottomMargin = bottomMargin + speed[0];
                if (bottomMargin < -topMenuLayoutParams.height) {
                    bottomMargin = -topMenuLayoutParams.height;
                    break;
                }
                if (bottomMargin > 0) {
                    bottomMargin = 0;
                    break;
                }
                publishProgress(bottomMargin);
                sleep(15);
            }
            if (speed[0] > 0) {
                isTopMenuVisible = false;
            } else {
                isTopMenuVisible = true;
            }
            isSliding = false;
            return bottomMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... bottomMargin) {
            contentLayoutParams.bottomMargin = bottomMargin[0];
            contentLayout.setLayoutParams(contentLayoutParams);
            unFocusBindView();
        }

        @Override
        protected void onPostExecute(Integer bottomMargin) {
            contentLayoutParams.bottomMargin = bottomMargin;
            contentLayout.setLayoutParams(contentLayoutParams);
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
