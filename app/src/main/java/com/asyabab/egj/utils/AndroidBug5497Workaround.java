package com.asyabab.egj.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;

public class AndroidBug5497Workaround {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    public static void assistActivity(Activity activity, int svChildLayoutId) {
        new AndroidBug5497Workaround(activity, svChildLayoutId);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;

    View svChildLayout;
    int originalGravity;
    Activity activity;

    private AndroidBug5497Workaround(Activity activity, int svChildLayoutId) {
        this.activity = activity;
        svChildLayout = activity.findViewById(svChildLayoutId);
        originalGravity = ((ScrollView.LayoutParams) svChildLayout.getLayoutParams()).gravity;

        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                onKeyboardVisible();
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            } else {
                // keyboard probably just became hidden
                onKeyboardHidden();
                frameLayoutParams.height = usableHeightNow;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return r.bottom;
    }

    private void onKeyboardVisible() {

        ScrollView.LayoutParams params = (ScrollView.LayoutParams) svChildLayout.getLayoutParams();
        params.gravity = Gravity.TOP;
        svChildLayout.requestLayout();

        final ScrollView parentSv = (ScrollView) svChildLayout.getParent();
        parentSv.post(() -> {
            View focusedEditText = activity.getWindow().getCurrentFocus();
            try {
                parentSv.smoothScrollTo(0, focusedEditText.getTop());
            } catch (Exception ignored) {
            }
        });
    }

    private void onKeyboardHidden() {
        ScrollView.LayoutParams params = (ScrollView.LayoutParams) svChildLayout.getLayoutParams();
        params.gravity = originalGravity;
        svChildLayout.requestLayout();
    }
}
