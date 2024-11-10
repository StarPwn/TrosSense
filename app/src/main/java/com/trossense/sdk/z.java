package com.trossense.sdk;

import com.trossense.dj;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.codec.v575.BedrockCodecHelper_v575;
import org.cloudburstmc.protocol.bedrock.data.Ability;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.TextProcessingEventOrigin;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestActionType;
import org.cloudburstmc.protocol.common.util.TypeMap;
import org.cloudburstmc.protocol.common.util.VarInts;

/* loaded from: classes3.dex */
public class z extends BedrockCodecHelper_v575 {
    private static final long a = dj.a(2017105530235790906L, -747067247791877423L, MethodHandles.lookup().lookupClass()).a(32208693560326L);
    private static PointerHolder[] b;
    private static final String[] c;

    /* JADX WARN: Failed to find 'out' block for switch in B:4:0x004b. Please report as an issue. */
    static {
        int i;
        int i2;
        String[] strArr = new String[4];
        String str = "+z@E_KA\n\"\u0001\u0000H^CN`ET\u001cXK\u001a\"\u0010\u0003k^E_ME\bv\nSTVA\u0002f";
        int length = "+z@E_KA\n\"\u0001\u0000H^CN`ET\u001cXK\u001a\"\u0010\u0003k^E_ME\bv\nSTVA\u0002f".length();
        b(new PointerHolder[5]);
        char c2 = 23;
        int i3 = 0;
        int i4 = -1;
        while (true) {
            int i5 = 94;
            int i6 = i4 + 1;
            String substring = str.substring(i6, i6 + c2);
            boolean z = -1;
            while (true) {
                String a2 = a(i5, a(substring));
                i = i3 + 1;
                switch (z) {
                    case false:
                        break;
                    default:
                        strArr[i3] = a2;
                        i4 = i6 + c2;
                        if (i4 < length) {
                            break;
                        }
                        str = "\u001eItgu\u007f!?H5w|{ekNa`t:t8Bg%}{u*,\u0002Sph9or.U5axn`kOtv9a|kUpd}{c'B5g`nd8\u0007y`\u007fn\u000b0Z";
                        length = "\u001eItgu\u007f!?H5w|{ekNa`t:t8Bg%}{u*,\u0002Sph9or.U5axn`kOtv9a|kUpd}{c'B5g`nd8\u0007y`\u007fn\u000b0Z".length();
                        c2 = 29;
                        i2 = -1;
                        i3 = i;
                        i5 = 123;
                        i6 = i2 + 1;
                        substring = str.substring(i6, i6 + c2);
                        z = false;
                        break;
                }
                strArr[i3] = a2;
                i2 = i6 + c2;
                if (i2 >= length) {
                    c = strArr;
                    return;
                }
                c2 = str.charAt(i2);
                i3 = i;
                i5 = 123;
                i6 = i2 + 1;
                substring = str.substring(i6, i6 + c2);
                z = false;
            }
            c2 = str.charAt(i4);
            i3 = i;
        }
    }

    public z(EntityDataTypeMap entityDataTypeMap, TypeMap<Class<?>> typeMap, TypeMap<ItemStackRequestActionType> typeMap2, TypeMap<ContainerSlotType> typeMap3, TypeMap<Ability> typeMap4, TypeMap<TextProcessingEventOrigin> typeMap5) {
        super(entityDataTypeMap, typeMap, typeMap2, typeMap3, typeMap4, typeMap5);
    }

    private static String a(int i, char[] cArr) {
        int i2;
        int length = cArr.length;
        for (int i3 = 0; length > i3; i3++) {
            char c2 = cArr[i3];
            switch (i3 % 7) {
                case 0:
                    i2 = 48;
                    break;
                case 1:
                    i2 = 92;
                    break;
                case 2:
                    i2 = 110;
                    break;
                case 3:
                    i2 = 126;
                    break;
                case 4:
                    i2 = 98;
                    break;
                case 5:
                    i2 = 97;
                    break;
                default:
                    i2 = 122;
                    break;
            }
            cArr[i3] = (char) (c2 ^ (i2 ^ i));
        }
        return new String(cArr).intern();
    }

    private static char[] a(String str) {
        char[] charArray = str.toCharArray();
        if (charArray.length < 2) {
            charArray[0] = (char) (charArray[0] ^ 'z');
        }
        return charArray;
    }

    public static void b(PointerHolder[] pointerHolderArr) {
        b = pointerHolderArr;
    }

    public static PointerHolder[] b() {
        return b;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00b0 A[Catch: all -> 0x0141, TryCatch #1 {all -> 0x0141, blocks: (B:21:0x0051, B:23:0x0057, B:24:0x005b, B:25:0x0087, B:29:0x0092, B:33:0x009c, B:36:0x00ad, B:38:0x00b0, B:44:0x00c2, B:46:0x00d1, B:34:0x00a6, B:65:0x0060, B:67:0x0066, B:68:0x006b, B:69:0x0085), top: B:20:0x0051, outer: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00a6 A[EDGE_INSN: B:63:0x00a6->B:34:0x00a6 BREAK  A[LOOP:0: B:26:0x008d->B:31:0x00a2], SYNTHETIC] */
    @Override // org.cloudburstmc.protocol.bedrock.codec.v431.BedrockCodecHelper_v431, org.cloudburstmc.protocol.bedrock.codec.v407.BedrockCodecHelper_v407, org.cloudburstmc.protocol.bedrock.codec.v291.BedrockCodecHelper_v291, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.cloudburstmc.protocol.bedrock.data.inventory.ItemData readNetItem(io.netty.buffer.ByteBuf r20) {
        /*
            Method dump skipped, instructions count: 357
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.sdk.z.readNetItem(io.netty.buffer.ByteBuf):org.cloudburstmc.protocol.bedrock.data.inventory.ItemData");
    }

    @Override // org.cloudburstmc.protocol.bedrock.codec.BaseBedrockCodecHelper, org.cloudburstmc.protocol.bedrock.codec.BedrockCodecHelper
    public void writeString(ByteBuf byteBuf, String str) {
        if (str == null) {
            str = "";
        }
        VarInts.writeUnsignedInt(byteBuf, ByteBufUtil.utf8Bytes(str));
        byteBuf.writeCharSequence(str, StandardCharsets.UTF_8);
    }
}
