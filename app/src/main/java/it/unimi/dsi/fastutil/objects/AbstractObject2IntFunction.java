package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;

/* loaded from: classes4.dex */
public abstract class AbstractObject2IntFunction<K> implements Object2IntFunction<K>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected int defRetValue;

    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    public void defaultReturnValue(int rv) {
        this.defRetValue = rv;
    }

    @Override // it.unimi.dsi.fastutil.objects.Object2IntFunction
    public int defaultReturnValue() {
        return this.defRetValue;
    }
}
