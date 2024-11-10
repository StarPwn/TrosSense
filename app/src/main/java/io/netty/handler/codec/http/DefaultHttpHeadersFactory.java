package io.netty.handler.codec.http;

import io.netty.handler.codec.DefaultHeaders;
import io.netty.util.internal.ObjectUtil;

/* loaded from: classes4.dex */
public final class DefaultHttpHeadersFactory implements HttpHeadersFactory {
    private final boolean combiningHeaders;
    private final DefaultHeaders.NameValidator<CharSequence> nameValidator;
    private final DefaultHeaders.ValueValidator<CharSequence> valueValidator;
    private static final DefaultHeaders.NameValidator<CharSequence> DEFAULT_NAME_VALIDATOR = new DefaultHeaders.NameValidator<CharSequence>() { // from class: io.netty.handler.codec.http.DefaultHttpHeadersFactory.1
        @Override // io.netty.handler.codec.DefaultHeaders.NameValidator
        public void validateName(CharSequence name) {
            if (name == null || name.length() == 0) {
                throw new IllegalArgumentException("empty headers are not allowed [" + ((Object) name) + ']');
            }
            int index = HttpHeaderValidationUtil.validateToken(name);
            if (index != -1) {
                throw new IllegalArgumentException("a header name can only contain \"token\" characters, but found invalid character 0x" + Integer.toHexString(name.charAt(index)) + " at index " + index + " of header '" + ((Object) name) + "'.");
            }
        }
    };
    private static final DefaultHeaders.ValueValidator<CharSequence> DEFAULT_VALUE_VALIDATOR = new DefaultHeaders.ValueValidator<CharSequence>() { // from class: io.netty.handler.codec.http.DefaultHttpHeadersFactory.2
        @Override // io.netty.handler.codec.DefaultHeaders.ValueValidator
        public void validate(CharSequence value) {
            int index = HttpHeaderValidationUtil.validateValidHeaderValue(value);
            if (index != -1) {
                throw new IllegalArgumentException("a header value contains prohibited character 0x" + Integer.toHexString(value.charAt(index)) + " at index " + index + '.');
            }
        }
    };
    private static final DefaultHeaders.NameValidator<CharSequence> DEFAULT_TRAILER_NAME_VALIDATOR = new DefaultHeaders.NameValidator<CharSequence>() { // from class: io.netty.handler.codec.http.DefaultHttpHeadersFactory.3
        @Override // io.netty.handler.codec.DefaultHeaders.NameValidator
        public void validateName(CharSequence name) {
            DefaultHttpHeadersFactory.DEFAULT_NAME_VALIDATOR.validateName(name);
            if (HttpHeaderNames.CONTENT_LENGTH.contentEqualsIgnoreCase(name) || HttpHeaderNames.TRANSFER_ENCODING.contentEqualsIgnoreCase(name) || HttpHeaderNames.TRAILER.contentEqualsIgnoreCase(name)) {
                throw new IllegalArgumentException("prohibited trailing header: " + ((Object) name));
            }
        }
    };
    private static final DefaultHeaders.NameValidator<CharSequence> NO_NAME_VALIDATOR = DefaultHeaders.NameValidator.NOT_NULL;
    private static final DefaultHeaders.ValueValidator<CharSequence> NO_VALUE_VALIDATOR = DefaultHeaders.ValueValidator.NO_VALIDATION;
    private static final DefaultHttpHeadersFactory DEFAULT = new DefaultHttpHeadersFactory(DEFAULT_NAME_VALIDATOR, DEFAULT_VALUE_VALIDATOR, false);
    private static final DefaultHttpHeadersFactory DEFAULT_TRAILER = new DefaultHttpHeadersFactory(DEFAULT_TRAILER_NAME_VALIDATOR, DEFAULT_VALUE_VALIDATOR, false);
    private static final DefaultHttpHeadersFactory DEFAULT_COMBINING = new DefaultHttpHeadersFactory(DEFAULT.nameValidator, DEFAULT.valueValidator, true);
    private static final DefaultHttpHeadersFactory DEFAULT_NO_VALIDATION = new DefaultHttpHeadersFactory(NO_NAME_VALIDATOR, NO_VALUE_VALIDATOR, false);

