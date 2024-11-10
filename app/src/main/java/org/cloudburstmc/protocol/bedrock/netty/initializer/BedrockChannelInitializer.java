package org.cloudburstmc.protocol.bedrock.netty.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import org.cloudburstmc.netty.channel.raknet.config.RakChannelOption;
import org.cloudburstmc.protocol.bedrock.BedrockPeer;
import org.cloudburstmc.protocol.bedrock.BedrockSession;
import org.cloudburstmc.protocol.bedrock.BedrockSessionFactory;
import org.cloudburstmc.protocol.bedrock.data.CompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.data.PacketCompressionAlgorithm;
import org.cloudburstmc.protocol.bedrock.netty.codec.FrameIdCodec;
import org.cloudburstmc.protocol.bedrock.netty.codec.batch.BedrockBatchDecoder;
import org.cloudburstmc.protocol.bedrock.netty.codec.batch.BedrockBatchEncoder;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.CompressionCodec;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.CompressionStrategy;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.NoopCompression;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.SimpleCompressionStrategy;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.SnappyCompression;
import org.cloudburstmc.protocol.bedrock.netty.codec.compression.ZlibCompression;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec_v1;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec_v2;
import org.cloudburstmc.protocol.bedrock.netty.codec.packet.BedrockPacketCodec_v3;
import org.cloudburstmc.protocol.common.util.Zlib;

/* loaded from: classes5.dex */
public abstract class BedrockChannelInitializer<T extends BedrockSession> extends ChannelInitializer<Channel> {
    public static final int RAKNET_MINECRAFT_ID = 254;
    private static final FrameIdCodec RAKNET_FRAME_CODEC = new FrameIdCodec(254);
    private static final BedrockBatchDecoder BATCH_DECODER = new BedrockBatchDecoder();
    private static final CompressionStrategy ZLIB_RAW_STRATEGY = new SimpleCompressionStrategy(new ZlibCompression(Zlib.RAW));
    private static final CompressionStrategy ZLIB_STRATEGY = new SimpleCompressionStrategy(new ZlibCompression(Zlib.DEFAULT));
    private static final CompressionStrategy SNAPPY_STRATEGY = new SimpleCompressionStrategy(new SnappyCompression());
    private static final CompressionStrategy NOOP_STRATEGY = new SimpleCompressionStrategy(new NoopCompression());

    protected abstract T createSession0(BedrockPeer bedrockPeer, int i);

    protected abstract void initSession(T t);

    @Override // io.netty.channel.ChannelInitializer
    protected final void initChannel(Channel channel) throws Exception {
        preInitChannel(channel);
        channel.pipeline().addLast(BedrockBatchDecoder.NAME, BATCH_DECODER).addLast(BedrockBatchEncoder.NAME, new BedrockBatchEncoder());
        initPacketCodec(channel);
        channel.pipeline().addLast(BedrockPeer.NAME, createPeer(channel));
        postInitChannel(channel);
    }

    protected void preInitChannel(Channel channel) throws Exception {
        channel.pipeline().addLast(FrameIdCodec.NAME, RAKNET_FRAME_CODEC);
        int rakVersion = ((Integer) channel.config().getOption(RakChannelOption.RAK_PROTOCOL_VERSION)).intValue();
        CompressionStrategy compression = getCompression(PacketCompressionAlgorithm.ZLIB, rakVersion, true);
        channel.pipeline().addLast(CompressionCodec.NAME, new CompressionCodec(compression, false));
    }

    public static CompressionStrategy getCompression(CompressionAlgorithm algorithm, int rakVersion, boolean initial) {
        switch (rakVersion) {
            case 7:
            case 8:
            case 9:
                return ZLIB_STRATEGY;
            case 10:
                return ZLIB_RAW_STRATEGY;
            case 11:
                return initial ? NOOP_STRATEGY : getCompression(algorithm);
            default:
                throw new UnsupportedOperationException("Unsupported RakNet protocol version: " + rakVersion);
        }
    }

    private static CompressionStrategy getCompression(CompressionAlgorithm algorithm) {
        if (algorithm == PacketCompressionAlgorithm.ZLIB) {
            return ZLIB_RAW_STRATEGY;
        }
        if (algorithm == PacketCompressionAlgorithm.SNAPPY) {
            return SNAPPY_STRATEGY;
        }
        if (algorithm == PacketCompressionAlgorithm.NONE) {
            return NOOP_STRATEGY;
        }
        throw new UnsupportedOperationException("Unsupported compression algorithm: " + algorithm);
    }

    protected void postInitChannel(Channel channel) throws Exception {
    }

    protected void initPacketCodec(Channel channel) throws Exception {
        int rakVersion = ((Integer) channel.config().getOption(RakChannelOption.RAK_PROTOCOL_VERSION)).intValue();
        switch (rakVersion) {
            case 7:
                channel.pipeline().addLast(BedrockPacketCodec.NAME, new BedrockPacketCodec_v1());
                return;
            case 8:
                channel.pipeline().addLast(BedrockPacketCodec.NAME, new BedrockPacketCodec_v2());
                return;
            case 9:
            case 10:
            case 11:
                channel.pipeline().addLast(BedrockPacketCodec.NAME, new BedrockPacketCodec_v3());
                return;
            default:
                throw new UnsupportedOperationException("Unsupported RakNet protocol version: " + rakVersion);
        }
    }

    protected BedrockPeer createPeer(Channel channel) {
        return new BedrockPeer(channel, new BedrockSessionFactory() { // from class: org.cloudburstmc.protocol.bedrock.netty.initializer.BedrockChannelInitializer$$ExternalSyntheticLambda0
            @Override // org.cloudburstmc.protocol.bedrock.BedrockSessionFactory
            public final BedrockSession createSession(BedrockPeer bedrockPeer, int i) {
                return BedrockChannelInitializer.this.createSession(bedrockPeer, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final T createSession(BedrockPeer peer, int subClientId) {
        T session = createSession0(peer, subClientId);
        initSession(session);
        return session;
    }
}
