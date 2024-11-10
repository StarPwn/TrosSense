package io.netty.handler.codec.mqtt;

import io.netty.util.collection.IntObjectHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/* loaded from: classes4.dex */
public final class MqttProperties {
    public static final MqttProperties NO_PROPERTIES = new MqttProperties(false);
    private final boolean canModify;
    private IntObjectHashMap<MqttProperty> props;
    private List<IntegerProperty> subscriptionIds;
    private List<UserProperty> userProperties;

    /* loaded from: classes4.dex */
    public enum MqttPropertyType {
        PAYLOAD_FORMAT_INDICATOR(1),
        REQUEST_PROBLEM_INFORMATION(23),
        REQUEST_RESPONSE_INFORMATION(25),
        MAXIMUM_QOS(36),
        RETAIN_AVAILABLE(37),
        WILDCARD_SUBSCRIPTION_AVAILABLE(40),
        SUBSCRIPTION_IDENTIFIER_AVAILABLE(41),
        SHARED_SUBSCRIPTION_AVAILABLE(42),
        SERVER_KEEP_ALIVE(19),
        RECEIVE_MAXIMUM(33),
        TOPIC_ALIAS_MAXIMUM(34),
        TOPIC_ALIAS(35),
        PUBLICATION_EXPIRY_INTERVAL(2),
        SESSION_EXPIRY_INTERVAL(17),
        WILL_DELAY_INTERVAL(24),
        MAXIMUM_PACKET_SIZE(39),
        SUBSCRIPTION_IDENTIFIER(11),
        CONTENT_TYPE(3),
        RESPONSE_TOPIC(8),
        ASSIGNED_CLIENT_IDENTIFIER(18),
        AUTHENTICATION_METHOD(21),
        RESPONSE_INFORMATION(26),
        SERVER_REFERENCE(28),
        REASON_STRING(31),
        USER_PROPERTY(38),
        CORRELATION_DATA(9),
        AUTHENTICATION_DATA(22);

        private static final MqttPropertyType[] VALUES = new MqttPropertyType[43];
        private final int value;

        static {
            for (MqttPropertyType v : values()) {
                VALUES[v.value] = v;
            }
        }

