package io.netty.util.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.PlatformDependent;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/* loaded from: classes4.dex */
public class FastThreadLocal<V> {
    private final int index = InternalThreadLocalMap.nextVariableIndex();

    public static void removeAll() {
        InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
        if (threadLocalMap == null) {
            return;
        }
        try {
            Object v = threadLocalMap.indexedVariable(InternalThreadLocalMap.VARIABLES_TO_REMOVE_INDEX);
            if (v != null && v != InternalThreadLocalMap.UNSET) {
                Set<FastThreadLocal<?>> variablesToRemove = (Set) v;
                FastThreadLocal<?>[] variablesToRemoveArray = (FastThreadLocal[]) variablesToRemove.toArray(new FastThreadLocal[0]);
                for (FastThreadLocal<?> tlv : variablesToRemoveArray) {
                    tlv.remove(threadLocalMap);
                }
            }
        } finally {
            InternalThreadLocalMap.remove();
        }
    }

    public static int size() {
        InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
        if (threadLocalMap == null) {
            return 0;
        }
        return threadLocalMap.size();
    }

    public static void destroy() {
        InternalThreadLocalMap.destroy();
    }

    private static void addToVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable) {
        Set<FastThreadLocal<?>> variablesToRemove;
        Object v = threadLocalMap.indexedVariable(InternalThreadLocalMap.VARIABLES_TO_REMOVE_INDEX);
        if (v == InternalThreadLocalMap.UNSET || v == null) {
            variablesToRemove = Collections.newSetFromMap(new IdentityHashMap());
            threadLocalMap.setIndexedVariable(InternalThreadLocalMap.VARIABLES_TO_REMOVE_INDEX, variablesToRemove);
        } else {
            variablesToRemove = (Set) v;
        }
        variablesToRemove.add(variable);
    }

    private static void removeFromVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable) {
        Object v = threadLocalMap.indexedVariable(InternalThreadLocalMap.VARIABLES_TO_REMOVE_INDEX);
        if (v == InternalThreadLocalMap.UNSET || v == null) {
            return;
        }
        Set<FastThreadLocal<?>> variablesToRemove = (Set) v;
        variablesToRemove.remove(variable);
    }

    public final V get() {
        InternalThreadLocalMap internalThreadLocalMap = InternalThreadLocalMap.get();
        V v = (V) internalThreadLocalMap.indexedVariable(this.index);
        if (v != InternalThreadLocalMap.UNSET) {
            return v;
        }
        return initialize(internalThreadLocalMap);
    }

    public final V getIfExists() {
        V v;
        InternalThreadLocalMap ifSet = InternalThreadLocalMap.getIfSet();
        if (ifSet != null && (v = (V) ifSet.indexedVariable(this.index)) != InternalThreadLocalMap.UNSET) {
            return v;
        }
        return null;
    }

    public final V get(InternalThreadLocalMap internalThreadLocalMap) {
        V v = (V) internalThreadLocalMap.indexedVariable(this.index);
        if (v != InternalThreadLocalMap.UNSET) {
            return v;
        }
        return initialize(internalThreadLocalMap);
    }

    private V initialize(InternalThreadLocalMap threadLocalMap) {
        V v = null;
        try {
            v = initialValue();
        } catch (Exception e) {
            PlatformDependent.throwException(e);
        }
        if (v == InternalThreadLocalMap.UNSET) {
            throw new IllegalArgumentException("InternalThreadLocalMap.UNSET can not be initial value.");
        }
        threadLocalMap.setIndexedVariable(this.index, v);
        addToVariablesToRemove(threadLocalMap, this);
        return v;
    }

    public final void set(V value) {
        if (value != InternalThreadLocalMap.UNSET) {
            InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
            setKnownNotUnset(threadLocalMap, value);
        } else {
            remove();
        }
    }

    public final void set(InternalThreadLocalMap threadLocalMap, V value) {
        if (value != InternalThreadLocalMap.UNSET) {
            setKnownNotUnset(threadLocalMap, value);
        } else {
            remove(threadLocalMap);
        }
    }

    private void setKnownNotUnset(InternalThreadLocalMap threadLocalMap, V value) {
        if (threadLocalMap.setIndexedVariable(this.index, value)) {
            addToVariablesToRemove(threadLocalMap, this);
        }
    }

    public final boolean isSet() {
        return isSet(InternalThreadLocalMap.getIfSet());
    }

    public final boolean isSet(InternalThreadLocalMap threadLocalMap) {
        return threadLocalMap != null && threadLocalMap.isIndexedVariableSet(this.index);
    }

    public final void remove() {
        remove(InternalThreadLocalMap.getIfSet());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void remove(InternalThreadLocalMap threadLocalMap) {
        Object v;
        if (threadLocalMap != null && (v = threadLocalMap.removeIndexedVariable(this.index)) != InternalThreadLocalMap.UNSET) {
            removeFromVariablesToRemove(threadLocalMap, this);
            try {
                onRemoval(v);
            } catch (Exception e) {
                PlatformDependent.throwException(e);
            }
        }
    }

    protected V initialValue() throws Exception {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onRemoval(V value) throws Exception {
    }
}
