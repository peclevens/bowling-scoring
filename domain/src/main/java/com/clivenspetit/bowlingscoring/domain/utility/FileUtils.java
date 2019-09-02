package com.clivenspetit.bowlingscoring.domain.utility;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Clivens Petit <clivens.petit@magicsoftbay.com>
 */
public final class FileUtils {

    public static String getAbsoluteFilePathForResource(Class<?> clazz, String relativePath)
            throws UnsupportedEncodingException {

        URL url = clazz.getClassLoader().getResource(relativePath);
        Objects.requireNonNull(url, "Empty file pass should be null.");

        return URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8.name());
    }
}
