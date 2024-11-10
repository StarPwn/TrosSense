package com.google.common.base;

/* loaded from: classes.dex */
abstract class CommonMatcher {
    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int end();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean find();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean find(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean matches();

    abstract String replaceAll(String str);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int start();
}
