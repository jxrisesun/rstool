package com.jxrisesun.rstool.core.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IO 工具类
 * @author zhangzl
 *
 */
public class IoUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(IoUtils.class);
	
	/** 默认数据缓冲区大小 */
	public static final int DEFAULT_BUFFER_SIZE = 1024 * 10;
	
	/** 数据流末尾 */
	public static final int EOF = -1;
	
	/**
	 * 获得{@link BufferedReader}<br>
	 * 如果是{@link BufferedReader}强转返回，否则新建。如果提供的Reader为null返回null
	 *
	 * @param reader 普通Reader，如果为null返回null
	 * @return {@link BufferedReader} or null
	 */
	public static BufferedReader getReader(Reader reader) {
		if (null == reader) {
			return null;
		}
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}
	
	/**
	 * 获得一个Reader
	 *
	 * @param in      输入流
	 * @param charset 字符集
	 * @return BufferedReader对象
	 */
	public static BufferedReader getReader(InputStream in, Charset charset) {
		if (null == in) {
			return null;
		}
		InputStreamReader reader;
		if (null == charset) {
			reader = new InputStreamReader(in);
		} else {
			reader = new InputStreamReader(in, charset);
		}
		return new BufferedReader(reader);
	}
	
	/**
	 * 获得一个Writer
	 *
	 * @param out     输入流
	 * @param charset 字符集
	 * @return OutputStreamWriter对象
	 */
	public static OutputStreamWriter getWriter(OutputStream out, Charset charset) {
		if (null == out) {
			return null;
		}
		if (null == charset) {
			return new OutputStreamWriter(out);
		} else {
			return new OutputStreamWriter(out, charset);
		}
	}
	
	/**
	 * 拷贝数据
	 * @param reader
	 * @param writer
	 * @param bufferSize
	 * @return
	 */
	public static long copy(Reader reader, Writer writer) throws IOException {
		return copy(reader, writer, DEFAULT_BUFFER_SIZE, null);
	}
	
	/**
	 * 拷贝数据
	 * @param reader
	 * @param writer
	 * @param bufferSize
	 * @return
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize) throws IOException {
		return copy(reader, writer, bufferSize, null);
	}
	
	/**
	 * 拷贝数据
	 * @param reader
	 * @param writer
	 * @param bufferSize
	 * @param streamProgress
	 * @return
	 * @throws IOException 
	 */
	public static long copy(Reader reader, Writer writer, int bufferSize, CopyProgress streamProgress) throws IOException {
		char[] buffer = new char[bufferSize];
		long size = 0;
		int readSize;
		if (null != streamProgress) {
			streamProgress.start();
		}
		while ((readSize = reader.read(buffer, 0, bufferSize)) != EOF) {
			writer.write(buffer, 0, readSize);
			size += readSize;
			writer.flush();
			if (null != streamProgress) {
				streamProgress.progress(size);
			}
		}
		if (null != streamProgress) {
			streamProgress.finish();
		}
		return size;
	}
	
	/**
	 * 拷贝数据
	 * @param in
	 * @param out
	 * @return
	 */
	public static long copy(InputStream in, OutputStream out) throws IOException {
		return copy(in, out, DEFAULT_BUFFER_SIZE, null);
	}
	
	/**
	 * 拷贝数据
	 * @param in
	 * @param out
	 * @param bufferSize
	 * @return
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
		return copy(in, out, bufferSize, null);
	}
	
	/**
	 * 拷贝数据
	 * @param in
	 * @param out
	 * @param bufferSize
	 * @param streamProgress
	 * @return
	 * @throws IOException 
	 */
	public static long copy(InputStream in, OutputStream out, int bufferSize, CopyProgress streamProgress) throws IOException {
		if (bufferSize <= 0) {
			bufferSize = DEFAULT_BUFFER_SIZE;
		}
		byte[] buffer = new byte[bufferSize];
		if (null != streamProgress) {
			streamProgress.start();
		}
		long size = 0;
		for (int readSize = -1; (readSize = in.read(buffer)) != EOF;) {
			out.write(buffer, 0, readSize);
			size += readSize;
			out.flush();
			if (null != streamProgress) {
				streamProgress.progress(size);
			}
		}
		if (null != streamProgress) {
			streamProgress.finish();
		}
		return size;
	}
	
	/**
	 * 从缓存中刷出数据
	 *
	 * @param flushable {@link Flushable}
	 * @return
	 */
	public static boolean flush(Flushable flushable) {
		if(flushable == null) {
			return false;
		}
		try {
			flushable.flush();
			return true;
		} catch (Exception e) {
			// 静默刷出
			return false;
		}
	}
	
	/**
	 * 关闭<br>
	 * 关闭失败不会抛出异常
	 *
	 * @param closeable 被关闭的对象
	 * @return
	 */
	public static boolean close(Closeable closeable) {
		if(closeable == null) {
			return false;
		}
		try {
			closeable.close();
			return true;
		} catch (Exception e) {
			// 静默关闭
			return false;
		}
	}
	
	/**
	 * 关闭
	 * @param closeable
	 * @return
	 */
	public static boolean close(AutoCloseable closeable) {
		if(closeable == null) {
			return false;
		}
		try {
			closeable.close();
			return true;
		} catch (Exception e) {
			// 静默关闭
			return false;
		}
	}
	
	/**
	 * 拷贝进度
	 * @author zhangzl
	 *
	 */
	public static interface CopyProgress {
		
		/**
		 * 开始
		 */
		public void start();
		
		/**
		 * 进行中
		 * @param size 已经进行的大小
		 */
		public void progress(long size);
		
		/**
		 * 结束
		 */
		public void finish();
	}
}
