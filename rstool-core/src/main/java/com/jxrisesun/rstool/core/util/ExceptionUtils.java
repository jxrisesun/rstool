package com.jxrisesun.rstool.core.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 * @author zhangzl
 *
 */
public class ExceptionUtils {
	
	/**
	 * Exception
	 * @param message
	 * @param cause
	 * @return
	 */
	public static Exception createException(String message, Throwable cause) {
		return cause == null ? new Exception(message) : new Exception(message, cause);
	}
	
	/**
	 * IOException
	 * @param message
	 * @param cause
	 * @return
	 */
	public static IOException createIOException(String message, Throwable cause) {
		return cause == null ? new IOException(message) : new IOException(message, cause);
	}
	
	/**
	 * RuntimeException
	 * @param message
	 * @param cause
	 * @return
	 */
	public static RuntimeException createRuntimeException(String message, Throwable cause) {
		return cause == null ? new RuntimeException(message) : new RuntimeException(message, cause);
	}
	
	/**
	 * 获得完整消息，包括异常名
	 * @param e
	 * @return
	 */
	public static String getMessage(Throwable e) {
		return e == null ? StringUtils.STRING_NULL : StringUtils.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
	}
	
	/**
	 * 获得消息，调用异常类的getMessage方法
	 * 
	 * @param e 异常
	 * @return 消息
	 */
	public static String getSimpleMessage(Throwable e) {
		return (e == null) ? StringUtils.STRING_NULL : e.getMessage();
	}

	/**
	 * 获取异常堆栈信息
	 * @param e
	 * @return
	 */
	public static String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        return sw.toString();
    }
	
	/**
	 * 获取异常堆栈信息
	 * @param e
	 * @param packageName 限制包名
	 * @return
	 */
	public static String getStackTrace(Throwable e, String packageName) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String str = sw.toString();
        if (packageName == null) {
            return str;
        }
        String[] arrs = str.split("\n");
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(arrs[0] + "\n");
        for (int i = 0; i < arrs.length; i++) {
            String temp = arrs[i];
            if (temp != null && temp.indexOf(packageName) > 0) {
                sbuf.append(temp + "\n");
            }
        }
        return sbuf.toString();
    }
}
