package org.jose4j.jwa;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jose4j.lang.InvalidAlgorithmException;

/* loaded from: classes5.dex */
public class AlgorithmConstraints {
    private final Set<String> algorithms;
    private final ConstraintType type;
    public static final AlgorithmConstraints NO_CONSTRAINTS = new AlgorithmConstraints(ConstraintType.BLOCK, new String[0]);
    public static final AlgorithmConstraints DISALLOW_NONE = new AlgorithmConstraints(ConstraintType.BLOCK, "none");
    public static final AlgorithmConstraints ALLOW_ONLY_NONE = new AlgorithmConstraints(ConstraintType.PERMIT, "none");

    /* loaded from: classes5.dex */
    public enum ConstraintType {
        WHITELIST,
        BLACKLIST,
        PERMIT,
        BLOCK
    }

    public AlgorithmConstraints(ConstraintType type, String... algorithms) {
        if (type == null) {
            throw new NullPointerException("ConstraintType cannot be null");
        }
        this.type = type;
        this.algorithms = new HashSet(Arrays.asList(algorithms));
    }

    public void checkConstraint(String algorithm) throws InvalidAlgorithmException {
        switch (this.type) {
            case PERMIT:
            case WHITELIST:
                if (!this.algorithms.contains(algorithm)) {
                    throw new InvalidAlgorithmException("'" + algorithm + "' is not a permitted algorithm.");
                }
                return;
            case BLOCK:
            case BLACKLIST:
                if (this.algorithms.contains(algorithm)) {
                    throw new InvalidAlgorithmException("'" + algorithm + "' is a blocked algorithm.");
                }
                return;
            default:
                return;
        }
    }
}
