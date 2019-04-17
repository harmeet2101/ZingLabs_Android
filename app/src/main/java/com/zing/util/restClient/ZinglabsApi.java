package com.zing.util.restClient;

import com.google.gson.JsonElement;
import com.zing.model.request.ReleaseShiftRequest;
import com.zing.model.request.SetPasswordRequest;
import com.zing.model.request.AddBankRequest;
import com.zing.model.request.BankTransferRequest;
import com.zing.model.request.CalendarRequest;
import com.zing.model.request.CompleteProfileRequest;
import com.zing.model.request.DeleteBankRequest;
import com.zing.model.request.EarningSlotRequest;
import com.zing.model.request.HomeRequest;
import com.zing.model.request.LeaveCancelRequest;
import com.zing.model.request.LeaveRequest;
import com.zing.model.request.LoginRequest;
import com.zing.model.request.PaymentIdRequest;
import com.zing.model.request.PhoneModel;
import com.zing.model.request.RateShiftRequest;
import com.zing.model.request.ShiftBreak;
import com.zing.model.request.ShiftCheckInRequest;
import com.zing.model.request.TimePreferenceRequest.TimePreferenceRequest;
import com.zing.model.request.UpcomingShiftRequest;
import com.zing.model.request.VerifyCheckInRequest;
import com.zing.model.request.VerifyNumberRequest;
import com.zing.model.response.AccountResponse.AccountResponse;
import com.zing.model.response.BroadcastResponse.BroadcastResponse;
import com.zing.model.response.CalendarScheduledShiftResponse.CalendarScheduledShiftResponse;
import com.zing.model.response.GetBusinessHourResponse.GetBusinessHours;
import com.zing.model.response.GetTimePreferenceResponse.GetTimePreferenceResponse;
import com.zing.model.response.HomeResponse.HomeResponse;
import com.zing.model.response.LoginResponse.LoginResponse;
import com.zing.model.response.RegisterResponse.RegisterResponse;
import com.zing.model.response.breakShift.ShiftBreakResponse;
import com.zing.model.response.ShiftDetailResponse.ShiftDetailResponse;
import com.zing.model.response.WeekPreference.WeekPreference;
import com.zing.model.response.countryListResponse.CountryResponse;
import com.zing.model.response.otpVerifyResponse.OtpVerifyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by savita on 1/5/18.
 */

public interface ZinglabsApi {
    @Headers("Content-Type:application/json")

    @POST("send_otp")
    Call<JsonElement> sendOtpApi(@Body PhoneModel jsonObject);

    @POST("verify_user_phone_otp")
    Call<OtpVerifyResponse> verifyOtpApi(@Body VerifyNumberRequest jsonObject);

    @POST("update_profile")
    Call<RegisterResponse> updateProfileApi(@Header("Authorization") String userToken,
                                            @Body CompleteProfileRequest completeProfileRequest);

    @POST("login")
    Call<LoginResponse> loginApi(@Body LoginRequest loginRequest);

    @POST("forgot_password")
    Call<JsonElement> forgotPasswordApi(@Body SetPasswordRequest setPasswordRequest);

    @GET("broadcasts")
    Call<BroadcastResponse> broadcastApi(@Header("Authorization") String userToken);

    @POST("set_preferences")
    Call<JsonElement> setPreferenceApi(@Header("Authorization") String userToken,
                                       @Body TimePreferenceRequest timePreferenceRequest);

    @GET("get_preferences")
    Call<GetTimePreferenceResponse> getPreferenceApi(@Header("Authorization") String userToken);

    @POST("account_details")
    Call<AccountResponse> accountDetailsApi(@Header("Authorization") String userToken);

    @POST("add_bank_account")
    Call<JsonElement> saveBankDetailsApi(@Header("Authorization") String userToken,
                                         @Body AddBankRequest addBankRequest);

    @POST("delete_bank")
    Call<JsonElement> deleteBankRequestApi(@Header("Authorization") String userToken,
                                           @Body DeleteBankRequest jsonObject);

    @POST("add_leave_request")
    Call<JsonElement> sendLeaveRequestApi(@Header("Authorization") String userToken,
                                          @Body LeaveRequest leaveRequest);

    @POST("cancel_leave_request")
    Call<JsonElement> cancelLeaveRequestApi(@Header("Authorization") String userToken,
                                            @Body LeaveCancelRequest leaveCancelRequest);

    @GET("my_stats")
    Call<JsonElement> statsDetailApi(@Header("Authorization") String userToken);

   /* @POST("check_in")
    Call<JsonElement> checkInShiftApi(@Header("Authorization") String userToken,
                                      @Body ShiftCheckInRequest shiftCheckInRequest);*/

    @POST("check_in")
    Call<JsonElement> checkInShiftApi(@Header("Authorization") String userToken,
                                      @Body VerifyCheckInRequest verifyCheckInRequest);

    @POST("check_out")
    Call<JsonElement> shiftCheckOutRatingApi(@Header("Authorization") String userToken,
                                             @Body RateShiftRequest rateShiftRequest);

    @POST("dashboard")
    Call<HomeResponse> dashboardApi(@Header("Authorization") String userToken,
                                    @Body HomeRequest homeRequest);

    @POST("calendar_slot")
    Call<JsonElement> calendarSlotDetailsApi(@Header("Authorization") String userToken,
                                             @Body CalendarRequest calendarRequest);

    @POST("calendar")
    Call<CalendarScheduledShiftResponse> calendarDetailApi(@Header("Authorization") String userToken,
                                                           @Body UpcomingShiftRequest upcomingShiftRequest);

    @POST("claim_shift")
    Call<JsonElement> claimShiftApi(@Header("Authorization") String userToken,
                                    @Body ShiftCheckInRequest shiftCheckInRequest);

    @POST("release_shift")
    Call<JsonElement> releaseShiftApi(@Header("Authorization") String userToken,
                                      @Body ReleaseShiftRequest shiftCheckInRequest);

    @POST("bank_transfer")
    Call<JsonElement> bankTransferApi(@Header("Authorization") String userToken,
                                      @Body BankTransferRequest bankTransferRequest);

    @POST("previous_paystubs")
    Call<JsonElement> previousPaystubApi(@Header("Authorization") String userToken);

    @POST("payment_overview")
    Call<JsonElement> paymentDetailApi(@Header("Authorization") String userToken,
                                       @Body PaymentIdRequest paymentIdRequest);

    @POST("logout")
    Call<JsonElement> logoutApi(@Header("Authorization") String userToken);

    @POST("earnings")
    Call<JsonElement> weekGraphApi(@Header("Authorization") String userToken,
                                   @Body EarningSlotRequest earningSlotRequest);

    @POST("shift_detail")
    Call<ShiftDetailResponse> shiftDetailApi(@Header("Authorization") String userToken,
                                             @Body ShiftCheckInRequest shiftCheckInRequest);

    @GET("get_preference_data")
    Call<WeekPreference> getWeekPreferenceApi(@Header("Authorization") String userToken);

    @GET("common/get_countries")
    Call<CountryResponse> getCountryListApi( );



    @GET("common/get_business_hours")
    Call<GetBusinessHours> getBusinessHour(@Header("Authorization") String userToken);


    @POST("shift_break")
    Call<ShiftBreakResponse> shiftBreakApi(@Header("Authorization") String userToken,
                                           @Body ShiftBreak shiftBreak);

}
