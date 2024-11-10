package io.netty.handler.codec.compression;

import io.netty.handler.codec.ByteToMessageDecoder;

/* loaded from: classes4.dex */
public class Bzip2Decoder extends ByteToMessageDecoder {
    private int blockCRC;
    private Bzip2BlockDecompressor blockDecompressor;
    private int blockSize;
    private Bzip2HuffmanStageDecoder huffmanStageDecoder;
    private int streamCRC;
    private State currentState = State.INIT;
    private final Bzip2BitReader reader = new Bzip2BitReader();

    /* loaded from: classes4.dex */
    private enum State {
        INIT,
        INIT_BLOCK,
        INIT_BLOCK_PARAMS,
        RECEIVE_HUFFMAN_USED_MAP,
        RECEIVE_HUFFMAN_USED_BITMAPS,
        RECEIVE_SELECTORS_NUMBER,
        RECEIVE_SELECTORS,
        RECEIVE_HUFFMAN_LENGTH,
        DECODE_HUFFMAN_DATA,
        EOF
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x017a, code lost:            r17.currentState = io.netty.handler.codec.compression.Bzip2Decoder.State.RECEIVE_HUFFMAN_LENGTH;     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x017e, code lost:            r0 = r17.huffmanStageDecoder;        r3 = r0.totalTables;        r4 = r0.tableCodeLengths;        r5 = r0.alphabetSize;        r6 = r0.currentLength;        r7 = 0;        r8 = r0.modifyLength;        r11 = false;        r12 = r0.currentGroup;     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x018e, code lost:            if (r12 >= r3) goto L190;     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0195, code lost:            if (r9.hasReadableBits(5) != false) goto L94;     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0199, code lost:            if (r6 >= 0) goto L96;     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x019b, code lost:            r6 = r9.readBits(5);     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x019f, code lost:            r7 = r0.currentAlpha;     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x01a1, code lost:            if (r7 >= r5) goto L193;     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x01a7, code lost:            if (r9.isReadable() != false) goto L101;     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x01ab, code lost:            if (r8 != false) goto L106;     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x01b1, code lost:            if (r9.readBoolean() == false) goto L194;     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x01b4, code lost:            r4[r12][r7] = (byte) r6;        r7 = r7 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x01c0, code lost:            if (r9.isReadable() != false) goto L109;     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x01c9, code lost:            if (r9.readBoolean() == false) goto L112;     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x01cb, code lost:            r14 = -1;     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x01ce, code lost:            r6 = r6 + r14;        r8 = false;     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x01d4, code lost:            if (r9.isReadable() != false) goto L195;     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x01d6, code lost:            r11 = true;     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x01e0, code lost:            if (r11 == false) goto L120;     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x01e2, code lost:            r0.currentGroup = r12;        r0.currentLength = r6;        r0.currentAlpha = r7;        r0.modifyLength = r8;     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x01ea, code lost:            return;     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x01eb, code lost:            r0.createHuffmanDecodingTables();        r17.currentState = io.netty.handler.codec.compression.Bzip2Decoder.State.DECODE_HUFFMAN_DATA;     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x01f2, code lost:            r3 = r17.blockDecompressor;        r4 = r19.readerIndex();        r5 = r3.decodeHuffmanData(r17.huffmanStageDecoder);     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x01fe, code lost:            if (r5 != false) goto L124;     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x0200, code lost:            return;     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0205, code lost:            if (r19.readerIndex() != r4) goto L129;     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x020b, code lost:            if (r19.isReadable() == false) goto L129;     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x020d, code lost:            r9.refill();     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0210, code lost:            r6 = r3.blockLength();        r7 = r18.alloc().buffer(r6);     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x021c, code lost:            r0 = r3.read();     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0221, code lost:            if (r0 < 0) goto L196;     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0223, code lost:            r7.writeByte(r0);     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0227, code lost:            r17.currentState = io.netty.handler.codec.compression.Bzip2Decoder.State.INIT_BLOCK;        r0 = r3.checkCRC();        r17.streamCRC = ((r17.streamCRC << 1) | (r17.streamCRC >>> 31)) ^ r0;     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x023c, code lost:            r20.add(r7);     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x023f, code lost:            r0 = null;     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x0240, code lost:            if (0 == 0) goto L197;     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x0242, code lost:            r0.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x0245, code lost:            return;     */
    /* JADX WARN: Code restructure failed: missing block: B:152:?, code lost:            return;     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x0246, code lost:            r0 = th;     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x024b, code lost:            if (r7 != null) goto L145;     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x024d, code lost:            r7.release();     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x0250, code lost:            throw r0;     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x0248, code lost:            r0 = th;     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x01cd, code lost:            r14 = 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x01c2, code lost:            r8 = true;        r11 = true;     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x01a9, code lost:            r11 = true;     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x01d8, code lost:            r6 = -1;        r0.currentAlpha = 0;        r7 = 0;        r8 = false;        r12 = r12 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0197, code lost:            r11 = true;     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x025a, code lost:            throw new io.netty.handler.codec.compression.DecompressionException("incorrect selectors number");     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x0264, code lost:            throw new io.netty.handler.codec.compression.DecompressionException("incorrect alphabet size");     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x026e, code lost:            throw new io.netty.handler.codec.compression.DecompressionException("incorrect huffman groups number");     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x0278, code lost:            throw new io.netty.handler.codec.compression.DecompressionException("bad block header");     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0282, code lost:            throw new io.netty.handler.codec.compression.DecompressionException("block size is invalid");     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0090, code lost:            if (r0 != 3227993) goto L153;     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0095, code lost:            if (r4 != 2511705) goto L153;     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0097, code lost:            r17.blockCRC = r9.readInt();        r17.currentState = io.netty.handler.codec.compression.Bzip2Decoder.State.INIT_BLOCK_PARAMS;     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00a7, code lost:            if (r9.hasReadableBits(25) != false) goto L42;     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00a9, code lost:            return;     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00aa, code lost:            r0 = r9.readBoolean();        r14 = r9.readBits(24);        r17.blockDecompressor = new io.netty.handler.codec.compression.Bzip2BlockDecompressor(r17.blockSize, r17.blockCRC, r0, r14, r9);        r17.currentState = io.netty.handler.codec.compression.Bzip2Decoder.State.RECEIVE_HUFFMAN_USED_MAP;     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00c5, code lost:            r0 = r9.hasReadableBits(16);     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00c9, code lost:            if (r0 != false) goto L46;     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00cb, code lost:            return;     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00cc, code lost:            r17.blockDecompressor.huffmanInUse16 = r9.readBits(16);        r17.currentState = io.netty.handler.codec.compression.Bzip2Decoder.State.RECEIVE_HUFFMAN_USED_BITMAPS;     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00d8, code lost:            r0 = r17.blockDecompressor;        r3 = r0.huffmanInUse16;        r4 = java.lang.Integer.bitCount(r3);        r5 = r0.huffmanSymbolMap;     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00ea, code lost:            if (r9.hasReadableBits((r4 * 16) + 3) != false) goto L50;     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00ec, code lost:            return;     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00ed, code lost:            r6 = 0;     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00ee, code lost:            if (r4 <= 0) goto L63;     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00f0, code lost:            r8 = 0;     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00f1, code lost:            if (r8 >= r12) goto L180;     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00f8, code lost:            if (((32768 >>> r8) & r3) == 0) goto L181;     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00fa, code lost:            r14 = 0;        r15 = r8 << 4;     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00fd, code lost:            if (r14 >= r12) goto L182;     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0103, code lost:            if (r9.readBoolean() == false) goto L184;     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0105, code lost:            r5[r6] = (byte) r15;        r6 = r6 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x010c, code lost:            r14 = r14 + 1;        r15 = r15 + 1;        r12 = 16;     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0113, code lost:            r8 = r8 + 1;        r12 = 16;     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0118, code lost:            r8 = r6 + 1;        r0.huffmanEndOfBlockSymbol = r8;        r7 = r9.readBits(3);     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0121, code lost:            if (r7 < 2) goto L151;     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0123, code lost:            if (r7 > 6) goto L151;     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0125, code lost:            r8 = r6 + 2;     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0129, code lost:            if (r8 > 258) goto L149;     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x012b, code lost:            r17.huffmanStageDecoder = new io.netty.handler.codec.compression.Bzip2HuffmanStageDecoder(r9, r7, r8);        r17.currentState = io.netty.handler.codec.compression.Bzip2Decoder.State.RECEIVE_SELECTORS_NUMBER;     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x013c, code lost:            if (r9.hasReadableBits(15) != false) goto L72;     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x013e, code lost:            return;     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x013f, code lost:            r0 = r9.readBits(15);     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0143, code lost:            if (r0 < 1) goto L147;     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0147, code lost:            if (r0 > 18002) goto L147;     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0149, code lost:            r17.huffmanStageDecoder.selectors = new byte[r0];        r17.currentState = io.netty.handler.codec.compression.Bzip2Decoder.State.RECEIVE_SELECTORS;     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0153, code lost:            r0 = r17.huffmanStageDecoder;        r3 = r0.selectors;        r4 = r3.length;        r5 = r0.tableMTF;        r6 = r0.currentSelector;     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x015c, code lost:            if (r6 >= r4) goto L186;     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0162, code lost:            if (r9.hasReadableBits(6) != false) goto L83;     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0167, code lost:            r7 = 0;     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x016c, code lost:            if (r9.readBoolean() == false) goto L187;     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x016e, code lost:            r7 = r7 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0171, code lost:            r3[r6] = r5.indexToFront(r7);        r6 = r6 + 1;     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0164, code lost:            r0.currentSelector = r6;     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0166, code lost:            return;     */
    /* JADX WARN: Failed to find 'out' block for switch in B:7:0x0021. Please report as an issue. */
    @Override // io.netty.handler.codec.ByteToMessageDecoder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void decode(io.netty.channel.ChannelHandlerContext r18, io.netty.buffer.ByteBuf r19, java.util.List<java.lang.Object> r20) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 678
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: io.netty.handler.codec.compression.Bzip2Decoder.decode(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, java.util.List):void");
    }

    public boolean isClosed() {
        return this.currentState == State.EOF;
    }
}
