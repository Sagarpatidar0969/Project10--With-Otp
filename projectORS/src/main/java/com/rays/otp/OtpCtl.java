package com.rays.otp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rays.common.BaseCtl;
import com.rays.common.ORSResponse;
import com.rays.common.UserContext;
import com.rays.dto.UserDTO;
import com.rays.form.LoginForm;
import com.rays.form.UserForm;
import com.rays.service.UserServiceInt;

@RestController
@RequestMapping(value = "otp")
public class OtpCtl  extends BaseCtl<UserForm, UserDTO, UserServiceInt>{
		
	@PostMapping("otpAuth")
	public ORSResponse otpLogin(@RequestBody @Valid OtpForm form, BindingResult bindingResult, HttpSession session,
			HttpServletRequest request) throws Exception {
		//System.out.println("loginCtl ki login API ko hit kiya");
		System.out.println("otp login=======================================");
		ORSResponse res = validate(bindingResult);

		session = request.getSession(true);

		if (!res.isSuccess()) {
			return res;
		}

		UserDTO dto = baseService.otpAuthenticate(form.getOtp());
		if (dto == null) {
			System.out.println("dto == null ");
			res.setSuccess(false);
			res.addMessage("Invalid opt");
			return res;
		} else {
			UserContext context = new UserContext(dto);

////			 session.setAttribute("userContext", context); 				
//
			session.setAttribute("test", dto.getFirstName());
//			System.out.println("login id => " + session.getId());
//
//			res.setSuccess(true);
			session.setAttribute("user", dto.getFirstName());
//			res.addData(dto);
////			res.addResult("jsessionid", session.getId());
//			res.addResult("loginId", dto.getLoginId());
//			res.addResult("role", dto.getRoleName());
//			res.addResult("fname", dto.getFirstName());
//			res.addResult("lname", dto.getLastName());

			/* System.out.println("jsessionid " + session.getId()); */
			System.out.println("Before calling userDetail authenticate");

			return res;

		}

		
	}
	
	
}
