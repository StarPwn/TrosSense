package it.unimi.dsi.fastutil.ints;

import java.io.Serializable;

/* loaded from: classes4.dex */
public abstract class AbstractInt2ObjectFunction<V> implements Int2ObjectFunction<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public void defaultReturnValue(V rv) {
        this.defRetValue = rv;
    }

    @Override // it.unimi.dsi.fastutil.ints.Int2ObjectFunction
    public V defaultReturnValue() {
        return this.defRetValue;
    }
}
