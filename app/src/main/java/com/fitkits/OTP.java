package com.fitkits;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OTP {

@SerializedName("mobileNumber")
@Expose
private String mobileNumber;
@SerializedName("otp")
@Expose
private String otp;

public OTP(String mobileNumber){
  this.mobileNumber=mobileNumber;

}

  public OTP(String mobileNumber,String otp){
    this.mobileNumber=mobileNumber;
    this.otp=otp;
  }

public String getMobileNumber() {
return mobileNumber;
}

public void setMobileNumber(String mobileNumber) {
this.mobileNumber = mobileNumber;
}

public String getOtp() {
return otp;
}

public void setOtp(String otp) {
this.otp = otp;
}

@Override
public String toString() {
return new ToStringBuilder(this).append("mobileNumber", mobileNumber).append("otp", otp).toString();
}

}