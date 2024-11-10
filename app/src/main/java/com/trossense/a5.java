package com.trossense;

import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.data.AuthoritativeMovementMode;
import org.cloudburstmc.protocol.bedrock.packet.NetworkSettingsPacket;
import org.cloudburstmc.protocol.bedrock.packet.RequestNetworkSettingsPacket;

/* loaded from: classes3.dex */
public class a5 {
    private static final boolean a = true;
    private static final long f = dj.a(5394746880706285232L, 8847216055179308627L, MethodHandles.lookup().lookupClass()).a(68844088854255L);
    public static boolean b = true;
    public static boolean c = true;
    public static boolean d = false;
    public static long e = 0;

    @bk
    public void a(ba baVar) {
        if (baVar.e() instanceof NetworkSettingsPacket) {
            e = 0L;
        }
        if (baVar.e() instanceof com.trossense.sdk.ae) {
            com.trossense.sdk.ae aeVar = (com.trossense.sdk.ae) baVar.e();
            b = aeVar.getAuthoritativeMovementMode() != AuthoritativeMovementMode.CLIENT;
            c = aeVar.isServerAuthoritativeBlockBreaking();
            d = aeVar.isInventoriesServerAuthoritative();
        }
    }

    @bk
    public void a(bb bbVar) {
        if (bbVar.e() instanceof RequestNetworkSettingsPacket) {
            e = 0L;
        }
    }

    @bk
    public void a(bg bgVar) {
        e++;
    }
}
