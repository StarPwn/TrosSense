package io.netty.handler.ssl;

import io.netty.util.internal.EmptyArrays;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/* loaded from: classes4.dex */
public final class IdentityCipherSuiteFilter implements CipherSuiteFilter {
    public static final IdentityCipherSuiteFilter INSTANCE = new IdentityCipherSuiteFilter(true);
    public static final IdentityCipherSuiteFilter INSTANCE_DEFAULTING_TO_SUPPORTED_CIPHERS = new IdentityCipherSuiteFilter(false);
    private final boolean defaultToDefaultCiphers;

    private IdentityCipherSuiteFilter(boolean defaultToDefaultCiphers) {
        this.defaultToDefaultCiphers = defaultToDefaultCiphers;
    }

    @Override // io.netty.handler.ssl.CipherSuiteFilter
    public String[] filterCipherSuites(Iterable<String> ciphers, List<String> defaultCiphers, Set<String> supportedCiphers) {
        String c;
        if (ciphers == null) {
            if (this.defaultToDefaultCiphers) {
                return (String[]) defaultCiphers.toArray(EmptyArrays.EMPTY_STRINGS);
            }
            return (String[]) supportedCiphers.toArray(EmptyArrays.EMPTY_STRINGS);
        }
        List<String> newCiphers = new ArrayList<>(supportedCiphers.size());
        Iterator<String> it2 = ciphers.iterator();
        while (it2.hasNext() && (c = it2.next()) != null) {
            newCiphers.add(c);
        }
        return (String[]) newCiphers.toArray(EmptyArrays.EMPTY_STRINGS);
    }
}
