package org.cloudburstmc.protocol.bedrock.packet;

import java.util.ArrayList;
import java.util.List;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SubClientLoginPacket implements BedrockPacket {
    private final List<String> chain = new ArrayList();
    private String extra;

    public void setExtra(String extra) {
        this.extra = extra;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SubClientLoginPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SubClientLoginPacket)) {
            return false;
        }
        SubClientLoginPacket other = (SubClientLoginPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$chain = this.chain;
        Object other$chain = other.chain;
        if (this$chain != null ? !this$chain.equals(other$chain) : other$chain != null) {
            return false;
        }
        Object this$extra = this.extra;
        Object other$extra = other.extra;
        return this$extra != null ? this$extra.equals(other$extra) : other$extra == null;
    }

    public int hashCode() {
        Object $chain = this.chain;
        int result = (1 * 59) + ($chain == null ? 43 : $chain.hashCode());
        Object $extra = this.extra;
        return (result * 59) + ($extra != null ? $extra.hashCode() : 43);
    }

    public String toString() {
        return "SubClientLoginPacket(chain=" + this.chain + ", extra=" + this.extra + ")";
    }

    public List<String> getChain() {
        return this.chain;
    }

    public String getExtra() {
        return this.extra;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SUB_CLIENT_LOGIN;
    }
}
