/*
 * All Application code is Copyright 2016, The Department of Homeland Security (DHS), U.S. Customs and Border Protection (CBP).
 *
 * Please see LICENSE.txt for details.
 */
package com.connector.qq.model;

import java.io.Serializable;

public class MessagePayload implements Serializable {

    private String messagePayload;

    @SuppressWarnings("unused")
    public String getMessagePayload() {
        return messagePayload;
    }

    public void setMessagePayload(String messagePayload) {
        this.messagePayload = messagePayload;
    }
}
