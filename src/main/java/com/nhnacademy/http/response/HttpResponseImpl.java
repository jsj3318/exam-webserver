/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2024. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Objects;


public class HttpResponseImpl implements HttpResponse {
    //TODO#4 HttpResponse를 구현 합니다.

    private final Socket socket;
    private String charset;

    public HttpResponseImpl(Socket socket) {
        if(socket == null){
            throw new IllegalArgumentException("socket이 없어유");
        }
        this.socket = socket;
        charset = "utf-8";
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new DataOutputStream(socket.getOutputStream()), true);
    }

    @Override
    public void setCharacterEncoding(String charset) {
        this.charset = charset;
    }

    @Override
    public String getCharacterEncoding() {
        return charset;
    }
}
