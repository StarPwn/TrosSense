package org.cloudburstmc.protocol.bedrock.codec;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataFormat;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataType;
import org.cloudburstmc.protocol.bedrock.transformer.EntityDataTransformer;
import org.cloudburstmc.protocol.common.util.Preconditions;

/* loaded from: classes5.dex */
public class EntityDataTypeMap {
    private static final EntityDataFormat[] FORMATS = EntityDataFormat.values();
    private final Definition<?>[][][] idDefinitions;
    private final Map<EntityDataType<?>, Definition<?>> typeDefinitionMap;

    private EntityDataTypeMap(Definition<?>[][][] idDefinitions, Map<EntityDataType<?>, Definition<?>> typeDefinitionMap) {
        this.idDefinitions = idDefinitions;
        this.typeDefinitionMap = typeDefinitionMap;
    }

    public Definition<?>[] fromId(int id, EntityDataFormat format) {
        if (id >= 0 && id < this.idDefinitions.length && format != null) {
            Definition<?>[][] definitions = this.idDefinitions[id];
            int formatId = format.ordinal();
            if (definitions != null && formatId < definitions.length) {
                return definitions[formatId];
            }
            return null;
        }
        return null;
    }

    public <T> Definition<T> fromType(EntityDataType<T> type) {
        return (Definition) this.typeDefinitionMap.get(type);
    }

