package org.cloudburstmc.protocol.bedrock.packet;

import java.util.Optional;
import org.cloudburstmc.protocol.bedrock.data.ModalFormCancelReason;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ModalFormResponsePacket implements BedrockPacket {
    private Optional<ModalFormCancelReason> cancelReason;
    private String formData;
    private int formId;

    public void setCancelReason(Optional<ModalFormCancelReason> cancelReason) {
        this.cancelReason = cancelReason;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ModalFormResponsePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ModalFormResponsePacket)) {
            return false;
        }
        ModalFormResponsePacket other = (ModalFormResponsePacket) o;
        if (!other.canEqual(this) || this.formId != other.formId) {
            return false;
        }
        Object this$formData = this.formData;
        Object other$formData = other.formData;
        if (this$formData != null ? !this$formData.equals(other$formData) : other$formData != null) {
            return false;
        }
        Object this$cancelReason = this.cancelReason;
        Object other$cancelReason = other.cancelReason;
        return this$cancelReason != null ? this$cancelReason.equals(other$cancelReason) : other$cancelReason == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.formId;
        Object $formData = this.formData;
        int result2 = (result * 59) + ($formData == null ? 43 : $formData.hashCode());
        Object $cancelReason = this.cancelReason;
        return (result2 * 59) + ($cancelReason != null ? $cancelReason.hashCode() : 43);
    }

    public String toString() {
        return "ModalFormResponsePacket(formId=" + this.formId + ", formData=" + this.formData + ", cancelReason=" + this.cancelReason + ")";
    }

    public int getFormId() {
        return this.formId;
    }

    public String getFormData() {
        return this.formData;
    }

    public Optional<ModalFormCancelReason> getCancelReason() {
        return this.cancelReason;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.MODAL_FORM_RESPONSE;
    }
}
