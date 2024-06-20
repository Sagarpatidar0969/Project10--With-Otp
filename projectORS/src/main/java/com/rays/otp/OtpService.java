package com.rays.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
	
	@Autowired
	OtpDao otpDao;
	
	
	public Long save(OtpDTO dto) {
		return otpDao.save(dto);
	}
  

}
