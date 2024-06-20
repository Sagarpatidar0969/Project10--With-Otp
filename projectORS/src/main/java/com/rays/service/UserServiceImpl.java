package com.rays.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rays.common.BaseServiceImpl;
import com.rays.common.UserContext;
import com.rays.common.mail.EmailDTO;
import com.rays.common.mail.EmailServiceImpl;
import com.rays.common.message.MessageDTO;
import com.rays.dao.UserDAOInt;
import com.rays.dto.UserDTO;
import com.rays.otp.OtpCtl;
import com.rays.otp.OtpDTO;
import com.rays.otp.OtpService;

/**
 * Session facade of Role Service. It is transactional, apply declarative
 * transactions with help of Spring AOP.
 * 
 * If unchecked exception is propagated from a method then transaction is rolled
 * back.
 *   
 * Default propagation value is Propagation.REQUIRED and readOnly = false
 * 
 * Sagar Patidar
 */
@Service
@Transactional
public class UserServiceImpl extends BaseServiceImpl<UserDTO, UserDAOInt> implements UserServiceInt {

	private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	EmailServiceImpl emailService = null;

	
	@Autowired
	UserDAOInt userDAO;

	/**
	 * Find user by login id
	 */
	@Transactional(readOnly = true)
	public UserDTO findByLoginId(String login, UserContext userContext) {
		System.out.println("findByLoginId in UserServiceImp");
		return baseDao.findByUniqueKey("loginId", login, userContext);
	}
	
	@Transactional(readOnly = true)
	public UserDTO findByOtp(String otp, UserContext userContext) {
		//System.out.println("findByLoginId in UserServiceImp");
		return baseDao.findByUniqueKey("otp", otp, userContext);
	}
	
	

	/**
	 * Authenticate a user
	 */
	@Override
	public UserDTO authenticate(String loginId, String password) {
		System.out.println("Authenticate in UserServiceImp");
		UserDTO dto = findByLoginId(loginId, null);
		if (dto != null) {
			UserContext userContext = new UserContext(dto);
			if (password.equals(dto.getPassword())) {
				dto.setLastLogin(new Timestamp((new Date()).getTime()));
				dto.setUnsucessfullLoginAttempt(0);
				
				EmailDTO emailDTO = new EmailDTO();
				emailDTO.addTo(dto.getLoginId());

				HashMap<String, String> params = new HashMap<String, String>();
				params.put("user", dto.getFirstName() + " " + dto.getLastName());
				System.out.println(dto.getFirstName() + dto.getLastName());
				
				
//				Random r = new Random();
//				String otp =String.valueOf(r.nextInt(10000));
//				
				Random r = new Random();
		        int randomNumber = r.nextInt(10000); // Generates a random number between 0 and 9999
		        String otp = String.format("%04d", randomNumber); 				
				System.out.println(otp); 
			
				dto.setOtp(otp);
				
				
				//params.put("password", dto.getPassword());
				params.put("otp", otp);
				System.out.println(dto.getPassword());
				emailDTO.setMessageCode("U-OT", params);
				emailService.send(emailDTO, null);
				
				update(dto, userContext);
				return dto;
			} else {
				dto.setUnsucessfullLoginAttempt(1 + dto.getUnsucessfullLoginAttempt());
				update(dto, userContext);
			}
		}
		return null;
	}
	
	
	
	

	/**
	 * Changes password of logged-in user
	 */
	@Override
	public UserDTO changePassword(String loginId, String oldPassword, String newPassword, UserContext userContext) {

		UserDTO dto = findByLoginId(loginId, null);

		if (oldPassword.equals(dto.getPassword())) {

			dto.setPassword(newPassword);
			update(dto, userContext);

			EmailDTO emailDTO = new EmailDTO();
			emailDTO.addTo(dto.getLoginId());

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("user", dto.getFirstName() + " " + dto.getLastName());
			emailDTO.setMessageCode("U-CP", params);

			emailService.send(emailDTO, null);

			return dto;
		} else {
			return null;
		}
	}

	/**
	 * Forgot password
	 */
	@Override
	public UserDTO forgotPassword(String loginId) {
		System.out.println("forgetPassword in UserService");
		UserDTO dto = findByLoginId(loginId, null);

		UserContext userContext = new UserContext();
		userContext.setLoginId("super@nenosystems.com");
		userContext.setOrgId(0L);
		userContext.setOrgName("root");

		if (dto == null) {
			return null;
		}

		EmailDTO emailDTO = new EmailDTO();
		emailDTO.addTo(dto.getLoginId());

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user", dto.getFirstName() + " " + dto.getLastName());
		System.out.println(dto.getFirstName() + dto.getLastName());
		
		
//		Random r = new Random();
//		String otp =String.valueOf(r.nextInt(10000));
//		
//		System.out.println(otp);
		params.put("password", dto.getPassword());
		//params.put("otp", otp);
		System.out.println(dto.getPassword());
		emailDTO.setMessageCode("U-FP", params);
		
		
		
		
		
	//	System.out.println("ooooooooooooooooooo" + emailDTO.getOtp());
		
//		OtpDTO otpDto = new OtpDTO();
//System.out.println("oooooooooooooooooooooo"+ 		otpDto.getOtp(););
//		
		//otpDto.setOtp(Integer.parseInt(otp));
	//	System.out.println("otpDto================="+otpDto);
		

		emailService.send(emailDTO, null);

		return dto;
	}

	/**
	 * Register new user
	 */
	@Override
	public UserDTO register(UserDTO dto) {

		UserContext userContext = new UserContext();
		userContext.setLoginId("super@nenosystems.com");
		userContext.setOrgId(0L);
		userContext.setOrgName("root");

		Long id =add(dto, userContext);

		dto.setId(id);
		System.out.println("ID :: " + dto.getId());
		System.out.println("Email Start");
		EmailDTO emailDTO = new EmailDTO();
		emailDTO.addTo(dto.getLoginId());

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user", dto.getFirstName() + " " + dto.getLastName());
		params.put("login", dto.getLoginId());
		params.put("password", dto.getPassword());
		emailDTO.setMessageCode("U-REG", params);

		emailService.send(emailDTO, userContext);

		return dto;
	}

	@Override
	public UserDTO findByEmail(String email, UserContext userContext) {
		return userDAO.findByEmail("email", email, userContext);

	}

	//============================================================
		@Override
		public UserDTO otpAuthenticate(String otp) {
			System.out.println("Authenticate in UserServiceImp");
			UserDTO dto = findByOtp(otp,null);
			if (dto != null) {
				UserContext userContext = new UserContext(dto);
				if (otp.equals(dto.getOtp())) {
					return dto;
				}
			}
			return null;
		}
	
}




