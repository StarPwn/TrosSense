package io.airlift.compress.zstd;

import io.airlift.compress.MalformedInputException;
import java.util.Arrays;
import sun.misc.Unsafe;

/* loaded from: classes.dex */
public class ZstdIncrementalFrameDecompressor {
    private FrameHeader frameHeader;
    private int inputConsumed;
    private int inputRequired;
    private int outputBufferUsed;
    private XxHash64 partialHash;
    private int requestedOutputSize;
    private final ZstdFrameDecompressor frameDecompressor = new ZstdFrameDecompressor();
    private State state = State.INITIAL;
    private int blockHeader = -1;
    private byte[] windowBase = new byte[0];
    private long windowAddress = Unsafe.ARRAY_BYTE_BASE_OFFSET;
    private long windowLimit = Unsafe.ARRAY_BYTE_BASE_OFFSET;
    private long windowPosition = Unsafe.ARRAY_BYTE_BASE_OFFSET;

    /* loaded from: classes.dex */
    private enum State {
        INITIAL,
        READ_FRAME_MAGIC,
        READ_FRAME_HEADER,
        READ_BLOCK_HEADER,
        READ_BLOCK,
        READ_BLOCK_CHECKSUM,
        FLUSH_OUTPUT
    }

    public boolean isAtStoppingPoint() {
        return this.state == State.READ_FRAME_MAGIC;
    }

    public int getInputConsumed() {
        return this.inputConsumed;
    }

    public int getOutputBufferUsed() {
        return this.outputBufferUsed;
    }

    public int getInputRequired() {
        return this.inputRequired;
    }

    public int getRequestedOutputSize() {
        return this.requestedOutputSize;
    }

