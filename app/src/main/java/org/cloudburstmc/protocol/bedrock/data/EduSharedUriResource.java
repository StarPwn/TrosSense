package org.cloudburstmc.protocol.bedrock.data;

/* loaded from: classes5.dex */
public final class EduSharedUriResource {
    public static final EduSharedUriResource EMPTY = new EduSharedUriResource("", "");
    private final String buttonName;
    private final String linkUri;

    public EduSharedUriResource(String buttonName, String linkUri) {
        this.buttonName = buttonName;
        this.linkUri = linkUri;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EduSharedUriResource)) {
            return false;
        }
        EduSharedUriResource other = (EduSharedUriResource) o;
        Object this$buttonName = getButtonName();
        Object other$buttonName = other.getButtonName();
        if (this$buttonName != null ? !this$buttonName.equals(other$buttonName) : other$buttonName != null) {
            return false;
        }
        Object this$linkUri = getLinkUri();
        Object other$linkUri = other.getLinkUri();
        return this$linkUri != null ? this$linkUri.equals(other$linkUri) : other$linkUri == null;
    }

    public int hashCode() {
        Object $buttonName = getButtonName();
        int result = (1 * 59) + ($buttonName == null ? 43 : $buttonName.hashCode());
        Object $linkUri = getLinkUri();
        return (result * 59) + ($linkUri != null ? $linkUri.hashCode() : 43);
    }

    public String toString() {
        return "EduSharedUriResource(buttonName=" + getButtonName() + ", linkUri=" + getLinkUri() + ")";
    }

    public String getButtonName() {
        return this.buttonName;
    }

    public String getLinkUri() {
        return this.linkUri;
    }
}
