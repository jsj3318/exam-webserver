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

import com.nhnacademy.http.context.Context;
import com.nhnacademy.http.context.ContextHolder;

public class CounterUtils {

    public final static String CONTEXT_COUNTER_NAME="Global-Counter";
    private CounterUtils(){}

    public static synchronized long increaseAndGet(){

        Context context = ContextHolder.getApplicationContext();
        long count = (long)context.getAttribute(CONTEXT_COUNTER_NAME);
        count++;
        context.setAttribute(CONTEXT_COUNTER_NAME, count);

        return count;
    }
}
