package org.cloudburstmc.protocol.bedrock.data;

import io.netty.buffer.ByteBuf;
import io.netty.util.AbstractReferenceCounted;
import org.cloudburstmc.math.vector.Vector3i;

/* loaded from: classes5.dex */
public class SubChunkData extends AbstractReferenceCounted {
    private long blobId;
    private boolean cacheEnabled;
    private ByteBuf data;
    private ByteBuf heightMapData;
    private HeightMapDataType heightMapType;
    private Vector3i position;
    private SubChunkRequestResult result;

    public void setBlobId(long blobId) {
        this.blobId = blobId;
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public void setData(ByteBuf data) {
        this.data = data;
    }

    public void setHeightMapData(ByteBuf heightMapData) {
        this.heightMapData = heightMapData;
    }

    public void setHeightMapType(HeightMapDataType heightMapType) {
        this.heightMapType = heightMapType;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public void setResult(SubChunkRequestResult result) {
        this.result = result;
    }

    public String toString() {
        return "SubChunkData(position=" + getPosition() + ", data=" + getData() + ", result=" + getResult() + ", heightMapType=" + getHeightMapType() + ", heightMapData=" + getHeightMapData() + ", cacheEnabled=" + isCacheEnabled() + ", blobId=" + getBlobId() + ")";
    }

    protected boolean canEqual(Object other) {
        return other instanceof SubChunkData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SubChunkData)) {
            return false;
        }
        SubChunkData other = (SubChunkData) o;
        if (!other.canEqual(this) || isCacheEnabled() != other.isCacheEnabled() || getBlobId() != other.getBlobId()) {
            return false;
        }
        Object this$position = getPosition();
        Object other$position = other.getPosition();
        if (this$position != null ? !this$position.equals(other$position) : other$position != null) {
            return false;
        }
        Object this$data = getData();
        Object other$data = other.getData();
        if (this$data != null ? !this$data.equals(other$data) : other$data != null) {
            return false;
        }
        Object this$result = getResult();
        Object other$result = other.getResult();
        if (this$result != null ? !this$result.equals(other$result) : other$result != null) {
            return false;
        }
        Object this$heightMapType = getHeightMapType();
        Object other$heightMapType = other.getHeightMapType();
        if (this$heightMapType != null ? !this$heightMapType.equals(other$heightMapType) : other$heightMapType != null) {
            return false;
        }
        Object this$heightMapData = getHeightMapData();
        Object other$heightMapData = other.getHeightMapData();
        return this$heightMapData != null ? this$heightMapData.equals(other$heightMapData) : other$heightMapData == null;
    }

    public int hashCode() {
        int result = (1 * 59) + (isCacheEnabled() ? 79 : 97);
        long $blobId = getBlobId();
        int result2 = (result * 59) + ((int) (($blobId >>> 32) ^ $blobId));
        Object $position = getPosition();
        int result3 = (result2 * 59) + ($position == null ? 43 : $position.hashCode());
        Object $data = getData();
        int result4 = (result3 * 59) + ($data == null ? 43 : $data.hashCode());
        Object $result = getResult();
        int result5 = (result4 * 59) + ($result == null ? 43 : $result.hashCode());
        Object $heightMapType = getHeightMapType();
        int result6 = (result5 * 59) + ($heightMapType == null ? 43 : $heightMapType.hashCode());
        Object $heightMapData = getHeightMapData();
        return (result6 * 59) + ($heightMapData != null ? $heightMapData.hashCode() : 43);
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public ByteBuf getData() {
        return this.data;
    }

    public SubChunkRequestResult getResult() {
        return this.result;
    }

    public HeightMapDataType getHeightMapType() {
        return this.heightMapType;
    }

    public ByteBuf getHeightMapData() {
        return this.heightMapData;
    }

    public boolean isCacheEnabled() {
        return this.cacheEnabled;
    }

    public long getBlobId() {
        return this.blobId;
    }

    @Override // io.netty.util.ReferenceCounted
    public SubChunkData touch(Object o) {
        if (this.data != null) {
            this.data.touch(o);
        }
        if (this.heightMapData != null) {
            this.heightMapData.touch(o);
        }
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        if (this.data != null) {
            this.data.release();
        }
        if (this.heightMapData != null) {
            this.heightMapData.release();
        }
    }
}
