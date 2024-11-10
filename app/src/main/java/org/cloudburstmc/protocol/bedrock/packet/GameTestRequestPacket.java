package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class GameTestRequestPacket implements BedrockPacket {
    private int maxTestsPerBatch;
    private int repeatCount;
    private int rotation;
    private boolean stoppingOnFailure;
    private String testName;
    private Vector3i testPos;
    private int testsPerRow;

    public void setMaxTestsPerBatch(int maxTestsPerBatch) {
        this.maxTestsPerBatch = maxTestsPerBatch;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public void setStoppingOnFailure(boolean stoppingOnFailure) {
        this.stoppingOnFailure = stoppingOnFailure;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setTestPos(Vector3i testPos) {
        this.testPos = testPos;
    }

    public void setTestsPerRow(int testsPerRow) {
        this.testsPerRow = testsPerRow;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GameTestRequestPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GameTestRequestPacket)) {
            return false;
        }
        GameTestRequestPacket other = (GameTestRequestPacket) o;
        if (!other.canEqual(this) || this.maxTestsPerBatch != other.maxTestsPerBatch || this.repeatCount != other.repeatCount || this.rotation != other.rotation || this.stoppingOnFailure != other.stoppingOnFailure || this.testsPerRow != other.testsPerRow) {
            return false;
        }
        Object this$testPos = this.testPos;
        Object other$testPos = other.testPos;
        if (this$testPos != null ? !this$testPos.equals(other$testPos) : other$testPos != null) {
            return false;
        }
        Object this$testName = this.testName;
        Object other$testName = other.testName;
        return this$testName != null ? this$testName.equals(other$testName) : other$testName == null;
    }

    public int hashCode() {
        int result = (1 * 59) + this.maxTestsPerBatch;
        int result2 = (((((((result * 59) + this.repeatCount) * 59) + this.rotation) * 59) + (this.stoppingOnFailure ? 79 : 97)) * 59) + this.testsPerRow;
        Object $testPos = this.testPos;
        int result3 = (result2 * 59) + ($testPos == null ? 43 : $testPos.hashCode());
        Object $testName = this.testName;
        return (result3 * 59) + ($testName != null ? $testName.hashCode() : 43);
    }

    public String toString() {
        return "GameTestRequestPacket(maxTestsPerBatch=" + this.maxTestsPerBatch + ", repeatCount=" + this.repeatCount + ", rotation=" + this.rotation + ", stoppingOnFailure=" + this.stoppingOnFailure + ", testPos=" + this.testPos + ", testsPerRow=" + this.testsPerRow + ", testName=" + this.testName + ")";
    }

    public int getMaxTestsPerBatch() {
        return this.maxTestsPerBatch;
    }

    public int getRepeatCount() {
        return this.repeatCount;
    }

    public int getRotation() {
        return this.rotation;
    }

    public boolean isStoppingOnFailure() {
        return this.stoppingOnFailure;
    }

    public Vector3i getTestPos() {
        return this.testPos;
    }

    public int getTestsPerRow() {
        return this.testsPerRow;
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
        return BedrockPacketType.GAME_TEST_REQUEST;
    }
}
