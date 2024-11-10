package io.netty.handler.codec.dns;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.util.internal.ObjectUtil;
import java.net.SocketAddress;

/* loaded from: classes4.dex */
abstract class DnsResponseDecoder<A extends SocketAddress> {
    private final DnsRecordDecoder recordDecoder;

    protected abstract DnsResponse newResponse(A a, A a2, int i, DnsOpCode dnsOpCode, DnsResponseCode dnsResponseCode) throws Exception;

    /* JADX INFO: Access modifiers changed from: package-private */
    public DnsResponseDecoder(DnsRecordDecoder recordDecoder) {
        this.recordDecoder = (DnsRecordDecoder) ObjectUtil.checkNotNull(recordDecoder, "recordDecoder");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final DnsResponse decode(A sender, A recipient, ByteBuf buffer) throws Exception {
        int id = buffer.readUnsignedShort();
        int flags = buffer.readUnsignedShort();
        if ((flags >> 15) == 0) {
            throw new CorruptedFrameException("not a response");
        }
        DnsResponse response = newResponse(sender, recipient, id, DnsOpCode.valueOf((byte) ((flags >> 11) & 15)), DnsResponseCode.valueOf((byte) (flags & 15)));
        response.setRecursionDesired(((flags >> 8) & 1) == 1);
        response.setAuthoritativeAnswer(((flags >> 10) & 1) == 1);
        response.setTruncated(((flags >> 9) & 1) == 1);
        response.setRecursionAvailable(((flags >> 7) & 1) == 1);
        response.setZ((flags >> 4) & 7);
        boolean success = false;
        try {
            int questionCount = buffer.readUnsignedShort();
            int answerCount = buffer.readUnsignedShort();
            int authorityRecordCount = buffer.readUnsignedShort();
            int additionalRecordCount = buffer.readUnsignedShort();
            decodeQuestions(response, buffer, questionCount);
            if (!decodeRecords(response, DnsSection.ANSWER, buffer, answerCount)) {
                success = true;
                return response;
            }
            if (!decodeRecords(response, DnsSection.AUTHORITY, buffer, authorityRecordCount)) {
                if (1 == 0) {
                    response.release();
                }
                return response;
            }
            decodeRecords(response, DnsSection.ADDITIONAL, buffer, additionalRecordCount);
            if (1 == 0) {
                response.release();
            }
            return response;
        } finally {
            if (!success) {
                response.release();
            }
        }
    }

    private void decodeQuestions(DnsResponse response, ByteBuf buf, int questionCount) throws Exception {
        for (int i = questionCount; i > 0; i--) {
            response.addRecord(DnsSection.QUESTION, (DnsRecord) this.recordDecoder.decodeQuestion(buf));
        }
    }

    private boolean decodeRecords(DnsResponse response, DnsSection section, ByteBuf buf, int count) throws Exception {
        for (int i = count; i > 0; i--) {
            DnsRecord r = this.recordDecoder.decodeRecord(buf);
            if (r == null) {
                return false;
            }
            response.addRecord(section, r);
        }
        return true;
    }
}
