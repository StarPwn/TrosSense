package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes4.dex */
public final /* synthetic */ class ShortComparator$$ExternalSyntheticLambda0 implements ShortComparator, Serializable {
    public final /* synthetic */ ShortComparator f$0;
    public final /* synthetic */ ShortComparator f$1;

    public /* synthetic */ ShortComparator$$ExternalSyntheticLambda0(ShortComparator shortComparator, ShortComparator shortComparator2) {
        this.f$0 = shortComparator;
        this.f$1 = shortComparator2;
    }

    @Override // it.unimi.dsi.fastutil.shorts.ShortComparator
    public final int compare(short s, short s2) {
        return ShortComparator.lambda$thenComparing$953dd6d$1(this.f$0, this.f$1, s, s2);
    }
}
