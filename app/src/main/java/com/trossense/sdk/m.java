package com.trossense.sdk;

import com.trossense.dj;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketType;
import org.cloudburstmc.protocol.common.PacketSignal;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

/* loaded from: classes3.dex */
public class m implements BedrockPacket {
    private static String[] c;
    private static final long d = dj.a(-5730650967872215487L, 9131473280590596916L, MethodHandles.lookup().lookupClass()).a(170014735483392L);
    private static final String[] e;
    private byte[] a;
    private long b;

    static {
        String[] strArr = new String[2];
        int length = "\u000eaR+pAk=se/;uk*y=\br8m(tXnc".length();
        b(new String[3]);
        int i = 0;
        char c2 = 17;
        int i2 = -1;
        while (true) {
            int i3 = i2 + 1;
            int i4 = c2 + i3;
            int i5 = i + 1;
            strArr[i] = a(126, a("\u000eaR+pAk=se/;uk*y=\br8m(tXnc".substring(i3, i4)));
            if (i4 >= length) {
                e = strArr;
                return;
            } else {
                i2 = i4;
                c2 = "\u000eaR+pAk=se/;uk*y=\br8m(tXnc".charAt(i4);
                i = i5;
            }
        }
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 32;
                    break;
                case 1:
                    i2 = 102;
                    break;
                case 2:
                    i2 = 126;
                    break;
                case 3:
                    i2 = 37;
                    break;
                case 4:
                    i2 = 109;
                    break;
                case 5:
                    i2 = 111;
                    break;
                default:
                    i2 = 116;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 't');
        }
        return charArray;
    }

    public static void b(String[] strArr) {
        c = strArr;
    }

    public static String[] d() {
        return c;
    }

    public MessageUnpacker a() {
        return MessagePack.newDefaultUnpacker(this.a);
    }

    public void a(long j) {
        this.b = j;
    }

    public void a(byte[] bArr) {
        this.a = bArr;
    }

    protected boolean a(Object obj) {
        return obj instanceof m;
    }

    public byte[] b() {
        return this.a;
    }

    public long c() {
        return this.b;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof m)) {
            return false;
        }
        m mVar = (m) obj;
        if (mVar.a(this) && this.b == mVar.b) {
            return Arrays.equals(this.a, mVar.a);
        }
        return false;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.UNKNOWN;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler bedrockPacketHandler) {
        return PacketSignal.UNHANDLED;
    }

    public int hashCode() {
        long j = this.b;
        return ((((int) (j ^ (j >>> 32))) + 59) * 59) + Arrays.hashCode(this.a);
    }

    public String toString() {
        String arrays = Arrays.toString(this.a);
        StringBuilder sb = new StringBuilder();
        String[] strArr = e;
        return sb.append(strArr[0]).append(arrays).append(strArr[1]).append(this.b).append(")").toString();
    }
}
