package io.netty.handler.codec.marshalling;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.FastThreadLocal;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

/* loaded from: classes4.dex */
public class ThreadLocalUnmarshallerProvider implements UnmarshallerProvider {
    private final MarshallingConfiguration config;
    private final MarshallerFactory factory;
    private final FastThreadLocal<Unmarshaller> unmarshallers = new FastThreadLocal<>();

    public ThreadLocalUnmarshallerProvider(MarshallerFactory factory, MarshallingConfiguration config) {
        this.factory = factory;
        this.config = config;
    }

    @Override // io.netty.handler.codec.marshalling.UnmarshallerProvider
    public Unmarshaller getUnmarshaller(ChannelHandlerContext ctx) throws Exception {
        Unmarshaller unmarshaller = this.unmarshallers.get();
        if (unmarshaller == null) {
            Unmarshaller unmarshaller2 = this.factory.createUnmarshaller(this.config);
            this.unmarshallers.set(unmarshaller2);
            return unmarshaller2;
        }
        return unmarshaller;
    }
}
