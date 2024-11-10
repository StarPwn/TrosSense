package io.netty.channel.epoll;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.unix.IovArray;
import io.netty.channel.unix.Limits;
import io.netty.channel.unix.NativeInetAddress;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class NativeDatagramPacketArray {
    private int count;
    private final NativeDatagramPacket[] packets = new NativeDatagramPacket[Limits.UIO_MAX_IOV];
    private final IovArray iovArray = new IovArray();
    private final byte[] ipv4Bytes = new byte[4];
    private final MyMessageProcessor processor = new MyMessageProcessor();

    /* JADX INFO: Access modifiers changed from: package-private */
    public NativeDatagramPacketArray() {
        for (int i = 0; i < this.packets.length; i++) {
            this.packets[i] = new NativeDatagramPacket();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean addWritable(ByteBuf buf, int index, int len) {
        return add0(buf, index, len, 0, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean add0(ByteBuf buf, int index, int len, int segmentLen, InetSocketAddress recipient) {
        if (this.count == this.packets.length) {
            return false;
        }
        if (len == 0) {
            return true;
        }
        int offset = this.iovArray.count();
        if (offset == Limits.IOV_MAX || !this.iovArray.add(buf, index, len)) {
            return false;
        }
        NativeDatagramPacket p = this.packets[this.count];
        p.init(this.iovArray.memoryAddress(offset), this.iovArray.count() - offset, segmentLen, recipient);
        this.count++;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void add(ChannelOutboundBuffer buffer, boolean connected, int maxMessagesPerWrite) throws Exception {
        this.processor.connected = connected;
        this.processor.maxMessagesPerWrite = maxMessagesPerWrite;
        buffer.forEachFlushedMessage(this.processor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int count() {
        return this.count;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public NativeDatagramPacket[] packets() {
        return this.packets;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        this.count = 0;
        this.iovArray.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void release() {
        this.iovArray.release();
    }

    /* loaded from: classes4.dex */
    private final class MyMessageProcessor implements ChannelOutboundBuffer.MessageProcessor {
        private boolean connected;
        private int maxMessagesPerWrite;

        private MyMessageProcessor() {
        }

        @Override // io.netty.channel.ChannelOutboundBuffer.MessageProcessor
        public boolean processMessage(Object msg) {
            boolean added;
            int segmentSize;
            int seg;
            if (msg instanceof DatagramPacket) {
                DatagramPacket packet = (DatagramPacket) msg;
                ByteBuf buf = (ByteBuf) packet.content();
                if ((packet instanceof io.netty.channel.unix.SegmentedDatagramPacket) && buf.readableBytes() > (seg = ((io.netty.channel.unix.SegmentedDatagramPacket) packet).segmentSize())) {
                    segmentSize = seg;
                } else {
                    segmentSize = 0;
                }
                added = NativeDatagramPacketArray.this.add0(buf, buf.readerIndex(), buf.readableBytes(), segmentSize, packet.recipient());
            } else {
                boolean added2 = msg instanceof ByteBuf;
                if (added2 && this.connected) {
                    ByteBuf buf2 = (ByteBuf) msg;
                    added = NativeDatagramPacketArray.this.add0(buf2, buf2.readerIndex(), buf2.readableBytes(), 0, null);
                } else {
                    added = false;
                }
            }
            if (!added) {
                return false;
            }
            this.maxMessagesPerWrite--;
            return this.maxMessagesPerWrite > 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static InetSocketAddress newAddress(byte[] addr, int addrLen, int port, int scopeId, byte[] ipv4Bytes) throws UnknownHostException {
        InetAddress address;
        if (addrLen == ipv4Bytes.length) {
            System.arraycopy(addr, 0, ipv4Bytes, 0, addrLen);
            address = InetAddress.getByAddress(ipv4Bytes);
        } else {
            address = Inet6Address.getByAddress((String) null, addr, scopeId);
        }
        return new InetSocketAddress(address, port);
    }

    /* loaded from: classes4.dex */
    public final class NativeDatagramPacket {
        private int count;
        private long memoryAddress;
        private int recipientAddrLen;
        private int recipientPort;
        private int recipientScopeId;
        private int segmentSize;
        private int senderAddrLen;
        private int senderPort;
        private int senderScopeId;
        private final byte[] senderAddr = new byte[16];
        private final byte[] recipientAddr = new byte[16];

        public NativeDatagramPacket() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void init(long memoryAddress, int count, int segmentSize, InetSocketAddress recipient) {
            this.memoryAddress = memoryAddress;
            this.count = count;
            this.segmentSize = segmentSize;
            this.senderScopeId = 0;
            this.senderPort = 0;
            this.senderAddrLen = 0;
            if (recipient == null) {
                this.recipientScopeId = 0;
                this.recipientPort = 0;
                this.recipientAddrLen = 0;
                return;
            }
            InetAddress address = recipient.getAddress();
            if (address instanceof Inet6Address) {
                System.arraycopy(address.getAddress(), 0, this.recipientAddr, 0, this.recipientAddr.length);
                this.recipientScopeId = ((Inet6Address) address).getScopeId();
            } else {
                NativeInetAddress.copyIpv4MappedIpv6Address(address.getAddress(), this.recipientAddr);
                this.recipientScopeId = 0;
            }
            this.recipientAddrLen = this.recipientAddr.length;
            this.recipientPort = recipient.getPort();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean hasSender() {
            return this.senderPort > 0;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public DatagramPacket newDatagramPacket(ByteBuf buffer, InetSocketAddress recipient) throws UnknownHostException {
            InetSocketAddress sender = NativeDatagramPacketArray.newAddress(this.senderAddr, this.senderAddrLen, this.senderPort, this.senderScopeId, NativeDatagramPacketArray.this.ipv4Bytes);
            if (this.recipientAddrLen != 0) {
                recipient = NativeDatagramPacketArray.newAddress(this.recipientAddr, this.recipientAddrLen, this.recipientPort, this.recipientScopeId, NativeDatagramPacketArray.this.ipv4Bytes);
            }
            ByteBuf slice = buffer.retainedSlice(buffer.readerIndex(), this.count);
            if (this.segmentSize > 0) {
                return new io.netty.channel.unix.SegmentedDatagramPacket(slice, this.segmentSize, recipient, sender);
            }
            return new DatagramPacket(slice, recipient, sender);
        }
    }
}