    private DefaultHttpHeadersFactory(DefaultHeaders.NameValidator<CharSequence> nameValidator, DefaultHeaders.ValueValidator<CharSequence> valueValidator, boolean combiningHeaders) {
        this.nameValidator = (DefaultHeaders.NameValidator) ObjectUtil.checkNotNull(nameValidator, "nameValidator");
        this.valueValidator = (DefaultHeaders.ValueValidator) ObjectUtil.checkNotNull(valueValidator, "valueValidator");
        this.combiningHeaders = combiningHeaders;
    }

    public static DefaultHttpHeadersFactory headersFactory() {
        return DEFAULT;
    }

    public static DefaultHttpHeadersFactory trailersFactory() {
        return DEFAULT_TRAILER;
    }

    @Override // io.netty.handler.codec.http.HttpHeadersFactory
    public HttpHeaders newHeaders() {
        if (isCombiningHeaders()) {
            return new CombinedHttpHeaders(getNameValidator(), getValueValidator());
        }
        return new DefaultHttpHeaders(getNameValidator(), getValueValidator());
    }

    @Override // io.netty.handler.codec.http.HttpHeadersFactory
    public HttpHeaders newEmptyHeaders() {
        if (isCombiningHeaders()) {
            return new CombinedHttpHeaders(getNameValidator(), getValueValidator(), 2);
        }
        return new DefaultHttpHeaders(getNameValidator(), getValueValidator(), 2);
    }

    public DefaultHttpHeadersFactory withNameValidation(boolean validation) {
        return withNameValidator(validation ? DEFAULT_NAME_VALIDATOR : NO_NAME_VALIDATOR);
    }

    public DefaultHttpHeadersFactory withNameValidator(DefaultHeaders.NameValidator<CharSequence> validator) {
        if (this.nameValidator == ObjectUtil.checkNotNull(validator, "validator")) {
            return this;
        }
        if (validator == DEFAULT_NAME_VALIDATOR && this.valueValidator == DEFAULT_VALUE_VALIDATOR) {
            return this.combiningHeaders ? DEFAULT_COMBINING : DEFAULT;
        }
        return new DefaultHttpHeadersFactory(validator, this.valueValidator, this.combiningHeaders);
    }

    public DefaultHttpHeadersFactory withValueValidation(boolean validation) {
        return withValueValidator(validation ? DEFAULT_VALUE_VALIDATOR : NO_VALUE_VALIDATOR);
    }

    public DefaultHttpHeadersFactory withValueValidator(DefaultHeaders.ValueValidator<CharSequence> validator) {
        if (this.valueValidator == ObjectUtil.checkNotNull(validator, "validator")) {
            return this;
        }
        if (this.nameValidator == DEFAULT_NAME_VALIDATOR && validator == DEFAULT_VALUE_VALIDATOR) {
            return this.combiningHeaders ? DEFAULT_COMBINING : DEFAULT;
        }
        return new DefaultHttpHeadersFactory(this.nameValidator, validator, this.combiningHeaders);
    }

    public DefaultHttpHeadersFactory withValidation(boolean validation) {
        if (this == DEFAULT && !validation) {
            return DEFAULT_NO_VALIDATION;
        }
        if (this == DEFAULT_NO_VALIDATION && validation) {
            return DEFAULT;
        }
        return withNameValidation(validation).withValueValidation(validation);
    }

    public DefaultHttpHeadersFactory withCombiningHeaders(boolean combiningHeaders) {
        if (this.combiningHeaders == combiningHeaders) {
            return this;
        }
        return new DefaultHttpHeadersFactory(this.nameValidator, this.valueValidator, combiningHeaders);
    }

    public DefaultHeaders.NameValidator<CharSequence> getNameValidator() {
        return this.nameValidator;
    }

    public DefaultHeaders.ValueValidator<CharSequence> getValueValidator() {
        return this.valueValidator;
    }

    public boolean isCombiningHeaders() {
        return this.combiningHeaders;
    }

    public boolean isValidatingHeaderNames() {
        return this.nameValidator != NO_NAME_VALIDATOR;
    }

    public boolean isValidatingHeaderValues() {
        return this.valueValidator != NO_VALUE_VALIDATOR;
    }
}
