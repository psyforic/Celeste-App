package com.celeste.celestedaylightapp.retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import com.celeste.celestedaylightapp.model.authenticate.AuthenticateModel;
import com.celeste.celestedaylightapp.model.authenticate.AuthenticateResult;
import com.celeste.celestedaylightapp.utils.Constants;
import com.celeste.celestedaylightapp.utils.Tools;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.annotation.Nullable;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    SharedPreferences sharedpreferences;
    private Context context;

    public TokenAuthenticator(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, Response response) throws IOException {
        try {
            String username = Tools.getEncryptedSharedPreferences(context).getString(Constants.USERNAME, null);
            String password = Tools.getEncryptedSharedPreferences(context).getString(Constants.PASSWORD, null);
            Api api = ApiClient.getInstance(context).create(Api.class);
            AuthenticateModel authenticateModel = new AuthenticateModel(username, password);
            retrofit2.Response<AuthenticateResult> resultResponse = api.authenticateUser(authenticateModel).execute();
            AuthenticateResult authenticateResult = resultResponse.body();
            if (authenticateResult != null) {
                String newToken = authenticateResult.getResult().getAccessToken();
                return response.request().newBuilder().header("Authorization", "Bearer" + newToken)
                        .build();
            }
        } catch (GeneralSecurityException | IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
