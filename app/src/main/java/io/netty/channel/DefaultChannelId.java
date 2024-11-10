package io.netty.channel;

import io.netty.buffer.ByteBufUtil;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.MacAddressUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes4.dex */
public final class DefaultChannelId implements ChannelId {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final byte[] MACHINE_ID;
    private static final int PROCESS_ID;
    private static final int PROCESS_ID_LEN = 4;
    private static final int RANDOM_LEN = 4;
    private static final int SEQUENCE_LEN = 4;
    private static final int TIMESTAMP_LEN = 8;
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) DefaultChannelId.class);
    private static final AtomicInteger nextSequence = new AtomicInteger();
    private static final long serialVersionUID = 3884076183504074063L;
    private final byte[] data;
    private final int hashCode;
    private transient String longValue;
    private transient String shortValue;

    static {
        int processId = -1;
        String customProcessId = SystemPropertyUtil.get("io.netty.processId");
        if (customProcessId != null) {
            try {
                processId = Integer.parseInt(customProcessId);
            } catch (NumberFormatException e) {
            }
            if (processId < 0) {
                processId = -1;
                logger.warn("-Dio.netty.processId: {} (malformed)", customProcessId);
            } else if (logger.isDebugEnabled()) {
                logger.debug("-Dio.netty.processId: {} (user-set)", Integer.valueOf(processId));
            }
        }
        if (processId < 0) {
            processId = defaultProcessId();
            if (logger.isDebugEnabled()) {
                logger.debug("-Dio.netty.processId: {} (auto-detected)", Integer.valueOf(processId));
            }
        }
        PROCESS_ID = processId;
        byte[] machineId = null;
        String customMachineId = SystemPropertyUtil.get("io.netty.machineId");
        if (customMachineId != null) {
            try {
                machineId = MacAddressUtil.parseMAC(customMachineId);
            } catch (Exception e2) {
                logger.warn("-Dio.netty.machineId: {} (malformed)", customMachineId, e2);
            }
            if (machineId != null) {
                logger.debug("-Dio.netty.machineId: {} (user-set)", customMachineId);
            }
        }
        if (machineId == null) {
            machineId = MacAddressUtil.defaultMachineId();
            if (logger.isDebugEnabled()) {
                logger.debug("-Dio.netty.machineId: {} (auto-detected)", MacAddressUtil.formatAddress(machineId));
            }
        }
        MACHINE_ID = machineId;
    }

    public static DefaultChannelId newInstance() {
        return new DefaultChannelId(MACHINE_ID, PROCESS_ID, nextSequence.getAndIncrement(), Long.reverse(System.nanoTime()) ^ System.currentTimeMillis(), PlatformDependent.threadLocalRandom().nextInt());
    }

    static int processHandlePid(ClassLoader loader) {
        if (PlatformDependent.javaVersion() < 9) {
            return -1;
        }
        try {
            Class<?> processHandleImplType = Class.forName("java.lang.ProcessHandle", true, loader);
            Method processHandleCurrent = processHandleImplType.getMethod("current", new Class[0]);
            Object processHandleInstance = processHandleCurrent.invoke(null, new Object[0]);
            Method processHandlePid = processHandleImplType.getMethod("pid", new Class[0]);
            Long pid = (Long) processHandlePid.invoke(processHandleInstance, new Object[0]);
            if (pid.longValue() > 2147483647L || pid.longValue() < -2147483648L) {
                throw new IllegalStateException("Current process ID exceeds int range: " + pid);
            }
            return pid.intValue();
        } catch (Exception e) {
            logger.debug("Could not invoke ProcessHandle.current().pid();", (Throwable) e);
            return -1;
        }
    }

    static int jmxPid(ClassLoader loader) {
        String value;
        int pid;
        try {
            Class<?> mgmtFactoryType = Class.forName("java.lang.management.ManagementFactory", true, loader);
            Class<?> runtimeMxBeanType = Class.forName("java.lang.management.RuntimeMXBean", true, loader);
            Method getRuntimeMXBean = mgmtFactoryType.getMethod("getRuntimeMXBean", EmptyArrays.EMPTY_CLASSES);
            Object bean = getRuntimeMXBean.invoke(null, EmptyArrays.EMPTY_OBJECTS);
            Method getName = runtimeMxBeanType.getMethod("getName", EmptyArrays.EMPTY_CLASSES);
            value = (String) getName.invoke(bean, EmptyArrays.EMPTY_OBJECTS);
        } catch (Throwable t) {
            logger.debug("Could not invoke ManagementFactory.getRuntimeMXBean().getName(); Android?", t);
            try {
                Class<?> processType = Class.forName("android.os.Process", true, loader);
                Method myPid = processType.getMethod("myPid", EmptyArrays.EMPTY_CLASSES);
                value = myPid.invoke(null, EmptyArrays.EMPTY_OBJECTS).toString();
            } catch (Throwable t2) {
                logger.debug("Could not invoke Process.myPid(); not Android?", t2);
                value = "";
            }
        }
        int atIndex = value.indexOf(64);
        if (atIndex >= 0) {
            value = value.substring(0, atIndex);
        }
        try {
            pid = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            pid = -1;
        }
        if (pid < 0) {
            int pid2 = PlatformDependent.threadLocalRandom().nextInt();
            logger.warn("Failed to find the current process ID from '{}'; using a random value: {}", value, Integer.valueOf(pid2));
            return pid2;
        }
        return pid;
    }

    static int defaultProcessId() {
        ClassLoader loader = PlatformDependent.getClassLoader(DefaultChannelId.class);
        int processId = processHandlePid(loader);
        if (processId != -1) {
            return processId;
        }
        return jmxPid(loader);
    }

    DefaultChannelId(byte[] machineId, int processId, int sequence, long timestamp, int random) {
        byte[] data = new byte[machineId.length + 4 + 4 + 8 + 4];
        System.arraycopy(machineId, 0, data, 0, machineId.length);
        int i = 0 + machineId.length;
        writeInt(data, i, processId);
        int i2 = i + 4;
        writeInt(data, i2, sequence);
        int i3 = i2 + 4;
        writeLong(data, i3, timestamp);
        int i4 = i3 + 8;
        writeInt(data, i4, random);
        if (i4 + 4 != data.length) {
            throw new AssertionError();
        }
        this.data = data;
        this.hashCode = Arrays.hashCode(data);
    }

    private static void writeInt(byte[] data, int i, int value) {
        if (PlatformDependent.isUnaligned()) {
            PlatformDependent.putInt(data, i, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? value : Integer.reverseBytes(value));
            return;
        }
        data[i] = (byte) (value >>> 24);
        data[i + 1] = (byte) (value >>> 16);
        data[i + 2] = (byte) (value >>> 8);
        data[i + 3] = (byte) value;
    }

    private static void writeLong(byte[] data, int i, long value) {
        if (PlatformDependent.isUnaligned()) {
            PlatformDependent.putLong(data, i, PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? value : Long.reverseBytes(value));
            return;
        }
        data[i] = (byte) (value >>> 56);
        data[i + 1] = (byte) (value >>> 48);
        data[i + 2] = (byte) (value >>> 40);
        data[i + 3] = (byte) (value >>> 32);
        data[i + 4] = (byte) (value >>> 24);
        data[i + 5] = (byte) (value >>> 16);
        data[i + 6] = (byte) (value >>> 8);
        data[i + 7] = (byte) value;
    }

    @Override // io.netty.channel.ChannelId
    public String asShortText() {
        String shortValue = this.shortValue;
        if (shortValue == null) {
            String shortValue2 = ByteBufUtil.hexDump(this.data, this.data.length - 4, 4);
            this.shortValue = shortValue2;
            return shortValue2;
        }
        return shortValue;
    }

    @Override // io.netty.channel.ChannelId
    public String asLongText() {
        String longValue = this.longValue;
        if (longValue == null) {
            String longValue2 = newLongValue();
            this.longValue = longValue2;
            return longValue2;
        }
        return longValue;
    }

    private String newLongValue() {
        StringBuilder buf = new StringBuilder((this.data.length * 2) + 5);
        int machineIdLen = (((this.data.length - 4) - 4) - 8) - 4;
        int i = appendHexDumpField(buf, 0, machineIdLen);
        if (appendHexDumpField(buf, appendHexDumpField(buf, appendHexDumpField(buf, appendHexDumpField(buf, i, 4), 4), 8), 4) != this.data.length) {
            throw new AssertionError();
        }
        return buf.substring(0, buf.length() - 1);
    }

    private int appendHexDumpField(StringBuilder buf, int i, int length) {
        buf.append(ByteBufUtil.hexDump(this.data, i, length));
        buf.append('-');
        return i + length;
    }

    public int hashCode() {
        return this.hashCode;
    }

    @Override // java.lang.Comparable
    public int compareTo(ChannelId o) {
        if (this == o) {
            return 0;
        }
        if (o instanceof DefaultChannelId) {
            byte[] otherData = ((DefaultChannelId) o).data;
            int len1 = this.data.length;
            int len2 = otherData.length;
            int len = Math.min(len1, len2);
            for (int k = 0; k < len; k++) {
                byte x = this.data[k];
                byte y = otherData[k];
                if (x != y) {
                    return (x & 255) - (y & 255);
                }
            }
            int k2 = len1 - len2;
            return k2;
        }
        return asLongText().compareTo(o.asLongText());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DefaultChannelId)) {
            return false;
        }
        DefaultChannelId other = (DefaultChannelId) obj;
        return this.hashCode == other.hashCode && Arrays.equals(this.data, other.data);
    }

    public String toString() {
        return asShortText();
    }
}
