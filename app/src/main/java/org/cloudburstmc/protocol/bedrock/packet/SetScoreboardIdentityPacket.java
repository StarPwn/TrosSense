package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class SetScoreboardIdentityPacket implements BedrockPacket {
    private Action action;
    private final List<Entry> entries = new ObjectArrayList();

    /* loaded from: classes5.dex */
    public enum Action {
        ADD,
        REMOVE
    }

    public void setAction(Action action) {
        this.action = action;
    }

    protected boolean canEqual(Object other) {
        return other instanceof SetScoreboardIdentityPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SetScoreboardIdentityPacket)) {
            return false;
        }
        SetScoreboardIdentityPacket other = (SetScoreboardIdentityPacket) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$entries = this.entries;
        Object other$entries = other.entries;
        if (this$entries != null ? !this$entries.equals(other$entries) : other$entries != null) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        return this$action != null ? this$action.equals(other$action) : other$action == null;
    }

    public int hashCode() {
        Object $entries = this.entries;
        int result = (1 * 59) + ($entries == null ? 43 : $entries.hashCode());
        Object $action = this.action;
        return (result * 59) + ($action != null ? $action.hashCode() : 43);
    }

    public String toString() {
        return "SetScoreboardIdentityPacket(entries=" + this.entries + ", action=" + this.action + ")";
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public Action getAction() {
        return this.action;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public final PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.SET_SCOREBOARD_IDENTITY;
    }

    /* loaded from: classes5.dex */
    public static final class Entry {
        private final long scoreboardId;
        private final UUID uuid;

        public Entry(long scoreboardId, UUID uuid) {
            this.scoreboardId = scoreboardId;
            this.uuid = uuid;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry other = (Entry) o;
            if (getScoreboardId() != other.getScoreboardId()) {
                return false;
            }
            Object this$uuid = getUuid();
            Object other$uuid = other.getUuid();
            return this$uuid != null ? this$uuid.equals(other$uuid) : other$uuid == null;
        }

        public int hashCode() {
            long $scoreboardId = getScoreboardId();
            int result = (1 * 59) + ((int) (($scoreboardId >>> 32) ^ $scoreboardId));
            Object $uuid = getUuid();
            return (result * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        }

        public String toString() {
            return "SetScoreboardIdentityPacket.Entry(scoreboardId=" + getScoreboardId() + ", uuid=" + getUuid() + ")";
        }

        public long getScoreboardId() {
            return this.scoreboardId;
        }

        public UUID getUuid() {
            return this.uuid;
        }
    }
}
