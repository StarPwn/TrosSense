package com.trossense.sdk;

import com.trossense.dj;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes3.dex */
public class l implements BedrockPacket {
    private static final long b = dj.a(3074776218922408609L, 6295712619376995800L, MethodHandles.lookup().lookupClass()).a(173486528102293L);
    private static final String c = a(83, b("S\"44 \u007f3W4/?\u0011m5v\"4y+\u007f9sz"));
    private String a;

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 78;
                    break;
                case 1:
                    i2 = 20;
                    break;
                case 2:
                    i2 = 19;
                    break;
                case 3:
                    i2 = 2;
                    break;
                case 4:
                    i2 = 18;
                    break;
                case 5:
                    i2 = 95;
                    break;
                default:
                    i2 = 5;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 5);
        }
        return charArray;
    }

    public String a() {
        return this.a;
    }

    public void a(String str) {
        this.a = str;
    }

    protected boolean a(Object obj) {
        return obj instanceof l;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof l)) {
            return false;
        }
        l lVar = (l) obj;
        if (!lVar.a(this)) {
            return false;
        }
        String str = this.a;
        String str2 = lVar.a;
        if (str == null) {
            if (str2 != null) {
                return false;
            }
        } else if (!str.equals(str2)) {
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
        String str = this.a;
        return 59 + (str == null ? 43 : str.hashCode());
    }

    public String toString() {
        return c + this.a + ")";
    }
}
