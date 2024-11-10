package com.trossense;

import com.trossense.clients.TrosSense;
import com.trossense.sdk.entity.type.EntityLocalPlayer;
import com.trossense.sdk.math.Vec3f;
import java.lang.invoke.MethodHandles;
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket;

@cg(a = "NoFall", b = b.Player, c = "没有摔落伤害")
/* loaded from: classes3.dex */
public class b7 extends bm {
    private static final long k = dj.a(5206157499482068826L, 6812541741592702385L, MethodHandles.lookup().lookupClass()).a(144579401939280L);
    private boolean j;

    public b7(long j) {
        super((j ^ k) ^ 39137892579172L);
    }

    @bk
    public void a(bb bbVar) {
        EntityLocalPlayer localPlayer;
        if (!(bbVar.e() instanceof MovePlayerPacket) || (localPlayer = TrosSense.INSTANCE.getLocalPlayer()) == null) {
            return;
        }
        MovePlayerPacket movePlayerPacket = (MovePlayerPacket) bbVar.e();
        if (localPlayer.B() >= 3.3d) {
            this.j = true;
        }
        if (this.j && localPlayer.A()) {
            movePlayerPacket.setOnGround(false);
            if (!localPlayer.ad().isJumpDown) {
                localPlayer.b(new Vec3f(movePlayerPacket.getPosition().getX(), movePlayerPacket.getPosition().getY() + 0.11f, movePlayerPacket.getPosition().getZ()));
            }
            this.j = false;
        }
    }
}
