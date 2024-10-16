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

package com.nhnacademy.http.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ResponseUtilsTest {

    @Test
    @DisplayName("isExist:/404.html")
    void isExist1() {
        boolean actual = ResponseUtils.isExist(ResponseUtils.DEFAULT_404);
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("isExist:/")
    void isExist2() {
        //TODO#108 uri:"/" false를 반환하는지 검증 합니다.
        Assertions.assertEquals(false, ResponseUtils.isExist("/"));
    }

    @Test
    @DisplayName("isExist:/favicon.ico")
    void isExist3() {
        //TODO#109 uri:/favicon.ico 이면 false를 반환하는지 검증 합니다.
        Assertions.assertEquals(false, ResponseUtils.isExist("/favicon.ico"));
    }

    @Test
    @DisplayName("tryGetBodyFromFile : /index.html")
    void tryGetBodyFromFile() throws IOException {
        String actual = ResponseUtils.tryGetBodyFromFile("/index.html");
        Assertions.assertAll(
                ()->{
                    Assertions.assertTrue(actual.contains("<head>"));
                },
                ()->{
                    Assertions.assertTrue(actual.contains("Hello"));
                },
                ()->{
                    Assertions.assertTrue(actual.contains("Java"));
                },
                ()->{
                    Assertions.assertTrue(actual.contains("</html>"));
                }
        );
    }

    @Test
    @DisplayName("createResponseHeader:200")
    void createResponseHeader1() {
        String actual = ResponseUtils.createResponseHeader(ResponseUtils.HttpStatus.OK.getCode(), "utf-8",100);
        log.debug("actual:{}",actual);

        //TODO#110 actual (responseHeader)의 statusCode(200), description(OK) 포함되었는지 검증 합니다.
        Assertions.assertAll(
                ()->{
                  Assertions.assertTrue(actual.contains("200"));
                },
                ()->{
                    Assertions.assertTrue(actual.contains("OK"));
                }
        );
    }

    @Test
    @DisplayName("createResponseHeader:404")
    void createResponseHeader2() {
        String actual = ResponseUtils.createResponseHeader(ResponseUtils.HttpStatus.NOT_FOUND.getCode(), "utf-8",100);
        log.debug("actual:{}",actual);

        //TODO#111 actual (responseHeader)의 statusCode(400), description(Not Found) 포함되었는지 검증 합니다.
        Assertions.assertAll(
                ()->{
                    Assertions.assertTrue(actual.contains("404"));
                },
                ()->{
                    Assertions.assertTrue(actual.contains("Not Found"));
                }
        );
    }
}