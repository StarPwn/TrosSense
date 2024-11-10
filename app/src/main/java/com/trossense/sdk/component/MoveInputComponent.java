package com.trossense.sdk.component;

import com.trossense.sdk.PointerHolder;

/* loaded from: classes3.dex */
public class MoveInputComponent {
    private static PointerHolder[] b;
    public boolean isAscendScaffoldingDown;
    public boolean isDescendScaffoldingDown;
    public boolean isDownDown;
    public boolean isFlyDownSlowDown;
    public boolean isFlyUpSlowDown;
    public boolean isFlyingAscendDown;
    public boolean isFlyingDescendDown;
    public boolean isJumpDown;
    public boolean isLeftDown;
    public boolean isRightDown;
    public boolean isSneakDown;
    public boolean isSneakToggleDown;
    public boolean isSprintDown;
    public boolean isUpDown;
    public boolean isUpLeftDown;
    public boolean isUpRightDown;
    public float moveForward;
    public float moveSide;

    static {
        if (b() == null) {
            b(new PointerHolder[2]);
        }
    }

    public static void b(PointerHolder[] pointerHolderArr) {
        b = pointerHolderArr;
    }

    public static PointerHolder[] b() {
        return b;
    }
}
