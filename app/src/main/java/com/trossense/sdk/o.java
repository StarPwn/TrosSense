package com.trossense.sdk;

import com.trossense.dj;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.data.command.CommandOriginData;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes3.dex */
public class o implements BedrockPacket {
    private static final long g = dj.a(-8387593487444601651L, 6422433770905095621L, MethodHandles.lookup().lookupClass()).a(217629712994046L);
    private static final String[] h;
    private String a;
    private CommandOriginData b;
    private boolean c;
    private int d;
    private boolean e;
    private boolean f;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x0045. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[6];
        String str = "\u0007_J\u0000\u0019)-_\rC\\\n\u0007_T\u0004\u0018\u001f<D\u0011\u001f$e\u001aV\u0004\u000b\u001f0h\u0010O\f\u000b\u00021y\u001aS\u0014\u000f\u001f!{\u001eA\n\u000f\u0018}H\u0010O\f\u000b\u00021\u0016\u000b\u0007_K\u000f\u001e\t'E\u001eN\\";
        int length = "\u0007_J\u0000\u0019)-_\rC\\\n\u0007_T\u0004\u0018\u001f<D\u0011\u001f$e\u001aV\u0004\u000b\u001f0h\u0010O\f\u000b\u00021y\u001aS\u0014\u000f\u001f!{\u001eA\n\u000f\u0018}H\u0010O\f\u000b\u00021\u0016\u000b\u0007_K\u000f\u001e\t'E\u001eN\\".length();
        char c = 11;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 20;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c);
            boolean z = -1;
            while (true) {
                String a = a(i5, b(substring));
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
                        str = "\u0017OW\t\u000e\u000e$\nR\u0014\u0017OQ\u001e\u0017\u0011$U\u000b}\u0003\u0013\u001b,U+S\u0005\u001bA";
                        length = "\u0017OW\t\u000e\u000e$\nR\u0014\u0017OQ\u001e\u0017\u0011$U\u000b}\u0003\u0013\u001b,U+S\u0005\u001bA".length();
                        c = '\t';
                        i2 = -1;
                        i3 = i;
                        i5 = 4;
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
                i5 = 4;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c);
                z = false;
            }
            c = str.charAt(i4);
            i3 = i;
        }
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 63;
                    break;
                case 1:
                    i2 = 107;
                    break;
                case 2:
                    i2 = 54;
                    break;
                case 3:
                    i2 = 117;
                    break;
                case 4:
                    i2 = 126;
                    break;
                case 5:
                    i2 = 120;
                    break;
                default:
                    i2 = 65;
                    break;
            }
            cArr[i3] = (char) (c ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] b(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'A');
        }
        return charArray;
    }

    public String a() {
        return this.a;
    }

    public void a(int i) {
        this.d = i;
    }

    public void a(String str) {
        this.a = str;
    }

    public void a(CommandOriginData commandOriginData) {
        this.b = commandOriginData;
    }

    public void a(boolean z) {
        this.c = z;
    }

    protected boolean a(Object obj) {
        return obj instanceof o;
    }

    public CommandOriginData b() {
        return this.b;
    }

    public void b(boolean z) {
        this.e = z;
    }

    public void c(boolean z) {
        this.f = z;
    }

    public boolean c() {
        return this.c;
    }

    public int d() {
        return this.d;
    }

    public boolean e() {
        return this.e;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof o)) {
            return false;
        }
        o oVar = (o) obj;
        if (!oVar.a(this) || this.c != oVar.c || this.d != oVar.d || this.e != oVar.e || this.f != oVar.f) {
            return false;
        }
        String str = this.a;
        String str2 = oVar.a;
        if (str == null) {
            if (str2 != null) {
                return false;
            }
        } else if (!str.equals(str2)) {
            return false;
        }
        CommandOriginData commandOriginData = this.b;
        CommandOriginData commandOriginData2 = oVar.b;
        if (commandOriginData == null) {
            if (commandOriginData2 != null) {
                return false;
            }
        } else if (!commandOriginData.equals(commandOriginData2)) {
            return false;
        }
        return true;
    }

    public boolean f() {
        return this.f;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.COMMAND_REQUEST;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler bedrockPacketHandler) {
        return PacketSignal.UNHANDLED;
    }

    public int hashCode() {
        ad.f();
        int i = (((((((this.c ? 79 : 97) + 59) * 59) + this.d) * 59) + (this.e ? 79 : 97)) * 59) + (this.f ? 79 : 97);
        String str = this.a;
        int hashCode = (i * 59) + (str == null ? 43 : str.hashCode());
        CommandOriginData commandOriginData = this.b;
        int hashCode2 = (hashCode * 59) + (commandOriginData != null ? commandOriginData.hashCode() : 43);
        if (PointerHolder.s() == null) {
            ad.b(new String[1]);
        }
        return hashCode2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] strArr = h;
        return sb.append(strArr[2]).append(this.a).append(strArr[5]).append(this.b).append(strArr[3]).append(this.c).append(strArr[1]).append(this.d).append(strArr[0]).append(this.e).append(strArr[4]).append(this.f).append(")").toString();
    }
}
