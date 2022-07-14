package com.jxrisesun.rstool.core.util;

import java.io.File;

/**
 * 文件工具类
 * @author zhangzl
 *
 */
public class FileUtils {

	/**
	 * 文件路径分隔符<br>
	 * 在Unix和Linux下 是{@code '/'}; 在Windows下是 {@code '\'}
	 */
	public static final String FILE_SEPARATOR = File.separator;
	
	/**
	 * 类Unix路径分隔符
	 */
	public static final char FILE_UNIX_SEPARATOR = CharUtils.CHAR_SLASH;
	/**
	 * Windows路径分隔符
	 */
	public static final char FILE_WINDOWS_SEPARATOR = CharUtils.CHAR_BACKSLASH;
	
	/**
	 * 多个PATH之间的分隔符<br>
	 * 在Unix和Linux下 是{@code ':'}; 在Windows下是 {@code ';'}
	 */
	public static final String PATH_SEPARATOR = File.pathSeparator;
	
	/**
	 * 是否为Windows环境
	 *
	 * @return 是否为Windows环境
	 * @since 3.0.9
	 */
	public static boolean isWindows() {
		return FILE_WINDOWS_SEPARATOR == File.separatorChar;
	}
}
