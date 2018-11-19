package com.tfbank.longkong.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>
 * Description: FileUtil 文件处理类<br>
 * Method Explanation：1.
 * 
 * 
 * </p>
 * 
 * @author lijunfeng
 * @version 1.0
 * 
 *          <p>
 *          History: <br>
 * 
 *          Date Author Version Description <br>
 *          ----------------------------------------------------------------
 *          <br>
 *          2014-10-11 AM 10:28:00 lijunfeng 1.0
 *          </p>
 * 
 * @since
 * @see
 */
public class ToolUtil {
	private static Log log = LogFactory.getLog(ToolUtil.class);

	public static JSONObject getJSONFile(String filename) {
		FileInputStream fis = null;
		try {
			byte[] buff = new byte[1024 * 1024];
			List<Byte> list = new ArrayList<Byte>();
			fis = new FileInputStream(filename);
			while (true) {
				int index = fis.read(buff);
				if (index >= 0) {
					for (int i = 0; i < index; i++) {
						list.add(Byte.valueOf(buff[i]));
					}
					buff = new byte[1024 * 1024];
				} else {
					break;
				}
			}
			byte[] buff_final = new byte[list.size()];
			for (int i = 0; i < list.size(); i++) {
				buff_final[i] = list.get(i).byteValue();
			}
			JSONObject jsObj = JSONObject.parseObject(new String(buff_final, "utf-8").trim());
			return jsObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fis = null;
		}
	}

	public static String readFileContent(String filename, String charset) {

		if (charset == null || charset.length() < 1) {
			charset = "utf-8";
		}

		// FileInputStream fis = null;
		InputStream fis = null;
		try {

			fis = ClassLoader.getSystemResourceAsStream(filename);

			byte[] buff = new byte[1024 * 1024];
			List<Byte> list = new ArrayList<Byte>();
			// fis = new FileInputStream(in);
			while (true) {
				int index = fis.read(buff);
				if (index >= 0) {
					for (int i = 0; i < index; i++) {
						list.add(Byte.valueOf(buff[i]));
					}
					buff = new byte[1024 * 1024];
				} else {
					break;
				}
			}
			byte[] buff_final = new byte[list.size()];
			for (int i = 0; i < list.size(); i++) {
				buff_final[i] = list.get(i).byteValue();
			}
			return new String(buff_final, charset).trim();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fis = null;
		}
	}

