package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AddBehaviorTreePacket implements BedrockPacket {
    private String behaviorTreeJson;

    public void setBehaviorTreeJson(String behaviorTreeJson) {
        this.behaviorTreeJson = behaviorTreeJson;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AddBehaviorTreePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AddBehaviorTreePacket)) {
            return false;
        }
        AddBehaviorTreePacket other = (AddBehaviorTreePacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$behaviorTreeJson = this.behaviorTreeJson;
        Object other$behaviorTreeJson = other.behaviorTreeJson;
        return this$behaviorTreeJson != null ? this$behaviorTreeJson.equals(other$behaviorTreeJson) : other$behaviorTreeJson == null;
    }

    public int hashCode() {
        Object $behaviorTreeJson = this.behaviorTreeJson;
        int result = (1 * 59) + ($behaviorTreeJson == null ? 43 : $behaviorTreeJson.hashCode());
        return result;
    }

    public String toString() {
        return "AddBehaviorTreePacket(behaviorTreeJson=" + this.behaviorTreeJson + ")";
    }

    public String getBehaviorTreeJson() {
        return this.behaviorTreeJson;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.ADD_BEHAVIOR_TREE;
    }
}
