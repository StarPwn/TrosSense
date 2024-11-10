package org.jose4j.jwa;

import org.jose4j.keys.KeyPersuasion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public abstract class AlgorithmInfo implements Algorithm {
    private String algorithmIdentifier;
    private String javaAlgorithm;
    private KeyPersuasion keyPersuasion;
    private String keyType;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public void setAlgorithmIdentifier(String algorithmIdentifier) {
        this.algorithmIdentifier = algorithmIdentifier;
    }

    public void setJavaAlgorithm(String javaAlgorithm) {
        this.javaAlgorithm = javaAlgorithm;
    }

    @Override // org.jose4j.jwa.Algorithm
    public String getJavaAlgorithm() {
        return this.javaAlgorithm;
    }

    @Override // org.jose4j.jwa.Algorithm
    public String getAlgorithmIdentifier() {
        return this.algorithmIdentifier;
    }

    @Override // org.jose4j.jwa.Algorithm
    public KeyPersuasion getKeyPersuasion() {
        return this.keyPersuasion;
    }

    public void setKeyPersuasion(KeyPersuasion keyPersuasion) {
        this.keyPersuasion = keyPersuasion;
    }

    @Override // org.jose4j.jwa.Algorithm
    public String getKeyType() {
        return this.keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String toString() {
        return getClass().getName() + "(" + this.algorithmIdentifier + "|" + this.javaAlgorithm + ")";
    }
}
