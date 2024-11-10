package com.google.common.collect;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import java.util.LinkedHashMap;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
    static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
    private final int[] cellColumnInRowIndices;
    private final int[] cellRowIndices;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final ImmutableMap<R, Map<C, V>> rowMap;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SparseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
        Map<R, Integer> rowIndex = Maps.indexMap(rowSpace);
        Map<R, Map<C, V>> rows = Maps.newLinkedHashMap();
        UnmodifiableIterator<R> it2 = rowSpace.iterator();
        while (it2.hasNext()) {
            rows.put(it2.next(), new LinkedHashMap<>());
        }
        Map<C, Map<R, V>> columns = Maps.newLinkedHashMap();
        UnmodifiableIterator<C> it3 = columnSpace.iterator();
        while (it3.hasNext()) {
            columns.put(it3.next(), new LinkedHashMap<>());
        }
        int[] cellRowIndices = new int[cellList.size()];
        int[] cellColumnInRowIndices = new int[cellList.size()];
        for (int i = 0; i < cellList.size(); i++) {
            Table.Cell<R, C, V> cell = cellList.get(i);
            R rowKey = cell.getRowKey();
            C columnKey = cell.getColumnKey();
            V value = cell.getValue();
            cellRowIndices[i] = rowIndex.get(rowKey).intValue();
            Map<C, V> thisRow = rows.get(rowKey);
            cellColumnInRowIndices[i] = thisRow.size();
            V oldValue = thisRow.put(columnKey, value);
            if (oldValue != null) {
                throw new IllegalArgumentException("Duplicate value for row=" + rowKey + ", column=" + columnKey + ": " + value + ", " + oldValue);
            }
            columns.get(columnKey).put(rowKey, value);
        }
        this.cellRowIndices = cellRowIndices;
        this.cellColumnInRowIndices = cellColumnInRowIndices;
        ImmutableMap.Builder<R, Map<C, V>> rowBuilder = new ImmutableMap.Builder<>(rows.size());
        for (Map.Entry<R, Map<C, V>> row : rows.entrySet()) {
            rowBuilder.put(row.getKey(), ImmutableMap.copyOf((Map) row.getValue()));
        }
        this.rowMap = rowBuilder.build();
        ImmutableMap.Builder<C, Map<R, V>> columnBuilder = new ImmutableMap.Builder<>(columns.size());
        for (Map.Entry<C, Map<R, V>> col : columns.entrySet()) {
            columnBuilder.put(col.getKey(), ImmutableMap.copyOf((Map) col.getValue()));
        }
        this.columnMap = columnBuilder.build();
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public ImmutableMap<C, Map<R, V>> columnMap() {
        return this.columnMap;
    }

    @Override // com.google.common.collect.ImmutableTable, com.google.common.collect.Table
    public ImmutableMap<R, Map<C, V>> rowMap() {
        return this.rowMap;
    }

    @Override // com.google.common.collect.Table
    public int size() {
        return this.cellRowIndices.length;
    }

    @Override // com.google.common.collect.RegularImmutableTable
    Table.Cell<R, C, V> getCell(int index) {
        int rowIndex = this.cellRowIndices[index];
        Map.Entry<R, Map<C, V>> rowEntry = this.rowMap.entrySet().asList().get(rowIndex);
        ImmutableMap<C, V> row = (ImmutableMap) rowEntry.getValue();
        int columnIndex = this.cellColumnInRowIndices[index];
        Map.Entry<C, V> colEntry = row.entrySet().asList().get(columnIndex);
        return cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
    }

    @Override // com.google.common.collect.RegularImmutableTable
    V getValue(int index) {
        int rowIndex = this.cellRowIndices[index];
        ImmutableMap<C, V> row = (ImmutableMap) this.rowMap.values().asList().get(rowIndex);
        int columnIndex = this.cellColumnInRowIndices[index];
        return row.values().asList().get(columnIndex);
    }

    @Override // com.google.common.collect.ImmutableTable
    ImmutableTable.SerializedForm createSerializedForm() {
        Map<C, Integer> columnKeyToIndex = Maps.indexMap(columnKeySet());
        int[] cellColumnIndices = new int[cellSet().size()];
        int i = 0;
        UnmodifiableIterator<Table.Cell<R, C, V>> it2 = cellSet().iterator();
        while (it2.hasNext()) {
            Table.Cell<R, C, V> cell = it2.next();
            cellColumnIndices[i] = columnKeyToIndex.get(cell.getColumnKey()).intValue();
            i++;
        }
        return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, cellColumnIndices);
    }
}
