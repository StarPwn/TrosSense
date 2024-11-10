package io.netty.handler.ssl;

import io.netty.util.ReferenceCounted;
import java.security.cert.X509Certificate;

/* loaded from: classes4.dex */
interface OpenSslKeyMaterial extends ReferenceCounted {
    X509Certificate[] certificateChain();

    long certificateChainAddress();

    long privateKeyAddress();

    @Override // io.netty.util.ReferenceCounted
    boolean release();

    @Override // io.netty.util.ReferenceCounted
    boolean release(int i);

    @Override // io.netty.util.ReferenceCounted
    OpenSslKeyMaterial retain();

    @Override // io.netty.util.ReferenceCounted
    OpenSslKeyMaterial retain(int i);

    @Override // io.netty.util.ReferenceCounted
    OpenSslKeyMaterial touch();

    @Override // io.netty.util.ReferenceCounted
    OpenSslKeyMaterial touch(Object obj);
}