    public Builder toBuilder() {
        final Builder builder = new Builder(copy(this.idDefinitions));
        this.typeDefinitionMap.forEach(new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap$$ExternalSyntheticLambda1
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                EntityDataTypeMap.lambda$toBuilder$0(EntityDataTypeMap.Builder.this, (EntityDataType) obj, (EntityDataTypeMap.Definition) obj2);
            }
        });
        return builder;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$toBuilder$0(Builder builder, EntityDataType type, Definition def) {
    }

    public static Builder builder() {
        return new Builder(new Definition[64][]);
    }

    public String prettyPrint() {
        final TreeMap<Integer, EntityDataType<?>> map = new TreeMap<>();
        this.typeDefinitionMap.forEach(new BiConsumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                EntityDataTypeMap.lambda$prettyPrint$1(map, (EntityDataType) obj, (EntityDataTypeMap.Definition) obj2);
            }
        });
        StringJoiner joiner = new StringJoiner("\n");
        for (Map.Entry<Integer, EntityDataType<?>> entry : map.entrySet()) {
            joiner.add(entry.getKey() + " => " + entry.getValue());
        }
        return joiner.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$prettyPrint$1(TreeMap map, EntityDataType type, Definition def) {
    }

    /* loaded from: classes5.dex */
    public static class Definition<T> {
        final EntityDataFormat format;
        int id;
        EntityDataTransformer<?, T> transformer;
        final EntityDataType<T> type;

        public String toString() {
            return "EntityDataTypeMap.Definition(id=" + getId() + ", type=" + getType() + ", transformer=" + getTransformer() + ", format=" + getFormat() + ")";
        }

        private Definition(int id, EntityDataType<T> type, EntityDataTransformer<?, T> transformer, EntityDataFormat format) {
            this.id = id;
            this.type = type;
            this.transformer = transformer;
            this.format = format;
        }

        public int getId() {
            return this.id;
        }

        public EntityDataType<T> getType() {
            return this.type;
        }

        public EntityDataTransformer<?, T> getTransformer() {
            return this.transformer;
        }

        public EntityDataFormat getFormat() {
            return this.format;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Definition<T> copy() {
            return new Definition<>(this.id, this.type, this.transformer, this.format);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int powerOfTwoCeiling(int value) {
        int value2 = value - 1;
        int value3 = value2 | (value2 >> 1);
        int value4 = value3 | (value3 >> 2);
        int value5 = value4 | (value4 >> 4);
        int value6 = value5 | (value5 >> 8);
        return (value6 | (value6 >> 16)) + 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Definition<?>[][][] copy(Definition<?>[][][] array) {
        Definition<?>[][][] copy = new Definition[lastNonNullIndex(array) + 1][];
        for (int i = 0; i < copy.length; i++) {
            if (array[i] != null) {
                copy[i] = new Definition[FORMATS.length];
                for (int i2 = 0; i2 < copy[i].length && i2 < array[i].length; i2++) {
                    if (array[i][i2] != null) {
                        int length = array[i][i2].length;
                        copy[i][i2] = new Definition[length];
                        for (int index = 0; index < copy[i][i2].length; index++) {
                            copy[i][i2][index] = array[i][i2][index].copy();
                        }
                    }
                }
            }
        }
        return copy;
    }

    private static <T> int lastNonNullIndex(T[] array) {
        int index = array.length;
        do {
            index--;
            if (array[index] != null) {
                break;
            }
        } while (index > 0);
        return index;
    }

    /* loaded from: classes5.dex */
    public static class Builder {
        private final Map<EntityDataType<?>, Definition<?>> typeDefinitionMap;
        private Definition<?>[][][] types;

        private Builder(Definition<?>[][][] types) {
            this.typeDefinitionMap = new IdentityHashMap();
            this.types = types;
        }

        private void ensureIndex(int index) {
            ensureCapacity(index + 1);
        }

        private void ensureCapacity(int size) {
            if (size > this.types.length) {
                int newSize = EntityDataTypeMap.powerOfTwoCeiling(size + 1);
                Definition<?>[][][] newTypes = new Definition[newSize][];
                System.arraycopy(this.types, 0, newTypes, 0, this.types.length);
                this.types = newTypes;
            }
        }

        public <T> Builder replace(EntityDataType<T> type, int index, EntityDataFormat format) {
            return replace(type, index, format, EntityDataTransformer.identity());
        }

        public <T> Builder replace(EntityDataType<T> type, int index, EntityDataFormat format, EntityDataTransformer<?, T> transformer) {
            Preconditions.checkArgument(index < this.types.length, "Index is out of bounds");
            Preconditions.checkArgument(this.types[index] != null, "No data types to replace at %s", index);
            iterateIndex(index, new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap$Builder$$ExternalSyntheticLambda3
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    EntityDataTypeMap.Builder.this.m2059x3712d47b((EntityDataTypeMap.Definition) obj);
                }
            });
            this.types[index] = null;
            return insert(type, index, format, transformer);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$replace$0$org-cloudburstmc-protocol-bedrock-codec-EntityDataTypeMap$Builder, reason: not valid java name */
        public /* synthetic */ void m2059x3712d47b(Definition definition) {
            this.typeDefinitionMap.remove(definition.type);
        }

        public <T> Builder insert(EntityDataType<T> type, int index, EntityDataFormat format) {
            return insert(type, index, format, EntityDataTransformer.identity());
        }

        public <T> Builder insert(EntityDataType<T> type, int index, EntityDataFormat format, EntityDataTransformer<?, T> transformer) {
            Definition<?>[] definitions;
            Preconditions.checkNotNull(type, "type");
            Preconditions.checkNotNull(transformer, "transformer");
            Preconditions.checkNotNull(format, "format");
            Preconditions.checkArgument(index >= 0, "index cannot be negative");
            Preconditions.checkArgument(!this.typeDefinitionMap.containsKey(type), "type already defined");
            ensureIndex(index + 1);
            if (this.types[index] == null) {
                this.types[index] = new Definition[EntityDataTypeMap.FORMATS.length];
            }
            Definition<?>[][] formats = this.types[index];
            int formatId = format.ordinal();
            Definition<?>[] definitions2 = formats[formatId];
            if (definitions2 != null) {
                Definition<?>[] definitionArr = (Definition[]) Arrays.copyOf(definitions2, definitions2.length + 1);
                formats[formatId] = definitionArr;
                definitions = definitionArr;
            } else {
                Definition<?>[] definitionArr2 = new Definition[1];
                formats[formatId] = definitionArr2;
                definitions = definitionArr2;
            }
            int length = definitions.length - 1;
            Definition<?> definition = new Definition<>(index, type, transformer, format);
            definitions[length] = definition;
            this.typeDefinitionMap.put(type, definition);
            return this;
        }

        public Builder shift(int startIndex, int delta) {
            return shift(startIndex, delta, this.types.length - startIndex);
        }

        public Builder shift(int startIndex, int delta, int length) {
            Preconditions.checkArgument(startIndex < this.types.length, "Start index is out of bounds (%s >= %s)", startIndex, this.types.length);
            int endIndex = startIndex + length;
            Preconditions.checkArgument(endIndex <= this.types.length, "Length exceeds array bounds");
            ensureCapacity(this.types.length + delta);
            System.arraycopy(this.types, startIndex, this.types, startIndex + delta, length);
            for (int i = 0; i < delta; i++) {
                this.types[startIndex + i] = null;
            }
            for (int i2 = 0; i2 < length; i2++) {
                final int index = startIndex + delta + i2;
                Definition<?>[][] formats = this.types[index];
                if (formats != null) {
                    iterateIndex(index, new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap$Builder$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            EntityDataTypeMap.Builder.this.m2060xe30a4fca(index, (EntityDataTypeMap.Definition) obj);
                        }
                    });
                }
            }
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$shift$1$org-cloudburstmc-protocol-bedrock-codec-EntityDataTypeMap$Builder, reason: not valid java name */
        public /* synthetic */ void m2060xe30a4fca(int index, Definition definition) {
            definition.id = index;
            Definition<?> def = this.typeDefinitionMap.get(definition.type);
            if (def != null) {
                def.id = index;
            }
        }

        public Builder remove(int index) {
            Preconditions.checkElementIndex(index, this.types.length);
            Preconditions.checkArgument(this.types[index] != null, "Cannot remove null value");
            iterateIndex(index, new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap$Builder$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    EntityDataTypeMap.Builder.this.m2058x8ab9b427((EntityDataTypeMap.Definition) obj);
                }
            });
            this.types[index] = null;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* renamed from: lambda$remove$2$org-cloudburstmc-protocol-bedrock-codec-EntityDataTypeMap$Builder, reason: not valid java name */
        public /* synthetic */ void m2058x8ab9b427(Definition definition) {
            this.typeDefinitionMap.remove(definition.type);
        }

        public <T> Builder update(EntityDataType<T> type, final EntityDataTransformer<?, T> transformer) {
            Preconditions.checkNotNull(type, "type");
            Preconditions.checkNotNull(transformer, "transformer");
            final Definition<?> definition = this.typeDefinitionMap.get(type);
            Preconditions.checkArgument(definition != null, "type not defined");
            definition.transformer = transformer;
            iterateIndex(definition.getId(), new Consumer() { // from class: org.cloudburstmc.protocol.bedrock.codec.EntityDataTypeMap$Builder$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    EntityDataTypeMap.Builder.lambda$update$3(EntityDataTypeMap.Definition.this, transformer, (EntityDataTypeMap.Definition) obj);
                }
            });
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$update$3(Definition definition, EntityDataTransformer transformer, Definition def) {
            if (def.getFormat() == definition.getFormat()) {
                def.transformer = transformer;
            }
        }

        private void iterateIndex(int index, Consumer<Definition<?>> consumer) {
            Definition<?>[][] formats = this.types[index];
            if (formats == null) {
                return;
            }
            for (Definition<?>[] format : formats) {
                if (format != null) {
                    for (Definition<?> definition : format) {
                        if (definition != null) {
                            consumer.accept(definition);
                        }
                    }
                }
            }
        }

        public EntityDataTypeMap build() {
            return new EntityDataTypeMap(EntityDataTypeMap.copy(this.types), this.typeDefinitionMap);
        }
    }
}
