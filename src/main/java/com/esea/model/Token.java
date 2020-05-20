package com.esea.model;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Token {
    private String tokenInfo;
    private Date tokenEndDate;
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    private static final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

    public Token() {
        byte[] randomBytes = new byte[1000];
        secureRandom.nextBytes(randomBytes);
        this.tokenInfo = base64Encoder.encodeToString(randomBytes);

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        Date afterAddingTenMins = new Date(t + (10 * ONE_MINUTE_IN_MILLIS));
        this.tokenEndDate = afterAddingTenMins;
    }

    public String getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(String tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public Date getTokenEndDate() {
        return tokenEndDate;
    }

    public void setTokenEndDate(Date tokenEndDate) {
        this.tokenEndDate = tokenEndDate;
    }
}
