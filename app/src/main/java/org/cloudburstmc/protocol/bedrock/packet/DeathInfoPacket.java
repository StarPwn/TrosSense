package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class DeathInfoPacket implements BedrockPacket {
    private String causeAttackName;
    private final List<String> messageList = new ObjectArrayList();

    public void setCauseAttackName(String causeAttackName) {
        this.causeAttackName = causeAttackName;
    }

    protected boolean canEqual(Object other) {
        return other instanceof DeathInfoPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DeathInfoPacket)) {
            return false;
        }
        DeathInfoPacket other = (DeathInfoPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$causeAttackName = this.causeAttackName;
        Object other$causeAttackName = other.causeAttackName;
        if (this$causeAttackName != null ? !this$causeAttackName.equals(other$causeAttackName) : other$causeAttackName != null) {
            return false;
        }
        Object this$messageList = this.messageList;
        Object other$messageList = other.messageList;
        return this$messageList != null ? this$messageList.equals(other$messageList) : other$messageList == null;
    }

    public int hashCode() {
        Object $causeAttackName = this.causeAttackName;
        int result = (1 * 59) + ($causeAttackName == null ? 43 : $causeAttackName.hashCode());
        Object $messageList = this.messageList;
        return (result * 59) + ($messageList != null ? $messageList.hashCode() : 43);
    }

    public String toString() {
        return "DeathInfoPacket(causeAttackName=" + this.causeAttackName + ", messageList=" + this.messageList + ")";
    }

    public String getCauseAttackName() {
        return this.causeAttackName;
    }

    public List<String> getMessageList() {
        return this.messageList;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.DEATH_INFO;
    }
}
