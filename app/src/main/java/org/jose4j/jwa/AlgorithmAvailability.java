package org.jose4j.jwa;

import java.security.Security;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: classes5.dex */
public class AlgorithmAvailability {
    private static Logger log = LoggerFactory.getLogger((Class<?>) AlgorithmAvailability.class);

    public static boolean isAvailable(String serviceName, String algorithm) {
        Set<String> algorithms = Security.getAlgorithms(serviceName);
        for (String serviceAlg : algorithms) {
            if (serviceAlg.equalsIgnoreCase(algorithm)) {
                return true;
            }
        }
        log.debug("{} is NOT available for {}. Algorithms available from underlying JCE: {}", algorithm, serviceName, algorithms);
        return false;
    }
}
