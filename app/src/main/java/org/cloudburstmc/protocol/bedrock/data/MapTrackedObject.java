package org.cloudburstmc.protocol.bedrock.data;

import org.cloudburstmc.math.vector.Vector3i;

/* loaded from: classes5.dex */
public class MapTrackedObject {
    private long entityId;
    private Vector3i position;
    private final Type type;

    /* loaded from: classes5.dex */
    public enum Type {
        ENTITY,
        BLOCK
    }

    public String toString() {
        return "MapTrackedObject(type=" + getType() + ", entityId=" + getEntityId() + ", position=" + getPosition() + ")";
    }

    public Type getType() {
        return this.type;
    }

    public long getEntityId() {
        return this.entityId;
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public MapTrackedObject(long entityId) {
        this.type = Type.ENTITY;
        this.entityId = entityId;
    }

    public MapTrackedObject(Vector3i position) {
        this.type = Type.BLOCK;
        this.position = position;
    }
}
