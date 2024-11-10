package org.cloudburstmc.protocol.bedrock.packet;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.cloudburstmc.math.vector.Vector2f;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.data.ClientPlayMode;
import org.cloudburstmc.protocol.bedrock.data.InputInteractionModel;
import org.cloudburstmc.protocol.bedrock.data.InputMode;
import org.cloudburstmc.protocol.bedrock.data.PlayerAuthInputData;
import org.cloudburstmc.protocol.bedrock.data.PlayerBlockActionData;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.ItemStackRequest;
import org.cloudburstmc.protocol.bedrock.data.inventory.transaction.ItemUseTransaction;
import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class PlayerAuthInputPacket implements BedrockPacket {
    private Vector2f analogMoveVector;
    private boolean cameraDeparted;
    private Vector3f delta;
    private InputInteractionModel inputInteractionModel;
    private InputMode inputMode;
    private ItemStackRequest itemStackRequest;
    private ItemUseTransaction itemUseTransaction;
    private Vector2f motion;
    private ClientPlayMode playMode;
    private Vector3f position;
    private long predictedVehicle;
    private Vector3f rotation;
    private long tick;
    private Vector2f vehicleRotation;
    private Vector3f vrGazeDirection;
    private final Set<PlayerAuthInputData> inputData = EnumSet.noneOf(PlayerAuthInputData.class);
    private final List<PlayerBlockActionData> playerActions = new ObjectArrayList();

    public void setAnalogMoveVector(Vector2f analogMoveVector) {
        this.analogMoveVector = analogMoveVector;
    }

    public void setCameraDeparted(boolean cameraDeparted) {
        this.cameraDeparted = cameraDeparted;
    }

    public void setDelta(Vector3f delta) {
        this.delta = delta;
    }

    public void setInputInteractionModel(InputInteractionModel inputInteractionModel) {
        this.inputInteractionModel = inputInteractionModel;
    }

    public void setInputMode(InputMode inputMode) {
        this.inputMode = inputMode;
    }

    public void setItemStackRequest(ItemStackRequest itemStackRequest) {
        this.itemStackRequest = itemStackRequest;
    }

    public void setItemUseTransaction(ItemUseTransaction itemUseTransaction) {
        this.itemUseTransaction = itemUseTransaction;
    }

    public void setMotion(Vector2f motion) {
        this.motion = motion;
    }

    public void setPlayMode(ClientPlayMode playMode) {
        this.playMode = playMode;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setPredictedVehicle(long predictedVehicle) {
        this.predictedVehicle = predictedVehicle;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    public void setVehicleRotation(Vector2f vehicleRotation) {
        this.vehicleRotation = vehicleRotation;
    }

    public void setVrGazeDirection(Vector3f vrGazeDirection) {
        this.vrGazeDirection = vrGazeDirection;
    }

    protected boolean canEqual(Object other) {
        return other instanceof PlayerAuthInputPacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerAuthInputPacket)) {
            return false;
        }
        PlayerAuthInputPacket other = (PlayerAuthInputPacket) o;
        if (!other.canEqual(this) || this.tick != other.tick || this.cameraDeparted != other.cameraDeparted || this.predictedVehicle != other.predictedVehicle) {
            return false;
        }
        Object this$rotation = this.rotation;
        Object other$rotation = other.rotation;
        if (this$rotation != null ? !this$rotation.equals(other$rotation) : other$rotation != null) {
            return false;
        }
        Object this$position = this.position;
        Object other$position = other.position;
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$motion = this.motion;
        Object other$motion = other.motion;
        if (this$motion != null ? !this$motion.equals(other$motion) : other$motion != null) {
            return false;
        }
        Object this$inputData = this.inputData;
        Object other$inputData = other.inputData;
        if (this$inputData != null ? !this$inputData.equals(other$inputData) : other$inputData != null) {
            return false;
        }
        Object this$inputMode = this.inputMode;
        Object other$inputMode = other.inputMode;
        if (this$inputMode != null ? !this$inputMode.equals(other$inputMode) : other$inputMode != null) {
            return false;
        }
        Object this$playMode = this.playMode;
        Object other$playMode = other.playMode;
        if (this$playMode != null ? !this$playMode.equals(other$playMode) : other$playMode != null) {
            return false;
        }
        Object this$vrGazeDirection = this.vrGazeDirection;
        Object other$vrGazeDirection = other.vrGazeDirection;
        if (this$vrGazeDirection == null) {
            if (other$vrGazeDirection != null) {
                return false;
            }
        } else if (!this$vrGazeDirection.equals(other$vrGazeDirection)) {
            return false;
        }
        Object other$vrGazeDirection2 = this.delta;
        Object other$playMode2 = other.delta;
        if (other$vrGazeDirection2 == null) {
            if (other$playMode2 != null) {
                return false;
            }
        } else if (!other$vrGazeDirection2.equals(other$playMode2)) {
            return false;
        }
        Object this$delta = this.itemUseTransaction;
        Object other$delta = other.itemUseTransaction;
        if (this$delta == null) {
            if (other$delta != null) {
                return false;
            }
        } else if (!this$delta.equals(other$delta)) {
            return false;
        }
        Object this$itemUseTransaction = this.itemStackRequest;
        Object other$itemUseTransaction = other.itemStackRequest;
        if (this$itemUseTransaction == null) {
            if (other$itemUseTransaction != null) {
                return false;
            }
        } else if (!this$itemUseTransaction.equals(other$itemUseTransaction)) {
            return false;
        }
        Object this$itemStackRequest = this.playerActions;
        Object other$itemStackRequest = other.playerActions;
        if (this$itemStackRequest == null) {
            if (other$itemStackRequest != null) {
                return false;
            }
        } else if (!this$itemStackRequest.equals(other$itemStackRequest)) {
            return false;
        }
        Object this$playerActions = this.inputInteractionModel;
        Object other$playerActions = other.inputInteractionModel;
        if (this$playerActions == null) {
            if (other$playerActions != null) {
                return false;
            }
        } else if (!this$playerActions.equals(other$playerActions)) {
            return false;
        }
        Object this$inputInteractionModel = this.analogMoveVector;
        Object other$inputInteractionModel = other.analogMoveVector;
        if (this$inputInteractionModel == null) {
            if (other$inputInteractionModel != null) {
                return false;
            }
        } else if (!this$inputInteractionModel.equals(other$inputInteractionModel)) {
            return false;
        }
        Object this$analogMoveVector = this.vehicleRotation;
        Object other$vehicleRotation = other.vehicleRotation;
        return this$analogMoveVector == null ? other$vehicleRotation == null : this$analogMoveVector.equals(other$vehicleRotation);
    }

    public int hashCode() {
        long $tick = this.tick;
        int result = (1 * 59) + ((int) (($tick >>> 32) ^ $tick));
        int result2 = (result * 59) + (this.cameraDeparted ? 79 : 97);
        long $predictedVehicle = this.predictedVehicle;
        int result3 = (result2 * 59) + ((int) (($predictedVehicle >>> 32) ^ $predictedVehicle));
        Object $rotation = this.rotation;
        int result4 = (result3 * 59) + ($rotation == null ? 43 : $rotation.hashCode());
        Object $position = this.position;
        int result5 = (result4 * 59) + ($position == null ? 43 : $position.hashCode());
        Object $motion = this.motion;
        int result6 = (result5 * 59) + ($motion == null ? 43 : $motion.hashCode());
        Object $inputData = this.inputData;
        int result7 = (result6 * 59) + ($inputData == null ? 43 : $inputData.hashCode());
        Object $inputMode = this.inputMode;
        int result8 = (result7 * 59) + ($inputMode == null ? 43 : $inputMode.hashCode());
        Object $playMode = this.playMode;
        int result9 = (result8 * 59) + ($playMode == null ? 43 : $playMode.hashCode());
        Object $vrGazeDirection = this.vrGazeDirection;
        int result10 = (result9 * 59) + ($vrGazeDirection == null ? 43 : $vrGazeDirection.hashCode());
        Object $delta = this.delta;
        int result11 = (result10 * 59) + ($delta == null ? 43 : $delta.hashCode());
        Object $itemUseTransaction = this.itemUseTransaction;
        int result12 = (result11 * 59) + ($itemUseTransaction == null ? 43 : $itemUseTransaction.hashCode());
        Object $itemStackRequest = this.itemStackRequest;
        int result13 = (result12 * 59) + ($itemStackRequest == null ? 43 : $itemStackRequest.hashCode());
        Object $playerActions = this.playerActions;
        int result14 = (result13 * 59) + ($playerActions == null ? 43 : $playerActions.hashCode());
        Object $playerActions2 = this.inputInteractionModel;
        int result15 = (result14 * 59) + ($playerActions2 == null ? 43 : $playerActions2.hashCode());
        Object $inputInteractionModel = this.analogMoveVector;
        int result16 = (result15 * 59) + ($inputInteractionModel == null ? 43 : $inputInteractionModel.hashCode());
        Object $analogMoveVector = this.vehicleRotation;
        return (result16 * 59) + ($analogMoveVector == null ? 43 : $analogMoveVector.hashCode());
    }

    public String toString() {
        return "PlayerAuthInputPacket(rotation=" + this.rotation + ", position=" + this.position + ", motion=" + this.motion + ", inputData=" + this.inputData + ", inputMode=" + this.inputMode + ", playMode=" + this.playMode + ", vrGazeDirection=" + this.vrGazeDirection + ", tick=" + this.tick + ", delta=" + this.delta + ", cameraDeparted=" + this.cameraDeparted + ", itemUseTransaction=" + this.itemUseTransaction + ", itemStackRequest=" + this.itemStackRequest + ", playerActions=" + this.playerActions + ", inputInteractionModel=" + this.inputInteractionModel + ", analogMoveVector=" + this.analogMoveVector + ", predictedVehicle=" + this.predictedVehicle + ", vehicleRotation=" + this.vehicleRotation + ")";
    }

    public Vector3f getRotation() {
        return this.rotation;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Vector2f getMotion() {
        return this.motion;
    }

    public Set<PlayerAuthInputData> getInputData() {
        return this.inputData;
    }

    public InputMode getInputMode() {
        return this.inputMode;
    }

    public ClientPlayMode getPlayMode() {
        return this.playMode;
    }

    public Vector3f getVrGazeDirection() {
        return this.vrGazeDirection;
    }

    public long getTick() {
        return this.tick;
    }

    public Vector3f getDelta() {
        return this.delta;
    }

    public boolean isCameraDeparted() {
        return this.cameraDeparted;
    }

    public ItemUseTransaction getItemUseTransaction() {
        return this.itemUseTransaction;
    }

    public ItemStackRequest getItemStackRequest() {
        return this.itemStackRequest;
    }

    public List<PlayerBlockActionData> getPlayerActions() {
        return this.playerActions;
    }

    public InputInteractionModel getInputInteractionModel() {
        return this.inputInteractionModel;
    }

    public Vector2f getAnalogMoveVector() {
        return this.analogMoveVector;
    }

    public long getPredictedVehicle() {
        return this.predictedVehicle;
    }

    public Vector2f getVehicleRotation() {
        return this.vehicleRotation;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.PLAYER_AUTH_INPUT;
    }
}
