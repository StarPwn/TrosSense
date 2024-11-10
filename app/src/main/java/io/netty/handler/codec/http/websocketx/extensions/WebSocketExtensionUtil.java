package io.netty.handler.codec.http.websocketx.extensions;

import com.trossense.bl;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes4.dex */
public final class WebSocketExtensionUtil {
    private static final String EXTENSION_SEPARATOR = ",";
    private static final Pattern PARAMETER = Pattern.compile("^([^=]+)(=[\\\"]?([^\\\"]+)[\\\"]?)?$");
    private static final char PARAMETER_EQUAL = '=';
    private static final String PARAMETER_SEPARATOR = ";";

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isWebsocketUpgrade(HttpHeaders headers) {
        return headers.contains(HttpHeaderNames.UPGRADE) && headers.containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true) && headers.contains((CharSequence) HttpHeaderNames.UPGRADE, (CharSequence) HttpHeaderValues.WEBSOCKET, true);
    }

    public static List<WebSocketExtensionData> extractExtensions(String extensionHeader) {
        Map<String, String> parameters;
        String[] rawExtensions = extensionHeader.split(EXTENSION_SEPARATOR);
        if (rawExtensions.length > 0) {
            List<WebSocketExtensionData> extensions = new ArrayList<>(rawExtensions.length);
            for (String rawExtension : rawExtensions) {
                String[] extensionParameters = rawExtension.split(PARAMETER_SEPARATOR);
                String name = extensionParameters[0].trim();
                if (extensionParameters.length > 1) {
                    parameters = new HashMap<>(extensionParameters.length - 1);
                    for (int i = 1; i < extensionParameters.length; i++) {
                        String parameter = extensionParameters[i].trim();
                        Matcher parameterMatcher = PARAMETER.matcher(parameter);
                        if (parameterMatcher.matches() && parameterMatcher.group(1) != null) {
                            parameters.put(parameterMatcher.group(1), parameterMatcher.group(3));
                        }
                    }
                } else {
                    parameters = Collections.emptyMap();
                }
                extensions.add(new WebSocketExtensionData(name, parameters));
            }
            return extensions;
        }
        return Collections.emptyList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String computeMergeExtensionsHeaderValue(String userDefinedHeaderValue, List<WebSocketExtensionData> extraExtensions) {
        List<WebSocketExtensionData> userDefinedExtensions;
        if (userDefinedHeaderValue != null) {
            userDefinedExtensions = extractExtensions(userDefinedHeaderValue);
        } else {
            userDefinedExtensions = Collections.emptyList();
        }
        for (WebSocketExtensionData userDefined : userDefinedExtensions) {
            WebSocketExtensionData matchingExtra = null;
            int i = 0;
            while (true) {
                if (i >= extraExtensions.size()) {
                    break;
                }
                WebSocketExtensionData extra = extraExtensions.get(i);
                if (!extra.name().equals(userDefined.name())) {
                    i++;
                } else {
                    matchingExtra = extra;
                    break;
                }
            }
            if (matchingExtra == null) {
                extraExtensions.add(userDefined);
            } else {
                Map<String, String> mergedParameters = new HashMap<>(matchingExtra.parameters());
                mergedParameters.putAll(userDefined.parameters());
                extraExtensions.set(i, new WebSocketExtensionData(matchingExtra.name(), mergedParameters));
            }
        }
        StringBuilder sb = new StringBuilder(bl.bL);
        for (WebSocketExtensionData data : extraExtensions) {
            sb.append(data.name());
            for (Map.Entry<String, String> parameter : data.parameters().entrySet()) {
                sb.append(PARAMETER_SEPARATOR);
                sb.append(parameter.getKey());
                if (parameter.getValue() != null) {
                    sb.append(PARAMETER_EQUAL);
                    sb.append(parameter.getValue());
                }
            }
            sb.append(EXTENSION_SEPARATOR);
        }
        if (!extraExtensions.isEmpty()) {
            sb.setLength(sb.length() - EXTENSION_SEPARATOR.length());
        }
        return sb.toString();
    }

    private WebSocketExtensionUtil() {
    }
}
