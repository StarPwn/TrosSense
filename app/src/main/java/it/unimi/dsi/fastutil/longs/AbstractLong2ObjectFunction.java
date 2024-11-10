package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;

/* loaded from: classes4.dex */
public abstract class AbstractLong2ObjectFunction<V> implements Long2ObjectFunction<V>, Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    public void defaultReturnValue(V rv) {
        this.defRetValue = rv;
    }

    @Override // it.unimi.dsi.fastutil.longs.Long2ObjectFunction
    public V defaultReturnValue() {
        return this.defRetValue;
    }
}
