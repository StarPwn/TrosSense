package io.netty.handler.ssl;

import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.ObjectUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes4.dex */
public final class SupportedCipherSuiteFilter implements CipherSuiteFilter {
    public static final SupportedCipherSuiteFilter INSTANCE = new SupportedCipherSuiteFilter();

    private SupportedCipherSuiteFilter() {
    }

    @Override // io.netty.handler.ssl.CipherSuiteFilter
    public String[] filterCipherSuites(Iterable<String> ciphers, List<String> defaultCiphers, Set<String> supportedCiphers) {
        List<String> newCiphers;
        String c;
        ObjectUtil.checkNotNull(defaultCiphers, "defaultCiphers");
        ObjectUtil.checkNotNull(supportedCiphers, "supportedCiphers");
        if (ciphers == null) {
            newCiphers = new ArrayList<>(defaultCiphers.size());
            ciphers = defaultCiphers;
        } else {
            newCiphers = new ArrayList<>(supportedCiphers.size());
        }
        Iterator<String> it2 = ciphers.iterator();
        while (it2.hasNext() && (c = it2.next()) != null) {
            if (supportedCiphers.contains(c)) {
                newCiphers.add(c);
            }
        }
        return (String[]) newCiphers.toArray(EmptyArrays.EMPTY_STRINGS);
    }
}
