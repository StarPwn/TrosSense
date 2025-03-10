package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/* loaded from: classes.dex */
public class Flow extends VirtualLayout {
    public static final int HORIZONTAL_ALIGN_CENTER = 2;
    public static final int HORIZONTAL_ALIGN_END = 1;
    public static final int HORIZONTAL_ALIGN_START = 0;
    public static final int VERTICAL_ALIGN_BASELINE = 3;
    public static final int VERTICAL_ALIGN_BOTTOM = 1;
    public static final int VERTICAL_ALIGN_CENTER = 2;
    public static final int VERTICAL_ALIGN_TOP = 0;
    public static final int WRAP_ALIGNED = 2;
    public static final int WRAP_CHAIN = 1;
    public static final int WRAP_NONE = 0;
    private ConstraintWidget[] mDisplayedWidgets;
    private int mHorizontalStyle = -1;
    private int mVerticalStyle = -1;
    private int mFirstHorizontalStyle = -1;
    private int mFirstVerticalStyle = -1;
    private int mLastHorizontalStyle = -1;
    private int mLastVerticalStyle = -1;
    private float mHorizontalBias = 0.5f;
    private float mVerticalBias = 0.5f;
    private float mFirstHorizontalBias = 0.5f;
    private float mFirstVerticalBias = 0.5f;
    private float mLastHorizontalBias = 0.5f;
    private float mLastVerticalBias = 0.5f;
    private int mHorizontalGap = 0;
    private int mVerticalGap = 0;
    private int mHorizontalAlign = 2;
    private int mVerticalAlign = 2;
    private int mWrapMode = 0;
    private int mMaxElementsWrap = -1;
    private int mOrientation = 0;
    private ArrayList<WidgetsList> mChainList = new ArrayList<>();
    private ConstraintWidget[] mAlignedBiggestElementsInRows = null;
    private ConstraintWidget[] mAlignedBiggestElementsInCols = null;
    private int[] mAlignedDimensions = null;
    private int mDisplayedWidgetsCount = 0;

