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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpRequestImpl implements HttpRequest {

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
            StringBuilder result = new StringBuilder();
            do{
                try{
                    result.append((char) client.getInputStream().read());
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
            }while(client.getInputStream().available() > 0);
            log.debug("result: {}", result);

            String[] lines = result.toString().split("\n");
            int count=0;
            for(int i=0; i<lines.length; i++){
                String line = lines[i];

                if(line == null || line.isBlank()){
                    count++;
                    continue;
                }

                if(i==0){
                    //첫 줄 GET /index.html?id=marco&age=40&name=마르코 HTTP/1.1
                    ParseFirstLine(line);
                    continue;
                }
                if(getMethod().equals("POST") && count > 0){
                    parseParams(line);
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

        //파라미터 맵 초기화
        Map<String, String> paramMap = new HashMap<>();
        headerMap.put(KEY_PARAM_MAP, paramMap);

        //uri넣기
        if(splitLine[1].contains("?")){
            //파라미터 있을 경우
            String[] strs = splitLine[1].split("\\?");
            // /index.html
            headerMap.put(KEY_PATH, strs[0]);

            //파라미터 넣기
            // id=marco&age=40&name=마르코
            parseParams(strs[1]);

        }
        else{
            //파라미터 없을 경우
            headerMap.put(KEY_PATH, splitLine[1]);

        }


    }

    private void ParseHeader(String line){
        // Host: localhost:8080
        String[] splitHeader = line.split(": ");

        String key = splitHeader[0];
        String value = splitHeader[1];

        if(key.equals("Content-Type") && value.contains("charset")){
            int index = value.indexOf("charset");
            String charsetStr = value.substring(index);
            ParseHeader(charsetStr.replace("=", ": "));
        }

        headerMap.put(splitHeader[0], splitHeader[1]);
    }

    private void parseParams(String line){
        // line : userId=marco&userPassword=12345&userEemail=marco@nhnacademy.com
        String[] params = line.split("&");

        for(String param : params){
            parseParam(param);
        }
    }

    private void parseParam(String param){
        String[] splitParam = param.split("=");
        String key = splitParam[0];
        String value = URLDecoder.decode(splitParam[1].trim(), StandardCharsets.UTF_8);

        Map<String, String> paramMap = getParameterMap();
        paramMap.put(key, value);
        log.debug("param key: {}, value: {}", key, value);
    }

}
