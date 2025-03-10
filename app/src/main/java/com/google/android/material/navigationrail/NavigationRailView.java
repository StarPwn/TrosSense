package com.google.android.material.navigationrail;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.resources.MaterialResources;

/* loaded from: classes.dex */
public class NavigationRailView extends NavigationBarView {
    private static final int DEFAULT_HEADER_GRAVITY = 49;
    static final int DEFAULT_MENU_GRAVITY = 49;
    static final int MAX_ITEM_COUNT = 7;
    static final int NO_ITEM_MINIMUM_HEIGHT = -1;
    private View headerView;
    private Boolean paddingBottomSystemWindowInsets;
    private Boolean paddingStartSystemWindowInsets;
    private Boolean paddingTopSystemWindowInsets;
    private final int topMargin;

    public NavigationRailView(Context context) {
        this(context, null);
    }

    public NavigationRailView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.navigationRailStyle);
    }

    public NavigationRailView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, R.style.Widget_MaterialComponents_NavigationRailView);
    }

    public NavigationRailView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.paddingTopSystemWindowInsets = null;
        this.paddingBottomSystemWindowInsets = null;
        this.paddingStartSystemWindowInsets = null;
        Resources res = getResources();
        this.topMargin = res.getDimensionPixelSize(R.dimen.mtrl_navigation_rail_margin);
        Context context2 = getContext();
        TintTypedArray attributes = ThemeEnforcement.obtainTintedStyledAttributes(context2, attrs, R.styleable.NavigationRailView, defStyleAttr, defStyleRes, new int[0]);
        int headerLayoutRes = attributes.getResourceId(R.styleable.NavigationRailView_headerLayout, 0);
        if (headerLayoutRes != 0) {
            addHeaderView(headerLayoutRes);
        }
        setMenuGravity(attributes.getInt(R.styleable.NavigationRailView_menuGravity, 49));
        if (attributes.hasValue(R.styleable.NavigationRailView_itemMinHeight)) {
            setItemMinimumHeight(attributes.getDimensionPixelSize(R.styleable.NavigationRailView_itemMinHeight, -1));
        }
        if (attributes.hasValue(R.styleable.NavigationRailView_paddingTopSystemWindowInsets)) {
            this.paddingTopSystemWindowInsets = Boolean.valueOf(attributes.getBoolean(R.styleable.NavigationRailView_paddingTopSystemWindowInsets, false));
        }
        if (attributes.hasValue(R.styleable.NavigationRailView_paddingBottomSystemWindowInsets)) {
            this.paddingBottomSystemWindowInsets = Boolean.valueOf(attributes.getBoolean(R.styleable.NavigationRailView_paddingBottomSystemWindowInsets, false));
        }
        if (attributes.hasValue(R.styleable.NavigationRailView_paddingStartSystemWindowInsets)) {
            this.paddingStartSystemWindowInsets = Boolean.valueOf(attributes.getBoolean(R.styleable.NavigationRailView_paddingStartSystemWindowInsets, false));
        }
        int largeFontTopPadding = getResources().getDimensionPixelOffset(R.dimen.m3_navigation_rail_item_padding_top_with_large_font);
        int largeFontBottomPadding = getResources().getDimensionPixelOffset(R.dimen.m3_navigation_rail_item_padding_bottom_with_large_font);
        float progress = AnimationUtils.lerp(0.0f, 1.0f, 0.3f, 1.0f, MaterialResources.getFontScale(context2) - 1.0f);
        float topPadding = AnimationUtils.lerp(getItemPaddingTop(), largeFontTopPadding, progress);
        float bottomPadding = AnimationUtils.lerp(getItemPaddingBottom(), largeFontBottomPadding, progress);
        setItemPaddingTop(Math.round(topPadding));
        setItemPaddingBottom(Math.round(bottomPadding));
        attributes.recycle();
        applyWindowInsets();
    }

    private void applyWindowInsets() {
        ViewUtils.doOnApplyWindowInsets(this, new ViewUtils.OnApplyWindowInsetsListener() { // from class: com.google.android.material.navigationrail.NavigationRailView.1
            @Override // com.google.android.material.internal.ViewUtils.OnApplyWindowInsetsListener
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets, ViewUtils.RelativePadding initialPadding) {
                Insets systemBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                if (NavigationRailView.this.shouldApplyWindowInsetPadding(NavigationRailView.this.paddingTopSystemWindowInsets)) {
                    initialPadding.top += systemBarInsets.top;
                }
                if (NavigationRailView.this.shouldApplyWindowInsetPadding(NavigationRailView.this.paddingBottomSystemWindowInsets)) {
                    initialPadding.bottom += systemBarInsets.bottom;
                }
                if (NavigationRailView.this.shouldApplyWindowInsetPadding(NavigationRailView.this.paddingStartSystemWindowInsets)) {
                    initialPadding.start += ViewUtils.isLayoutRtl(view) ? systemBarInsets.right : systemBarInsets.left;
                }
                initialPadding.applyToView(view);
                return insets;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldApplyWindowInsetPadding(Boolean paddingInsetFlag) {
        return paddingInsetFlag != null ? paddingInsetFlag.booleanValue() : ViewCompat.getFitsSystemWindows(this);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minWidthSpec = makeMinWidthSpec(widthMeasureSpec);
        super.onMeasure(minWidthSpec, heightMeasureSpec);
        if (isHeaderViewVisible()) {
            int maxMenuHeight = (getMeasuredHeight() - this.headerView.getMeasuredHeight()) - this.topMargin;
            int menuHeightSpec = View.MeasureSpec.makeMeasureSpec(maxMenuHeight, Integer.MIN_VALUE);
            measureChild(getNavigationRailMenuView(), minWidthSpec, menuHeightSpec);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        NavigationRailMenuView menuView = getNavigationRailMenuView();
        int offsetY = 0;
        if (isHeaderViewVisible()) {
            int usedTop = this.headerView.getBottom() + this.topMargin;
            int menuTop = menuView.getTop();
            if (menuTop < usedTop) {
                offsetY = usedTop - menuTop;
            }
        } else if (menuView.isTopGravity()) {
            offsetY = this.topMargin;
        }
        if (offsetY > 0) {
            menuView.layout(menuView.getLeft(), menuView.getTop() + offsetY, menuView.getRight(), menuView.getBottom() + offsetY);
        }
    }

    public void addHeaderView(int layoutRes) {
        addHeaderView(LayoutInflater.from(getContext()).inflate(layoutRes, (ViewGroup) this, false));
    }

    public void addHeaderView(View headerView) {
        removeHeaderView();
        this.headerView = headerView;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
        params.gravity = 49;
        params.topMargin = this.topMargin;
        addView(headerView, 0, params);
    }

    public View getHeaderView() {
        return this.headerView;
    }

    public void removeHeaderView() {
        if (this.headerView != null) {
            removeView(this.headerView);
            this.headerView = null;
        }
    }

    public void setMenuGravity(int gravity) {
        getNavigationRailMenuView().setMenuGravity(gravity);
    }

    public int getMenuGravity() {
        return getNavigationRailMenuView().getMenuGravity();
    }

    public int getItemMinimumHeight() {
        NavigationRailMenuView menuView = (NavigationRailMenuView) getMenuView();
        return menuView.getItemMinimumHeight();
    }

    public void setItemMinimumHeight(int minHeight) {
        NavigationRailMenuView menuView = (NavigationRailMenuView) getMenuView();
        menuView.setItemMinimumHeight(minHeight);
    }

    @Override // com.google.android.material.navigation.NavigationBarView
    public int getMaxItemCount() {
        return 7;
    }

    private NavigationRailMenuView getNavigationRailMenuView() {
        return (NavigationRailMenuView) getMenuView();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.material.navigation.NavigationBarView
    public NavigationRailMenuView createNavigationBarMenuView(Context context) {
        return new NavigationRailMenuView(context);
    }

    private int makeMinWidthSpec(int measureSpec) {
        int minWidth = getSuggestedMinimumWidth();
        if (View.MeasureSpec.getMode(measureSpec) != 1073741824 && minWidth > 0) {
            return View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(measureSpec), minWidth + getPaddingLeft() + getPaddingRight()), 1073741824);
        }
        return measureSpec;
    }

    private boolean isHeaderViewVisible() {
        return (this.headerView == null || this.headerView.getVisibility() == 8) ? false : true;
    }
}
