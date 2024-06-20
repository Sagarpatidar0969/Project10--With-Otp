package com.rays.otp;

import javax.validation.constraints.NotEmpty;

import com.rays.common.BaseForm;

public class OtpForm extends BaseForm{
	
	@NotEmpty(message = "please enter otp")
	private String otp;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

}