        MqttPropertyType(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static MqttPropertyType valueOf(int type) {
            MqttPropertyType t = null;
            try {
                t = VALUES[type];
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            if (t == null) {
                throw new IllegalArgumentException("unknown property type: " + type);
            }
            return t;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MqttProperties withEmptyDefaults(MqttProperties properties) {
        if (properties == null) {
            return NO_PROPERTIES;
        }
        return properties;
    }

    /* loaded from: classes4.dex */
    public static abstract class MqttProperty<T> {
        final int propertyId;
        final T value;

        protected MqttProperty(int propertyId, T value) {
            this.propertyId = propertyId;
            this.value = value;
        }

        public T value() {
            return this.value;
        }

        public int propertyId() {
            return this.propertyId;
        }

        public int hashCode() {
            return this.propertyId + (this.value.hashCode() * 31);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            MqttProperty that = (MqttProperty) obj;
            if (this.propertyId == that.propertyId && this.value.equals(that.value)) {
                return true;
            }
            return false;
        }
    }

    /* loaded from: classes4.dex */
    public static final class IntegerProperty extends MqttProperty<Integer> {
        public IntegerProperty(int propertyId, Integer value) {
            super(propertyId, value);
        }

        public String toString() {
            return "IntegerProperty(" + this.propertyId + ", " + this.value + ")";
        }
    }

    /* loaded from: classes4.dex */
    public static final class StringProperty extends MqttProperty<String> {
        public StringProperty(int propertyId, String value) {
            super(propertyId, value);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public String toString() {
            return "StringProperty(" + this.propertyId + ", " + ((String) this.value) + ")";
        }
    }

    /* loaded from: classes4.dex */
    public static final class StringPair {
        public final String key;
        public final String value;

        public StringPair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public int hashCode() {
            return this.key.hashCode() + (this.value.hashCode() * 31);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            StringPair that = (StringPair) obj;
            if (that.key.equals(this.key) && that.value.equals(this.value)) {
                return true;
            }
            return false;
        }
    }

    /* loaded from: classes4.dex */
    public static final class UserProperties extends MqttProperty<List<StringPair>> {
        public UserProperties() {
            super(MqttPropertyType.USER_PROPERTY.value, new ArrayList());
        }

        public UserProperties(Collection<StringPair> values) {
            this();
            ((List) this.value).addAll(values);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Multi-variable type inference failed */
        public static UserProperties fromUserPropertyCollection(Collection<UserProperty> properties) {
            UserProperties userProperties = new UserProperties();
            for (UserProperty property : properties) {
                userProperties.add(new StringPair(((StringPair) property.value).key, ((StringPair) property.value).value));
            }
            return userProperties;
        }

        public void add(StringPair pair) {
            ((List) this.value).add(pair);
        }

        public void add(String key, String value) {
            ((List) this.value).add(new StringPair(key, value));
        }

        public String toString() {
            StringBuilder builder = new StringBuilder("UserProperties(");
            boolean first = true;
            for (StringPair pair : (List) this.value) {
                if (!first) {
                    builder.append(", ");
                }
                builder.append(pair.key + "->" + pair.value);
                first = false;
            }
            builder.append(")");
            return builder.toString();
        }
    }

    /* loaded from: classes4.dex */
    public static final class UserProperty extends MqttProperty<StringPair> {
        public UserProperty(String key, String value) {
            super(MqttPropertyType.USER_PROPERTY.value, new StringPair(key, value));
        }

        /* JADX WARN: Multi-variable type inference failed */
        public String toString() {
            return "UserProperty(" + ((StringPair) this.value).key + ", " + ((StringPair) this.value).value + ")";
        }
    }

    /* loaded from: classes4.dex */
    public static final class BinaryProperty extends MqttProperty<byte[]> {
        public BinaryProperty(int propertyId, byte[] value) {
            super(propertyId, value);
        }

        /* JADX WARN: Multi-variable type inference failed */
        public String toString() {
            return "BinaryProperty(" + this.propertyId + ", " + ((byte[]) this.value).length + " bytes)";
        }
    }

    public MqttProperties() {
        this(true);
    }

    private MqttProperties(boolean canModify) {
        this.canModify = canModify;
    }

    public void add(MqttProperty property) {
        if (!this.canModify) {
            throw new UnsupportedOperationException("adding property isn't allowed");
        }
        IntObjectHashMap<MqttProperty> props = this.props;
        if (property.propertyId != MqttPropertyType.USER_PROPERTY.value) {
            if (property.propertyId == MqttPropertyType.SUBSCRIPTION_IDENTIFIER.value) {
                List<IntegerProperty> subscriptionIds = this.subscriptionIds;
                if (subscriptionIds == null) {
                    subscriptionIds = new ArrayList(1);
                    this.subscriptionIds = subscriptionIds;
                }
                if (property instanceof IntegerProperty) {
                    subscriptionIds.add((IntegerProperty) property);
                    return;
                }
                throw new IllegalArgumentException("Subscription ID must be an integer property");
            }
            if (props == null) {
                props = new IntObjectHashMap<>();
                this.props = props;
            }
            props.put(property.propertyId, (int) property);
            return;
        }
        List<UserProperty> userProperties = this.userProperties;
        if (userProperties == null) {
            userProperties = new ArrayList(1);
            this.userProperties = userProperties;
        }
        if (property instanceof UserProperty) {
            userProperties.add((UserProperty) property);
        } else {
            if (property instanceof UserProperties) {
                for (StringPair pair : (List) ((UserProperties) property).value) {
                    userProperties.add(new UserProperty(pair.key, pair.value));
                }
                return;
            }
            throw new IllegalArgumentException("User property must be of UserProperty or UserProperties type");
        }
    }

    public Collection<? extends MqttProperty> listAll() {
        IntObjectHashMap<MqttProperty> props = this.props;
        if (props == null && this.subscriptionIds == null && this.userProperties == null) {
            return Collections.emptyList();
        }
        if (this.subscriptionIds == null && this.userProperties == null) {
            return props.values();
        }
        if (props == null && this.userProperties == null) {
            return this.subscriptionIds;
        }
        List<MqttProperty> propValues = new ArrayList<>(props != null ? props.size() : 1);
        if (props != null) {
            propValues.addAll(props.values());
        }
        if (this.subscriptionIds != null) {
            propValues.addAll(this.subscriptionIds);
        }
        if (this.userProperties != null) {
            propValues.add(UserProperties.fromUserPropertyCollection(this.userProperties));
        }
        return propValues;
    }

    public boolean isEmpty() {
        IntObjectHashMap<MqttProperty> props = this.props;
        return props == null || props.isEmpty();
    }

    public MqttProperty getProperty(int propertyId) {
        if (propertyId != MqttPropertyType.USER_PROPERTY.value) {
            if (propertyId == MqttPropertyType.SUBSCRIPTION_IDENTIFIER.value) {
                List<IntegerProperty> subscriptionIds = this.subscriptionIds;
                if (subscriptionIds == null || subscriptionIds.isEmpty()) {
                    return null;
                }
                return subscriptionIds.get(0);
            }
            IntObjectHashMap<MqttProperty> props = this.props;
            if (props == null) {
                return null;
            }
            return props.get(propertyId);
        }
        List<UserProperty> userProperties = this.userProperties;
        if (userProperties == null) {
            return null;
        }
        return UserProperties.fromUserPropertyCollection(userProperties);
    }

    public List<? extends MqttProperty> getProperties(int propertyId) {
        if (propertyId == MqttPropertyType.USER_PROPERTY.value) {
            return this.userProperties == null ? Collections.emptyList() : this.userProperties;
        }
        if (propertyId == MqttPropertyType.SUBSCRIPTION_IDENTIFIER.value) {
            return this.subscriptionIds == null ? Collections.emptyList() : this.subscriptionIds;
        }
        IntObjectHashMap<MqttProperty> props = this.props;
        if (props == null || !props.containsKey(propertyId)) {
            return Collections.emptyList();
        }
        return Collections.singletonList(props.get(propertyId));
    }
}
