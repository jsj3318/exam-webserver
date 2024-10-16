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

package com.nhnacademy.http.request;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class HttpRequestImpl implements HttpRequest {
    /* TODO#2 HttpRequest를 구현 합니다.
    *  test/java/com/nhnacademy/http/request/HttpRequestImplTest TestCode를 실행하고 검증 합니다.
    */

    private final Socket client;
    private Map<String, Object> headerMap;
    private Map<String, Object> attributeMap;
    private final static String KEY_HTTP_METHOD = "HTTP_METHOD";
    private final static String KEY_PARAM_MAP = "PARAM_MAP";
    private final static String KEY_PATH = "PATH";


    public HttpRequestImpl(Socket client) {
        this.client = client;
        headerMap = new HashMap<>();
        attributeMap = new HashMap<>();

        init();
    }

    @Override
    public String getMethod() {
        return (String)headerMap.get(KEY_HTTP_METHOD);
    }

    @Override
    public String getParameter(String name) {
        return getParameterMap().get(name);
    }

    @Override
    public Map<String, String> getParameterMap() {
        return (Map<String, String>) headerMap.get(KEY_PARAM_MAP);
    }

    @Override
    public String getHeader(String name) {
        return (String)headerMap.get(name);
    }

    @Override
    public void setAttribute(String name, Object o) {
        attributeMap.put(name, o);
    }

    @Override
    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }

    @Override
    public String getRequestURI() {
        return (String)headerMap.get(KEY_PATH);
    }

    private void init(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            //첫 줄 GET /index.html?id=marco&age=40&name=마르코 HTTP/1.1
            String line = bufferedReader.readLine();
            log.debug("line: {}",line);
            ParseFirstLine(line);

            //나머지 줄
            while (true){
                line = bufferedReader.readLine();
                log.debug("line: {}",line);

                if(line == null || line.isEmpty()){
                    break;
                }
                ParseHeader(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void ParseFirstLine(String line){
        //첫 줄 GET /index.html?id=marco&age=40&name=마르코 HTTP/1.1
        String[] splitLine = line.split(" ");

        //메소드 넣기
        headerMap.put(KEY_HTTP_METHOD, splitLine[0]);

        //uri넣기
        if(splitLine[1].contains("?")){
            //파라미터 있을 경우
            String[] strs = splitLine[1].split("\\?");
            // /index.html
            headerMap.put(KEY_PATH, strs[0]);

            //파라미터 넣기
            Map<String, String> paramMap = new HashMap<>();
            // id=marco&age=40&name=마르코
            String[] params = strs[1].split("&");

            for(String s : params){
                // s : "id=marco"
                String[] splitParam = s.split("=");
                paramMap.put(splitParam[0], splitParam[1]);
            }

            headerMap.put(KEY_PARAM_MAP, paramMap);

            return;
        }
        //파라미터 없을 경우
        headerMap.put(KEY_PATH, splitLine[1]);

    }

    private void ParseHeader(String line){
        // Host: localhost:8080
        String[] splitHeader = line.split(": ");
        headerMap.put(splitHeader[0], splitHeader[1]);
    }


}
