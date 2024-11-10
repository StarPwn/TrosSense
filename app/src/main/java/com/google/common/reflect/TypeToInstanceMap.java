package com.google.common.reflect;

import java.util.Map;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public interface TypeToInstanceMap<B> extends Map<TypeToken<? extends B>, B> {
    @Nullable
    <T extends B> T getInstance(TypeToken<T> typeToken);

    @Nullable
    <T extends B> T getInstance(Class<T> cls);

    @Nullable
    <T extends B> T putInstance(TypeToken<T> typeToken, @Nullable T t);

    @Nullable
    <T extends B> T putInstance(Class<T> cls, @Nullable T t);
}
