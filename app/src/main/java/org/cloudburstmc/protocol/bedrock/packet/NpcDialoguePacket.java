package org.cloudburstmc.protocol.bedrock.packet;

import org.cloudburstmc.protocol.common.PacketSignal;

/* loaded from: classes5.dex */
public class NpcDialoguePacket implements BedrockPacket {
    private Action action;
    private String actionJson;
    private String dialogue;
    private String npcName;
    private String sceneName;
    private long uniqueEntityId;

    /* loaded from: classes5.dex */
    public enum Action {
        OPEN,
        CLOSE
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setActionJson(String actionJson) {
        this.actionJson = actionJson;
    }

    public void setDialogue(String dialogue) {
        this.dialogue = dialogue;
    }

    public void setNpcName(String npcName) {
        this.npcName = npcName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public void setUniqueEntityId(long uniqueEntityId) {
        this.uniqueEntityId = uniqueEntityId;
    }

    public String toString() {
        return "NpcDialoguePacket(uniqueEntityId=" + getUniqueEntityId() + ", action=" + getAction() + ", dialogue=" + getDialogue() + ", sceneName=" + getSceneName() + ", npcName=" + getNpcName() + ", actionJson=" + getActionJson() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof NpcDialoguePacket;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof NpcDialoguePacket)) {
            return false;
        }
        NpcDialoguePacket other = (NpcDialoguePacket) o;
        if (!other.canEqual(this) || this.uniqueEntityId != other.uniqueEntityId) {
            return false;
        }
        Object this$action = this.action;
        Object other$action = other.action;
        if (this$action != null ? !this$action.equals(other$action) : other$action != null) {
            return false;
        }
        Object this$dialogue = this.dialogue;
        Object other$dialogue = other.dialogue;
        if (this$dialogue != null ? !this$dialogue.equals(other$dialogue) : other$dialogue != null) {
            return false;
        }
        Object this$sceneName = this.sceneName;
        Object other$sceneName = other.sceneName;
        if (this$sceneName != null ? !this$sceneName.equals(other$sceneName) : other$sceneName != null) {
            return false;
        }
        Object this$npcName = this.npcName;
        Object other$npcName = other.npcName;
        if (this$npcName != null ? !this$npcName.equals(other$npcName) : other$npcName != null) {
            return false;
        }
        Object this$actionJson = this.actionJson;
        Object other$actionJson = other.actionJson;
        return this$actionJson != null ? this$actionJson.equals(other$actionJson) : other$actionJson == null;
    }

    public int hashCode() {
        long $uniqueEntityId = this.uniqueEntityId;
        int result = (1 * 59) + ((int) (($uniqueEntityId >>> 32) ^ $uniqueEntityId));
        Object $action = this.action;
        int result2 = (result * 59) + ($action == null ? 43 : $action.hashCode());
        Object $dialogue = this.dialogue;
        int result3 = (result2 * 59) + ($dialogue == null ? 43 : $dialogue.hashCode());
        Object $sceneName = this.sceneName;
        int result4 = (result3 * 59) + ($sceneName == null ? 43 : $sceneName.hashCode());
        Object $npcName = this.npcName;
        int result5 = (result4 * 59) + ($npcName == null ? 43 : $npcName.hashCode());
        Object $actionJson = this.actionJson;
        return (result5 * 59) + ($actionJson != null ? $actionJson.hashCode() : 43);
    }

    public long getUniqueEntityId() {
        return this.uniqueEntityId;
    }

    public Action getAction() {
        return this.action;
    }

    public String getDialogue() {
        return this.dialogue;
    }

    public String getSceneName() {
        return this.sceneName;
    }

    public String getNpcName() {
        return this.npcName;
    }

    public String getActionJson() {
        return this.actionJson;
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public PacketSignal handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    @Override // org.cloudburstmc.protocol.bedrock.packet.BedrockPacket
    public BedrockPacketType getPacketType() {
        return BedrockPacketType.NPC_DIALOGUE;
    }
}
