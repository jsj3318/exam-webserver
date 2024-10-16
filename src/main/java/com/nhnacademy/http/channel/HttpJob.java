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

package com.nhnacademy.http.channel;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.security.cert.CRL;
import java.util.Objects;

@Slf4j
public class HttpJob implements Executable {
    private final Socket client;
    private static final String CRLF="\r\n";

    public HttpJob(Socket client) {
        if(Objects.isNull(client)){
            throw new IllegalArgumentException("client Socket is null");
        }
        this.client = client;
    }

    public Socket getClient() {
        return client;
    }

    @Override
    public void execute(){

        //TODO#23 HttpJob는 execute() method를 구현 합니다. step2~3 참고하여 구현합니다.
        //<html><body><h1>thread-0:hello java</h1></body>
        //<html><body><h1>thread-1:hello java</h1></body>
        //<html><body><h1>thread-2:hello java</h1></body>
        //....
        try(
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        ) {

            StringBuilder requestBuilder = new StringBuilder();
            log.debug("------HTTP-REQUEST_start()");
            while (true) {
                String line = bufferedReader.readLine();
                requestBuilder.append(line);
                log.debug("{}", line);

                if(line == null || line.isEmpty()){
                    break;
                }
            }
            log.debug("------HTTP-REQUEST_end()");


            StringBuilder responseBody = new StringBuilder();
            responseBody.append("<html>");
            responseBody.append("   <body>");
            responseBody.append("       <h1>" + Thread.currentThread().getName() + ":hello java</h1>");
            responseBody.append("   </body>");
            responseBody.append("</html>");

            StringBuilder responseHeader = new StringBuilder();

            //HTTP/1.0 200 OK
            responseHeader.append("HTTP/1.0 200 OK" + CRLF);

            responseHeader.append(String.format("Server: HTTP server/0.1%s",CRLF));

            //Content-type: text/html; charset=UTF-8"
            responseHeader.append("Content-type: text/html; charset=UTF-8" + CRLF);


            //Connection: close
            responseHeader.append("Connection: closed" + CRLF);

            //Content-Length
            responseHeader.append("Content-Length: " + responseBody.length() + CRLF);

            //write Response Header
            bufferedWriter.write(responseHeader + CRLF);

            //write Response Body
            bufferedWriter.write(responseBody.toString());

            bufferedWriter.flush();

            log.debug("header:{}",responseHeader);
            log.debug("body:{}",responseBody);

        }catch (IOException e){
            log.error("socket error : {}",e);
        }
    }
}