	/**
	 * 根据txt文件的绝对路径，读取文件内容
	 * 
	 * @param filePath
	 * @return 文件内容的List对象
	 */
	public static List<String> readTxtFile(String filePath, String charset) {

		if (charset == null || charset.length() < 1) {
			charset = "utf-8";
		}
		List<String> lineList = new ArrayList<String>();
		try {
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), charset);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (lineTxt.length() > 0) {
						lineList.add(lineTxt.substring(0, lineTxt.lastIndexOf(";")));
					}
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return lineList;
	}

	/**
	 * 读取JSONObject格式的数据
	 * 
	 * @param filename
	 *            文件全路径
	 * @param charset
	 *            字符集编码，默认为utf-8
	 * @return
	 */
	public static JSONObject getJSONFile(String filename, String charset) {

		if (charset == null || charset.length() < 1) {
			charset = "utf-8";
		}

		 FileInputStream fis = null;
//		InputStream fis = null;
		try {

//			fis = ClassLoader.getSystemResourceAsStream(filename);

			byte[] buff = new byte[1024 * 1024];
			List<Byte> list = new ArrayList<Byte>();
			 fis = new FileInputStream(filename);
			while (true) {
				int index = fis.read(buff);
				if (index >= 0) {
					for (int i = 0; i < index; i++) {
						list.add(Byte.valueOf(buff[i]));
					}
					buff = new byte[1024 * 1024];
				} else {
					break;
				}
			}
			byte[] buff_final = new byte[list.size()];
			for (int i = 0; i < list.size(); i++) {
				buff_final[i] = list.get(i).byteValue();
			}
			JSONObject jsObj = JSONObject.parseObject(new String(buff_final, charset).trim());
			return jsObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fis = null;
		}
	}

	/**
	 * 读取JSONArray格式的数据
	 * 
	 * @param filename
	 * @param charset
	 * @return
	 */
	public static JSONArray getJSONArrayFile(String filename, String charset) {

		if (charset == null || charset.length() < 1) {
			charset = "utf-8";
		}

		// FileInputStream fis = null;
		InputStream fis = null;
		try {
			fis = ClassLoader.getSystemResourceAsStream(filename);
			byte[] buff = new byte[1024 * 1024];
			List<Byte> list = new ArrayList<Byte>();
			// fis = new FileInputStream(filename);
			while (true) {
				int index = fis.read(buff);
				if (index >= 0) {
					for (int i = 0; i < index; i++) {
						list.add(Byte.valueOf(buff[i]));
					}
					buff = new byte[1024 * 1024];
				} else {
					break;
				}
			}
			byte[] buff_final = new byte[list.size()];
			for (int i = 0; i < list.size(); i++) {
				buff_final[i] = list.get(i).byteValue();
			}
			JSONArray jsObj = JSONArray.parseArray(new String(buff_final, charset).trim());
			return jsObj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fis = null;
		}
	}

	/**
	 * 将数据写入制定的文件中
	 * 
	 * @param filename
	 * @param content
	 */
	public static void setJSONFile(String filename, String content) {
		FileOutputStream fos = null;
		try {
			String path = ClassLoader.getSystemResource(filename).getPath();
			path = path.substring(1);
			
			fos = new FileOutputStream(new File(path));
			fos.write(content.getBytes("utf-8"));
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@SuppressWarnings({ "resource", "unused" })
	public static String readFile(String filename, String content, String mode, String charset) {
		int BUFFER_SIZE = 0x300000;
		FileChannel in = null;
		MappedByteBuffer inputBuffer = null;
		File file = new File(filename);
		try {
			in = new RandomAccessFile(file, mode).getChannel();
			inputBuffer = in.map(FileChannel.MapMode.READ_ONLY, file.length() / 2, file.length() / 2);
			byte[] dst = new byte[BUFFER_SIZE];// 每次读出3M的内容
			for (int offset = 0; offset < inputBuffer.capacity(); offset += BUFFER_SIZE) {
				if (inputBuffer.capacity() - offset >= BUFFER_SIZE) {
					for (int i = 0; i < BUFFER_SIZE; i++)
						dst[i] = inputBuffer.get(offset + i);
				} else {
					for (int i = 0; i < inputBuffer.capacity() - offset; i++)
						dst[i] = inputBuffer.get(offset + i);
				}
				int length = (inputBuffer.capacity() % BUFFER_SIZE == 0) ? BUFFER_SIZE
						: inputBuffer.capacity() % BUFFER_SIZE;
				return new String(dst, 0, length,charset);
			}
		} catch (Exception e) {
			log.error("write file error", e);
		}
		return null;
	}
	
	@SuppressWarnings("resource")
	public static String readFileSmall(String filename, String content, String mode,String charset) {
		String ret = null;
		FileChannel channel = null;
		try {
			int bufSize = 1024;
			byte[] bs = new byte[bufSize];
			ByteBuffer byteBuf = ByteBuffer.allocate(bufSize);
			channel = new RandomAccessFile(filename, mode).getChannel();
			StringBuffer sb = new StringBuffer();
			while (channel.read(byteBuf) != -1) {
				int size = byteBuf.position();
				byteBuf.rewind();
				byteBuf.get(bs);
				ret = new String(bs, 0, size, charset);
				sb.append(ret);
				byteBuf.clear();
			}
			ret = sb.toString();
			channel.close();
		} catch (Exception e) {
			log.error("write file error", e);
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					log.error("write file error", e);
				}
			}
			channel = null;
		}
		return ret;
	}

	/**
	 * 将数据写入制定的文件中
	 * 
	 * @param filename
	 * @param content
	 */
	@SuppressWarnings("resource")
	public static void writeFile(String filename, String content, String mode, String charset) {
		FileChannel out = null;
		try {
			out = new RandomAccessFile(filename, mode).getChannel();
			out.write(ByteBuffer.wrap(content.getBytes(charset), 0, content.getBytes(charset).length));
		} catch (Exception e) {
			log.error("write file error", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("write file error", e);
				}
			}
			out = null;
		}
	}

	/**
	 * 数字的四舍五入，并保留小数位
	 * 
	 * @param numberStr
	 * @param decimalDigit
	 * @return 返回四舍五入之后的数字
	 */
	public static Object roudNumber(Object numberStr, int decimalDigit) {
		Object retVal = new Object();
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(decimalDigit);
		retVal = nf.format(Double.parseDouble(String.valueOf(numberStr)));
		return retVal;
	}

	/**
	 * 正则判断字符串是否是整数，小数，包括负数
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("^\\d+$|-\\d+$"); // 就是判断是否为整数
		Pattern pattern2 = Pattern.compile("\\d+\\.\\d+$|-\\d+\\.\\d+$");// 判断是否为小数
		return pattern.matcher(str).matches() || pattern2.matcher(str).matches();
	}
	
	public static String readFile(String path) {
		BufferedReader br = null;
		try {
			File file = new File(path);
			if (!file.exists() || file.isDirectory())
				throw new FileNotFoundException();
			br = new BufferedReader(new FileReader(file));
			String temp = null;
			StringBuffer sb = new StringBuffer();
			temp = br.readLine();
			while (temp != null) {
				sb.append(temp + " ");
				temp = br.readLine();
			}
			return sb.toString();
		} catch (Exception e) {
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/***
	 * 测试主函数
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		String a = "";
//		try {
//			a = readFile("/Users/lijunfeng/Documents/接口文档/地面站/data/DTC.dat");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(a);
		String a = "aaaa.bbbb";
		String[] b = a.split("\\.");
		System.out.println(b[1]);
	}
}
