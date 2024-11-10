package com.trossense.sdk;

import com.trossense.dj;
import io.netty.handler.codec.http.HttpConstants;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes3.dex */
public class p implements BedrockPacket {
    private static final long g = dj.a(-6944167603307250198L, 6827198442240556406L, MethodHandles.lookup().lookupClass()).a(81060109677639L);
    private static final String[] h;
    private byte a;
    private ContainerType b;
    private Vector3i c;
    private long d = -1;
    private boolean e;
    private boolean f;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[6];
        String str = "\u000f\u0018l\u0014\u0018\tMF}w\u000e\u0018\fAj\\$\u0010\u000f\u0018{\u0016\u001e\u001bSsWj\u0013\u0005\u0011WM\u0005\u001em]m\u001f\u0010\u000b]`Ww\u000e\u0010\u0011VFJV\n\u0014\u0016hB[r\u001f\u0005PQG\u0005\u0007\u000f\u0018m\u0003\u0001\u001d\u0005";
        int length = "\u000f\u0018l\u0014\u0018\tMF}w\u000e\u0018\fAj\\$\u0010\u000f\u0018{\u0016\u001e\u001bSsWj\u0013\u0005\u0011WM\u0005\u001em]m\u001f\u0010\u000b]`Ww\u000e\u0010\u0011VFJV\n\u0014\u0016hB[r\u001f\u0005PQG\u0005\u0007\u000f\u0018m\u0003\u0001\u001d\u0005".length();
        char c = 17;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 50;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a;
                        i4 = i6 + c;
                        if (i4 < length) {
                            break;
                        }
                        str = "W@)CZe\u0018\u000f\u0012 \u001f\tW@$Z]R\u0001J]";
                        length = "W@)CZe\u0018\u000f\u0012 \u001f\tW@$Z]R\u0001J]".length();
                        c = 11;
                        i2 = -1;
                        i3 = i;
                        i5 = 106;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c);
                        z = false;
                        break;
                }
                strArr[i3] = a;
                i2 = i6 + c;
                if (i2 >= length) {
                    h = strArr;
                    return;
                }
                c = str.charAt(i2);
                i3 = i;
                i5 = 106;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    private static String a(int i, char[] cArr) {
        int length = cArr.length;
        for (int i2 = 0; length > i2; i2++) {
            char c = cArr[i2];
            int i3 = 10;
            switch (i2 % 7) {
                case 0:
                    i3 = 17;
                    break;
                case 2:
                    i3 = 43;
                    break;
                case 3:
                    i3 = 72;
                    break;
                case 4:
                    i3 = 67;
                    break;
                case 5:
                    i3 = 74;
                    break;
            }
            cArr[i2] = (char) (c ^ (i ^ i3));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ '\n');
        }
        return charArray;
    }

    public byte a() {
        return this.a;
    }

    public void a(byte b) {
        this.a = b;
    }

    public void a(long j) {
        this.d = j;
    }

    public void a(Vector3i vector3i) {
        this.c = vector3i;
    }

    public void a(ContainerType containerType) {
        this.b = containerType;
    }

    public void a(boolean z) {
        this.e = z;
    }

    protected boolean a(Object obj) {
        return obj instanceof p;
    }

    public ContainerType b() {
        return this.b;
    }

    public void b(boolean z) {
        this.f = z;
    }

    public Vector3i c() {
        return this.c;
    }

    public long d() {
        return this.d;
    }

    public boolean e() {
        return this.e;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof p)) {
            return false;
        }
        p pVar = (p) obj;
        if (!pVar.a(this) || this.a != pVar.a || this.d != pVar.d || this.e != pVar.e || this.f != pVar.f) {
            return false;
        }
        ContainerType containerType = this.b;
        ContainerType containerType2 = pVar.b;
        if (containerType == null) {
            if (containerType2 != null) {
                return false;
            }
        } else if (!containerType.equals(containerType2)) {
            return false;
        }
        Vector3i vector3i = this.c;
        Vector3i vector3i2 = pVar.c;
        if (vector3i == null) {
            if (vector3i2 != null) {
                return false;
            }
        } else if (!vector3i.equals(vector3i2)) {
            return false;
        }
        return true;
    }

    public boolean f() {
        return this.f;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CONTAINER_OPEN;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler bedrockPacketHandler) {
        return PacketSignal.UNHANDLED;
    }

    public int hashCode() {
        String[] f = ad.f();
        int i = this.a + HttpConstants.SEMICOLON;
        long j = this.d;
        int i2 = ((((i * 59) + ((int) (j ^ (j >>> 32)))) * 59) + (this.e ? 79 : 97)) * 59;
        int i3 = this.f ? 79 : 97;
        ContainerType containerType = this.b;
        int hashCode = ((i2 + i3) * 59) + (containerType == null ? 43 : containerType.hashCode());
        Vector3i vector3i = this.c;
        int hashCode2 = (hashCode * 59) + (vector3i != null ? vector3i.hashCode() : 43);
        if (f != null) {
            PointerHolder.b(new int[1]);
        }
        return hashCode2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] strArr = h;
        return sb.append(strArr[2]).append((int) this.a).append(strArr[3]).append(this.b).append(strArr[1]).append(this.c).append(strArr[0]).append(this.d).append(strArr[4]).append(this.e).append(strArr[5]).append(this.f).append(")").toString();
    }
}
