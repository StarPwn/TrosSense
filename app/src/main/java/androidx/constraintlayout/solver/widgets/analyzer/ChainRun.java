package androidx.constraintlayout.solver.widgets.analyzer;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class ChainRun extends WidgetRun {
    private int chainStyle;
    ArrayList<WidgetRun> widgets;

    public ChainRun(ConstraintWidget widget, int orientation) {
        super(widget);
        this.widgets = new ArrayList<>();
        this.orientation = orientation;
        build();
    }

    public String toString() {
        String log = "ChainRun " + (this.orientation == 0 ? "horizontal : " : "vertical : ");
        Iterator<WidgetRun> it2 = this.widgets.iterator();
        while (it2.hasNext()) {
            WidgetRun run = it2.next();
            log = ((log + "<") + run) + "> ";
        }
        return log;
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    boolean supportsWrapComputation() {
        int count = this.widgets.size();
        for (int i = 0; i < count; i++) {
            WidgetRun run = this.widgets.get(i);
            if (!run.supportsWrapComputation()) {
                return false;
            }
        }
        return true;
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public long getWrapDimension() {
        int count = this.widgets.size();
        long wrapDimension = 0;
        for (int i = 0; i < count; i++) {
            WidgetRun run = this.widgets.get(i);
            wrapDimension = wrapDimension + run.start.margin + run.getWrapDimension() + run.end.margin;
        }
        return wrapDimension;
    }

    private void build() {
        ConstraintWidget current = this.widget;
        ConstraintWidget previous = current.getPreviousChainMember(this.orientation);
        while (previous != null) {
            current = previous;
            previous = current.getPreviousChainMember(this.orientation);
        }
        this.widget = current;
        this.widgets.add(current.getRun(this.orientation));
        ConstraintWidget next = current.getNextChainMember(this.orientation);
        while (next != null) {
            ConstraintWidget current2 = next;
            this.widgets.add(current2.getRun(this.orientation));
            next = current2.getNextChainMember(this.orientation);
        }
        Iterator<WidgetRun> it2 = this.widgets.iterator();
        while (it2.hasNext()) {
            WidgetRun run = it2.next();
            if (this.orientation == 0) {
                run.widget.horizontalChainRun = this;
            } else if (this.orientation == 1) {
                run.widget.verticalChainRun = this;
            }
        }
        boolean isInRtl = this.orientation == 0 && ((ConstraintWidgetContainer) this.widget.getParent()).isRtl();
        if (isInRtl && this.widgets.size() > 1) {
            this.widget = this.widgets.get(this.widgets.size() - 1).widget;
        }
        this.chainStyle = this.orientation == 0 ? this.widget.getHorizontalChainStyle() : this.widget.getVerticalChainStyle();
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    void clear() {
        this.runGroup = null;
        Iterator<WidgetRun> it2 = this.widgets.iterator();
        while (it2.hasNext()) {
            WidgetRun run = it2.next();
            run.clear();
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    void reset() {
        this.start.resolved = false;
        this.end.resolved = false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:305:0x0477, code lost:            r6 = r6 - r15;     */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00f9  */
    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun, androidx.constraintlayout.solver.widgets.analyzer.Dependency
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void update(androidx.constraintlayout.solver.widgets.analyzer.Dependency r27) {
        /*
            Method dump skipped, instructions count: 1196
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.analyzer.ChainRun.update(androidx.constraintlayout.solver.widgets.analyzer.Dependency):void");
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    public void applyToWidget() {
        for (int i = 0; i < this.widgets.size(); i++) {
            WidgetRun run = this.widgets.get(i);
            run.applyToWidget();
        }
    }

    private ConstraintWidget getFirstVisibleWidget() {
        for (int i = 0; i < this.widgets.size(); i++) {
            WidgetRun run = this.widgets.get(i);
            if (run.widget.getVisibility() != 8) {
                return run.widget;
            }
        }
        return null;
    }

    private ConstraintWidget getLastVisibleWidget() {
        for (int i = this.widgets.size() - 1; i >= 0; i--) {
            WidgetRun run = this.widgets.get(i);
            if (run.widget.getVisibility() != 8) {
                return run.widget;
            }
        }
        return null;
    }

    @Override // androidx.constraintlayout.solver.widgets.analyzer.WidgetRun
    void apply() {
        Iterator<WidgetRun> it2 = this.widgets.iterator();
        while (it2.hasNext()) {
            WidgetRun run = it2.next();
            run.apply();
        }
        int count = this.widgets.size();
        if (count < 1) {
            return;
        }
        ConstraintWidget firstWidget = this.widgets.get(0).widget;
        ConstraintWidget lastWidget = this.widgets.get(count - 1).widget;
        if (this.orientation == 0) {
            ConstraintAnchor startAnchor = firstWidget.mLeft;
            ConstraintAnchor endAnchor = lastWidget.mRight;
            DependencyNode startTarget = getTarget(startAnchor, 0);
            int startMargin = startAnchor.getMargin();
            ConstraintWidget firstVisibleWidget = getFirstVisibleWidget();
            if (firstVisibleWidget != null) {
                startMargin = firstVisibleWidget.mLeft.getMargin();
            }
            if (startTarget != null) {
                addTarget(this.start, startTarget, startMargin);
            }
            DependencyNode endTarget = getTarget(endAnchor, 0);
            int endMargin = endAnchor.getMargin();
            ConstraintWidget lastVisibleWidget = getLastVisibleWidget();
            if (lastVisibleWidget != null) {
                endMargin = lastVisibleWidget.mRight.getMargin();
            }
            if (endTarget != null) {
                addTarget(this.end, endTarget, -endMargin);
            }
        } else {
            ConstraintAnchor startAnchor2 = firstWidget.mTop;
            ConstraintAnchor endAnchor2 = lastWidget.mBottom;
            DependencyNode startTarget2 = getTarget(startAnchor2, 1);
            int startMargin2 = startAnchor2.getMargin();
            ConstraintWidget firstVisibleWidget2 = getFirstVisibleWidget();
            if (firstVisibleWidget2 != null) {
                startMargin2 = firstVisibleWidget2.mTop.getMargin();
            }
            if (startTarget2 != null) {
                addTarget(this.start, startTarget2, startMargin2);
            }
            DependencyNode endTarget2 = getTarget(endAnchor2, 1);
            int endMargin2 = endAnchor2.getMargin();
            ConstraintWidget lastVisibleWidget2 = getLastVisibleWidget();
            if (lastVisibleWidget2 != null) {
                endMargin2 = lastVisibleWidget2.mBottom.getMargin();
            }
            if (endTarget2 != null) {
                addTarget(this.end, endTarget2, -endMargin2);
            }
        }
        this.start.updateDelegate = this;
        this.end.updateDelegate = this;
    }
}
