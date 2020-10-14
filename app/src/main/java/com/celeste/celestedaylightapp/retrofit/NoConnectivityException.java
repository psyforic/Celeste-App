package com.celeste.celestedaylightapp.retrofit;

import java.io.IOException;

public class NoConnectivityException extends IOException {
    @Override
    public String getMessage() {
        return "Network Error, Please Make Sure You're Connected To The Internet";
    }
}
