package org.cloudburstmc.protocol.bedrock.codec;

import java.util.function.Supplier;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;

/* loaded from: classes5.dex */
public final class BedrockPacketDefinition<T extends BedrockPacket> {
    private final Supplier<T> factory;
    private final int id;
    private final BedrockPacketSerializer<T> serializer;

    public BedrockPacketDefinition(int id, Supplier<T> factory, BedrockPacketSerializer<T> serializer) {
        this.id = id;
        this.factory = factory;
        this.serializer = serializer;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BedrockPacketDefinition)) {
            return false;
        }
        BedrockPacketDefinition<?> other = (BedrockPacketDefinition) o;
        if (getId() != other.getId()) {
            return false;
        }
        Object this$factory = getFactory();
        Object other$factory = other.getFactory();
        if (this$factory != null ? !this$factory.equals(other$factory) : other$factory != null) {
            return false;
        }
        Object this$serializer = getSerializer();
        Object other$serializer = other.getSerializer();
        return this$serializer != null ? this$serializer.equals(other$serializer) : other$serializer == null;
    }

    public int hashCode() {
        int result = (1 * 59) + getId();
        Object $factory = getFactory();
        int result2 = (result * 59) + ($factory == null ? 43 : $factory.hashCode());
        Object $serializer = getSerializer();
        return (result2 * 59) + ($serializer != null ? $serializer.hashCode() : 43);
    }

    public String toString() {
        return "BedrockPacketDefinition(id=" + getId() + ", factory=" + getFactory() + ", serializer=" + getSerializer() + ")";
    }

    public int getId() {
        return this.id;
    }

    public Supplier<T> getFactory() {
        return this.factory;
    }

    public BedrockPacketSerializer<T> getSerializer() {
        return this.serializer;
    }
}
