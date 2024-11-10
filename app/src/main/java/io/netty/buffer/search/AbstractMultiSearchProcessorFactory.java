package io.netty.buffer.search;

/* loaded from: classes4.dex */
public abstract class AbstractMultiSearchProcessorFactory implements MultiSearchProcessorFactory {
    public static AhoCorasicSearchProcessorFactory newAhoCorasicSearchProcessorFactory(byte[]... needles) {
        return new AhoCorasicSearchProcessorFactory(needles);
    }
}
