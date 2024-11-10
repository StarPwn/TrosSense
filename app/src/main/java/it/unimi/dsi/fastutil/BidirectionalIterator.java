package it.unimi.dsi.fastutil;

import java.util.Iterator;

/* loaded from: classes4.dex */
public interface BidirectionalIterator<K> extends Iterator<K> {
    boolean hasPrevious();

    K previous();
}
