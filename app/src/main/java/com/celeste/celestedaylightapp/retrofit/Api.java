package com.celeste.celestedaylightapp.retrofit;

import com.celeste.celestedaylightapp.model.authenticate.AuthenticateModel;
import com.celeste.celestedaylightapp.model.authenticate.AuthenticateResult;
import com.celeste.celestedaylightapp.model.tenant.TenantDetailResult;
import com.celeste.celestedaylightapp.model.user.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("/api/TokenAuth/Authenticate")
    Call<AuthenticateResult> authenticateUser(@Body AuthenticateModel model);

    @GET("/api/services/app/Tenant/GetAll")
    Call<TenantDetailResult> getTenants(@Query("Keyword") String Keyword, @Query("IsActive") boolean IsActive, @Query("SkipCount") int SkipCount, @Query("MaxResultCount") int MaxResultCount);

//    @GET("/api/services/app/Tenant/GetAll")
//    Call<TenantDetailModel> getTenant(@Query("Id") String Id);

    @GET("/api/services/app/User/Get")
    Call<UserProfile> getUser(@Query("id") int Id);

    @GET("/api/services/app/User/GetAll")
    Call<UserProfile> getUser(@Query("Keyword") String Keyword, @Query("IsActive") boolean IsActive, @Query("SkipCount") int SkipCount, @Query("MaxResultCount") int MaxResultCount);
}
