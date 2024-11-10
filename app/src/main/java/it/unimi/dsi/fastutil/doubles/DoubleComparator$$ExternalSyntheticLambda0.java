package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes4.dex */
public final /* synthetic */ class DoubleComparator$$ExternalSyntheticLambda0 implements DoubleComparator, Serializable {
    public final /* synthetic */ DoubleComparator f$0;
    public final /* synthetic */ DoubleComparator f$1;

    public /* synthetic */ DoubleComparator$$ExternalSyntheticLambda0(DoubleComparator doubleComparator, DoubleComparator doubleComparator2) {
        this.f$0 = doubleComparator;
        this.f$1 = doubleComparator2;
    }

    @Override // it.unimi.dsi.fastutil.doubles.DoubleComparator
    public final int compare(double d, double d2) {
        return DoubleComparator.lambda$thenComparing$f8e9881b$1(this.f$0, this.f$1, d, d2);
    }
}
