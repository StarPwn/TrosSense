package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.bedrock.data.ee.AgentActionType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class AgentActionEventPacket implements BedrockPacket {
    private AgentActionType actionType;
    private String requestId;
    private String responseJson;

    public void setActionType(AgentActionType actionType) {
        this.actionType = actionType;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    protected boolean canEqual(Object other) {
        return other instanceof AgentActionEventPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AgentActionEventPacket)) {
            return false;
        }
        AgentActionEventPacket other = (AgentActionEventPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$requestId = this.requestId;
        Object other$requestId = other.requestId;
        if (this$requestId != null ? !this$requestId.equals(other$requestId) : other$requestId != null) {
            return false;
        }
        Object this$actionType = this.actionType;
        Object other$actionType = other.actionType;
        if (this$actionType != null ? !this$actionType.equals(other$actionType) : other$actionType != null) {
            return false;
        }
        Object this$responseJson = this.responseJson;
        Object other$responseJson = other.responseJson;
        return this$responseJson != null ? this$responseJson.equals(other$responseJson) : other$responseJson == null;
    }

    public int hashCode() {
        Object $requestId = this.requestId;
        int result = (1 * 59) + ($requestId == null ? 43 : $requestId.hashCode());
        Object $actionType = this.actionType;
        int result2 = (result * 59) + ($actionType == null ? 43 : $actionType.hashCode());
        Object $responseJson = this.responseJson;
        return (result2 * 59) + ($responseJson != null ? $responseJson.hashCode() : 43);
    }

    public String toString() {
        return "AgentActionEventPacket(requestId=" + this.requestId + ", actionType=" + this.actionType + ", responseJson=" + this.responseJson + ")";
    }

    public String getRequestId() {
        return this.requestId;
    }

    public AgentActionType getActionType() {
        return this.actionType;
    }

    public String getResponseJson() {
        return this.responseJson;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.AGENT_ACTION_EVENT;
    }
}
