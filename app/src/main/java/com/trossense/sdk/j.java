package com.trossense.sdk;

import com.trossense.dj;
import io.netty.util.internal.StringUtil;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes3.dex */
public class j implements BedrockPacket {
    private static final long b = dj.a(2312478040809509342L, 1706817002140953616L, MethodHandles.lookup().lookupClass()).a(227589178474146L);
    private static final String c = a(95, a("\u000b\u0002st\u007f\u0012?\u001b\u0006t|F\u00011#\bi:s\u000e&:\u0004xa+"));
    private List<k> a = new ArrayList();

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 23;
                    break;
                case 1:
                    i2 = 50;
                    break;
                case 2:
                    i2 = 66;
                    break;
                case 3:
                    i2 = 77;
                    break;
                case 4:
                    i2 = 73;
                    break;
                case 5:
                    i2 = 63;
                    break;
                default:
                    i2 = 13;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ StringUtil.CARRIAGE_RETURN);
        }
        return charArray;
    }

    public List<k> a() {
        return this.a;
    }

    public void a(List<k> list) {
        this.a = list;
    }

    protected boolean a(Object obj) {
        return obj instanceof j;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof j)) {
            return false;
        }
        j jVar = (j) obj;
        if (!jVar.a(this)) {
            return false;
        }
        List<k> list = this.a;
        List<k> list2 = jVar.a;
        if (list == null) {
            if (list2 != null) {
                return false;
            }
        } else if (!list.equals(list2)) {
            return false;
        }
        return true;
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
        List<k> list = this.a;
        return 59 + (list == null ? 43 : list.hashCode());
    }

    public String toString() {
        return c + this.a + ")";
    }
}
