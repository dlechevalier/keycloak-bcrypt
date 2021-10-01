package com.github.leroyguillaume.keycloak.bcrypt;

import org.junit.jupiter.api.Test;

public class BCryptPasswordHashProviderTest {

    private static String password;
    private static String sha256password;
    private static String bcryptpassword;



    @Test
    void encode() {
        BCryptPasswordHashProvider provider;
        provider = new BCryptPasswordHashProvider("bcrypt",10);
        String result = provider.encode(password,10);
        System.out.println(result);

    }

    @Test
    void verify() {
        BCryptPasswordHashProvider provider;
        provider = new BCryptPasswordHashProvider("bcrypt",10);
        //String result = provider.verify();
    }
}