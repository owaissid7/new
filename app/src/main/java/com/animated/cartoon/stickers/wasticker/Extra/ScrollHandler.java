
package com.animated.cartoon.stickers.wasticker.Extra;


        import android.view.View;
        import android.widget.FrameLayout;
        import android.widget.LinearLayout;

        import androidx.coordinatorlayout.widget.CoordinatorLayout;
        import androidx.core.view.ViewCompat;



public  class ScrollHandler extends CoordinatorLayout.Behavior<LinearLayout> {
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof FrameLayout;

    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, LinearLayout child,
                                       View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, LinearLayout child,
                                  View target, int dx, int dy, int[] consumed) {
        if (dy < 0) {
            showBottomNavigationView(child);
        } else if (dy > 0) {
            hideBottomNavigationView(child);
        }
    }

    private void hideBottomNavigationView(LinearLayout view) {
        view.animate().translationY(view.getHeight());
    }

    public void showBottomNavigationView(LinearLayout view) {
        view.animate().translationY(0);
    }
}