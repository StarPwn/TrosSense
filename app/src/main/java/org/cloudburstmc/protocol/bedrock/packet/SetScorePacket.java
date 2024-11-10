package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.bedrock.data.ScoreInfo;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetScorePacket implements BedrockPacket {
    private Action action;
    private List<ScoreInfo> infos = new ObjectArrayList();

    /* loaded from: classes5.dex */
    public enum Action {
        SET,
        REMOVE
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setInfos(List<ScoreInfo> infos) {
        this.infos = infos;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetScorePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetScorePacket)) {
            return false;
        }
        SetScorePacket other = (SetScorePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        if (this$action != null ? !this$action.equals(other$action) : other$action != null) {
            return false;
        }
        Object this$infos = this.infos;
        Object other$infos = other.infos;
        return this$infos != null ? this$infos.equals(other$infos) : other$infos == null;
    }

    public int hashCode() {
        Object $action = this.action;
        int result = (1 * 59) + ($action == null ? 43 : $action.hashCode());
        Object $infos = this.infos;
        return (result * 59) + ($infos != null ? $infos.hashCode() : 43);
    }

    public String toString() {
        return "SetScorePacket(action=" + this.action + ", infos=" + this.infos + ")";
    }

    public Action getAction() {
        return this.action;
    }

    public List<ScoreInfo> getInfos() {
        return this.infos;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_SCORE;
    }
}
