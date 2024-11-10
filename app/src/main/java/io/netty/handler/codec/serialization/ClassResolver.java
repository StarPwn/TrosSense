package io.netty.handler.codec.serialization;

@Deprecated
/* loaded from: classes4.dex */
public interface ClassResolver {
    Class<?> resolve(String str) throws ClassNotFoundException;
}
