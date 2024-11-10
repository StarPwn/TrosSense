package org.cloudburstmc.protocol.bedrock.data.entity;

/* loaded from: classes5.dex */
public class EntityDataType<T> {
    private final String name;
    private final Class<?> type;

    public EntityDataType(Class<? super T> type, String name) {
        this.name = name;
        this.type = type;
    }

    public boolean isInstance(Object value) {
        return this.type.isInstance(value);
    }

    public String getTypeName() {
        return this.type.getTypeName();
    }

    public String toString() {
        return this.name;
    }
}
