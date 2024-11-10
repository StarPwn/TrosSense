package com.google.common.util.concurrent;

import javax.annotation.Nullable;

/* loaded from: classes.dex */
final class Platform {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isInstanceOfThrowableClass(@Nullable Throwable t, Class<? extends Throwable> expectedClass) {
        return expectedClass.isInstance(t);
    }

    private Platform() {
    }
}
