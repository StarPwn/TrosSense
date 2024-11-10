package io.netty.handler.ssl.ocsp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.SslHandshakeCompletionEvent;
import io.netty.handler.ssl.ocsp.OcspResponse;
import io.netty.resolver.dns.DnsNameResolver;
import io.netty.resolver.dns.DnsNameResolverBuilder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.RevokedStatus;
import org.bouncycastle.cert.ocsp.SingleResp;

/* loaded from: classes4.dex */
public class OcspServerCertificateValidator extends ChannelInboundHandlerAdapter {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final AttributeKey<Boolean> OCSP_PIPELINE_ATTRIBUTE = AttributeKey.newInstance("io.netty.handler.ssl.ocsp.pipeline");
    private final boolean closeAndThrowIfNotValid;
    private final DnsNameResolver dnsNameResolver;
    private final IoTransport ioTransport;
    private final boolean validateNonce;

    public OcspServerCertificateValidator() {
        this(false);
    }

    public OcspServerCertificateValidator(boolean validateNonce) {
        this(validateNonce, IoTransport.DEFAULT);
    }

    public OcspServerCertificateValidator(boolean validateNonce, IoTransport ioTransport) {
        this(validateNonce, ioTransport, createDefaultResolver(ioTransport));
    }

    public OcspServerCertificateValidator(boolean validateNonce, IoTransport ioTransport, DnsNameResolver dnsNameResolver) {
        this(true, validateNonce, ioTransport, dnsNameResolver);
    }

    public OcspServerCertificateValidator(boolean closeAndThrowIfNotValid, boolean validateNonce, IoTransport ioTransport, DnsNameResolver dnsNameResolver) {
        this.closeAndThrowIfNotValid = closeAndThrowIfNotValid;
        this.validateNonce = validateNonce;
        this.ioTransport = (IoTransport) ObjectUtil.checkNotNull(ioTransport, "IoTransport");
        this.dnsNameResolver = (DnsNameResolver) ObjectUtil.checkNotNull(dnsNameResolver, "DnsNameResolver");
    }

    protected static DnsNameResolver createDefaultResolver(IoTransport ioTransport) {
        return new DnsNameResolverBuilder().eventLoop(ioTransport.eventLoop()).channelFactory(ioTransport.datagramChannel()).socketChannelFactory(ioTransport.socketChannel()).build();
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void userEventTriggered(final ChannelHandlerContext ctx, Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
        if (evt instanceof SslHandshakeCompletionEvent) {
            SslHandshakeCompletionEvent sslHandshakeCompletionEvent = (SslHandshakeCompletionEvent) evt;
            if (sslHandshakeCompletionEvent.isSuccess()) {
                Certificate[] certificates = ((SslHandler) ctx.pipeline().get(SslHandler.class)).engine().getSession().getPeerCertificates();
                if (certificates.length < 2) {
                    throw new AssertionError("There must an end-entity certificate and issuer certificate");
                }
                Promise<BasicOCSPResp> ocspRespPromise = OcspClient.query((X509Certificate) certificates[0], (X509Certificate) certificates[1], this.validateNonce, this.ioTransport, this.dnsNameResolver);
                ocspRespPromise.addListener((GenericFutureListener<? extends Future<? super BasicOCSPResp>>) new GenericFutureListener<Future<BasicOCSPResp>>() { // from class: io.netty.handler.ssl.ocsp.OcspServerCertificateValidator.1
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<BasicOCSPResp> future) throws Exception {
                        OcspResponse.Status status;
                        if (future.isSuccess()) {
                            SingleResp response = future.get().getResponses()[0];
                            Date current = new Date();
                            if (!current.after(response.getThisUpdate()) || !current.before(response.getNextUpdate())) {
                                ctx.fireExceptionCaught((Throwable) new IllegalStateException("OCSP Response is out-of-date"));
                            }
                            if (response.getCertStatus() == null) {
                                status = OcspResponse.Status.VALID;
                            } else if (response.getCertStatus() instanceof RevokedStatus) {
                                status = OcspResponse.Status.REVOKED;
                            } else {
                                status = OcspResponse.Status.UNKNOWN;
                            }
                            ctx.fireUserEventTriggered((Object) new OcspValidationEvent(new OcspResponse(status, response.getThisUpdate(), response.getNextUpdate())));
                            if (status != OcspResponse.Status.VALID && OcspServerCertificateValidator.this.closeAndThrowIfNotValid) {
                                ctx.channel().close();
                                ctx.fireExceptionCaught((Throwable) new OCSPException("Certificate not valid. Status: " + status));
                                return;
                            }
                            return;
                        }
                        ctx.fireExceptionCaught(future.cause());
                    }
                });
            }
            ctx.pipeline().remove(this);
        }
    }

    @Override // io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.channel().close();
    }
}
