package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.NpcRequestType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class NpcRequestPacket implements BedrockPacket {
    private int actionType;
    private String command;
    private NpcRequestType requestType;
    private long runtimeEntityId;
    private String sceneName;

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setRequestType(NpcRequestType requestType) {
        this.requestType = requestType;
    }

    public void setRuntimeEntityId(long runtimeEntityId) {
        this.runtimeEntityId = runtimeEntityId;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    protected boolean canEqual(Object other) {
        return other instanceof NpcRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NpcRequestPacket)) {
            return false;
        }
        NpcRequestPacket other = (NpcRequestPacket) o;
        if (!other.canEqual(this) || this.runtimeEntityId != other.runtimeEntityId || this.actionType != other.actionType) {
            return false;
        }
        Object this$requestType = this.requestType;
        Object other$requestType = other.requestType;
        if (this$requestType != null ? !this$requestType.equals(other$requestType) : other$requestType != null) {
            return false;
        }
        Object this$command = this.command;
        Object other$command = other.command;
        if (this$command != null ? !this$command.equals(other$command) : other$command != null) {
            return false;
        }
        Object this$sceneName = this.sceneName;
        Object other$sceneName = other.sceneName;
        return this$sceneName != null ? this$sceneName.equals(other$sceneName) : other$sceneName == null;
    }

    public int hashCode() {
        long $runtimeEntityId = this.runtimeEntityId;
        int result = (1 * 59) + ((int) (($runtimeEntityId >>> 32) ^ $runtimeEntityId));
        int result2 = (result * 59) + this.actionType;
        Object $requestType = this.requestType;
        int result3 = (result2 * 59) + ($requestType == null ? 43 : $requestType.hashCode());
        Object $command = this.command;
        int result4 = (result3 * 59) + ($command == null ? 43 : $command.hashCode());
        Object $sceneName = this.sceneName;
        return (result4 * 59) + ($sceneName != null ? $sceneName.hashCode() : 43);
    }

    public String toString() {
        return "NpcRequestPacket(runtimeEntityId=" + this.runtimeEntityId + ", requestType=" + this.requestType + ", command=" + this.command + ", actionType=" + this.actionType + ", sceneName=" + this.sceneName + ")";
    }

    public long getRuntimeEntityId() {
        return this.runtimeEntityId;
    }

    public NpcRequestType getRequestType() {
        return this.requestType;
    }

    public String getCommand() {
        return this.command;
    }

    public int getActionType() {
        return this.actionType;
    }

    public String getSceneName() {
        return this.sceneName;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.NPC_REQUEST;
    }
}
