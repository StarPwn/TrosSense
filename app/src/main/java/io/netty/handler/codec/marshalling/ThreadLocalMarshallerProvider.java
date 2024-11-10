package io.netty.handler.codec.marshalling;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.FastThreadLocal;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.MarshallingConfiguration;

/* loaded from: classes4.dex */
public class ThreadLocalMarshallerProvider implements MarshallerProvider {
    private final MarshallingConfiguration config;
    private final MarshallerFactory factory;
    private final FastThreadLocal<Marshaller> marshallers = new FastThreadLocal<>();

    public ThreadLocalMarshallerProvider(MarshallerFactory factory, MarshallingConfiguration config) {
        this.factory = factory;
        this.config = config;
    }

    @Override // io.netty.handler.codec.marshalling.MarshallerProvider
    public Marshaller getMarshaller(ChannelHandlerContext ctx) throws Exception {
        Marshaller marshaller = this.marshallers.get();
        if (marshaller == null) {
            Marshaller marshaller2 = this.factory.createMarshaller(this.config);
            this.marshallers.set(marshaller2);
            return marshaller2;
        }
        return marshaller;
    }
}
