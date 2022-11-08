package com.codesoom.assignment.methodController.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class UtfEncoder {

    private final int contentLength;
    private final byte[] content;

    public UtfEncoder(StringBuilder jsonResponse) {
        ByteBuffer byteData = Charset.forName("UTF-8").encode(jsonResponse.toString());
        this.contentLength = byteData.limit();
        this.content = new byte[contentLength];
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public byte[] getContent() {
        return this.content;
    }
}
