package org.msgpack.core;

import java.nio.charset.CharacterCodingException;

/* loaded from: classes5.dex */
public class MessageStringCodingException extends MessagePackException {
    public MessageStringCodingException(String str, CharacterCodingException characterCodingException) {
        super(str, characterCodingException);
    }

    public MessageStringCodingException(CharacterCodingException characterCodingException) {
        super(characterCodingException);
    }

    @Override // java.lang.Throwable
    public CharacterCodingException getCause() {
        return (CharacterCodingException) super.getCause();
    }
}
