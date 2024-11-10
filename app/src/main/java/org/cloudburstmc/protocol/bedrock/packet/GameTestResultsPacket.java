package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class GameTestResultsPacket implements BedrockPacket {
    private String error;
    private boolean successful;
    private String testName;

    public void setError(String error) {
        this.error = error;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameTestResultsPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GameTestResultsPacket)) {
            return false;
        }
        GameTestResultsPacket other = (GameTestResultsPacket) o;
        if (!other.canEqual(this) || this.successful != other.successful) {
            return false;
        }
        Object this$error = this.error;
        Object other$error = other.error;
        if (this$error != null ? !this$error.equals(other$error) : other$error != null) {
            return false;
        }
        Object this$testName = this.testName;
        Object other$testName = other.testName;
        return this$testName != null ? this$testName.equals(other$testName) : other$testName == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (this.successful ? 79 : 97);
        Object $error = this.error;
        int result2 = (result * 59) + ($error == null ? 43 : $error.hashCode());
        Object $testName = this.testName;
        return (result2 * 59) + ($testName != null ? $testName.hashCode() : 43);
    }

    public String toString() {
        return "GameTestResultsPacket(successful=" + this.successful + ", error=" + this.error + ", testName=" + this.testName + ")";
    }

    public boolean isSuccessful() {
        return this.successful;
    }

    public String getError() {
        return this.error;
    }

    public String getTestName() {
        return this.testName;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.GAME_TEST_RESULTS;
    }
}
