package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Table;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
    abstract Table.Cell<R, C, V> getCell(int i);

    abstract V getValue(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    public final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
        return isEmpty() ? ImmutableSet.of() : new CellSet();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class CellSet extends ImmutableSet.Indexed<Table.Cell<R, C, V>> {
        private CellSet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return RegularImmutableTable.this.size();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableSet.Indexed
        public Table.Cell<R, C, V> get(int index) {
            return RegularImmutableTable.this.getCell(index);
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(@Nullable Object object) {
            if (!(object instanceof Table.Cell)) {
                return false;
            }
            Table.Cell<?, ?, ?> cell = (Table.Cell) object;
            Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
            return value != null && value.equals(cell.getValue());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.AbstractTable
    public final ImmutableCollection<V> createValues() {
        return isEmpty() ? ImmutableList.of() : new Values();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class Values extends ImmutableList<V> {
        private Values() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return RegularImmutableTable.this.size();
        }

        @Override // java.util.List
        public V get(int i) {
            return (V) RegularImmutableTable.this.getValue(i);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableCollection
        public boolean isPartialView() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, @Nullable final Comparator<? super R> rowComparator, @Nullable final Comparator<? super C> columnComparator) {
        Preconditions.checkNotNull(cells);
        if (rowComparator != null || columnComparator != null) {
            Comparator<Table.Cell<R, C, V>> comparator = new Comparator<Table.Cell<R, C, V>>() { // from class: com.google.common.collect.RegularImmutableTable.1
                @Override // java.util.Comparator
                public int compare(Table.Cell<R, C, V> cell1, Table.Cell<R, C, V> cell2) {
                    int rowCompare = rowComparator == null ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
                    if (rowCompare != 0) {
                        return rowCompare;
                    }
                    if (columnComparator == null) {
                        return 0;
                    }
                    return columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey());
                }
            };
            Collections.sort(cells, comparator);
        }
        return forCellsInternal(cells, rowComparator, columnComparator);
    }

    static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
        return forCellsInternal(cells, null, null);
    }

    private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable Comparator<? super C> columnComparator) {
        ImmutableSet<R> rowSpace;
        ImmutableSet<C> columnSpace;
        Set<R> rowSpaceBuilder = new LinkedHashSet<>();
        Set<C> columnSpaceBuilder = new LinkedHashSet<>();
        ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
        for (Table.Cell<R, C, V> cell : cells) {
            rowSpaceBuilder.add(cell.getRowKey());
            columnSpaceBuilder.add(cell.getColumnKey());
        }
        if (rowComparator == null) {
            rowSpace = ImmutableSet.copyOf((Collection) rowSpaceBuilder);
        } else {
            rowSpace = ImmutableSet.copyOf((Collection) ImmutableList.sortedCopyOf(rowComparator, rowSpaceBuilder));
        }
        if (columnComparator == null) {
            columnSpace = ImmutableSet.copyOf((Collection) columnSpaceBuilder);
        } else {
            columnSpace = ImmutableSet.copyOf((Collection) ImmutableList.sortedCopyOf(columnComparator, columnSpaceBuilder));
        }
        return forOrderedComponents(cellList, rowSpace, columnSpace);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
        return ((long) cellList.size()) > (((long) rowSpace.size()) * ((long) columnSpace.size())) / 2 ? new DenseImmutableTable(cellList, rowSpace, columnSpace) : new SparseImmutableTable(cellList, rowSpace, columnSpace);
    }
}
