package io.netty.handler.codec.marshalling;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;

/* loaded from: classes4.dex */
public class ContextBoundUnmarshallerProvider extends DefaultUnmarshallerProvider {
    private static final AttributeKey<Unmarshaller> UNMARSHALLER = AttributeKey.valueOf(ContextBoundUnmarshallerProvider.class, "UNMARSHALLER");

    public ContextBoundUnmarshallerProvider(MarshallerFactory factory, MarshallingConfiguration config) {
        super(factory, config);
    }

    @Override // io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider, io.netty.handler.codec.marshalling.UnmarshallerProvider
    public Unmarshaller getUnmarshaller(ChannelHandlerContext ctx) throws Exception {
        Attribute<Unmarshaller> attr = ctx.channel().attr(UNMARSHALLER);
        Unmarshaller unmarshaller = attr.get();
        if (unmarshaller == null) {
            Unmarshaller unmarshaller2 = super.getUnmarshaller(ctx);
            attr.set(unmarshaller2);
            return unmarshaller2;
        }
        return unmarshaller;
    }
}
