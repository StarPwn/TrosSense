package io.netty.util;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.SystemPropertyUtil;
import java.util.Locale;

/* loaded from: classes4.dex */
public final class NettyRuntime {
    private static final AvailableProcessorsHolder holder = new AvailableProcessorsHolder();

    /* loaded from: classes4.dex */
    static class AvailableProcessorsHolder {
        private int availableProcessors;

        AvailableProcessorsHolder() {
        }

        synchronized void setAvailableProcessors(int availableProcessors) {
            ObjectUtil.checkPositive(availableProcessors, "availableProcessors");
            if (this.availableProcessors != 0) {
                String message = String.format(Locale.ROOT, "availableProcessors is already set to [%d], rejecting [%d]", Integer.valueOf(this.availableProcessors), Integer.valueOf(availableProcessors));
                throw new IllegalStateException(message);
            }
            this.availableProcessors = availableProcessors;
        }

        synchronized int availableProcessors() {
            int availableProcessors;
            if (this.availableProcessors == 0) {
                int availableProcessors2 = SystemPropertyUtil.getInt("io.netty.availableProcessors", Runtime.getRuntime().availableProcessors());
                setAvailableProcessors(availableProcessors2);
            }
            availableProcessors = this.availableProcessors;
            return availableProcessors;
        }
    }

    public static void setAvailableProcessors(int availableProcessors) {
        holder.setAvailableProcessors(availableProcessors);
    }

    public static int availableProcessors() {
        return holder.availableProcessors();
    }

    private NettyRuntime() {
    }
}
