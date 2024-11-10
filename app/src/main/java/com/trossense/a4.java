package com.trossense;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;

/* loaded from: classes3.dex */
public class a4 {
    private static final long g = dj.a(8386138544805663699L, -2054733332877044711L, MethodHandles.lookup().lookupClass()).a(276564239034540L);
    public static final ConcurrentLinkedQueue<BedrockPacket> a = new ConcurrentLinkedQueue<>();
    public static boolean b = false;
    public static ArrayList<Class<?>> c = new ArrayList<>();
    public static ArrayList<Class<?>> d = new ArrayList<>();
    private static boolean e = false;
    private static boolean f = false;

    public static void a() {
        f = true;
    }

    public static void a(Class<?>... clsArr) {
        c = new ArrayList<>(Arrays.asList(clsArr));
        e = false;
    }

    public static void b() {
        a.clear();
    }

    public static void b(Class<?>... clsArr) {
        d = new ArrayList<>(Arrays.asList(clsArr));
        e = true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0055, code lost:            if (r2 != false) goto L23;     */
    @com.trossense.bk(0)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(com.trossense.bb r8) {
        /*
            r7 = this;
            long r0 = com.trossense.a4.g
            r2 = 126411683643371(0x72f8837fbfeb, double:6.24556701211414E-310)
            long r0 = r0 ^ r2
            r2 = 76140020343464(0x453fbadda2a8, double:3.7618168325359E-310)
            long r0 = r0 ^ r2
            boolean r2 = com.trossense.a8.b()
            org.cloudburstmc.protocol.bedrock.packet.BedrockPacket r3 = r8.e()
            boolean r3 = r3 instanceof org.cloudburstmc.protocol.bedrock.packet.RequestNetworkSettingsPacket
            if (r3 == 0) goto L20
            java.util.concurrent.ConcurrentLinkedQueue<org.cloudburstmc.protocol.bedrock.packet.BedrockPacket> r8 = com.trossense.a4.a
            r8.clear()
            return
        L20:
            boolean r3 = com.trossense.a4.b
            if (r3 == 0) goto L7c
            boolean r3 = com.trossense.a4.f
            if (r3 != 0) goto L7c
            org.cloudburstmc.protocol.bedrock.packet.BedrockPacket r3 = r8.e()
            boolean r4 = com.trossense.a4.e
            if (r4 == 0) goto L57
            java.util.ArrayList<java.lang.Class<?>> r4 = com.trossense.a4.d
            java.util.Iterator r4 = r4.iterator()
        L36:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L55
            java.lang.Object r5 = r4.next()
            java.lang.Class r5 = (java.lang.Class) r5
            if (r2 != 0) goto L7c
            java.lang.Class r6 = r3.getClass()
            if (r5 != r6) goto L4b
            return
        L4b:
            java.util.concurrent.ConcurrentLinkedQueue<org.cloudburstmc.protocol.bedrock.packet.BedrockPacket> r5 = com.trossense.a4.a
            r5.add(r3)
            r8.c()
            if (r2 == 0) goto L36
        L55:
            if (r2 == 0) goto L7c
        L57:
            java.util.ArrayList<java.lang.Class<?>> r4 = com.trossense.a4.c
            java.util.Iterator r4 = r4.iterator()
        L5d:
            boolean r5 = r4.hasNext()
            if (r5 == 0) goto L7c
            java.lang.Object r5 = r4.next()
            java.lang.Class r5 = (java.lang.Class) r5
            if (r2 != 0) goto Laa
            java.lang.Class r6 = r3.getClass()
            if (r5 != r6) goto L7a
            java.util.concurrent.ConcurrentLinkedQueue<org.cloudburstmc.protocol.bedrock.packet.BedrockPacket> r0 = com.trossense.a4.a
            r0.add(r3)
            r8.c()
            return
        L7a:
            if (r2 == 0) goto L5d
        L7c:
            boolean r8 = com.trossense.a4.f
            if (r8 == 0) goto Laa
            com.trossense.clients.TrosSense r8 = com.trossense.clients.TrosSense.INSTANCE
            com.trossense.sdk.entity.type.EntityLocalPlayer r8 = r8.getLocalPlayer()
            if (r8 != 0) goto L89
            return
        L89:
            java.util.concurrent.ConcurrentLinkedQueue<org.cloudburstmc.protocol.bedrock.packet.BedrockPacket> r3 = com.trossense.a4.a
            java.util.Iterator r3 = r3.iterator()
        L8f:
            boolean r4 = r3.hasNext()
            if (r4 == 0) goto La2
            java.lang.Object r4 = r3.next()
            org.cloudburstmc.protocol.bedrock.packet.BedrockPacket r4 = (org.cloudburstmc.protocol.bedrock.packet.BedrockPacket) r4
            r8.d(r4, r0)
            if (r2 != 0) goto La5
            if (r2 == 0) goto L8f
        La2:
            r8 = 0
            com.trossense.a4.f = r8
        La5:
            java.util.concurrent.ConcurrentLinkedQueue<org.cloudburstmc.protocol.bedrock.packet.BedrockPacket> r8 = com.trossense.a4.a
            r8.clear()
        Laa:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.trossense.a4.a(com.trossense.bb):void");
    }
}
