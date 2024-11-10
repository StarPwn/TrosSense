package com.google.common.collect;

import com.google.common.collect.Maps;
import java.util.function.BinaryOperator;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Maps$$ExternalSyntheticLambda2 implements BinaryOperator {
    @Override // java.util.function.BiFunction
    public final Object apply(Object obj, Object obj2) {
        return ((Maps.Accumulator) obj).combine((Maps.Accumulator) obj2);
    }
}
