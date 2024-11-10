package io.netty.channel.kqueue;

import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class AcceptFilter {
    static final AcceptFilter PLATFORM_UNSUPPORTED = new AcceptFilter("", "");
    private final String filterArgs;
    private final String filterName;

    public AcceptFilter(String filterName, String filterArgs) {
        this.filterName = (String) ObjectUtil.checkNotNull(filterName, "filterName");
        this.filterArgs = (String) ObjectUtil.checkNotNull(filterArgs, "filterArgs");
    }

    public String filterName() {
        return this.filterName;
    }

    public String filterArgs() {
        return this.filterArgs;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AcceptFilter)) {
            return false;
        }
        AcceptFilter rhs = (AcceptFilter) o;
        return this.filterName.equals(rhs.filterName) && this.filterArgs.equals(rhs.filterArgs);
    }

    public int hashCode() {
        return ((this.filterName.hashCode() + 31) * 31) + this.filterArgs.hashCode();
    }

    public String toString() {
        return this.filterName + ", " + this.filterArgs;
    }
}
