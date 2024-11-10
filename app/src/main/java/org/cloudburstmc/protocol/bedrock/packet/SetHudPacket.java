package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import org.cloudburstmc.protocol.bedrock.data.HudElement;
import org.cloudburstmc.protocol.bedrock.data.HudVisibility;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetHudPacket implements BedrockPacket {
    private final Set<HudElement> elements = new ObjectOpenHashSet();
    private HudVisibility visibility;

    public void setVisibility(HudVisibility visibility) {
        this.visibility = visibility;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetHudPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetHudPacket)) {
            return false;
        }
        SetHudPacket other = (SetHudPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$elements = this.elements;
        Object other$elements = other.elements;
        if (this$elements != null ? !this$elements.equals(other$elements) : other$elements != null) {
            return false;
        }
        Object this$visibility = this.visibility;
        Object other$visibility = other.visibility;
        return this$visibility != null ? this$visibility.equals(other$visibility) : other$visibility == null;
    }

    public int hashCode() {
        Object $elements = this.elements;
        int result = (1 * 59) + ($elements == null ? 43 : $elements.hashCode());
        Object $visibility = this.visibility;
        return (result * 59) + ($visibility != null ? $visibility.hashCode() : 43);
    }

    public String toString() {
        return "SetHudPacket(elements=" + this.elements + ", visibility=" + this.visibility + ")";
    }

    public Set<HudElement> getElements() {
        return this.elements;
    }

    public HudVisibility getVisibility() {
        return this.visibility;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_HUD;
    }
}
