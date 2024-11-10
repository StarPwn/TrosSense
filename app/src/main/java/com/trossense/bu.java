package com.trossense;

import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.packet.DisconnectPacket;

@cg(a = "AntiKick", b = b.Misc, c = "防踢")
/* loaded from: classes3.dex */
public class bu extends bm {
    private static final long j = dj.a(-5736373087414846786L, -7682033640105690351L, MethodHandles.lookup().lookupClass()).a(194001412566092L);

    public bu(short s, int i, char c) {
        super(((((c << 48) >>> 48) | ((s << 48) | ((i << 32) >>> 16))) ^ j) ^ 107793989104757L);
    }

    @bk
    public void a(ba baVar) {
        if (baVar.e() instanceof DisconnectPacket) {
            baVar.c();
        }
    }
}
