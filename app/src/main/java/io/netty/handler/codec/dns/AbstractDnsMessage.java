package io.netty.handler.codec.dns;

import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.ReferenceCounted;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetectorFactory;
import io.netty.util.ResourceLeakTracker;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;
import java.util.ArrayList;
import java.util.List;
import kotlin.UShort;

/* loaded from: classes4.dex */
public abstract class AbstractDnsMessage extends AbstractReferenceCounted implements DnsMessage {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int SECTION_COUNT = 4;
    private Object additionals;
    private Object answers;
    private Object authorities;
    private short id;
    private final ResourceLeakTracker<DnsMessage> leak;
    private DnsOpCode opCode;
    private Object questions;
    private boolean recursionDesired;
    private byte z;
    private static final ResourceLeakDetector<DnsMessage> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(DnsMessage.class);
    private static final int SECTION_QUESTION = DnsSection.QUESTION.ordinal();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractDnsMessage(int id) {
        this(id, DnsOpCode.QUERY);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractDnsMessage(int id, DnsOpCode opCode) {
        this.leak = leakDetector.track(this);
        setId(id);
        setOpCode(opCode);
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public int id() {
        return this.id & UShort.MAX_VALUE;
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public DnsMessage setId(int id) {
        this.id = (short) id;
        return this;
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public DnsOpCode opCode() {
        return this.opCode;
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public DnsMessage setOpCode(DnsOpCode opCode) {
        this.opCode = (DnsOpCode) ObjectUtil.checkNotNull(opCode, "opCode");
        return this;
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public boolean isRecursionDesired() {
        return this.recursionDesired;
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public DnsMessage setRecursionDesired(boolean recursionDesired) {
        this.recursionDesired = recursionDesired;
        return this;
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public int z() {
        return this.z;
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public DnsMessage setZ(int z) {
        this.z = (byte) (z & 7);
        return this;
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public int count(DnsSection section) {
        return count(sectionOrdinal(section));
    }

    private int count(int section) {
        Object records = sectionAt(section);
        if (records == null) {
            return 0;
        }
        if (records instanceof DnsRecord) {
            return 1;
        }
        List<DnsRecord> recordList = (List) records;
        return recordList.size();
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public int count() {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            count += count(i);
        }
        return count;
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public <T extends DnsRecord> T recordAt(DnsSection dnsSection) {
        return (T) recordAt(sectionOrdinal(dnsSection));
    }

    private <T extends DnsRecord> T recordAt(int i) {
        Object sectionAt = sectionAt(i);
        if (sectionAt == null) {
            return null;
        }
        if (sectionAt instanceof DnsRecord) {
            return (T) castRecord(sectionAt);
        }
        List list = (List) sectionAt;
        if (list.isEmpty()) {
            return null;
        }
        return (T) castRecord(list.get(0));
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public <T extends DnsRecord> T recordAt(DnsSection dnsSection, int i) {
        return (T) recordAt(sectionOrdinal(dnsSection), i);
    }

    private <T extends DnsRecord> T recordAt(int i, int i2) {
        Object sectionAt = sectionAt(i);
        if (sectionAt == null) {
            throw new IndexOutOfBoundsException("index: " + i2 + " (expected: none)");
        }
        if (sectionAt instanceof DnsRecord) {
            if (i2 == 0) {
                return (T) castRecord(sectionAt);
            }
            throw new IndexOutOfBoundsException("index: " + i2 + "' (expected: 0)");
        }
        return (T) castRecord(((List) sectionAt).get(i2));
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public DnsMessage setRecord(DnsSection section, DnsRecord record) {
        setRecord(sectionOrdinal(section), record);
        return this;
    }

    private void setRecord(int section, DnsRecord record) {
        clear(section);
        setSection(section, checkQuestion(section, record));
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public <T extends DnsRecord> T setRecord(DnsSection dnsSection, int i, DnsRecord dnsRecord) {
        return (T) setRecord(sectionOrdinal(dnsSection), i, dnsRecord);
    }

    private <T extends DnsRecord> T setRecord(int i, int i2, DnsRecord dnsRecord) {
        checkQuestion(i, dnsRecord);
        Object sectionAt = sectionAt(i);
        if (sectionAt == null) {
            throw new IndexOutOfBoundsException("index: " + i2 + " (expected: none)");
        }
        if (sectionAt instanceof DnsRecord) {
            if (i2 == 0) {
                setSection(i, dnsRecord);
                return (T) castRecord(sectionAt);
            }
            throw new IndexOutOfBoundsException("index: " + i2 + " (expected: 0)");
        }
        return (T) castRecord(((List) sectionAt).set(i2, dnsRecord));
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public DnsMessage addRecord(DnsSection section, DnsRecord record) {
        addRecord(sectionOrdinal(section), record);
        return this;
    }

    private void addRecord(int section, DnsRecord record) {
        checkQuestion(section, record);
        Object records = sectionAt(section);
        if (records == null) {
            setSection(section, record);
            return;
        }
        if (records instanceof DnsRecord) {
            List<DnsRecord> recordList = newRecordList();
            recordList.add(castRecord(records));
            recordList.add(record);
            setSection(section, recordList);
            return;
        }
        ((List) records).add(record);
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public DnsMessage addRecord(DnsSection section, int index, DnsRecord record) {
        addRecord(sectionOrdinal(section), index, record);
        return this;
    }

    private void addRecord(int section, int index, DnsRecord record) {
        List<DnsRecord> recordList;
        checkQuestion(section, record);
        Object records = sectionAt(section);
        if (records == null) {
            if (index != 0) {
                throw new IndexOutOfBoundsException("index: " + index + " (expected: 0)");
            }
            setSection(section, record);
        } else {
            if (records instanceof DnsRecord) {
                if (index == 0) {
                    recordList = newRecordList();
                    recordList.add(record);
                    recordList.add(castRecord(records));
                } else if (index == 1) {
                    recordList = newRecordList();
                    recordList.add(castRecord(records));
                    recordList.add(record);
                } else {
                    throw new IndexOutOfBoundsException("index: " + index + " (expected: 0 or 1)");
                }
                setSection(section, recordList);
                return;
            }
            ((List) records).add(index, record);
        }
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public <T extends DnsRecord> T removeRecord(DnsSection dnsSection, int i) {
        return (T) removeRecord(sectionOrdinal(dnsSection), i);
    }

    private <T extends DnsRecord> T removeRecord(int i, int i2) {
        Object sectionAt = sectionAt(i);
        if (sectionAt == null) {
            throw new IndexOutOfBoundsException("index: " + i2 + " (expected: none)");
        }
        if (sectionAt instanceof DnsRecord) {
            if (i2 != 0) {
                throw new IndexOutOfBoundsException("index: " + i2 + " (expected: 0)");
            }
            T t = (T) castRecord(sectionAt);
            setSection(i, null);
            return t;
        }
        return (T) castRecord(((List) sectionAt).remove(i2));
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public DnsMessage clear(DnsSection section) {
        clear(sectionOrdinal(section));
        return this;
    }

    @Override // io.netty.handler.codec.dns.DnsMessage
    public DnsMessage clear() {
        for (int i = 0; i < 4; i++) {
            clear(i);
        }
        return this;
    }

    private void clear(int section) {
        Object recordOrList = sectionAt(section);
        setSection(section, null);
        if (recordOrList instanceof ReferenceCounted) {
            ((ReferenceCounted) recordOrList).release();
            return;
        }
        if (recordOrList instanceof List) {
            List<DnsRecord> list = (List) recordOrList;
            if (!list.isEmpty()) {
                for (Object r : list) {
                    ReferenceCountUtil.release(r);
                }
            }
        }
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DnsMessage touch() {
        return (DnsMessage) super.touch();
    }

    @Override // io.netty.util.ReferenceCounted
    public DnsMessage touch(Object hint) {
        if (this.leak != null) {
            this.leak.record(hint);
        }
        return this;
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DnsMessage retain() {
        return (DnsMessage) super.retain();
    }

    @Override // io.netty.util.AbstractReferenceCounted, io.netty.util.ReferenceCounted
    public DnsMessage retain(int increment) {
        return (DnsMessage) super.retain(increment);
    }

    @Override // io.netty.util.AbstractReferenceCounted
    protected void deallocate() {
        clear();
        ResourceLeakTracker<DnsMessage> leak = this.leak;
        if (leak != null) {
            boolean closed = leak.close(this);
            if (!closed) {
                throw new AssertionError();
            }
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DnsMessage)) {
            return false;
        }
        DnsMessage that = (DnsMessage) obj;
        if (id() != that.id()) {
            return false;
        }
        if (this instanceof DnsQuery) {
            if (!(that instanceof DnsQuery)) {
                return false;
            }
        } else if (that instanceof DnsQuery) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (id() * 31) + (!(this instanceof DnsQuery) ? 1 : 0);
    }

    private Object sectionAt(int section) {
        switch (section) {
            case 0:
                return this.questions;
            case 1:
                return this.answers;
            case 2:
                return this.authorities;
            case 3:
                return this.additionals;
            default:
                throw new Error();
        }
    }

    private void setSection(int section, Object value) {
        switch (section) {
            case 0:
                this.questions = value;
                return;
            case 1:
                this.answers = value;
                return;
            case 2:
                this.authorities = value;
                return;
            case 3:
                this.additionals = value;
                return;
            default:
                throw new Error();
        }
    }

    private static int sectionOrdinal(DnsSection section) {
        return ((DnsSection) ObjectUtil.checkNotNull(section, "section")).ordinal();
    }

    private static DnsRecord checkQuestion(int section, DnsRecord record) {
        if (section == SECTION_QUESTION && !(ObjectUtil.checkNotNull(record, "record") instanceof DnsQuestion)) {
            throw new IllegalArgumentException("record: " + record + " (expected: " + StringUtil.simpleClassName((Class<?>) DnsQuestion.class) + ')');
        }
        return record;
    }

    private static <T extends DnsRecord> T castRecord(Object record) {
        return (T) record;
    }

    private static ArrayList<DnsRecord> newRecordList() {
        return new ArrayList<>(2);
    }
}
