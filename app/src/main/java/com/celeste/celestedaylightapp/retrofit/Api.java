package com.celeste.celestedaylightapp.retrofit;

import com.celeste.celestedaylightapp.model.authenticate.AuthenticateModel;
import com.celeste.celestedaylightapp.model.authenticate.AuthenticateResult;
import com.celeste.celestedaylightapp.model.modes.AddModeResponse;
import com.celeste.celestedaylightapp.model.registertenant.RegisterTenantResponse;
import com.celeste.celestedaylightapp.model.registertenant.RegisterTenantResult;
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

    @POST("/api/services/app/TenantRegistration/RegisterTenant")
    Call<RegisterTenantResponse> registerTenant(@Body() RegisterTenantResult registerTenantResult);

    @POST("/api/services/app/Account/IsTenantAvailable")
    Call<TenantResponse> tenantLogin(@Body() TenantLoginModel tenantLoginModel);

    @GET("/api/services/app/User/GetAll")
    Call<UserGetResponse> getUsers(@Query("Keyword") String Keyword, @Query("IsActive") boolean IsActive, @Query("SkipCount") int SkipCount, @Query("MaxResultCount") int MaxResultCount);

    @GET("/api/services/app/User/Get")
    Call<GetSingleUserResponse> getSingleUser(@Query("Id") int id);

    @POST("/api/services/app/Mode/AddSelectedMode")
    Call<AddModeResponse> addSelectedMode(@Query("tenantId") int id, @Query("modeId") String modeId);

    @PUT("/api/services/app/User/UpdateUser")
    Call<UpdateUserResponse> updateUser(@Query("id") int id, @Body() UpdateUserResult userModel);

}
