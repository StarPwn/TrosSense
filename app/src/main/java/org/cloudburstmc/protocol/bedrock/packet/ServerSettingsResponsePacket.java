package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ServerSettingsResponsePacket implements BedrockPacket {
    private String formData;
    private int formId;

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ServerSettingsResponsePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ServerSettingsResponsePacket)) {
            return false;
        }
        ServerSettingsResponsePacket other = (ServerSettingsResponsePacket) o;
        if (!other.canEqual(this) || this.formId != other.formId) {
            return false;
        }
        Object this$formData = this.formData;
        Object other$formData = other.formData;
        return this$formData != null ? this$formData.equals(other$formData) : other$formData == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.formId;
        Object $formData = this.formData;
        return (result * 59) + ($formData == null ? 43 : $formData.hashCode());
    }

    public String toString() {
        return "ServerSettingsResponsePacket(formId=" + this.formId + ", formData=" + this.formData + ")";
    }

    public int getFormId() {
        return this.formId;
    }

    public String getFormData() {
        return this.formData;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SERVER_SETTINGS_RESPONSE;
    }
}
