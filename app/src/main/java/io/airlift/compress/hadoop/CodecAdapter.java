package io.airlift.compress.hadoop;

import io.airlift.compress.hadoop.CompressionInputStreamAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Seekable;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.Compressor;
import org.apache.hadoop.io.compress.Decompressor;
import org.apache.hadoop.io.compress.DoNotPool;

/* loaded from: classes.dex */
public class CodecAdapter implements Configurable, CompressionCodec {
    private Configuration conf;
    private HadoopStreams hadoopStreams;
    private final Function<Optional<Configuration>, HadoopStreams> streamsFactory;

    public CodecAdapter(Function<Optional<Configuration>, HadoopStreams> streamsFactory) {
        this.streamsFactory = (Function) Objects.requireNonNull(streamsFactory, "streamsFactory is null");
        this.hadoopStreams = streamsFactory.apply(Optional.empty());
    }

    public final Configuration getConf() {
        return this.conf;
    }

    public final void setConf(Configuration conf) {
        this.conf = conf;
        this.hadoopStreams = this.streamsFactory.apply(Optional.of(conf));
    }

    public final CompressionOutputStream createOutputStream(OutputStream out) throws IOException {
        return new CompressionOutputStreamAdapter(this.hadoopStreams.createOutputStream(out));
    }

    public final CompressionOutputStream createOutputStream(OutputStream out, Compressor compressor) throws IOException {
        if (!(compressor instanceof CompressorAdapter)) {
            throw new IllegalArgumentException("Compressor is not the compressor adapter");
        }
        return new CompressionOutputStreamAdapter(this.hadoopStreams.createOutputStream(out));
    }

    public final Class<? extends Compressor> getCompressorType() {
        return CompressorAdapter.class;
    }

    public Compressor createCompressor() {
        return new CompressorAdapter();
    }

    public final CompressionInputStream createInputStream(InputStream in) throws IOException {
        return new CompressionInputStreamAdapter(this.hadoopStreams.createInputStream(in), getPositionSupplier(in));
    }

    public final CompressionInputStream createInputStream(InputStream in, Decompressor decompressor) throws IOException {
        if (!(decompressor instanceof DecompressorAdapter)) {
            throw new IllegalArgumentException("Decompressor is not the decompressor adapter");
        }
        return new CompressionInputStreamAdapter(this.hadoopStreams.createInputStream(in), getPositionSupplier(in));
    }

    private static CompressionInputStreamAdapter.PositionSupplier getPositionSupplier(InputStream inputStream) {
        if (inputStream instanceof Seekable) {
            final Seekable seekable = (Seekable) inputStream;
            seekable.getClass();
            return new CompressionInputStreamAdapter.PositionSupplier() { // from class: io.airlift.compress.hadoop.CodecAdapter$$ExternalSyntheticLambda0
                @Override // io.airlift.compress.hadoop.CompressionInputStreamAdapter.PositionSupplier
                public final long getPosition() {
                    return seekable.getPos();
                }
            };
        }
        return new CompressionInputStreamAdapter.PositionSupplier() { // from class: io.airlift.compress.hadoop.CodecAdapter$$ExternalSyntheticLambda1
            @Override // io.airlift.compress.hadoop.CompressionInputStreamAdapter.PositionSupplier
            public final long getPosition() {
                return CodecAdapter.lambda$getPositionSupplier$0();
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ long lambda$getPositionSupplier$0() throws IOException {
        return 0L;
    }

    public final Class<? extends Decompressor> getDecompressorType() {
        return DecompressorAdapter.class;
    }

    public final Decompressor createDecompressor() {
        return new DecompressorAdapter();
    }

    public final String getDefaultExtension() {
        return this.hadoopStreams.getDefaultFileExtension();
    }

    @DoNotPool
    /* loaded from: classes.dex */
    private static class CompressorAdapter implements Compressor {
        private CompressorAdapter() {
        }

        public void setInput(byte[] b, int off, int len) {
            throw new UnsupportedOperationException("Block compressor is not supported");
        }

        public boolean needsInput() {
            throw new UnsupportedOperationException("Block compressor is not supported");
        }

        public void setDictionary(byte[] b, int off, int len) {
            throw new UnsupportedOperationException("Block compressor is not supported");
        }

        public long getBytesRead() {
            throw new UnsupportedOperationException("Block compressor is not supported");
        }

        public long getBytesWritten() {
            throw new UnsupportedOperationException("Block compressor is not supported");
        }

        public void finish() {
            throw new UnsupportedOperationException("Block compressor is not supported");
        }

        public boolean finished() {
            throw new UnsupportedOperationException("Block compressor is not supported");
        }

        public int compress(byte[] b, int off, int len) throws IOException {
            throw new UnsupportedOperationException("Block compressor is not supported");
        }

        public void reset() {
        }

        public void end() {
        }

        public void reinit(Configuration conf) {
        }
    }

    @DoNotPool
    /* loaded from: classes.dex */
    private static class DecompressorAdapter implements Decompressor {
        private DecompressorAdapter() {
        }

        public void setInput(byte[] b, int off, int len) {
            throw new UnsupportedOperationException("Block decompressor is not supported");
        }

        public boolean needsInput() {
            throw new UnsupportedOperationException("Block decompressor is not supported");
        }

        public void setDictionary(byte[] b, int off, int len) {
            throw new UnsupportedOperationException("Block decompressor is not supported");
        }

        public boolean needsDictionary() {
            throw new UnsupportedOperationException("Block decompressor is not supported");
        }

        public boolean finished() {
            throw new UnsupportedOperationException("Block decompressor is not supported");
        }

        public int decompress(byte[] b, int off, int len) throws IOException {
            throw new UnsupportedOperationException("Block decompressor is not supported");
        }

        public int getRemaining() {
            throw new UnsupportedOperationException("Block decompressor is not supported");
        }

        public void reset() {
        }

        public void end() {
        }
    }
}
