package org.cloudburstmc.protocol.bedrock;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.StringJoiner;

/* loaded from: classes5.dex */
public class BedrockPong {
    private String edition;
    private String[] extras;
    private String gameType;
    private String motd;
    private boolean nintendoLimited;
    private long serverId;
    private String subMotd;
    private String version;
    private int protocolVersion = -1;
    private int playerCount = -1;
    private int maximumPlayerCount = -1;
    private int ipv4Port = -1;
    private int ipv6Port = -1;

    protected boolean canEqual(Object other) {
        return other instanceof BedrockPong;
    }

    public BedrockPong edition(String edition) {
        this.edition = edition;
        return this;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BedrockPong)) {
            return false;
        }
        BedrockPong other = (BedrockPong) o;
        if (!other.canEqual(this) || protocolVersion() != other.protocolVersion() || playerCount() != other.playerCount() || maximumPlayerCount() != other.maximumPlayerCount() || serverId() != other.serverId() || nintendoLimited() != other.nintendoLimited() || ipv4Port() != other.ipv4Port() || ipv6Port() != other.ipv6Port()) {
            return false;
        }
        Object this$edition = edition();
        Object other$edition = other.edition();
        if (this$edition != null ? !this$edition.equals(other$edition) : other$edition != null) {
            return false;
        }
        Object this$motd = motd();
        Object other$motd = other.motd();
        if (this$motd != null ? !this$motd.equals(other$motd) : other$motd != null) {
            return false;
        }
        Object this$version = version();
        Object other$version = other.version();
        if (this$version != null ? !this$version.equals(other$version) : other$version != null) {
            return false;
        }
        Object this$subMotd = subMotd();
        Object other$subMotd = other.subMotd();
        if (this$subMotd != null ? !this$subMotd.equals(other$subMotd) : other$subMotd != null) {
            return false;
        }
        Object this$gameType = gameType();
        Object other$gameType = other.gameType();
        if (this$gameType != null ? this$gameType.equals(other$gameType) : other$gameType == null) {
            return Arrays.deepEquals(extras(), other.extras());
        }
        return false;
    }

    public BedrockPong extras(String[] extras) {
        this.extras = extras;
        return this;
    }

    public BedrockPong gameType(String gameType) {
        this.gameType = gameType;
        return this;
    }

    public int hashCode() {
        int result = (1 * 59) + protocolVersion();
        int result2 = (((result * 59) + playerCount()) * 59) + maximumPlayerCount();
        long $serverId = serverId();
        int result3 = (((((((result2 * 59) + ((int) (($serverId >>> 32) ^ $serverId))) * 59) + (nintendoLimited() ? 79 : 97)) * 59) + ipv4Port()) * 59) + ipv6Port();
        Object $edition = edition();
        int result4 = (result3 * 59) + ($edition == null ? 43 : $edition.hashCode());
        Object $motd = motd();
        int result5 = (result4 * 59) + ($motd == null ? 43 : $motd.hashCode());
        Object $version = version();
        int result6 = (result5 * 59) + ($version == null ? 43 : $version.hashCode());
        Object $subMotd = subMotd();
        int result7 = (result6 * 59) + ($subMotd == null ? 43 : $subMotd.hashCode());
        Object $gameType = gameType();
        return (((result7 * 59) + ($gameType != null ? $gameType.hashCode() : 43)) * 59) + Arrays.deepHashCode(extras());
    }

    public BedrockPong ipv4Port(int ipv4Port) {
        this.ipv4Port = ipv4Port;
        return this;
    }

    public BedrockPong ipv6Port(int ipv6Port) {
        this.ipv6Port = ipv6Port;
        return this;
    }

    public BedrockPong maximumPlayerCount(int maximumPlayerCount) {
        this.maximumPlayerCount = maximumPlayerCount;
        return this;
    }

    public BedrockPong motd(String motd) {
        this.motd = motd;
        return this;
    }

    public BedrockPong nintendoLimited(boolean nintendoLimited) {
        this.nintendoLimited = nintendoLimited;
        return this;
    }

    public BedrockPong playerCount(int playerCount) {
        this.playerCount = playerCount;
        return this;
    }

    public BedrockPong protocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
        return this;
    }

    public BedrockPong serverId(long serverId) {
        this.serverId = serverId;
        return this;
    }

    public BedrockPong subMotd(String subMotd) {
        this.subMotd = subMotd;
        return this;
    }

    public String toString() {
        return "BedrockPong(edition=" + edition() + ", motd=" + motd() + ", protocolVersion=" + protocolVersion() + ", version=" + version() + ", playerCount=" + playerCount() + ", maximumPlayerCount=" + maximumPlayerCount() + ", serverId=" + serverId() + ", subMotd=" + subMotd() + ", gameType=" + gameType() + ", nintendoLimited=" + nintendoLimited() + ", ipv4Port=" + ipv4Port() + ", ipv6Port=" + ipv6Port() + ", extras=" + Arrays.deepToString(extras()) + ")";
    }

    public BedrockPong version(String version) {
        this.version = version;
        return this;
    }

    public String edition() {
        return this.edition;
    }

    public String motd() {
        return this.motd;
    }

    public int protocolVersion() {
        return this.protocolVersion;
    }

    public String version() {
        return this.version;
    }

    public int playerCount() {
        return this.playerCount;
    }

    public int maximumPlayerCount() {
        return this.maximumPlayerCount;
    }

    public long serverId() {
        return this.serverId;
    }

    public String subMotd() {
        return this.subMotd;
    }

    public String gameType() {
        return this.gameType;
    }

    public boolean nintendoLimited() {
        return this.nintendoLimited;
    }

    public int ipv4Port() {
        return this.ipv4Port;
    }

    public int ipv6Port() {
        return this.ipv6Port;
    }

    public String[] extras() {
        return this.extras;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static BedrockPong fromRakNet(ByteBuf pong) {
        String info = pong.toString(StandardCharsets.UTF_8);
        BedrockPong bedrockPong = new BedrockPong();
        String[] infos = info.split(";");
        switch (infos.length) {
            case 0:
                break;
            case 1:
                bedrockPong.edition = infos[0];
                break;
            case 2:
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
            case 3:
                try {
                    bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                } catch (NumberFormatException e) {
                }
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
            case 4:
                bedrockPong.version = infos[3];
                bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
            case 5:
                try {
                    bedrockPong.playerCount = Integer.parseInt(infos[4]);
                } catch (NumberFormatException e2) {
                }
                bedrockPong.version = infos[3];
                bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
            case 6:
                try {
                    bedrockPong.maximumPlayerCount = Integer.parseInt(infos[5]);
                } catch (NumberFormatException e3) {
                }
                bedrockPong.playerCount = Integer.parseInt(infos[4]);
                bedrockPong.version = infos[3];
                bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
            case 7:
                try {
                    bedrockPong.serverId = Long.parseLong(infos[6]);
                } catch (NumberFormatException e4) {
                }
                bedrockPong.maximumPlayerCount = Integer.parseInt(infos[5]);
                bedrockPong.playerCount = Integer.parseInt(infos[4]);
                bedrockPong.version = infos[3];
                bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
            case 8:
                bedrockPong.subMotd = infos[7];
                bedrockPong.serverId = Long.parseLong(infos[6]);
                bedrockPong.maximumPlayerCount = Integer.parseInt(infos[5]);
                bedrockPong.playerCount = Integer.parseInt(infos[4]);
                bedrockPong.version = infos[3];
                bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
            case 9:
                bedrockPong.gameType = infos[8];
                bedrockPong.subMotd = infos[7];
                bedrockPong.serverId = Long.parseLong(infos[6]);
                bedrockPong.maximumPlayerCount = Integer.parseInt(infos[5]);
                bedrockPong.playerCount = Integer.parseInt(infos[4]);
                bedrockPong.version = infos[3];
                bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
            case 10:
                bedrockPong.nintendoLimited = !"1".equalsIgnoreCase(infos[9]);
                bedrockPong.gameType = infos[8];
                bedrockPong.subMotd = infos[7];
                bedrockPong.serverId = Long.parseLong(infos[6]);
                bedrockPong.maximumPlayerCount = Integer.parseInt(infos[5]);
                bedrockPong.playerCount = Integer.parseInt(infos[4]);
                bedrockPong.version = infos[3];
                bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
            case 11:
                try {
                    bedrockPong.ipv4Port = Integer.parseInt(infos[10]);
                } catch (NumberFormatException e5) {
                }
                bedrockPong.nintendoLimited = !"1".equalsIgnoreCase(infos[9]);
                bedrockPong.gameType = infos[8];
                bedrockPong.subMotd = infos[7];
                bedrockPong.serverId = Long.parseLong(infos[6]);
                bedrockPong.maximumPlayerCount = Integer.parseInt(infos[5]);
                bedrockPong.playerCount = Integer.parseInt(infos[4]);
                bedrockPong.version = infos[3];
                bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
            case 12:
                try {
                    bedrockPong.ipv6Port = Integer.parseInt(infos[11]);
                } catch (NumberFormatException e6) {
                }
                bedrockPong.ipv4Port = Integer.parseInt(infos[10]);
                bedrockPong.nintendoLimited = !"1".equalsIgnoreCase(infos[9]);
                bedrockPong.gameType = infos[8];
                bedrockPong.subMotd = infos[7];
                bedrockPong.serverId = Long.parseLong(infos[6]);
                bedrockPong.maximumPlayerCount = Integer.parseInt(infos[5]);
                bedrockPong.playerCount = Integer.parseInt(infos[4]);
                bedrockPong.version = infos[3];
                bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
            default:
                bedrockPong.extras = new String[infos.length - 12];
                System.arraycopy(infos, 12, bedrockPong.extras, 0, bedrockPong.extras.length);
                bedrockPong.ipv6Port = Integer.parseInt(infos[11]);
                bedrockPong.ipv4Port = Integer.parseInt(infos[10]);
                bedrockPong.nintendoLimited = !"1".equalsIgnoreCase(infos[9]);
                bedrockPong.gameType = infos[8];
                bedrockPong.subMotd = infos[7];
                bedrockPong.serverId = Long.parseLong(infos[6]);
                bedrockPong.maximumPlayerCount = Integer.parseInt(infos[5]);
                bedrockPong.playerCount = Integer.parseInt(infos[4]);
                bedrockPong.version = infos[3];
                bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                bedrockPong.motd = infos[1];
                bedrockPong.edition = infos[0];
                break;
        }
        return bedrockPong;
    }

    public ByteBuf toByteBuf() {
        String m;
        StringJoiner add = new StringJoiner(";", "", ";").add(this.edition).add(toString(this.motd)).add(Integer.toString(this.protocolVersion)).add(toString(this.version)).add(Integer.toString(this.playerCount)).add(Integer.toString(this.maximumPlayerCount));
        m = BedrockPong$$ExternalSyntheticBackport1.m(this.serverId, 10);
        StringJoiner joiner = add.add(m).add(toString(this.subMotd)).add(toString(this.gameType)).add(this.nintendoLimited ? "0" : "1").add(Integer.toString(this.ipv4Port)).add(Integer.toString(this.ipv6Port));
        if (this.extras != null) {
            for (String extra : this.extras) {
                joiner.add(extra);
            }
        }
        return Unpooled.wrappedBuffer(joiner.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String toString(String string) {
        return string == null ? "" : string;
    }
}
