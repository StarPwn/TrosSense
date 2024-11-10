package io.netty.util;

import androidx.concurrent.futures.AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0;
import io.netty.util.internal.ObjectUtil;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/* loaded from: classes4.dex */
public class DefaultAttributeMap implements AttributeMap {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final AtomicReferenceFieldUpdater<DefaultAttributeMap, DefaultAttribute[]> ATTRIBUTES_UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultAttributeMap.class, DefaultAttribute[].class, "attributes");
    private static final DefaultAttribute[] EMPTY_ATTRIBUTES = new DefaultAttribute[0];
    private volatile DefaultAttribute[] attributes = EMPTY_ATTRIBUTES;

    private static int searchAttributeByKey(DefaultAttribute[] sortedAttributes, AttributeKey<?> key) {
        int low = 0;
        int high = sortedAttributes.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            DefaultAttribute midVal = sortedAttributes[mid];
            AttributeKey midValKey = midVal.key;
            if (midValKey == key) {
                return mid;
            }
            int midValKeyId = midValKey.id();
            int keyId = key.id();
            if (midValKeyId == keyId) {
                throw new AssertionError();
            }
            boolean searchRight = midValKeyId < keyId;
            if (searchRight) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -(low + 1);
    }

    private static void orderedCopyOnInsert(DefaultAttribute[] sortedSrc, int srcLength, DefaultAttribute[] copy, DefaultAttribute toInsert) {
        int id = toInsert.key.id();
        int i = srcLength - 1;
        while (i >= 0) {
            DefaultAttribute attribute = sortedSrc[i];
            if (attribute.key.id() == id) {
                throw new AssertionError();
            }
            if (attribute.key.id() < id) {
                break;
            }
            copy[i + 1] = sortedSrc[i];
            i--;
        }
        copy[i + 1] = toInsert;
        int toCopy = i + 1;
        if (toCopy > 0) {
            System.arraycopy(sortedSrc, 0, copy, 0, toCopy);
        }
    }

    @Override // io.netty.util.AttributeMap
    public <T> Attribute<T> attr(AttributeKey<T> key) {
        DefaultAttribute[] attributes;
        DefaultAttribute[] newAttributes;
        ObjectUtil.checkNotNull(key, "key");
        DefaultAttribute newAttribute = null;
        do {
            attributes = this.attributes;
            int index = searchAttributeByKey(attributes, key);
            if (index >= 0) {
                DefaultAttribute attribute = attributes[index];
                if (attribute.key() != key) {
                    throw new AssertionError();
                }
                if (!attribute.isRemoved()) {
                    return attribute;
                }
                if (newAttribute == null) {
                    newAttribute = new DefaultAttribute(this, key);
                }
                newAttributes = (DefaultAttribute[]) Arrays.copyOf(attributes, attributes.length);
                newAttributes[index] = newAttribute;
            } else {
                if (newAttribute == null) {
                    newAttribute = new DefaultAttribute(this, key);
                }
                int count = attributes.length;
                newAttributes = new DefaultAttribute[count + 1];
                orderedCopyOnInsert(attributes, count, newAttributes, newAttribute);
            }
        } while (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(ATTRIBUTES_UPDATER, this, attributes, newAttributes));
        return newAttribute;
    }

    @Override // io.netty.util.AttributeMap
    public <T> boolean hasAttr(AttributeKey<T> key) {
        ObjectUtil.checkNotNull(key, "key");
        return searchAttributeByKey(this.attributes, key) >= 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> void removeAttributeIfMatch(AttributeKey<T> key, DefaultAttribute<T> value) {
        DefaultAttribute[] attributes;
        DefaultAttribute[] newAttributes;
        do {
            attributes = this.attributes;
            int index = searchAttributeByKey(attributes, key);
            if (index < 0) {
                return;
            }
            DefaultAttribute attribute = attributes[index];
            if (attribute.key() != key) {
                throw new AssertionError();
            }
            if (attribute != value) {
                return;
            }
            int count = attributes.length;
            int newCount = count - 1;
            newAttributes = newCount == 0 ? EMPTY_ATTRIBUTES : new DefaultAttribute[newCount];
            System.arraycopy(attributes, 0, newAttributes, 0, index);
            int remaining = (count - index) - 1;
            if (remaining > 0) {
                System.arraycopy(attributes, index + 1, newAttributes, index, remaining);
            }
        } while (!AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(ATTRIBUTES_UPDATER, this, attributes, newAttributes));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class DefaultAttribute<T> extends AtomicReference<T> implements Attribute<T> {
        private static final AtomicReferenceFieldUpdater<DefaultAttribute, DefaultAttributeMap> MAP_UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultAttribute.class, DefaultAttributeMap.class, "attributeMap");
        private static final long serialVersionUID = -2661411462200283011L;
        private volatile DefaultAttributeMap attributeMap;
        private final AttributeKey<T> key;

        DefaultAttribute(DefaultAttributeMap attributeMap, AttributeKey<T> key) {
            this.attributeMap = attributeMap;
            this.key = key;
        }

        @Override // io.netty.util.Attribute
        public AttributeKey<T> key() {
            return this.key;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isRemoved() {
            return this.attributeMap == null;
        }

        @Override // io.netty.util.Attribute
        public T setIfAbsent(T value) {
            while (!compareAndSet(null, value)) {
                T old = get();
                if (old != null) {
                    return old;
                }
            }
            return null;
        }

        @Override // io.netty.util.Attribute
        public T getAndRemove() {
            DefaultAttributeMap attributeMap = this.attributeMap;
            boolean removed = attributeMap != null && AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(MAP_UPDATER, this, attributeMap, null);
            T oldValue = getAndSet(null);
            if (removed) {
                attributeMap.removeAttributeIfMatch(this.key, this);
            }
            return oldValue;
        }

        @Override // io.netty.util.Attribute
        public void remove() {
            DefaultAttributeMap attributeMap = this.attributeMap;
            boolean removed = attributeMap != null && AbstractResolvableFuture$SafeAtomicHelper$$ExternalSyntheticBackportWithForwarding0.m(MAP_UPDATER, this, attributeMap, null);
            set(null);
            if (removed) {
                attributeMap.removeAttributeIfMatch(this.key, this);
            }
        }
    }
}
