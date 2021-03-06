package fall2018.csc2017.slidingtiles;
//View class that creates the grid for the main game, not tested.
/**
Adapted from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/GestureDetectGridView.java

This extension of GridView contains built in logic for handling swipes between buttons
 **/

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * A grid containing tile buttons that can be swiped to move positions.
 */
public class GestureDetectGridView extends GridView {
    public static final int SWIPE_MIN_DISTANCE = 100;
    private GestureDetector gDetector;
    private SlidingTilesGameController mController;
    private boolean mFlingConfirmed = false;
    private float mTouchX;
    private float mTouchY;
    private SlidingTilesManager slidingTilesManager;

    /**
     * A grid that detects a user's motion.
     * @param context the activity context.
     */
    public GestureDetectGridView(Context context) {
        super(context);
        init(context);
    }

    /**
     * A grid that detects a user's motion.
     * @param context the activity context.
     * @param attrs identifies a set of attributes.
     */
    public GestureDetectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     *
     * @param context the activity context.
     * @param attrs identifies a set of attributes.
     * @param defStyleAttr the default attribute style.
     */
    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) // API 21
    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr,
                                 int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        mController = new SlidingTilesGameController();
        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                int position = GestureDetectGridView.this.pointToPosition
                        (Math.round(event.getX()), Math.round(event.getY()));

                mController.processTapMovement(context, position);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        gDetector.onTouchEvent(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mFlingConfirmed = false;
        } else if (action == MotionEvent.ACTION_DOWN) {
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        } else {

            if (mFlingConfirmed) {
                return true;
            }

            float dX = (Math.abs(ev.getX() - mTouchX));
            float dY = (Math.abs(ev.getY() - mTouchY));
            if ((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
                mFlingConfirmed = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gDetector.onTouchEvent(ev);
    }

    /**
     * a Manager that manages the behavior of the tiles buttons in the grid.
     * @param slidingTilesManager the manager for the grid.
     */
    public void setSlidingTilesManager(SlidingTilesManager slidingTilesManager) {
        this.slidingTilesManager = slidingTilesManager;
        mController.setSlidingTilesManager(slidingTilesManager);
    }
}
