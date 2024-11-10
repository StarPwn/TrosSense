package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.AppendableCharSequence;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes4.dex */
public class StompSubframeEncoder extends MessageToMessageEncoder<StompSubframe> {
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final FastThreadLocal<LinkedHashMap<CharSequence, CharSequence>> ESCAPE_HEADER_KEY_CACHE = new FastThreadLocal<LinkedHashMap<CharSequence, CharSequence>>() { // from class: io.netty.handler.codec.stomp.StompSubframeEncoder.1
        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.util.concurrent.FastThreadLocal
        public LinkedHashMap<CharSequence, CharSequence> initialValue() throws Exception {
            LinkedHashMap<CharSequence, CharSequence> cache = new LinkedHashMap<CharSequence, CharSequence>(32, 0.75f, true) { // from class: io.netty.handler.codec.stomp.StompSubframeEncoder.1.1
                @Override // java.util.LinkedHashMap
                protected boolean removeEldestEntry(Map.Entry<CharSequence, CharSequence> entry) {
                    return size() > 32;
                }
            };
            cache.put(StompHeaders.ACCEPT_VERSION, StompHeaders.ACCEPT_VERSION);
            cache.put(StompHeaders.HOST, StompHeaders.HOST);
            cache.put(StompHeaders.LOGIN, StompHeaders.LOGIN);
            cache.put(StompHeaders.PASSCODE, StompHeaders.PASSCODE);
            cache.put(StompHeaders.HEART_BEAT, StompHeaders.HEART_BEAT);
            cache.put(StompHeaders.VERSION, StompHeaders.VERSION);
            cache.put(StompHeaders.SESSION, StompHeaders.SESSION);
            cache.put(StompHeaders.SERVER, StompHeaders.SERVER);
            cache.put(StompHeaders.DESTINATION, StompHeaders.DESTINATION);
            cache.put(StompHeaders.ID, StompHeaders.ID);
            cache.put(StompHeaders.ACK, StompHeaders.ACK);
            cache.put(StompHeaders.TRANSACTION, StompHeaders.TRANSACTION);
            cache.put(StompHeaders.RECEIPT, StompHeaders.RECEIPT);
            cache.put(StompHeaders.MESSAGE_ID, StompHeaders.MESSAGE_ID);
            cache.put(StompHeaders.SUBSCRIPTION, StompHeaders.SUBSCRIPTION);
            cache.put(StompHeaders.RECEIPT_ID, StompHeaders.RECEIPT_ID);
            cache.put(StompHeaders.MESSAGE, StompHeaders.MESSAGE);
            cache.put(StompHeaders.CONTENT_LENGTH, StompHeaders.CONTENT_LENGTH);
            cache.put(StompHeaders.CONTENT_TYPE, StompHeaders.CONTENT_TYPE);
            return cache;
        }
    };
    private static final int ESCAPE_HEADER_KEY_CACHE_LIMIT = 32;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.handler.codec.MessageToMessageEncoder
    public /* bridge */ /* synthetic */ void encode(ChannelHandlerContext channelHandlerContext, StompSubframe stompSubframe, List list) throws Exception {
        encode2(channelHandlerContext, stompSubframe, (List<Object>) list);
    }

    /* renamed from: encode, reason: avoid collision after fix types in other method */
    protected void encode2(ChannelHandlerContext ctx, StompSubframe msg, List<Object> out) throws Exception {
        if (msg instanceof StompFrame) {
            StompFrame stompFrame = (StompFrame) msg;
            out.add(convertFullFrame(stompFrame, encodeFullFrame(stompFrame, ctx)));
        } else {
            if (msg instanceof StompHeadersSubframe) {
                StompHeadersSubframe stompHeadersSubframe = (StompHeadersSubframe) msg;
                ByteBuf buf = ctx.alloc().buffer(headersSubFrameSize(stompHeadersSubframe));
                encodeHeaders(stompHeadersSubframe, buf);
                out.add(convertHeadersSubFrame(stompHeadersSubframe, buf));
                return;
            }
            if (msg instanceof StompContentSubframe) {
                StompContentSubframe stompContentSubframe = (StompContentSubframe) msg;
                out.add(convertContentSubFrame(stompContentSubframe, encodeContent(stompContentSubframe, ctx)));
            }
        }
    }

