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

package com.nhnacademy.http;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.cert.CRL;
import java.util.Objects;

@Slf4j
public class SimpleHttpServer {

    private final int port;
    private static final int DEFAULT_PORT=8080;
    private final ServerSocket serverSocket;

    //TODO#1 CRLF를 선언합니다. CRLF는 Carriage Return (CR) 와 Line Feed (LF)를 의미하며, HTTP 프로토콜에서 줄 바꿈을 나타내기 위해 사용됩니다. 이는 \r\n으로 표현됩니다.
    private static final String CRLF="\r\n";

    public SimpleHttpServer(){
        //TODO#2 기본 port는 DEFAULT_PORT을 사용합니다.
        port = DEFAULT_PORT;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SimpleHttpServer(int port) {
        //TODO#3 port range <=0 IllegalArgumentException 예외가 발생 합니다. 적절한 Error Message를 작성해주세요.
        if(port <= 0){
            throw new IllegalArgumentException("올바르지 않은 port번호.");
        }

        this.port = port;


        //TODO#4 serverSocket을 초기화 합니다.
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() throws IOException {

        while(true){

            try(
                //TODO#5 client가 연결될 때 까지 대기 합니다.
                Socket client = serverSocket.accept();

                //TODO#6 입출력을 위해서  Reader, Writer를 선언 합니다.
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            ) {

                StringBuilder requestBuilder = new StringBuilder();
                log.debug("------HTTP-REQUEST_start()");
                while (true) {
                    String line = bufferedReader.readLine();
                    //TODO#7  requestBuilder에 append 합니다.
                    requestBuilder.append(line);
                    log.debug("{}", line);

                    //TODO#8 종료 조건 null or size==0
                    if(line == null || line.isEmpty()){
                        break;
                    }
                }
                log.debug("------HTTP-REQUEST_end()");

                //TODO#9 clinet에 응답할 html을 작성합니다.
                /*
                    <html>
                        <body>
                            <h1>hello hava</h1>
                        </body>
                    </html>
                */

                StringBuilder responseBody = new StringBuilder();
                responseBody.append("<html>");
                responseBody.append("   <body>");
                responseBody.append("       <h1>hello sava</h1>");
                responseBody.append("   </body>");
                responseBody.append("</html>");

                StringBuilder responseHeader = new StringBuilder();

                //TODO#10 HTTP/1.0 200 OK
                responseHeader.append("HTTP/1.0 200 OK" + CRLF);

                responseHeader.append(String.format("Server: HTTP server/0.1%s",CRLF));

                //TODO#11 Content-type: text/html; charset=UTF-8"
                responseHeader.append("Content-type: text/html; charset=UTF-8" + CRLF);


                //TODO#12  Connection: close 헤더를 사용하면 해당 요청 후에 연결이 닫히게 된다.
                responseHeader.append("Connection: closed" + CRLF);

                //TODO#13 responseBody의  Content-Length를 설정 합니다.
                responseHeader.append("Content-Length: " + responseBody.length() + CRLF);

                //TODO#14 write Response Header
                bufferedWriter.write(responseHeader + CRLF);

                //TODO#15 write Response Body
                bufferedWriter.write(responseBody.toString());

                //TODO#16 buffer에 등록된 Response (header, body) flush 합니다.(socket을 통해서 clent에 응답 합니다.)
                bufferedWriter.flush();

                log.debug("header:{}",responseHeader);
                log.debug("body:{}",responseBody);

            }catch (IOException e){
                log.error("socket error : {}",e);
            }
        }//end while
    }//end start
}