    public void partialDecompress(final Object inputBase, final long inputAddress, final long inputLimit, final byte[] outputArray, final int outputOffset, final int outputLimit) {
        long input;
        int output;
        Object obj;
        int decodedSize;
        int flushableOutputSize;
        int output2;
        Object obj2 = inputBase;
        long j = inputLimit;
        byte[] bArr = outputArray;
        int i = outputLimit;
        if (this.inputRequired > j - inputAddress) {
            throw new IllegalArgumentException(String.format("Required %s input bytes, but only %s input bytes were supplied", Integer.valueOf(this.inputRequired), Long.valueOf(inputLimit - inputAddress)));
        }
        if (this.requestedOutputSize > 0 && outputOffset >= i) {
            throw new IllegalArgumentException("Not enough space in output buffer to output");
        }
        int output3 = outputOffset;
        long input2 = inputAddress;
        while (true) {
            int flushableOutputSize2 = computeFlushableOutputSize(this.frameHeader);
            if (flushableOutputSize2 <= 0) {
                input = input2;
                output = output3;
            } else {
                int freeOutputSize = i - output3;
                if (freeOutputSize > 0) {
                    int copySize = Math.min(freeOutputSize, flushableOutputSize2);
                    System.arraycopy(this.windowBase, Math.toIntExact(this.windowAddress - Unsafe.ARRAY_BYTE_BASE_OFFSET), bArr, output3, copySize);
                    if (this.partialHash != null) {
                        this.partialHash.update(bArr, output3, copySize);
                    }
                    this.windowAddress += copySize;
                    flushableOutputSize = flushableOutputSize2 - copySize;
                    output2 = output3 + copySize;
                } else {
                    flushableOutputSize = flushableOutputSize2;
                    output2 = output3;
                }
                if (flushableOutputSize <= 0) {
                    input = input2;
                    output = output2;
                } else {
                    requestOutput(inputAddress, outputOffset, input2, output2, flushableOutputSize);
                    return;
                }
            }
            Util.checkState(computeFlushableOutputSize(this.frameHeader) == 0, "Expected output to be flushed");
            if (this.state == State.READ_FRAME_MAGIC || this.state == State.INITIAL) {
                if (j - input < 4) {
                    inputRequired(inputAddress, outputOffset, input, output, 4);
                    return;
                } else {
                    this.state = State.READ_FRAME_HEADER;
                    input = ZstdFrameDecompressor.verifyMagic(obj2, input, j) + input;
                }
            }
            if (this.state != State.READ_FRAME_HEADER) {
                Util.verify(this.frameHeader != null, input, "Frame header is not set");
            } else {
                if (j - input < 1) {
                    inputRequired(inputAddress, outputOffset, input, output, 1);
                    return;
                }
                int frameHeaderSize = determineFrameHeaderSize(obj2, input, j);
                if (j - input >= frameHeaderSize) {
                    this.frameHeader = ZstdFrameDecompressor.readFrameHeader(obj2, input, j);
                    Util.verify(((long) frameHeaderSize) == this.frameHeader.headerSize, input, "Unexpected frame header size");
                    input += frameHeaderSize;
                    this.state = State.READ_BLOCK_HEADER;
                    reset();
                    if (this.frameHeader.hasChecksum) {
                        this.partialHash = new XxHash64();
                    }
                } else {
                    inputRequired(inputAddress, outputOffset, input, output, frameHeaderSize);
                    return;
                }
            }
            if (this.state == State.READ_BLOCK_HEADER) {
                long inputBufferSize = j - input;
                if (inputBufferSize < 3) {
                    inputRequired(inputAddress, outputOffset, input, output, 3);
                    return;
                }
                if (inputBufferSize >= 4) {
                    this.blockHeader = UnsafeUtil.UNSAFE.getInt(obj2, input) & 16777215;
                } else {
                    this.blockHeader = ((UnsafeUtil.UNSAFE.getByte(obj2, input + 1) & 255) << 8) | (UnsafeUtil.UNSAFE.getByte(obj2, input) & 255) | ((UnsafeUtil.UNSAFE.getByte(obj2, input + 2) & 255) << 16);
                    int expected = UnsafeUtil.UNSAFE.getInt(obj2, input) & 16777215;
                    Util.verify(this.blockHeader == expected, input, "oops");
                }
                input += 3;
                this.state = State.READ_BLOCK;
            } else {
                Util.verify(this.blockHeader != -1, input, "Block header is not set");
            }
            boolean lastBlock = (this.blockHeader & 1) != 0;
            if (this.state == State.READ_BLOCK) {
                int blockType = (this.blockHeader >>> 1) & 3;
                int blockSize = (this.blockHeader >>> 3) & 2097151;
                resizeWindowBufferIfNecessary(this.frameHeader, blockType, blockSize);
                switch (blockType) {
                    case 0:
                        long input3 = input;
                        if (inputLimit - input3 >= blockSize) {
                            Util.verify(this.windowLimit - this.windowPosition >= ((long) blockSize), input3, "window buffer is too small");
                            decodedSize = ZstdFrameDecompressor.decodeRawBlock(inputBase, input3, blockSize, this.windowBase, this.windowPosition, this.windowLimit);
                            input = input3 + blockSize;
                            break;
                        } else {
                            inputRequired(inputAddress, outputOffset, input3, output, blockSize);
                            return;
                        }
                    case 1:
                        long input4 = input;
                        if (inputLimit - input4 >= 1) {
                            Util.verify(this.windowLimit - this.windowPosition >= ((long) blockSize), input4, "window buffer is too small");
                            decodedSize = ZstdFrameDecompressor.decodeRleBlock(blockSize, inputBase, input4, this.windowBase, this.windowPosition, this.windowLimit);
                            input = input4 + 1;
                            break;
                        } else {
                            inputRequired(inputAddress, outputOffset, input4, output, 1);
                            return;
                        }
                    case 2:
                        if (j - input >= blockSize) {
                            Util.verify(this.windowLimit - this.windowPosition >= 131072, input, "window buffer is too small");
                            decodedSize = this.frameDecompressor.decodeCompressedBlock(inputBase, input, blockSize, this.windowBase, this.windowPosition, this.windowLimit, this.frameHeader.windowSize, this.windowAddress);
                            input += blockSize;
                            break;
                        } else {
                            inputRequired(inputAddress, outputOffset, input, output, blockSize);
                            return;
                        }
                    default:
                        throw Util.fail(input, "Invalid block type");
                }
                this.windowPosition += decodedSize;
                if (lastBlock) {
                    this.state = State.READ_BLOCK_CHECKSUM;
                } else {
                    this.state = State.READ_BLOCK_HEADER;
                }
            }
            if (this.state != State.READ_BLOCK_CHECKSUM) {
                obj = inputBase;
            } else {
                if (!this.frameHeader.hasChecksum) {
                    obj = inputBase;
                } else {
                    if (inputLimit - input < 4) {
                        inputRequired(inputAddress, outputOffset, input, output, 4);
                        return;
                    }
                    obj = inputBase;
                    int checksum = UnsafeUtil.UNSAFE.getInt(obj, input);
                    input += 4;
                    Util.checkState(this.partialHash != null, "Partial hash not set");
                    int pendingOutputSize = Math.toIntExact(this.windowPosition - this.windowAddress);
                    this.partialHash.update(this.windowBase, Math.toIntExact(this.windowAddress - Unsafe.ARRAY_BYTE_BASE_OFFSET), pendingOutputSize);
                    long hash = this.partialHash.hash();
                    if (checksum != ((int) hash)) {
                        throw new MalformedInputException(input, String.format("Bad checksum. Expected: %s, actual: %s", Integer.toHexString(checksum), Integer.toHexString((int) hash)));
                    }
                }
                this.state = State.READ_FRAME_MAGIC;
                this.frameHeader = null;
                this.blockHeader = -1;
            }
            input2 = input;
            j = inputLimit;
            bArr = outputArray;
            i = outputLimit;
            obj2 = obj;
            output3 = output;
        }
    }

