package io.airlift.compress.zstd;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class HuffmanCompressionContext {
    private final HuffmanTableWriterWorkspace tableWriterWorkspace = new HuffmanTableWriterWorkspace();
    private final HuffmanCompressionTableWorkspace compressionTableWorkspace = new HuffmanCompressionTableWorkspace();
    private HuffmanCompressionTable previousTable = new HuffmanCompressionTable(256);
    private HuffmanCompressionTable temporaryTable = new HuffmanCompressionTable(256);
    private HuffmanCompressionTable previousCandidate = this.previousTable;
    private HuffmanCompressionTable temporaryCandidate = this.temporaryTable;

    public HuffmanCompressionTable getPreviousTable() {
        return this.previousTable;
    }

    public HuffmanCompressionTable borrowTemporaryTable() {
        this.previousCandidate = this.temporaryTable;
        this.temporaryCandidate = this.previousTable;
        return this.temporaryTable;
    }

    public void discardTemporaryTable() {
        this.previousCandidate = this.previousTable;
        this.temporaryCandidate = this.temporaryTable;
    }

    public void saveChanges() {
        this.temporaryTable = this.temporaryCandidate;
        this.previousTable = this.previousCandidate;
    }

    public HuffmanCompressionTableWorkspace getCompressionTableWorkspace() {
        return this.compressionTableWorkspace;
    }

    public HuffmanTableWriterWorkspace getTableWriterWorkspace() {
        return this.tableWriterWorkspace;
    }
}
