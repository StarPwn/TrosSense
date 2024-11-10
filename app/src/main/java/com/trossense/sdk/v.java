package com.trossense.sdk;

import org.cloudburstmc.protocol.bedrock.data.PlayerActionType;

/* loaded from: classes3.dex */
/* synthetic */ class v {
    static final int[] a;

    static {
        int[] iArr = new int[PlayerActionType.values().length];
        a = iArr;
        try {
            iArr[PlayerActionType.START_BREAK.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            a[PlayerActionType.ABORT_BREAK.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            a[PlayerActionType.CONTINUE_BREAK.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        try {
            a[PlayerActionType.BLOCK_PREDICT_DESTROY.ordinal()] = 4;
        } catch (NoSuchFieldError e4) {
        }
        try {
            a[PlayerActionType.BLOCK_CONTINUE_DESTROY.ordinal()] = 5;
        } catch (NoSuchFieldError e5) {
        }
    }
}