    @Override // androidx.constraintlayout.solver.widgets.HelperWidget, androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void copy(ConstraintWidget src, HashMap<ConstraintWidget, ConstraintWidget> map) {
        super.copy(src, map);
        Flow srcFLow = (Flow) src;
        this.mHorizontalStyle = srcFLow.mHorizontalStyle;
        this.mVerticalStyle = srcFLow.mVerticalStyle;
        this.mFirstHorizontalStyle = srcFLow.mFirstHorizontalStyle;
        this.mFirstVerticalStyle = srcFLow.mFirstVerticalStyle;
        this.mLastHorizontalStyle = srcFLow.mLastHorizontalStyle;
        this.mLastVerticalStyle = srcFLow.mLastVerticalStyle;
        this.mHorizontalBias = srcFLow.mHorizontalBias;
        this.mVerticalBias = srcFLow.mVerticalBias;
        this.mFirstHorizontalBias = srcFLow.mFirstHorizontalBias;
        this.mFirstVerticalBias = srcFLow.mFirstVerticalBias;
        this.mLastHorizontalBias = srcFLow.mLastHorizontalBias;
        this.mLastVerticalBias = srcFLow.mLastVerticalBias;
        this.mHorizontalGap = srcFLow.mHorizontalGap;
        this.mVerticalGap = srcFLow.mVerticalGap;
        this.mHorizontalAlign = srcFLow.mHorizontalAlign;
        this.mVerticalAlign = srcFLow.mVerticalAlign;
        this.mWrapMode = srcFLow.mWrapMode;
        this.mMaxElementsWrap = srcFLow.mMaxElementsWrap;
        this.mOrientation = srcFLow.mOrientation;
    }

    public void setOrientation(int value) {
        this.mOrientation = value;
    }

    public void setFirstHorizontalStyle(int value) {
        this.mFirstHorizontalStyle = value;
    }

    public void setFirstVerticalStyle(int value) {
        this.mFirstVerticalStyle = value;
    }

    public void setLastHorizontalStyle(int value) {
        this.mLastHorizontalStyle = value;
    }

    public void setLastVerticalStyle(int value) {
        this.mLastVerticalStyle = value;
    }

    public void setHorizontalStyle(int value) {
        this.mHorizontalStyle = value;
    }

    public void setVerticalStyle(int value) {
        this.mVerticalStyle = value;
    }

    public void setHorizontalBias(float value) {
        this.mHorizontalBias = value;
    }

    public void setVerticalBias(float value) {
        this.mVerticalBias = value;
    }

    public void setFirstHorizontalBias(float value) {
        this.mFirstHorizontalBias = value;
    }

    public void setFirstVerticalBias(float value) {
        this.mFirstVerticalBias = value;
    }

    public void setLastHorizontalBias(float value) {
        this.mLastHorizontalBias = value;
    }

    public void setLastVerticalBias(float value) {
        this.mLastVerticalBias = value;
    }

    public void setHorizontalAlign(int value) {
        this.mHorizontalAlign = value;
    }

    public void setVerticalAlign(int value) {
        this.mVerticalAlign = value;
    }

    public void setWrapMode(int value) {
        this.mWrapMode = value;
    }

    public void setHorizontalGap(int value) {
        this.mHorizontalGap = value;
    }

    public void setVerticalGap(int value) {
        this.mVerticalGap = value;
    }

    public void setMaxElementsWrap(int value) {
        this.mMaxElementsWrap = value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getWidgetWidth(ConstraintWidget widget, int max) {
        if (widget == null) {
            return 0;
        }
        if (widget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            if (widget.mMatchConstraintDefaultWidth == 0) {
                return 0;
            }
            if (widget.mMatchConstraintDefaultWidth == 2) {
                int value = (int) (widget.mMatchConstraintPercentWidth * max);
                if (value != widget.getWidth()) {
                    measure(widget, ConstraintWidget.DimensionBehaviour.FIXED, value, widget.getVerticalDimensionBehaviour(), widget.getHeight());
                }
                return value;
            }
            if (widget.mMatchConstraintDefaultWidth == 1) {
                return widget.getWidth();
            }
            if (widget.mMatchConstraintDefaultWidth == 3) {
                return (int) ((widget.getHeight() * widget.mDimensionRatio) + 0.5f);
            }
        }
        return widget.getWidth();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int getWidgetHeight(ConstraintWidget widget, int max) {
        if (widget == null) {
            return 0;
        }
        if (widget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            if (widget.mMatchConstraintDefaultHeight == 0) {
                return 0;
            }
            if (widget.mMatchConstraintDefaultHeight == 2) {
                int value = (int) (widget.mMatchConstraintPercentHeight * max);
                if (value != widget.getHeight()) {
                    measure(widget, widget.getHorizontalDimensionBehaviour(), widget.getWidth(), ConstraintWidget.DimensionBehaviour.FIXED, value);
                }
                return value;
            }
            if (widget.mMatchConstraintDefaultHeight == 1) {
                return widget.getHeight();
            }
            if (widget.mMatchConstraintDefaultHeight == 3) {
                return (int) ((widget.getWidth() * widget.mDimensionRatio) + 0.5f);
            }
        }
        return widget.getHeight();
    }

    /* JADX WARN: Incorrect condition in loop: B:21:0x006a */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r20v0 */
    /* JADX WARN: Type inference failed for: r20v1 */
    /* JADX WARN: Type inference failed for: r20v2 */
    /* JADX WARN: Type inference failed for: r20v3 */
    /* JADX WARN: Type inference failed for: r20v4 */
    @Override // androidx.constraintlayout.solver.widgets.VirtualLayout
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void measure(int r24, int r25, int r26, int r27) {
        /*
            Method dump skipped, instructions count: 320
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.Flow.measure(int, int, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class WidgetsList {
        private ConstraintAnchor mBottom;
        private ConstraintAnchor mLeft;
        private int mMax;
        private int mOrientation;
        private int mPaddingBottom;
        private int mPaddingLeft;
        private int mPaddingRight;
        private int mPaddingTop;
        private ConstraintAnchor mRight;
        private ConstraintAnchor mTop;
        private ConstraintWidget biggest = null;
        int biggestDimension = 0;
        private int mWidth = 0;
        private int mHeight = 0;
        private int mStartIndex = 0;
        private int mCount = 0;
        private int mNbMatchConstraintsWidgets = 0;

        public WidgetsList(int orientation, ConstraintAnchor left, ConstraintAnchor top, ConstraintAnchor right, ConstraintAnchor bottom, int max) {
            this.mOrientation = 0;
            this.mPaddingLeft = 0;
            this.mPaddingTop = 0;
            this.mPaddingRight = 0;
            this.mPaddingBottom = 0;
            this.mMax = 0;
            this.mOrientation = orientation;
            this.mLeft = left;
            this.mTop = top;
            this.mRight = right;
            this.mBottom = bottom;
            this.mPaddingLeft = Flow.this.getPaddingLeft();
            this.mPaddingTop = Flow.this.getPaddingTop();
            this.mPaddingRight = Flow.this.getPaddingRight();
            this.mPaddingBottom = Flow.this.getPaddingBottom();
            this.mMax = max;
        }

        public void setup(int orientation, ConstraintAnchor left, ConstraintAnchor top, ConstraintAnchor right, ConstraintAnchor bottom, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom, int max) {
            this.mOrientation = orientation;
            this.mLeft = left;
            this.mTop = top;
            this.mRight = right;
            this.mBottom = bottom;
            this.mPaddingLeft = paddingLeft;
            this.mPaddingTop = paddingTop;
            this.mPaddingRight = paddingRight;
            this.mPaddingBottom = paddingBottom;
            this.mMax = max;
        }

        public void clear() {
            this.biggestDimension = 0;
            this.biggest = null;
            this.mWidth = 0;
            this.mHeight = 0;
            this.mStartIndex = 0;
            this.mCount = 0;
            this.mNbMatchConstraintsWidgets = 0;
        }

        public void setStartIndex(int value) {
            this.mStartIndex = value;
        }

        public int getWidth() {
            if (this.mOrientation == 0) {
                return this.mWidth - Flow.this.mHorizontalGap;
            }
            return this.mWidth;
        }

        public int getHeight() {
            if (this.mOrientation == 1) {
                return this.mHeight - Flow.this.mVerticalGap;
            }
            return this.mHeight;
        }

        public void add(ConstraintWidget widget) {
            if (this.mOrientation == 0) {
                int width = Flow.this.getWidgetWidth(widget, this.mMax);
                if (widget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    this.mNbMatchConstraintsWidgets++;
                    width = 0;
                }
                int gap = Flow.this.mHorizontalGap;
                if (widget.getVisibility() == 8) {
                    gap = 0;
                }
                this.mWidth += width + gap;
                int height = Flow.this.getWidgetHeight(widget, this.mMax);
                if (this.biggest == null || this.biggestDimension < height) {
                    this.biggest = widget;
                    this.biggestDimension = height;
                    this.mHeight = height;
                }
            } else {
                int width2 = Flow.this.getWidgetWidth(widget, this.mMax);
                int height2 = Flow.this.getWidgetHeight(widget, this.mMax);
                if (widget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    this.mNbMatchConstraintsWidgets++;
                    height2 = 0;
                }
                int gap2 = Flow.this.mVerticalGap;
                if (widget.getVisibility() == 8) {
                    gap2 = 0;
                }
                this.mHeight += height2 + gap2;
                if (this.biggest == null || this.biggestDimension < width2) {
                    this.biggest = widget;
                    this.biggestDimension = width2;
                    this.mWidth = width2;
                }
            }
            this.mCount++;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public void createConstraints(boolean isInRtl, int chainIndex, boolean isLastChain) {
            int count = this.mCount;
            for (int i = 0; i < count && this.mStartIndex + i < Flow.this.mDisplayedWidgetsCount; i++) {
                ConstraintWidget widget = Flow.this.mDisplayedWidgets[this.mStartIndex + i];
                if (widget != null) {
                    widget.resetAnchors();
                }
            }
            if (count == 0 || this.biggest == null) {
                return;
            }
            boolean singleChain = isLastChain && chainIndex == 0;
            int firstVisible = -1;
            int lastVisible = -1;
            for (int i2 = 0; i2 < count; i2++) {
                int index = i2;
                if (isInRtl) {
                    index = (count - 1) - i2;
                }
                if (this.mStartIndex + index >= Flow.this.mDisplayedWidgetsCount) {
                    break;
                }
                if (Flow.this.mDisplayedWidgets[this.mStartIndex + index].getVisibility() == 0) {
                    if (firstVisible == -1) {
                        firstVisible = i2;
                    }
                    lastVisible = i2;
                }
            }
            ConstraintWidget previous = null;
            if (this.mOrientation == 0) {
                ConstraintWidget verticalWidget = this.biggest;
                verticalWidget.setVerticalChainStyle(Flow.this.mVerticalStyle);
                int padding = this.mPaddingTop;
                if (chainIndex > 0) {
                    padding += Flow.this.mVerticalGap;
                }
                verticalWidget.mTop.connect(this.mTop, padding);
                if (isLastChain) {
                    verticalWidget.mBottom.connect(this.mBottom, this.mPaddingBottom);
                }
                if (chainIndex > 0) {
                    ConstraintAnchor bottom = this.mTop.mOwner.mBottom;
                    bottom.connect(verticalWidget.mTop, 0);
                }
                ConstraintWidget baselineVerticalWidget = verticalWidget;
                if (Flow.this.mVerticalAlign == 3 && !verticalWidget.hasBaseline()) {
                    int i3 = 0;
                    while (true) {
                        if (i3 >= count) {
                            break;
                        }
                        int index2 = i3;
                        if (isInRtl) {
                            index2 = (count - 1) - i3;
                        }
                        if (this.mStartIndex + index2 >= Flow.this.mDisplayedWidgetsCount) {
                            break;
                        }
                        ConstraintWidget widget2 = Flow.this.mDisplayedWidgets[this.mStartIndex + index2];
                        if (!widget2.hasBaseline()) {
                            i3++;
                        } else {
                            baselineVerticalWidget = widget2;
                            break;
                        }
                    }
                }
                for (int i4 = 0; i4 < count; i4++) {
                    int index3 = i4;
                    if (isInRtl) {
                        index3 = (count - 1) - i4;
                    }
                    if (this.mStartIndex + index3 < Flow.this.mDisplayedWidgetsCount) {
                        ConstraintWidget widget3 = Flow.this.mDisplayedWidgets[this.mStartIndex + index3];
                        if (i4 == 0) {
                            widget3.connect(widget3.mLeft, this.mLeft, this.mPaddingLeft);
                        }
                        if (index3 == 0) {
                            int style = Flow.this.mHorizontalStyle;
                            float bias = Flow.this.mHorizontalBias;
                            if (this.mStartIndex == 0 && Flow.this.mFirstHorizontalStyle != -1) {
                                style = Flow.this.mFirstHorizontalStyle;
                                bias = Flow.this.mFirstHorizontalBias;
                            } else if (isLastChain && Flow.this.mLastHorizontalStyle != -1) {
                                style = Flow.this.mLastHorizontalStyle;
                                bias = Flow.this.mLastHorizontalBias;
                            }
                            widget3.setHorizontalChainStyle(style);
                            widget3.setHorizontalBiasPercent(bias);
                        }
                        if (i4 == count - 1) {
                            widget3.connect(widget3.mRight, this.mRight, this.mPaddingRight);
                        }
                        if (previous != null) {
                            widget3.mLeft.connect(previous.mRight, Flow.this.mHorizontalGap);
                            if (i4 == firstVisible) {
                                widget3.mLeft.setGoneMargin(this.mPaddingLeft);
                            }
                            previous.mRight.connect(widget3.mLeft, 0);
                            if (i4 == lastVisible + 1) {
                                previous.mRight.setGoneMargin(this.mPaddingRight);
                            }
                        }
                        if (widget3 != verticalWidget) {
                            if (Flow.this.mVerticalAlign == 3 && baselineVerticalWidget.hasBaseline() && widget3 != baselineVerticalWidget && widget3.hasBaseline()) {
                                widget3.mBaseline.connect(baselineVerticalWidget.mBaseline, 0);
                            } else {
                                switch (Flow.this.mVerticalAlign) {
                                    case 0:
                                        widget3.mTop.connect(verticalWidget.mTop, 0);
                                        break;
                                    case 1:
                                        widget3.mBottom.connect(verticalWidget.mBottom, 0);
                                        break;
                                    default:
                                        if (singleChain) {
                                            widget3.mTop.connect(this.mTop, this.mPaddingTop);
                                            widget3.mBottom.connect(this.mBottom, this.mPaddingBottom);
                                            break;
                                        } else {
                                            widget3.mTop.connect(verticalWidget.mTop, 0);
                                            widget3.mBottom.connect(verticalWidget.mBottom, 0);
                                            break;
                                        }
                                }
                            }
                        }
                        previous = widget3;
                    } else {
                        return;
                    }
                }
                return;
            }
            ConstraintWidget horizontalWidget = this.biggest;
            horizontalWidget.setHorizontalChainStyle(Flow.this.mHorizontalStyle);
            int padding2 = this.mPaddingLeft;
            if (chainIndex > 0) {
                padding2 += Flow.this.mHorizontalGap;
            }
            if (isInRtl) {
                horizontalWidget.mRight.connect(this.mRight, padding2);
                if (isLastChain) {
                    horizontalWidget.mLeft.connect(this.mLeft, this.mPaddingRight);
                }
                if (chainIndex > 0) {
                    ConstraintAnchor left = this.mRight.mOwner.mLeft;
                    left.connect(horizontalWidget.mRight, 0);
                }
            } else {
                horizontalWidget.mLeft.connect(this.mLeft, padding2);
                if (isLastChain) {
                    horizontalWidget.mRight.connect(this.mRight, this.mPaddingRight);
                }
                if (chainIndex > 0) {
                    ConstraintAnchor right = this.mLeft.mOwner.mRight;
                    right.connect(horizontalWidget.mLeft, 0);
                }
            }
            for (int i5 = 0; i5 < count && this.mStartIndex + i5 < Flow.this.mDisplayedWidgetsCount; i5++) {
                ConstraintWidget widget4 = Flow.this.mDisplayedWidgets[this.mStartIndex + i5];
                if (i5 == 0) {
                    widget4.connect(widget4.mTop, this.mTop, this.mPaddingTop);
                    int style2 = Flow.this.mVerticalStyle;
                    float bias2 = Flow.this.mVerticalBias;
                    if (this.mStartIndex == 0 && Flow.this.mFirstVerticalStyle != -1) {
                        style2 = Flow.this.mFirstVerticalStyle;
                        bias2 = Flow.this.mFirstVerticalBias;
                    } else if (isLastChain && Flow.this.mLastVerticalStyle != -1) {
                        style2 = Flow.this.mLastVerticalStyle;
                        bias2 = Flow.this.mLastVerticalBias;
                    }
                    widget4.setVerticalChainStyle(style2);
                    widget4.setVerticalBiasPercent(bias2);
                }
                if (i5 == count - 1) {
                    widget4.connect(widget4.mBottom, this.mBottom, this.mPaddingBottom);
                }
                if (previous != null) {
                    widget4.mTop.connect(previous.mBottom, Flow.this.mVerticalGap);
                    if (i5 == firstVisible) {
                        widget4.mTop.setGoneMargin(this.mPaddingTop);
                    }
                    previous.mBottom.connect(widget4.mTop, 0);
                    if (i5 == lastVisible + 1) {
                        previous.mBottom.setGoneMargin(this.mPaddingBottom);
                    }
                }
                if (widget4 != horizontalWidget) {
                    if (isInRtl) {
                        switch (Flow.this.mHorizontalAlign) {
                            case 0:
                                widget4.mRight.connect(horizontalWidget.mRight, 0);
                                break;
                            case 1:
                                widget4.mLeft.connect(horizontalWidget.mLeft, 0);
                                break;
                            case 2:
                                widget4.mLeft.connect(horizontalWidget.mLeft, 0);
                                widget4.mRight.connect(horizontalWidget.mRight, 0);
                                break;
                        }
                    } else {
                        switch (Flow.this.mHorizontalAlign) {
                            case 0:
                                widget4.mLeft.connect(horizontalWidget.mLeft, 0);
                                break;
                            case 1:
                                widget4.mRight.connect(horizontalWidget.mRight, 0);
                                break;
                            case 2:
                                if (singleChain) {
                                    widget4.mLeft.connect(this.mLeft, this.mPaddingLeft);
                                    widget4.mRight.connect(this.mRight, this.mPaddingRight);
                                    break;
                                } else {
                                    widget4.mLeft.connect(horizontalWidget.mLeft, 0);
                                    widget4.mRight.connect(horizontalWidget.mRight, 0);
                                    break;
                                }
                        }
                    }
                }
                previous = widget4;
            }
        }

        public void measureMatchConstraints(int availableSpace) {
            if (this.mNbMatchConstraintsWidgets == 0) {
                return;
            }
            int count = this.mCount;
            int widgetSize = availableSpace / this.mNbMatchConstraintsWidgets;
            for (int i = 0; i < count && this.mStartIndex + i < Flow.this.mDisplayedWidgetsCount; i++) {
                ConstraintWidget widget = Flow.this.mDisplayedWidgets[this.mStartIndex + i];
                if (this.mOrientation == 0) {
                    if (widget != null && widget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widget.mMatchConstraintDefaultWidth == 0) {
                        Flow.this.measure(widget, ConstraintWidget.DimensionBehaviour.FIXED, widgetSize, widget.getVerticalDimensionBehaviour(), widget.getHeight());
                    }
                } else if (widget != null && widget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widget.mMatchConstraintDefaultHeight == 0) {
                    Flow.this.measure(widget, widget.getHorizontalDimensionBehaviour(), widget.getWidth(), ConstraintWidget.DimensionBehaviour.FIXED, widgetSize);
                }
            }
            recomputeDimensions();
        }

        private void recomputeDimensions() {
            this.mWidth = 0;
            this.mHeight = 0;
            this.biggest = null;
            this.biggestDimension = 0;
            int count = this.mCount;
            for (int i = 0; i < count && this.mStartIndex + i < Flow.this.mDisplayedWidgetsCount; i++) {
                ConstraintWidget widget = Flow.this.mDisplayedWidgets[this.mStartIndex + i];
                if (this.mOrientation != 0) {
                    int width = Flow.this.getWidgetWidth(widget, this.mMax);
                    int height = Flow.this.getWidgetHeight(widget, this.mMax);
                    int gap = Flow.this.mVerticalGap;
                    if (widget.getVisibility() == 8) {
                        gap = 0;
                    }
                    this.mHeight += height + gap;
                    if (this.biggest == null || this.biggestDimension < width) {
                        this.biggest = widget;
                        this.biggestDimension = width;
                        this.mWidth = width;
                    }
                } else {
                    int width2 = widget.getWidth();
                    int gap2 = Flow.this.mHorizontalGap;
                    if (widget.getVisibility() == 8) {
                        gap2 = 0;
                    }
                    this.mWidth += width2 + gap2;
                    int height2 = Flow.this.getWidgetHeight(widget, this.mMax);
                    if (this.biggest == null || this.biggestDimension < height2) {
                        this.biggest = widget;
                        this.biggestDimension = height2;
                        this.mHeight = height2;
                    }
                }
            }
        }
    }

    private void measureChainWrap(ConstraintWidget[] widgets, int count, int orientation, int max, int[] measured) {
        WidgetsList list;
        int nbMatchConstraintsWidgets;
        boolean doWrap;
        int nbMatchConstraintsWidgets2;
        int nbMatchConstraintsWidgets3;
        boolean doWrap2;
        if (count == 0) {
            return;
        }
        this.mChainList.clear();
        WidgetsList list2 = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, max);
        this.mChainList.add(list2);
        int nbMatchConstraintsWidgets4 = 0;
        if (orientation == 0) {
            int width = 0;
            WidgetsList list3 = list2;
            int i = 0;
            while (i < count) {
                ConstraintWidget widget = widgets[i];
                int w = getWidgetWidth(widget, max);
                if (widget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    nbMatchConstraintsWidgets3 = nbMatchConstraintsWidgets4;
                } else {
                    nbMatchConstraintsWidgets3 = nbMatchConstraintsWidgets4 + 1;
                }
                boolean doWrap3 = (width == max || (this.mHorizontalGap + width) + w > max) && list3.biggest != null;
                if (!doWrap3 && i > 0 && this.mMaxElementsWrap > 0 && i % this.mMaxElementsWrap == 0) {
                    doWrap2 = true;
                } else {
                    doWrap2 = doWrap3;
                }
                if (doWrap2) {
                    WidgetsList list4 = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, max);
                    list4.setStartIndex(i);
                    this.mChainList.add(list4);
                    list3 = list4;
                    width = w;
                } else if (i > 0) {
                    width += this.mHorizontalGap + w;
                } else {
                    width = w;
                }
                list3.add(widget);
                i++;
                nbMatchConstraintsWidgets4 = nbMatchConstraintsWidgets3;
            }
            list = list3;
        } else {
            int height = 0;
            WidgetsList list5 = list2;
            int i2 = 0;
            while (i2 < count) {
                ConstraintWidget widget2 = widgets[i2];
                int h = getWidgetHeight(widget2, max);
                if (widget2.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    nbMatchConstraintsWidgets = nbMatchConstraintsWidgets4;
                } else {
                    nbMatchConstraintsWidgets = nbMatchConstraintsWidgets4 + 1;
                }
                boolean doWrap4 = (height == max || (this.mVerticalGap + height) + h > max) && list5.biggest != null;
                if (!doWrap4 && i2 > 0 && this.mMaxElementsWrap > 0 && i2 % this.mMaxElementsWrap == 0) {
                    doWrap = true;
                } else {
                    doWrap = doWrap4;
                }
                if (doWrap) {
                    WidgetsList list6 = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, max);
                    list6.setStartIndex(i2);
                    this.mChainList.add(list6);
                    list5 = list6;
                    height = h;
                } else if (i2 > 0) {
                    height += this.mVerticalGap + h;
                } else {
                    height = h;
                }
                list5.add(widget2);
                i2++;
                nbMatchConstraintsWidgets4 = nbMatchConstraintsWidgets;
            }
            list = list5;
        }
        int listCount = this.mChainList.size();
        ConstraintAnchor left = this.mLeft;
        ConstraintAnchor top = this.mTop;
        ConstraintAnchor right = this.mRight;
        ConstraintAnchor bottom = this.mBottom;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        boolean needInternalMeasure = getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT || getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        if (nbMatchConstraintsWidgets4 > 0 && needInternalMeasure) {
            int i3 = 0;
            while (i3 < listCount) {
                boolean needInternalMeasure2 = needInternalMeasure;
                WidgetsList current = this.mChainList.get(i3);
                if (orientation == 0) {
                    nbMatchConstraintsWidgets2 = nbMatchConstraintsWidgets4;
                    current.measureMatchConstraints(max - current.getWidth());
                } else {
                    nbMatchConstraintsWidgets2 = nbMatchConstraintsWidgets4;
                    current.measureMatchConstraints(max - current.getHeight());
                }
                i3++;
                needInternalMeasure = needInternalMeasure2;
                nbMatchConstraintsWidgets4 = nbMatchConstraintsWidgets2;
            }
        }
        int paddingTop2 = paddingTop;
        int paddingRight2 = paddingRight;
        int paddingBottom2 = paddingBottom;
        ConstraintAnchor left2 = left;
        int maxWidth = 0;
        int maxHeight = 0;
        for (int i4 = 0; i4 < listCount; i4++) {
            WidgetsList current2 = this.mChainList.get(i4);
            if (orientation == 0) {
                if (i4 < listCount - 1) {
                    WidgetsList next = this.mChainList.get(i4 + 1);
                    bottom = next.biggest.mTop;
                    paddingBottom2 = 0;
                } else {
                    bottom = this.mBottom;
                    paddingBottom2 = getPaddingBottom();
                }
                ConstraintAnchor currentBottom = current2.biggest.mBottom;
                ConstraintAnchor currentBottom2 = left2;
                current2.setup(orientation, currentBottom2, top, right, bottom, paddingLeft, paddingTop2, paddingRight2, paddingBottom2, max);
                paddingTop2 = 0;
                int maxWidth2 = Math.max(maxWidth, current2.getWidth());
                maxHeight += current2.getHeight();
                if (i4 > 0) {
                    maxHeight += this.mVerticalGap;
                }
                maxWidth = maxWidth2;
                top = currentBottom;
            } else {
                ConstraintAnchor top2 = top;
                int maxHeight2 = maxHeight;
                int maxWidth3 = maxWidth;
                if (i4 < listCount - 1) {
                    WidgetsList next2 = this.mChainList.get(i4 + 1);
                    right = next2.biggest.mLeft;
                    paddingRight2 = 0;
                } else {
                    right = this.mRight;
                    paddingRight2 = getPaddingRight();
                }
                ConstraintAnchor currentRight = current2.biggest.mRight;
                ConstraintAnchor currentRight2 = bottom;
                current2.setup(orientation, left2, top2, right, currentRight2, paddingLeft, paddingTop2, paddingRight2, paddingBottom2, max);
                left2 = currentRight;
                paddingLeft = 0;
                maxWidth = maxWidth3 + current2.getWidth();
                int maxWidth4 = current2.getHeight();
                int maxHeight3 = Math.max(maxHeight2, maxWidth4);
                if (i4 <= 0) {
                    maxHeight = maxHeight3;
                    top = top2;
                } else {
                    maxWidth += this.mHorizontalGap;
                    maxHeight = maxHeight3;
                    top = top2;
                }
            }
        }
        measured[0] = maxWidth;
        measured[1] = maxHeight;
    }

    private void measureNoWrap(ConstraintWidget[] widgets, int count, int orientation, int max, int[] measured) {
        WidgetsList list;
        if (count == 0) {
            return;
        }
        if (this.mChainList.size() == 0) {
            list = new WidgetsList(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, max);
            this.mChainList.add(list);
        } else {
            list = this.mChainList.get(0);
            list.clear();
            list.setup(orientation, this.mLeft, this.mTop, this.mRight, this.mBottom, getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom(), max);
        }
        for (int i = 0; i < count; i++) {
            ConstraintWidget widget = widgets[i];
            list.add(widget);
        }
        int i2 = list.getWidth();
        measured[0] = i2;
        measured[1] = list.getHeight();
    }

    private void measureAligned(ConstraintWidget[] widgets, int count, int orientation, int max, int[] measured) {
        ConstraintWidget widget;
        boolean done = false;
        int rows = 0;
        int cols = 0;
        if (orientation == 0) {
            cols = this.mMaxElementsWrap;
            if (cols <= 0) {
                int w = 0;
                cols = 0;
                for (int i = 0; i < count; i++) {
                    if (i > 0) {
                        w += this.mHorizontalGap;
                    }
                    ConstraintWidget widget2 = widgets[i];
                    if (widget2 != null) {
                        w += getWidgetWidth(widget2, max);
                        if (w > max) {
                            break;
                        } else {
                            cols++;
                        }
                    }
                }
            }
        } else {
            rows = this.mMaxElementsWrap;
            if (rows <= 0) {
                int h = 0;
                rows = 0;
                for (int i2 = 0; i2 < count; i2++) {
                    if (i2 > 0) {
                        h += this.mVerticalGap;
                    }
                    ConstraintWidget widget3 = widgets[i2];
                    if (widget3 != null) {
                        h += getWidgetHeight(widget3, max);
                        if (h > max) {
                            break;
                        } else {
                            rows++;
                        }
                    }
                }
            }
        }
        if (this.mAlignedDimensions == null) {
            this.mAlignedDimensions = new int[2];
        }
        int w2 = 1;
        if ((rows == 0 && orientation == 1) || (cols == 0 && orientation == 0)) {
            done = true;
        }
        while (!done) {
            if (orientation == 0) {
                rows = (int) Math.ceil(count / cols);
            } else {
                cols = (int) Math.ceil(count / rows);
            }
            if (this.mAlignedBiggestElementsInCols == null || this.mAlignedBiggestElementsInCols.length < cols) {
                this.mAlignedBiggestElementsInCols = new ConstraintWidget[cols];
            } else {
                Arrays.fill(this.mAlignedBiggestElementsInCols, (Object) null);
            }
            if (this.mAlignedBiggestElementsInRows == null || this.mAlignedBiggestElementsInRows.length < rows) {
                this.mAlignedBiggestElementsInRows = new ConstraintWidget[rows];
            } else {
                Arrays.fill(this.mAlignedBiggestElementsInRows, (Object) null);
            }
            int i3 = 0;
            while (i3 < cols) {
                int j = 0;
                while (j < rows) {
                    int index = (j * cols) + i3;
                    if (orientation == w2) {
                        index = (i3 * rows) + j;
                    }
                    if (index < widgets.length && (widget = widgets[index]) != null) {
                        int w3 = getWidgetWidth(widget, max);
                        if (this.mAlignedBiggestElementsInCols[i3] == null || this.mAlignedBiggestElementsInCols[i3].getWidth() < w3) {
                            this.mAlignedBiggestElementsInCols[i3] = widget;
                        }
                        int h2 = getWidgetHeight(widget, max);
                        if (this.mAlignedBiggestElementsInRows[j] == null || this.mAlignedBiggestElementsInRows[j].getHeight() < h2) {
                            this.mAlignedBiggestElementsInRows[j] = widget;
                        }
                    }
                    j++;
                    w2 = 1;
                }
                i3++;
                w2 = 1;
            }
            int w4 = 0;
            for (int i4 = 0; i4 < cols; i4++) {
                ConstraintWidget widget4 = this.mAlignedBiggestElementsInCols[i4];
                if (widget4 != null) {
                    if (i4 > 0) {
                        w4 += this.mHorizontalGap;
                    }
                    w4 += getWidgetWidth(widget4, max);
                }
            }
            int h3 = 0;
            for (int j2 = 0; j2 < rows; j2++) {
                ConstraintWidget widget5 = this.mAlignedBiggestElementsInRows[j2];
                if (widget5 != null) {
                    if (j2 > 0) {
                        h3 += this.mVerticalGap;
                    }
                    h3 += getWidgetHeight(widget5, max);
                }
            }
            measured[0] = w4;
            measured[1] = h3;
            if (orientation == 0) {
                if (w4 > max) {
                    if (cols > 1) {
                        cols--;
                    } else {
                        done = true;
                    }
                } else {
                    done = true;
                }
            } else if (h3 > max) {
                if (rows > 1) {
                    rows--;
                } else {
                    done = true;
                }
            } else {
                done = true;
            }
            w2 = 1;
        }
        this.mAlignedDimensions[0] = cols;
        this.mAlignedDimensions[1] = rows;
    }

    private void createAlignedConstraints(boolean isInRtl) {
        ConstraintWidget widget;
        if (this.mAlignedDimensions == null || this.mAlignedBiggestElementsInCols == null || this.mAlignedBiggestElementsInRows == null) {
            return;
        }
        for (int i = 0; i < this.mDisplayedWidgetsCount; i++) {
            this.mDisplayedWidgets[i].resetAnchors();
        }
        int cols = this.mAlignedDimensions[0];
        int rows = this.mAlignedDimensions[1];
        ConstraintWidget previous = null;
        for (int i2 = 0; i2 < cols; i2++) {
            int index = i2;
            if (isInRtl) {
                index = (cols - i2) - 1;
            }
            ConstraintWidget widget2 = this.mAlignedBiggestElementsInCols[index];
            if (widget2 != null && widget2.getVisibility() != 8) {
                if (i2 == 0) {
                    widget2.connect(widget2.mLeft, this.mLeft, getPaddingLeft());
                    widget2.setHorizontalChainStyle(this.mHorizontalStyle);
                    widget2.setHorizontalBiasPercent(this.mHorizontalBias);
                }
                if (i2 == cols - 1) {
                    widget2.connect(widget2.mRight, this.mRight, getPaddingRight());
                }
                if (i2 > 0) {
                    widget2.connect(widget2.mLeft, previous.mRight, this.mHorizontalGap);
                    previous.connect(previous.mRight, widget2.mLeft, 0);
                }
                previous = widget2;
            }
        }
        for (int j = 0; j < rows; j++) {
            ConstraintWidget widget3 = this.mAlignedBiggestElementsInRows[j];
            if (widget3 != null && widget3.getVisibility() != 8) {
                if (j == 0) {
                    widget3.connect(widget3.mTop, this.mTop, getPaddingTop());
                    widget3.setVerticalChainStyle(this.mVerticalStyle);
                    widget3.setVerticalBiasPercent(this.mVerticalBias);
                }
                if (j == rows - 1) {
                    widget3.connect(widget3.mBottom, this.mBottom, getPaddingBottom());
                }
                if (j > 0) {
                    widget3.connect(widget3.mTop, previous.mBottom, this.mVerticalGap);
                    previous.connect(previous.mBottom, widget3.mTop, 0);
                }
                previous = widget3;
            }
        }
        for (int i3 = 0; i3 < cols; i3++) {
            for (int j2 = 0; j2 < rows; j2++) {
                int index2 = (j2 * cols) + i3;
                if (this.mOrientation == 1) {
                    index2 = (i3 * rows) + j2;
                }
                if (index2 < this.mDisplayedWidgets.length && (widget = this.mDisplayedWidgets[index2]) != null && widget.getVisibility() != 8) {
                    ConstraintWidget biggestInCol = this.mAlignedBiggestElementsInCols[i3];
                    ConstraintWidget biggestInRow = this.mAlignedBiggestElementsInRows[j2];
                    if (widget != biggestInCol) {
                        widget.connect(widget.mLeft, biggestInCol.mLeft, 0);
                        widget.connect(widget.mRight, biggestInCol.mRight, 0);
                    }
                    if (widget != biggestInRow) {
                        widget.connect(widget.mTop, biggestInRow.mTop, 0);
                        widget.connect(widget.mBottom, biggestInRow.mBottom, 0);
                    }
                }
            }
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void addToSolver(LinearSystem system) {
        super.addToSolver(system);
        boolean isInRtl = getParent() != null ? ((ConstraintWidgetContainer) getParent()).isRtl() : false;
        switch (this.mWrapMode) {
            case 0:
                if (this.mChainList.size() > 0) {
                    WidgetsList list = this.mChainList.get(0);
                    list.createConstraints(isInRtl, 0, true);
                    break;
                }
                break;
            case 1:
                int count = this.mChainList.size();
                int i = 0;
                while (i < count) {
                    WidgetsList list2 = this.mChainList.get(i);
                    list2.createConstraints(isInRtl, i, i == count + (-1));
                    i++;
                }
                break;
            case 2:
                createAlignedConstraints(isInRtl);
                break;
        }
        needsCallbackFromSolver(false);
    }
}
