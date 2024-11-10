package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ExplodePacket implements BedrockPacket {
    private Vector3f position;
    private float radius;
    private final List<Vector3i> records = new ObjectArrayList();

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ExplodePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExplodePacket)) {
            return false;
        }
        ExplodePacket other = (ExplodePacket) o;
        if (!other.canEqual(this) || Float.compare(this.radius, other.radius) != 0) {
            return false;
        }
        Object this$records = this.records;
        Object other$records = other.records;
        if (this$records != null ? !this$records.equals(other$records) : other$records != null) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        return this$position != null ? this$position.equals(other$position) : other$position == null;
    }

    public int hashCode() {
        int result = (1 * 59) + Float.floatToIntBits(this.radius);
        Object $records = this.records;
        int result2 = (result * 59) + ($records == null ? 43 : $records.hashCode());
        Object $position = this.position;
        return (result2 * 59) + ($position != null ? $position.hashCode() : 43);
    }

    public String toString() {
        return "ExplodePacket(records=" + this.records + ", position=" + this.position + ", radius=" + this.radius + ")";
    }

    public List<Vector3i> getRecords() {
        return this.records;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public float getRadius() {
        return this.radius;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.EXPLODE;
    }
}
