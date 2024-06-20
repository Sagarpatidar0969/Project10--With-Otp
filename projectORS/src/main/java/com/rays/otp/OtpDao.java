package com.rays.otp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OtpDao {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	
public Long save(OtpDTO dto) {
	entityManager.persist(dto);
	return dto.getId();
	
}
}
