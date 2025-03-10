package io.netty.handler.codec.memcache.binary;

import io.netty.channel.CombinedChannelDuplexHandler;

/* loaded from: classes4.dex */
public class BinaryMemcacheServerCodec extends CombinedChannelDuplexHandler<BinaryMemcacheRequestDecoder, BinaryMemcacheResponseEncoder> {
    public BinaryMemcacheServerCodec() {
        this(8192);
    }

    public BinaryMemcacheServerCodec(int decodeChunkSize) {
        super(new BinaryMemcacheRequestDecoder(decodeChunkSize), new BinaryMemcacheResponseEncoder());
    }
}
