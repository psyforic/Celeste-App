package com.celeste.celestedaylightapp.retrofit;

import com.celeste.celestedaylightapp.model.authenticate.AuthenticateModel;
import com.celeste.celestedaylightapp.model.authenticate.AuthenticateResult;
import com.celeste.celestedaylightapp.model.modes.ModeGetResponse;
import com.celeste.celestedaylightapp.model.tenant.TenantLoginModel;
import com.celeste.celestedaylightapp.model.tenant.TenantResponse;
import com.celeste.celestedaylightapp.model.user.GetSingleUserResponse;
import com.celeste.celestedaylightapp.model.user.UpdateUserResponse;
import com.celeste.celestedaylightapp.model.user.UpdateUserResult;
import com.celeste.celestedaylightapp.model.user.UserGetResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Api {

    @POST("/api/TokenAuth/Authenticate")
    Call<AuthenticateResult> authenticateUser(@Body AuthenticateModel model);

    @GET("/api/services/app/Tenant/GetAll")
    Call<TenantResponse> getTenants(@Query("Keyword") String Keyword, @Query("IsActive") boolean IsActive, @Query("SkipCount") int SkipCount, @Query("MaxResultCount") int MaxResultCount);

//    @GET("/api/services/app/Tenant/GetAll")
//    Call<TenantDetailModel> getTenant(@Query("Id") String Id);


//    @GET("/api/services/app/UserModes/GetAll")
//    Call<UserModeGetResponse> getUserModes(@Query("MaxResultCount") int MaxResultCount,@Query("SkipCount") int SkipCount);

    @GET("/api/services/app/Mode/GetAll")
    Call<ModeGetResponse> getModes(@Query("MaxResultCount") int MaxResultCount, @Query("SkipCount") int SkipCount);

    @GET("/api/services/app/User/GetUserandModes")
    Call<ModeGetResponse> getUserMode(@Query("id") int id);

    @GET("/api/services/app/User/GetAll")
    Call<UserGetResponse> getUsers(@Query("Keyword") String Keyword, @Query("IsActive") boolean IsActive, @Query("SkipCount") int SkipCount, @Query("MaxResultCount") int MaxResultCount);

    @GET("/api/services/app/User/GetUserandModes")
    Call<UserGetResponse> getUser(@Query("id") int id);

    @GET("/api/services/app/User/Get")
    Call<GetSingleUserResponse> getSingleUser(@Query("Id") int id);

    @PUT("/api/services/app/User/UpdateUser")
    Call<UpdateUserResponse> updateUser(@Query("id") int id, @Body() UpdateUserResult userModel);

    @POST("/api/services/app/Account/IsTenantAvailable")
    Call<TenantResponse> tenantLogin(@Body() TenantLoginModel tenantLoginModel);

}
