package com.trossense;

import com.trossense.clients.TrosSense;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.InventoryTransactionPacket;

/* loaded from: classes3.dex */
public class cn {
    private static final long a = dj.a(-8440330748892997654L, -389707653923780020L, MethodHandles.lookup().lookupClass()).a(166158019400437L);

    @bk
    public void a(bb bbVar) {
        if (bbVar.e() instanceof InventoryTransactionPacket) {
            ca caVar = (ca) TrosSense.INSTANCE.f().a(ca.class);
            if (caVar.l()) {
                InventoryTransactionPacket inventoryTransactionPacket = (InventoryTransactionPacket) bbVar.e();
                if (caVar.j.contains(inventoryTransactionPacket.getBlockPosition())) {
                    inventoryTransactionPacket.setClickPosition(Vector3f.from(Math.random(), Math.random(), Math.random()));
                }
            }
        }
    }
}
