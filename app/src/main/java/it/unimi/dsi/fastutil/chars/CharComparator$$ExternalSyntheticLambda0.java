package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes4.dex */
public final /* synthetic */ class CharComparator$$ExternalSyntheticLambda0 implements CharComparator, Serializable {
    public final /* synthetic */ CharComparator f$0;
    public final /* synthetic */ CharComparator f$1;

    public /* synthetic */ CharComparator$$ExternalSyntheticLambda0(CharComparator charComparator, CharComparator charComparator2) {
        this.f$0 = charComparator;
        this.f$1 = charComparator2;
    }

    @Override // it.unimi.dsi.fastutil.chars.CharComparator
    public final int compare(char c, char c2) {
        return CharComparator.lambda$thenComparing$2b1ecd07$1(this.f$0, this.f$1, c, c2);
    }
}