    private void reset() {
        this.frameDecompressor.reset();
        this.windowAddress = Unsafe.ARRAY_BYTE_BASE_OFFSET;
        this.windowPosition = Unsafe.ARRAY_BYTE_BASE_OFFSET;
    }

    private int computeFlushableOutputSize(FrameHeader frameHeader) {
        return Math.max(0, Math.toIntExact((this.windowPosition - this.windowAddress) - (frameHeader == null ? 0 : frameHeader.computeRequiredOutputBufferLookBackSize())));
    }

    private void resizeWindowBufferIfNecessary(FrameHeader frameHeader, int blockType, int blockSize) {
        int maxBlockOutput;
        int newWindowSize;
        if (blockType == 0 || blockType == 1) {
            maxBlockOutput = blockSize;
        } else {
            maxBlockOutput = 131072;
        }
        if (this.windowLimit - this.windowPosition < 131072) {
            int requiredWindowSize = frameHeader.computeRequiredOutputBufferLookBackSize();
            Util.checkState(this.windowPosition - this.windowAddress <= ((long) requiredWindowSize), "Expected output to be flushed");
            int windowContentsSize = Math.toIntExact(this.windowPosition - this.windowAddress);
            if (this.windowAddress != Unsafe.ARRAY_BYTE_BASE_OFFSET) {
                System.arraycopy(this.windowBase, Math.toIntExact(this.windowAddress - Unsafe.ARRAY_BYTE_BASE_OFFSET), this.windowBase, 0, windowContentsSize);
                this.windowAddress = Unsafe.ARRAY_BYTE_BASE_OFFSET;
                this.windowPosition = this.windowAddress + windowContentsSize;
            }
            Util.checkState(this.windowAddress == ((long) Unsafe.ARRAY_BYTE_BASE_OFFSET), "Window should be packed");
            if (this.windowLimit - this.windowPosition < maxBlockOutput) {
                if (frameHeader.contentSize >= 0 && frameHeader.contentSize < requiredWindowSize) {
                    newWindowSize = Math.toIntExact(frameHeader.contentSize);
                } else {
                    newWindowSize = Math.max(windowContentsSize + maxBlockOutput, Math.min(Math.min((windowContentsSize + maxBlockOutput) * 2, Math.max(requiredWindowSize, 131072) * 4), 8519680));
                    Util.checkState(windowContentsSize + maxBlockOutput <= newWindowSize, "Computed new window size buffer is not large enough");
                }
                this.windowBase = Arrays.copyOf(this.windowBase, newWindowSize);
                this.windowLimit = Unsafe.ARRAY_BYTE_BASE_OFFSET + newWindowSize;
            }
            Util.checkState(this.windowLimit - this.windowPosition >= ((long) maxBlockOutput), "window buffer is too small");
        }
    }

    private static int determineFrameHeaderSize(final Object inputBase, final long inputAddress, final long inputLimit) {
        int i = 0;
        Util.verify(inputAddress < inputLimit, inputAddress, "Not enough input bytes");
        int frameHeaderDescriptor = UnsafeUtil.UNSAFE.getByte(inputBase, inputAddress) & 255;
        boolean singleSegment = (frameHeaderDescriptor & 32) != 0;
        int dictionaryDescriptor = frameHeaderDescriptor & 3;
        int contentSizeDescriptor = frameHeaderDescriptor >>> 6;
        int i2 = (singleSegment ? 0 : 1) + 1 + (dictionaryDescriptor == 0 ? 0 : 1 << (dictionaryDescriptor - 1));
        if (contentSizeDescriptor != 0) {
            i = 1 << contentSizeDescriptor;
        } else if (singleSegment) {
            i = 1;
        }
        return i2 + i;
    }

    private void requestOutput(long inputAddress, int outputOffset, long input, int output, int requestedOutputSize) {
        updateInputOutputState(inputAddress, outputOffset, input, output);
        Util.checkArgument(requestedOutputSize >= 0, "requestedOutputSize is negative");
        this.requestedOutputSize = requestedOutputSize;
        this.inputRequired = 0;
    }

    private void inputRequired(long inputAddress, int outputOffset, long input, int output, int inputRequired) {
        updateInputOutputState(inputAddress, outputOffset, input, output);
        Util.checkState(inputRequired >= 0, "inputRequired is negative");
        this.inputRequired = inputRequired;
        this.requestedOutputSize = 0;
    }

    private void updateInputOutputState(long inputAddress, int outputOffset, long input, int output) {
        this.inputConsumed = (int) (input - inputAddress);
        Util.checkState(this.inputConsumed >= 0, "inputConsumed is negative");
        this.outputBufferUsed = output - outputOffset;
        Util.checkState(this.outputBufferUsed >= 0, "outputBufferUsed is negative");
    }
}
