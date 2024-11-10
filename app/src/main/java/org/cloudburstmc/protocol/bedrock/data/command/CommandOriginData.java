package org.cloudburstmc.protocol.bedrock.data.command;

import java.util.UUID;

/* loaded from: classes5.dex */
public final class CommandOriginData {
    private final long event;
    private final CommandOriginType origin;
    private final String requestId;
    private final UUID uuid;

    public CommandOriginData(CommandOriginType origin, UUID uuid, String requestId, long event) {
        this.origin = origin;
        this.uuid = uuid;
        this.requestId = requestId;
        this.event = event;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandOriginData)) {
            return false;
        }
        CommandOriginData other = (CommandOriginData) o;
        if (getEvent() != other.getEvent()) {
            return false;
        }
        Object this$origin = getOrigin();
        Object other$origin = other.getOrigin();
        if (this$origin != null ? !this$origin.equals(other$origin) : other$origin != null) {
            return false;
        }
        Object this$uuid = getUuid();
        Object other$uuid = other.getUuid();
        if (this$uuid != null ? !this$uuid.equals(other$uuid) : other$uuid != null) {
            return false;
        }
        Object this$requestId = getRequestId();
        Object other$requestId = other.getRequestId();
        return this$requestId != null ? this$requestId.equals(other$requestId) : other$requestId == null;
    }

    public int hashCode() {
        long $event = getEvent();
        int result = (1 * 59) + ((int) (($event >>> 32) ^ $event));
        Object $origin = getOrigin();
        int result2 = (result * 59) + ($origin == null ? 43 : $origin.hashCode());
        Object $uuid = getUuid();
        int result3 = (result2 * 59) + ($uuid == null ? 43 : $uuid.hashCode());
        Object $requestId = getRequestId();
        return (result3 * 59) + ($requestId != null ? $requestId.hashCode() : 43);
    }

    public String toString() {
        return "CommandOriginData(origin=" + getOrigin() + ", uuid=" + getUuid() + ", requestId=" + getRequestId() + ", event=" + getEvent() + ")";
    }

    public CommandOriginType getOrigin() {
        return this.origin;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public long getEvent() {
        return this.event;
    }
}
