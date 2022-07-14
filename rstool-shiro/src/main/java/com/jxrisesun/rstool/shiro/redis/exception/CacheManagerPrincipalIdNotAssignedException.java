package com.jxrisesun.rstool.shiro.redis.exception;

public class CacheManagerPrincipalIdNotAssignedException extends RuntimeException  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 620739805226563236L;
	
	private static final String MESSAGE = "CacheManager didn't assign Principal Id field name!";

    public CacheManagerPrincipalIdNotAssignedException() {
        super(MESSAGE);
    }
}
