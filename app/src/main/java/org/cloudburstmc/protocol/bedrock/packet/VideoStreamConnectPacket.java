package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class VideoStreamConnectPacket implements BedrockPacket {
    private Action action;
    private String address;
    private int height;
    private float screenshotFrequency;
    private int width;

    /* loaded from: classes5.dex */
    public enum Action {
        OPEN,
        CLOSE
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setScreenshotFrequency(float screenshotFrequency) {
        this.screenshotFrequency = screenshotFrequency;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    protected boolean canEqual(Object other) {
        return other instanceof VideoStreamConnectPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof VideoStreamConnectPacket)) {
            return false;
        }
        VideoStreamConnectPacket other = (VideoStreamConnectPacket) o;
        if (!other.canEqual(this) || Float.compare(this.screenshotFrequency, other.screenshotFrequency) != 0 || this.width != other.width || this.height != other.height) {
            return false;
        }
        Object this$address = this.address;
        Object other$address = other.address;
        if (this$address != null ? !this$address.equals(other$address) : other$address != null) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        return this$action != null ? this$action.equals(other$action) : other$action == null;
    }

    public int hashCode() {
        int result = (1 * 59) + Float.floatToIntBits(this.screenshotFrequency);
        int result2 = (((result * 59) + this.width) * 59) + this.height;
        Object $address = this.address;
        int result3 = (result2 * 59) + ($address == null ? 43 : $address.hashCode());
        Object $action = this.action;
        return (result3 * 59) + ($action != null ? $action.hashCode() : 43);
    }

    public String toString() {
        return "VideoStreamConnectPacket(address=" + this.address + ", screenshotFrequency=" + this.screenshotFrequency + ", action=" + this.action + ", width=" + this.width + ", height=" + this.height + ")";
    }

    public String getAddress() {
        return this.address;
    }

    public float getScreenshotFrequency() {
        return this.screenshotFrequency;
    }

    public Action getAction() {
        return this.action;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.VIDEO_STREAM_CONNECT;
    }
}
