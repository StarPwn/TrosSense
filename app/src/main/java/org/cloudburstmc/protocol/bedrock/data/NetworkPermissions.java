package org.cloudburstmc.protocol.bedrock.data;

/* loaded from: classes5.dex */
public final class NetworkPermissions {
    public static final NetworkPermissions DEFAULT = new NetworkPermissions(false);
    private final boolean serverAuthSounds;

    public NetworkPermissions(boolean serverAuthSounds) {
        this.serverAuthSounds = serverAuthSounds;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NetworkPermissions)) {
            return false;
        }
        NetworkPermissions other = (NetworkPermissions) o;
        return isServerAuthSounds() == other.isServerAuthSounds();
    }

    public int hashCode() {
        int result = (1 * 59) + (isServerAuthSounds() ? 79 : 97);
        return result;
    }

    public String toString() {
        return "NetworkPermissions(serverAuthSounds=" + isServerAuthSounds() + ")";
    }

    public boolean isServerAuthSounds() {
        return this.serverAuthSounds;
    }
}
