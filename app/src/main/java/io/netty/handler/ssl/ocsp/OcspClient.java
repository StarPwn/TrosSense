package io.netty.handler.ssl.ocsp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFactory;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.resolver.dns.DnsNameResolver;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetAddress;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.OCSPObjectIdentifiers;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.operator.ContentVerifierProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public final class OcspClient {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance((Class<?>) OcspClient.class);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int OCSP_RESPONSE_MAX_SIZE = SystemPropertyUtil.getInt("io.netty.ocsp.responseSize", 10240);

    static {
        logger.debug("-Dio.netty.ocsp.responseSize: {} bytes", Integer.valueOf(OCSP_RESPONSE_MAX_SIZE));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Promise<BasicOCSPResp> query(final X509Certificate x509Certificate, final X509Certificate issuer, final boolean validateResponseNonce, final IoTransport ioTransport, final DnsNameResolver dnsNameResolver) {
        final EventLoop eventLoop = ioTransport.eventLoop();
        final Promise<BasicOCSPResp> responsePromise = eventLoop.newPromise();
        eventLoop.execute(new Runnable() { // from class: io.netty.handler.ssl.ocsp.OcspClient.1
            @Override // java.lang.Runnable
            public void run() {
                int port;
                String path;
                try {
                    CertificateID certificateID = new CertificateID(new JcaDigestCalculatorProviderBuilder().build().get(CertificateID.HASH_SHA1), new JcaX509CertificateHolder(issuer), x509Certificate.getSerialNumber());
                    OCSPReqBuilder builder = new OCSPReqBuilder();
                    builder.addRequest(certificateID);
                    byte[] nonce = new byte[16];
                    OcspClient.SECURE_RANDOM.nextBytes(nonce);
                    final DEROctetString derNonce = new DEROctetString(nonce);
                    builder.setRequestExtensions(new Extensions(new Extension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce, false, derNonce)));
                    URL uri = new URL(OcspClient.parseOcspUrlFromCertificate(x509Certificate));
                    int port2 = uri.getPort();
                    if (port2 != -1) {
                        port = port2;
                    } else {
                        port = uri.getDefaultPort();
                    }
                    String path2 = uri.getPath();
                    if (path2.isEmpty()) {
                        path = "/";
                    } else if (uri.getQuery() == null) {
                        path = path2;
                    } else {
                        path = path2 + '?' + uri.getQuery();
                    }
                    Promise<OCSPResp> ocspResponsePromise = OcspClient.query(eventLoop, Unpooled.wrappedBuffer(builder.build().getEncoded()), uri.getHost(), port, path, ioTransport, dnsNameResolver);
                    ocspResponsePromise.addListener(new GenericFutureListener<Future<OCSPResp>>() { // from class: io.netty.handler.ssl.ocsp.OcspClient.1.1
                        @Override // io.netty.util.concurrent.GenericFutureListener
                        public void operationComplete(Future<OCSPResp> future) throws Exception {
                            if (future.isSuccess()) {
                                BasicOCSPResp resp = (BasicOCSPResp) future.get().getResponseObject();
                                OcspClient.validateResponse(responsePromise, resp, derNonce, issuer, validateResponseNonce);
                            } else {
                                responsePromise.tryFailure(future.cause());
                            }
                        }
                    });
                } catch (Exception ex) {
                    responsePromise.tryFailure(ex);
                }
            }
        });
        return responsePromise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Promise<OCSPResp> query(EventLoop eventLoop, final ByteBuf ocspRequest, final String host, final int port, final String path, IoTransport ioTransport, DnsNameResolver dnsNameResolver) {
        final Promise<OCSPResp> responsePromise = eventLoop.newPromise();
        try {
            final Bootstrap bootstrap = new Bootstrap().group(ioTransport.eventLoop()).option(ChannelOption.TCP_NODELAY, true).channelFactory((ChannelFactory) ioTransport.socketChannel()).attr(OcspServerCertificateValidator.OCSP_PIPELINE_ATTRIBUTE, Boolean.TRUE).handler(new Initializer(responsePromise));
            try {
                dnsNameResolver.resolve(host).addListener(new FutureListener<InetAddress>() { // from class: io.netty.handler.ssl.ocsp.OcspClient.2
                    @Override // io.netty.util.concurrent.GenericFutureListener
                    public void operationComplete(Future<InetAddress> future) throws Exception {
                        if (future.isSuccess()) {
                            InetAddress hostAddress = future.get();
                            final ChannelFuture channelFuture = Bootstrap.this.connect(hostAddress, port);
                            channelFuture.addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: io.netty.handler.ssl.ocsp.OcspClient.2.1
                                @Override // io.netty.util.concurrent.GenericFutureListener
                                public void operationComplete(ChannelFuture future2) {
                                    if (future2.isSuccess()) {
                                        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, path, ocspRequest);
                                        request.headers().add(HttpHeaderNames.HOST, host);
                                        request.headers().add(HttpHeaderNames.USER_AGENT, "Netty OCSP Client");
                                        request.headers().add(HttpHeaderNames.CONTENT_TYPE, OcspHttpHandler.OCSP_REQUEST_TYPE);
                                        request.headers().add(HttpHeaderNames.ACCEPT_ENCODING, OcspHttpHandler.OCSP_RESPONSE_TYPE);
                                        request.headers().add(HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(ocspRequest.readableBytes()));
                                        channelFuture.channel().writeAndFlush(request);
                                        return;
                                    }
                                    responsePromise.tryFailure(new IllegalStateException("Connection to OCSP Responder Failed", future2.cause()));
                                }
                            });
                            return;
                        }
                        responsePromise.tryFailure(future.cause());
                    }
                });
            } catch (Exception e) {
                ex = e;
                responsePromise.tryFailure(ex);
                return responsePromise;
            }
        } catch (Exception e2) {
            ex = e2;
        }
        return responsePromise;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void validateResponse(Promise<BasicOCSPResp> responsePromise, BasicOCSPResp basicResponse, DEROctetString derNonce, X509Certificate issuer, boolean validateNonce) {
        try {
            int responses = basicResponse.getResponses().length;
            if (responses != 1) {
                throw new IllegalArgumentException("Expected number of responses was 1 but got: " + responses);
            }
            if (validateNonce) {
                validateNonce(basicResponse, derNonce);
            }
            validateSignature(basicResponse, issuer);
            responsePromise.trySuccess(basicResponse);
        } catch (Exception ex) {
            responsePromise.tryFailure(ex);
        }
    }

    private static void validateNonce(BasicOCSPResp basicResponse, DEROctetString encodedNonce) throws OCSPException {
        Extension nonceExt = basicResponse.getExtension(OCSPObjectIdentifiers.id_pkix_ocsp_nonce);
        if (nonceExt != null) {
            DEROctetString responseNonceString = nonceExt.getExtnValue();
            if (!responseNonceString.equals(encodedNonce)) {
                throw new OCSPException("Nonce does not match");
            }
            return;
        }
        throw new IllegalArgumentException("Nonce is not present");
    }

    private static void validateSignature(BasicOCSPResp resp, X509Certificate certificate) throws OCSPException {
        try {
            ContentVerifierProvider verifier = new JcaContentVerifierProviderBuilder().build(certificate);
            if (!resp.isSignatureValid(verifier)) {
                throw new OCSPException("OCSP signature is not valid");
            }
        } catch (OperatorCreationException e) {
            throw new OCSPException("Error validating OCSP-Signature", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String parseOcspUrlFromCertificate(X509Certificate cert) {
        try {
            AuthorityInformationAccess aiaExtension = AuthorityInformationAccess.fromExtensions(new JcaX509CertificateHolder(cert).getExtensions());
            for (AccessDescription accessDescription : aiaExtension.getAccessDescriptions()) {
                if (accessDescription.getAccessMethod().equals(X509ObjectIdentifiers.id_ad_ocsp)) {
                    return accessDescription.getAccessLocation().getName().toASN1Primitive().toString();
                }
            }
            throw new NullPointerException("Unable to find OCSP responder URL in Certificate");
        } catch (CertificateEncodingException e) {
            throw new IllegalArgumentException("Error while parsing X509Certificate into JcaX509CertificateHolder", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static final class Initializer extends ChannelInitializer<SocketChannel> {
        private final Promise<OCSPResp> responsePromise;

        Initializer(Promise<OCSPResp> responsePromise) {
            this.responsePromise = (Promise) ObjectUtil.checkNotNull(responsePromise, "ResponsePromise");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // io.netty.channel.ChannelInitializer
        public void initChannel(SocketChannel socketChannel) {
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(new HttpClientCodec());
            pipeline.addLast(new HttpObjectAggregator(OcspClient.OCSP_RESPONSE_MAX_SIZE));
            pipeline.addLast(new OcspHttpHandler(this.responsePromise));
        }
    }

    private OcspClient() {
    }
}
