package org.openhds.webservice;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class CacheResponseWriter {

    public static void writeResponse(File fileToWrite, HttpServletResponse response) throws IOException {
        if (!fileToWrite.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        response.setStatus(HttpServletResponse.SC_OK);

        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(fileToWrite));
            IOUtils.copy(is, response.getOutputStream());
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
        }
    }
}
