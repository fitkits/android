package com.fitkits.RealmObjects;

import com.fitkits.Auth.Beans.OTP;
import com.fitkits.Model.ActivityAnswer;
import com.fitkits.Model.Aggregate;
import com.fitkits.Model.AttAggregate;
import com.fitkits.Model.CalorieAnswer;
import com.fitkits.Model.Feed;
import com.fitkits.Model.ItemParent;
import com.fitkits.Model.Membership;
import com.fitkits.Model.SleepAnswer;
import com.fitkits.Model.WaterAnswer;
import com.fitkits.Model.WeightAnswer;
import io.reactivex.Observable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import com.fitkits.Model.User;
import com.fitkits.Misc.Payment;

/**
 * Created by akshay on 20/12/17.
 */

public interface ApiService {

  @POST("/api/v1/payments/capture")
  Observable<Payment> capturePayment(@Body Payment payment);

  @POST("/api/v1/cms/users/create")
  Observable<User> userSignUp(@Body User_Profile user_);

  @POST("/otp/sendOTP")
  Observable<ResponseBody> sendOTP(@Body OTP user_);


  @POST("/otp/verifyOTP")
  Observable<User> verifyOtp(@Body OTP user_);

  @POST("/api/v1/cms/subscriptions/create")
  Observable<ResponseBody> subscribe(@Body Membership membership);



  @GET
  Observable<User> getUserProfile(@Url String path);
  @GET
  Observable<User> getPendingMembership(@Url String path);


  @GET
  Observable<ItemParent> getSubscriptions(@Url String path);

  @PATCH
  Observable<User> updateProfile(@Url String path, @Body User user_profile);

  @PATCH
  Observable<User> update_PendingMembership(@Url String path, @Body User user);


  @GET("/api/v1/cms/memberships/")
  Observable<ItemParent> getMemberships();

  @GET("/api/v1/cms/feeds/")
  Observable<ItemParent> getFeeds();

  @GET
  Observable<Feed> getFeedDetail(@Url String path);

  @POST("/api/v1/cms/answers/create")
  Observable<ResponseBody> logWeight(@Body WeightAnswer user_);

  @POST("/api/v1/cms/answers/create")
  Observable<ResponseBody> logWater(@Body WaterAnswer user_);

  @POST("/api/v1/cms/answers/create")
  Observable<ResponseBody> logActivity(@Body ActivityAnswer user_);


  @POST("/api/v1/cms/answers/create")
  Observable<ResponseBody> logCalorie(@Body CalorieAnswer user_);

  @POST("/api/v1/cms/answers/create")
  Observable<ResponseBody> logSleep(@Body SleepAnswer user_);

  @GET
  Observable<ItemParent> getWeeklyAggregate(@Url String path);

  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=caloriesPerDay&timeFrame=weekly")
  Observable<Aggregate> getCalorieAggregateMonthly(@Query("user") String user);

  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=caloriesPerDay&timeFrame=monthly")
  Observable<Aggregate> getCalorieAggregateQuarterly(@Query("user") String user);

  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=caloriesPerDay&timeFrame=quarterly")
  Observable<Aggregate> getCalorieAggregateYearly(@Query("user") String user);


  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=caloriesPerDay&timeFrame=yearly")
  Observable<Aggregate> getCalorieAggregateAllYear(@Query("user") String user);



  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=sleepDurationPerDay&timeFrame=weekly")
  Observable<Aggregate> getSleepAggregateMonthly(@Query("user") String user);

  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=sleepDurationPerDay&timeFrame=monthly")
  Observable<Aggregate> getSleepAggregateQuarterly(@Query("user") String user);

  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=sleepDurationPerDay&timeFrame=quarterly")
  Observable<Aggregate> getSleepAggregateYearly(@Query("user") String user);


  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=sleepDurationPerDay&timeFrame=yearly")
  Observable<Aggregate> getSleepAggregateAllYear(@Query("user") String user);



  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=activePerDay&timeFrame=weekly")
  Observable<Aggregate> getActiveHoursAggregateMonthly(@Query("user") String user);

  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=activePerDay&timeFrame=monthly")
  Observable<Aggregate> getActiveHoursAggregateQuarterly(@Query("user") String user);

  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=activePerDay&timeFrame=quarterly")
  Observable<Aggregate> getActiveHoursAggregateYearly(@Query("user") String user);


  @GET("/analytics/answers?type=goalAnalysis&goalType=activePerDay&timeFrame=yearly")
  Observable<Aggregate> getActiveHoursAggregateAllYear(@Query("user") String user);



  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=weight&timeFrame=weekly")
  Observable<Aggregate> getWeightAggregateMonthly(@Query("user") String user);

  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=weight&timeFrame=monthly")
  Observable<Aggregate> getWeightAggregateQuarterly(@Query("user") String user);

  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=weight&timeFrame=quarterly")
  Observable<Aggregate> getWeightAggregateYearly(@Query("user") String user);


  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=weight&timeFrame=yearly")
  Observable<Aggregate> getWeightAggregateAllYear(@Query("user") String user);




  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=waterConsumptionPerDay&timeFrame=weekly")
  Observable<Aggregate> getWaterAggregateMonthly(@Query("user") String user);

  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=waterConsumptionPerDay&timeFrame=monthly")
  Observable<Aggregate> getWaterAggregateQuarterly(@Query("user") String user);

  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=waterConsumptionPerDay&timeFrame=quarterly")
  Observable<Aggregate> getWaterAggregateYearly(@Query("user") String user);


  @GET("/api/v1/analytics/answers?type=goalAnalysis&goalType=waterConsumptionPerDay&timeFrame=yearly")
  Observable<Aggregate> getWaterAggregateAllYear(@Query("user") String user);

  @GET
  Observable<String>getAssessments(@Url String Url);

  @GET
  Observable<AttAggregate> getAttendanceAvg(@Url String path);

  @GET("/api/v1/cms/attendance")
  Observable<ItemParent> getAttendance();
  @GET
  Observable<ItemParent> getAttendance(@Url String path);

  @Multipart
  @PATCH
  Observable<ResponseBody> updateProfileImage(@Url String path, @Part MultipartBody.Part image);

  @Multipart
  @PATCH
  Observable<ResponseBody> updateProfileImagesWithWeight(@Url String path, @Part MultipartBody.Part image,
                                                         @Part("weight") RequestBody weight);

}
