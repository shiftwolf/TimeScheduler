package com.company;

import javax.mail.MessagingException;

public class Main {

    public static void main(String[] args) throws MessagingException {
        JavaMailUtil.sendMail("put in recepient email");

        //Add following to maven file:
        /*
        <dependencies>
            <dependency>
                <groupId>com.sun.mail</groupId>
                <artifactId>javax.mail</artifactId>
                <version>1.6.2</version>
            </dependency>
        </dependencies>
        */
    }
}
