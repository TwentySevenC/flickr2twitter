/*
 * Copyright (c) 2005 Aetrion LLC.
 */
package com.googlecode.flickr2twitter.com.aetrion.flickr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Common IO utilities.
 *
 * @author Anthony Eden
 */
public class IOUtilities {

    private IOUtilities() {

    }

    public static void close(InputStream s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {

            }
        }
    }

    public static void close(OutputStream s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {

            }
        }
    }

    public static void close(Reader s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {

            }
        }
    }

    public static void close(Writer s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {

            }
        }
    }

	public static String stringOf(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = "";
		try {
			StringBuilder builder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			return builder.toString();
		} finally {
			IOUtilities.close(reader);
		}
	}

}