    protected Object convertFullFrame(StompFrame original, ByteBuf encoded) {
        return encoded;
    }

    protected Object convertHeadersSubFrame(StompHeadersSubframe original, ByteBuf encoded) {
        return encoded;
    }

    protected Object convertContentSubFrame(StompContentSubframe original, ByteBuf encoded) {
        return encoded;
    }

    protected int headersSubFrameSize(StompHeadersSubframe headersSubframe) {
        int estimatedSize = (headersSubframe.headers().size() * 34) + 48;
        if (estimatedSize < 128) {
            return 128;
        }
        return Math.max(estimatedSize, 256);
    }

    private ByteBuf encodeFullFrame(StompFrame frame, ChannelHandlerContext ctx) {
        int contentReadableBytes = frame.content().readableBytes();
        ByteBuf buf = ctx.alloc().buffer(headersSubFrameSize(frame) + contentReadableBytes);
        encodeHeaders(frame, buf);
        if (contentReadableBytes > 0) {
            buf.writeBytes(frame.content());
        }
        return buf.writeByte(0);
    }

    private static void encodeHeaders(StompHeadersSubframe frame, ByteBuf buf) {
        StompCommand command = frame.command();
        ByteBufUtil.writeUtf8(buf, command.toString());
        buf.writeByte(10);
        boolean shouldEscape = shouldEscape(command);
        LinkedHashMap<CharSequence, CharSequence> cache = ESCAPE_HEADER_KEY_CACHE.get();
        for (Map.Entry<CharSequence, CharSequence> entry : frame.headers()) {
            CharSequence headerKey = entry.getKey();
            if (shouldEscape) {
                CharSequence cachedHeaderKey = cache.get(headerKey);
                if (cachedHeaderKey == null) {
                    cachedHeaderKey = escape(headerKey);
                    cache.put(headerKey, cachedHeaderKey);
                }
                headerKey = cachedHeaderKey;
            }
            ByteBufUtil.writeUtf8(buf, headerKey);
            buf.writeByte(58);
            CharSequence headerValue = entry.getValue();
            if (shouldEscape) {
                headerValue = escape(headerValue);
            }
            ByteBufUtil.writeUtf8(buf, headerValue);
            buf.writeByte(10);
        }
        buf.writeByte(10);
    }

    private static ByteBuf encodeContent(StompContentSubframe content, ChannelHandlerContext ctx) {
        if (content instanceof LastStompContentSubframe) {
            ByteBuf buf = ctx.alloc().buffer(content.content().readableBytes() + 1);
            buf.writeBytes(content.content());
            buf.writeByte(0);
            return buf;
        }
        return content.content().retain();
    }

    private static boolean shouldEscape(StompCommand command) {
        return (command == StompCommand.CONNECT || command == StompCommand.CONNECTED) ? false : true;
    }

    private static CharSequence escape(CharSequence input) {
        AppendableCharSequence builder = null;
        for (int i = 0; i < input.length(); i++) {
            char chr = input.charAt(i);
            if (chr == '\\') {
                builder = escapeBuilder(builder, input, i);
                builder.append((CharSequence) "\\\\");
            } else if (chr == ':') {
                builder = escapeBuilder(builder, input, i);
                builder.append((CharSequence) "\\c");
            } else if (chr == '\n') {
                builder = escapeBuilder(builder, input, i);
                builder.append((CharSequence) "\\n");
            } else if (chr == '\r') {
                builder = escapeBuilder(builder, input, i);
                builder.append((CharSequence) "\\r");
            } else if (builder != null) {
                builder.append(chr);
            }
        }
        return builder != null ? builder : input;
    }

    private static AppendableCharSequence escapeBuilder(AppendableCharSequence builder, CharSequence input, int offset) {
        if (builder != null) {
            return builder;
        }
        return new AppendableCharSequence(input.length() + 8).append(input, 0, offset);
    }
}
