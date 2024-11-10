package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes4.dex */
public final /* synthetic */ class FloatComparator$$ExternalSyntheticLambda0 implements FloatComparator, Serializable {
    public final /* synthetic */ FloatComparator f$0;
    public final /* synthetic */ FloatComparator f$1;

    public /* synthetic */ FloatComparator$$ExternalSyntheticLambda0(FloatComparator floatComparator, FloatComparator floatComparator2) {
        this.f$0 = floatComparator;
        this.f$1 = floatComparator2;
    }

    @Override // it.unimi.dsi.fastutil.floats.FloatComparator
    public final int compare(float f, float f2) {
        return FloatComparator.lambda$thenComparing$99a1156d$1(this.f$0, this.f$1, f, f2);
    }
}
