package org.cloudburstmc.netty.channel.raknet;

import org.cloudburstmc.netty.channel.raknet.packet.RakDatagramPacket;

/* loaded from: classes5.dex */
public class RakSlidingWindow {
    private boolean backoffThisBlock;
    private double cwnd;
    private final int mtu;
    private long nextCongestionControlBlock;
    private long oldestUnsentAck;
    private double ssThresh;
    private int unackedBytes;
    private double estimatedRTT = -1.0d;
    private double lastRTT = -1.0d;
    private double deviationRTT = -1.0d;

    public RakSlidingWindow(int mtu) {
        this.mtu = mtu;
        this.cwnd = mtu;
    }

    public int getRetransmissionBandwidth() {
        return this.unackedBytes;
    }

    public int getTransmissionBandwidth() {
        if (this.unackedBytes <= this.cwnd) {
            return (int) (this.cwnd - this.unackedBytes);
        }
        return 0;
    }

    public void onPacketReceived(long curTime) {
        if (this.oldestUnsentAck == 0) {
            this.oldestUnsentAck = curTime;
        }
    }

    public void onResend(long curSequenceIndex) {
        if (!this.backoffThisBlock && this.cwnd > this.mtu * 2.0d) {
            this.ssThresh = this.cwnd * 0.5d;
            if (this.ssThresh < this.mtu) {
                this.ssThresh = this.mtu;
            }
            this.cwnd = this.mtu;
            this.nextCongestionControlBlock = curSequenceIndex;
            this.backoffThisBlock = true;
        }
    }

    public void onNak() {
        if (!this.backoffThisBlock) {
            this.ssThresh = this.cwnd * 0.75d;
        }
    }

    public void onAck(long curTime, RakDatagramPacket datagram, long curSequenceIndex) {
        long rtt = curTime - datagram.getSendTime();
        this.lastRTT = rtt;
        this.unackedBytes -= datagram.getSize();
        if (this.estimatedRTT == -1.0d) {
            this.estimatedRTT = rtt;
            this.deviationRTT = rtt;
        } else {
            double difference = rtt - this.estimatedRTT;
            this.estimatedRTT += 0.05d * difference;
            this.deviationRTT += (Math.abs(difference) - this.deviationRTT) * 0.05d;
        }
        boolean isNewCongestionControlPeriod = ((long) datagram.getSequenceIndex()) > this.nextCongestionControlBlock;
        if (isNewCongestionControlPeriod) {
            this.backoffThisBlock = false;
            this.nextCongestionControlBlock = curSequenceIndex;
        }
        if (isInSlowStart()) {
            this.cwnd += this.mtu;
            if (this.cwnd > this.ssThresh && this.ssThresh != 0.0d) {
                this.cwnd = this.ssThresh + ((this.mtu * this.mtu) / this.cwnd);
                return;
            }
            return;
        }
        if (isNewCongestionControlPeriod) {
            this.cwnd += (this.mtu * this.mtu) / this.cwnd;
        }
    }

    public void onReliableSend(RakDatagramPacket datagram) {
        this.unackedBytes += datagram.getSize();
    }

    public boolean isInSlowStart() {
        return this.cwnd <= this.ssThresh || this.ssThresh == 0.0d;
    }

    public void onSendAck() {
        this.oldestUnsentAck = 0L;
    }

    public long getRtoForRetransmission() {
        if (this.estimatedRTT == -1.0d) {
            return RakConstants.CC_MAXIMUM_THRESHOLD;
        }
        long threshold = (long) ((this.estimatedRTT * 2.0d) + (this.deviationRTT * 4.0d) + 30.0d);
        return threshold > RakConstants.CC_MAXIMUM_THRESHOLD ? RakConstants.CC_MAXIMUM_THRESHOLD : threshold;
    }

    public double getRTT() {
        return this.estimatedRTT;
    }

    public boolean shouldSendAcks(long curTime) {
        long rto = getSenderRtoForAck();
        return rto == -1 || curTime >= this.oldestUnsentAck + 10;
    }

    public long getSenderRtoForAck() {
        if (this.lastRTT == -1.0d) {
            return -1L;
        }
        return (long) (this.lastRTT + 10.0d);
    }

    public int getUnackedBytes() {
        return this.unackedBytes;
    }
}
