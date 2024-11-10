package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.ClientboundDebugRendererType;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class ClientboundDebugRendererPacket implements BedrockPacket {
    private ClientboundDebugRendererType debugMarkerType;
    private float markerColorAlpha;
    private float markerColorBlue;
    private float markerColorGreen;
    private float markerColorRed;
    private long markerDuration;
    private Vector3f markerPosition;
    private String markerText;

    public void setDebugMarkerType(ClientboundDebugRendererType debugMarkerType) {
        this.debugMarkerType = debugMarkerType;
    }

    public void setMarkerColorAlpha(float markerColorAlpha) {
        this.markerColorAlpha = markerColorAlpha;
    }

    public void setMarkerColorBlue(float markerColorBlue) {
        this.markerColorBlue = markerColorBlue;
    }

    public void setMarkerColorGreen(float markerColorGreen) {
        this.markerColorGreen = markerColorGreen;
    }

    public void setMarkerColorRed(float markerColorRed) {
        this.markerColorRed = markerColorRed;
    }

    public void setMarkerDuration(long markerDuration) {
        this.markerDuration = markerDuration;
    }

    public void setMarkerPosition(Vector3f markerPosition) {
        this.markerPosition = markerPosition;
    }

    public void setMarkerText(String markerText) {
        this.markerText = markerText;
    }

    protected boolean canEqual(Object other) {
        return other instanceof ClientboundDebugRendererPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ClientboundDebugRendererPacket)) {
            return false;
        }
        ClientboundDebugRendererPacket other = (ClientboundDebugRendererPacket) o;
        if (!other.canEqual(this) || Float.compare(this.markerColorRed, other.markerColorRed) != 0 || Float.compare(this.markerColorGreen, other.markerColorGreen) != 0 || Float.compare(this.markerColorBlue, other.markerColorBlue) != 0 || Float.compare(this.markerColorAlpha, other.markerColorAlpha) != 0 || this.markerDuration != other.markerDuration) {
            return false;
        }
        Object this$debugMarkerType = this.debugMarkerType;
        Object other$debugMarkerType = other.debugMarkerType;
        if (this$debugMarkerType != null ? !this$debugMarkerType.equals(other$debugMarkerType) : other$debugMarkerType != null) {
            return false;
        }
        Object this$markerText = this.markerText;
        Object other$markerText = other.markerText;
        if (this$markerText != null ? !this$markerText.equals(other$markerText) : other$markerText != null) {
            return false;
        }
        Object this$markerPosition = this.markerPosition;
        Object other$markerPosition = other.markerPosition;
        return this$markerPosition != null ? this$markerPosition.equals(other$markerPosition) : other$markerPosition == null;
    }

    public int hashCode() {
        int result = (1 * 59) + Float.floatToIntBits(this.markerColorRed);
        int result2 = (((((result * 59) + Float.floatToIntBits(this.markerColorGreen)) * 59) + Float.floatToIntBits(this.markerColorBlue)) * 59) + Float.floatToIntBits(this.markerColorAlpha);
        long $markerDuration = this.markerDuration;
        int result3 = (result2 * 59) + ((int) (($markerDuration >>> 32) ^ $markerDuration));
        Object $debugMarkerType = this.debugMarkerType;
        int result4 = (result3 * 59) + ($debugMarkerType == null ? 43 : $debugMarkerType.hashCode());
        Object $markerText = this.markerText;
        int result5 = (result4 * 59) + ($markerText == null ? 43 : $markerText.hashCode());
        Object $markerPosition = this.markerPosition;
        return (result5 * 59) + ($markerPosition != null ? $markerPosition.hashCode() : 43);
    }

    public String toString() {
        return "ClientboundDebugRendererPacket(debugMarkerType=" + this.debugMarkerType + ", markerText=" + this.markerText + ", markerPosition=" + this.markerPosition + ", markerColorRed=" + this.markerColorRed + ", markerColorGreen=" + this.markerColorGreen + ", markerColorBlue=" + this.markerColorBlue + ", markerColorAlpha=" + this.markerColorAlpha + ", markerDuration=" + this.markerDuration + ")";
    }

    public ClientboundDebugRendererType getDebugMarkerType() {
        return this.debugMarkerType;
    }

    public String getMarkerText() {
        return this.markerText;
    }

    public Vector3f getMarkerPosition() {
        return this.markerPosition;
    }

    public float getMarkerColorRed() {
        return this.markerColorRed;
    }

    public float getMarkerColorGreen() {
        return this.markerColorGreen;
    }

    public float getMarkerColorBlue() {
        return this.markerColorBlue;
    }

    public float getMarkerColorAlpha() {
        return this.markerColorAlpha;
    }

    public long getMarkerDuration() {
        return this.markerDuration;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.CLIENTBOUND_DEBUG_RENDERER;
    }
}
