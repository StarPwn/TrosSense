package io.netty.handler.codec.spdy;

import io.netty.util.internal.StringUtil;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/* loaded from: classes4.dex */
public class DefaultSpdySettingsFrame implements SpdySettingsFrame {
    private boolean clear;
    private final Map<Integer, Setting> settingsMap = new TreeMap();

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public Set<Integer> ids() {
        return this.settingsMap.keySet();
    }

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public boolean isSet(int id) {
        return this.settingsMap.containsKey(Integer.valueOf(id));
    }

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public int getValue(int id) {
        Setting setting = this.settingsMap.get(Integer.valueOf(id));
        if (setting != null) {
            return setting.getValue();
        }
        return -1;
    }

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public SpdySettingsFrame setValue(int id, int value) {
        return setValue(id, value, false, false);
    }

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public SpdySettingsFrame setValue(int id, int value, boolean persistValue, boolean persisted) {
        if (id < 0 || id > 16777215) {
            throw new IllegalArgumentException("Setting ID is not valid: " + id);
        }
        Integer key = Integer.valueOf(id);
        Setting setting = this.settingsMap.get(key);
        if (setting != null) {
            setting.setValue(value);
            setting.setPersist(persistValue);
            setting.setPersisted(persisted);
        } else {
            this.settingsMap.put(key, new Setting(value, persistValue, persisted));
        }
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public SpdySettingsFrame removeValue(int id) {
        this.settingsMap.remove(Integer.valueOf(id));
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public boolean isPersistValue(int id) {
        Setting setting = this.settingsMap.get(Integer.valueOf(id));
        return setting != null && setting.isPersist();
    }

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public SpdySettingsFrame setPersistValue(int id, boolean persistValue) {
        Setting setting = this.settingsMap.get(Integer.valueOf(id));
        if (setting != null) {
            setting.setPersist(persistValue);
        }
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public boolean isPersisted(int id) {
        Setting setting = this.settingsMap.get(Integer.valueOf(id));
        return setting != null && setting.isPersisted();
    }

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public SpdySettingsFrame setPersisted(int id, boolean persisted) {
        Setting setting = this.settingsMap.get(Integer.valueOf(id));
        if (setting != null) {
            setting.setPersisted(persisted);
        }
        return this;
    }

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public boolean clearPreviouslyPersistedSettings() {
        return this.clear;
    }

    @Override // io.netty.handler.codec.spdy.SpdySettingsFrame
    public SpdySettingsFrame setClearPreviouslyPersistedSettings(boolean clear) {
        this.clear = clear;
        return this;
    }

    private Set<Map.Entry<Integer, Setting>> getSettings() {
        return this.settingsMap.entrySet();
    }

    private void appendSettings(StringBuilder buf) {
        for (Map.Entry<Integer, Setting> e : getSettings()) {
            Setting setting = e.getValue();
            buf.append("--> ");
            buf.append(e.getKey());
            buf.append(':');
            buf.append(setting.getValue());
            buf.append(" (persist value: ");
            buf.append(setting.isPersist());
            buf.append("; persisted: ");
            buf.append(setting.isPersisted());
            buf.append(')');
            buf.append(StringUtil.NEWLINE);
        }
    }

    public String toString() {
        StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append(StringUtil.NEWLINE);
        appendSettings(buf);
        buf.setLength(buf.length() - StringUtil.NEWLINE.length());
        return buf.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class Setting {
        private boolean persist;
        private boolean persisted;
        private int value;

        Setting(int value, boolean persist, boolean persisted) {
            this.value = value;
            this.persist = persist;
            this.persisted = persisted;
        }

        int getValue() {
            return this.value;
        }

        void setValue(int value) {
            this.value = value;
        }

        boolean isPersist() {
            return this.persist;
        }

        void setPersist(boolean persist) {
            this.persist = persist;
        }

        boolean isPersisted() {
            return this.persisted;
        }

        void setPersisted(boolean persisted) {
            this.persisted = persisted;
        }
    }
}
