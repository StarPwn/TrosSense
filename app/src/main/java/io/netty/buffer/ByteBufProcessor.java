package io.netty.buffer;

import io.netty.util.ByteProcessor;

@Deprecated
/* loaded from: classes4.dex */
public interface ByteBufProcessor extends ByteProcessor {

    @Deprecated
    public static final ByteBufProcessor FIND_NUL = new ByteBufProcessor() { // from class: io.netty.buffer.ByteBufProcessor.1
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return value != 0;
        }
    };

    @Deprecated
    public static final ByteBufProcessor FIND_NON_NUL = new ByteBufProcessor() { // from class: io.netty.buffer.ByteBufProcessor.2
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return value == 0;
        }
    };

    @Deprecated
    public static final ByteBufProcessor FIND_CR = new ByteBufProcessor() { // from class: io.netty.buffer.ByteBufProcessor.3
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return value != 13;
        }
    };

    @Deprecated
    public static final ByteBufProcessor FIND_NON_CR = new ByteBufProcessor() { // from class: io.netty.buffer.ByteBufProcessor.4
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return value == 13;
        }
    };

    @Deprecated
    public static final ByteBufProcessor FIND_LF = new ByteBufProcessor() { // from class: io.netty.buffer.ByteBufProcessor.5
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return value != 10;
        }
    };

    @Deprecated
    public static final ByteBufProcessor FIND_NON_LF = new ByteBufProcessor() { // from class: io.netty.buffer.ByteBufProcessor.6
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return value == 10;
        }
    };

    @Deprecated
    public static final ByteBufProcessor FIND_CRLF = new ByteBufProcessor() { // from class: io.netty.buffer.ByteBufProcessor.7
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return (value == 13 || value == 10) ? false : true;
        }
    };

    @Deprecated
    public static final ByteBufProcessor FIND_NON_CRLF = new ByteBufProcessor() { // from class: io.netty.buffer.ByteBufProcessor.8
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return value == 13 || value == 10;
        }
    };

    @Deprecated
    public static final ByteBufProcessor FIND_LINEAR_WHITESPACE = new ByteBufProcessor() { // from class: io.netty.buffer.ByteBufProcessor.9
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return (value == 32 || value == 9) ? false : true;
        }
    };

    @Deprecated
    public static final ByteBufProcessor FIND_NON_LINEAR_WHITESPACE = new ByteBufProcessor() { // from class: io.netty.buffer.ByteBufProcessor.10
        @Override // io.netty.util.ByteProcessor
        public boolean process(byte value) throws Exception {
            return value == 32 || value == 9;
        }
    };
}
