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

package com.nhnacademy.http.context;

import com.nhnacademy.http.context.exception.ObjectNotFoundException;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ApplicationContext  implements Context {
    ConcurrentMap<String, Object> objectMap;

    public ApplicationContext() {
        this.objectMap = new ConcurrentHashMap<>();
    }


    @Override
    public void setAttribute(String name, Object object) {
        if(object == null){
            throw new IllegalArgumentException("null 값 들어옴");
        }
        objectMap.put(name, object);
    }

    @Override
    public void removeAttribute(String name) {
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException();
        }
        objectMap.remove(name);
    }

    @Override
    public Object getAttribute(String name) {
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException();
        }
        if(!objectMap.containsKey(name)){
            throw new ObjectNotFoundException("존재하지 않는 컨텍스트");
        }
        return objectMap.get(name);
    }
}
