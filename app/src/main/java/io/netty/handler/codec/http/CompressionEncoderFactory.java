package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

/* loaded from: classes4.dex */
interface CompressionEncoderFactory {
    MessageToByteEncoder<ByteBuf> createEncoder();
}
