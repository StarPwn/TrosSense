package org.jose4j.jwa;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.jose4j.jwa.Algorithm;
import org.jose4j.lang.ExceptionHelp;
import org.jose4j.lang.InvalidAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class AlgorithmFactory<A extends Algorithm> {
    private final Map<String, A> algorithms = new LinkedHashMap();
    private final Logger log;
    private String parameterName;

    public AlgorithmFactory(String parameterName, Class<A> type) {
        this.parameterName = parameterName;
        this.log = LoggerFactory.getLogger(getClass().getName() + "->" + type.getSimpleName());
    }

    public A getAlgorithm(String algorithmIdentifier) throws InvalidAlgorithmException {
        A algo = this.algorithms.get(algorithmIdentifier);
        if (algo == null) {
            throw new InvalidAlgorithmException(algorithmIdentifier + " is an unknown, unsupported or unavailable " + this.parameterName + " algorithm (not one of " + getSupportedAlgorithms() + ").");
        }
        return algo;
    }

    public boolean isAvailable(String algorithmIdentifier) {
        return this.algorithms.containsKey(algorithmIdentifier);
    }

    public Set<String> getSupportedAlgorithms() {
        return Collections.unmodifiableSet(this.algorithms.keySet());
    }

    public void registerAlgorithm(A algorithm) {
        String algId = algorithm.getAlgorithmIdentifier();
        if (isAvailable((AlgorithmFactory<A>) algorithm)) {
            this.algorithms.put(algId, algorithm);
            this.log.debug("{} registered for {} algorithm {}", algorithm, this.parameterName, algId);
        } else {
            this.log.debug("{} is unavailable so will not be registered for {} algorithms.", algId, this.parameterName);
        }
    }

    private boolean isAvailable(A algorithm) {
        try {
            return algorithm.isAvailable();
        } catch (Throwable e) {
            this.log.debug("Unexpected problem checking for availability of " + algorithm.getAlgorithmIdentifier() + " algorithm: " + ExceptionHelp.toStringWithCauses(e));
            return false;
        }
    }

    public void unregisterAlgorithm(String algorithmIdentifier) {
        this.algorithms.remove(algorithmIdentifier);
    }
}
