package com.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.ObjectOutputStream;
import java.sql.Blob;
import java.util.UUID;


public class ByteUtil {
	
	public static void main(String[] args) {
		byte[] data = new byte[]{1,2,3,4,5};
		try {
			data = cutByte(data, 2, data.length-2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(data.length);
	}
	
	/**
	 * 判断blob头是否为7格式
	 * 取第5-8个字节，对应"BLOB"([66, 76, 79, 66])，即为7格式头。即：5位字符为66
	 * @param data
	 * @return
	 */
	public static boolean isHead7(byte[] data){
		byte type = data[4];
		return type==66;
	} 
	
	/**
	 * 截取blob数据头（6格式截取前64位，7格式截取前128位）
	 * 取第5-8个字节，对应"BLOB"([66, 76, 79, 66])，即为7格式头。即：5位字符为66
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	public static byte[] cutBlobHead(byte[] data) throws Exception{
		if(data==null || data.length<65){
			throw new Exception("BLOB数据错误，为空或长度不符。");
		}
		if(isHead7(data)){
			return cutByte(data, 128, data.length-128);
		}else{
			return cutByte(data, 64, data.length-64);
	
		}
	}
	
	/**
	 * 截取byte[]
	 * @param src 待截取byte[]
	 * @param begin 开始截取位置
	 * @param length 截取长度
	 * @return
	 * @throws Exception 
	 */
	public static byte[] cutByte(byte[] src, int begin, int length) throws Exception{
		if(length>src.length){
			throw new Exception("截取字符串超出范围。");
		}
		byte[] res = new byte[length];
		for (int i = begin; i < begin + length; i++) {
			res[i - begin] = src[i];
		}
		return res;
	}
	
	/**
	 * @param b
	 * @param start
	 * @param len
	 * @return
	 */
	public static int byteArray2Int(byte[] b, int start, int len){
		if(b.length < start || len > 4){
			return 0;
		}
		int value = 0;
		int end = start + len;
		int j = 0;
		for (int i = start; i < end; i++) {
			value = value << 8 | (b[i] & 0xff) ;
		}
		return value;
	}
	
	/**
	 * 字节数组转字符串
	 * @param b
	 * @return
	 */
	public static String byteToString(byte[] b) { 
		String key = "";
		char[] c = new char[32];
		for (int i = 0;i<b.length;i++) {
			if (b[i]==0)
				break;
			c[i] = (char) (((c[i] & 0xFF) << 8) | (b[i] & 0xFF)); 
			key += c[i];
		}
        return key; 
    }
	
	public static String afisDateTimeToString(byte[] b) {
		String date = "";
		int ny = (int)((char)(b[7] & 0xFF) * 256 + (char)(b[6] & 0xFF));
		int nM = (int)(char)(b[5] & 0xFF) + 1;
		int nd = (int)(char)(b[4] & 0xFF);
		
		int nh = (int)(char)(b[3] & 0xFF);
		int nm = (int)(char)(b[2] & 0xFF);
		int ns = (int)((char)(b[1]&0xFF) * 256 + (char)(b[0]&0xFF)) / 1000;
		String sns = ns < 10 ? "0"+ns : String.valueOf(ns);
		
		date = ny + "-" + nM + "-" + nd + " " + nh + ":" + nm + ":" + sns;
		return date; 
	}
	
	/**
	 * int转byte[]
	 * @param iSource
	 * @param iArrayLen 数组长度
	 * @return
	 */
	public static byte[] intToByte(int iSource, int iArrayLen) {
	    byte[] bLocalArr = new byte[iArrayLen];
	    for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
	        bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
	    }
	    return bLocalArr;
	}
	
	/**
	 * byte[]转int
	 * @param bRefArr
	 * @return
	 */
	public static int byteToInt(byte[] bRefArr) {
		int iOutcome = 0;
		byte bLoop;

		for (int i = 0; i < bRefArr.length; i++) {
			bLoop = bRefArr[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);
		}
		return iOutcome;
	}
	
	/**
	 * blob转byte[]
	 * @param blob
	 * @return
	 */
	public static byte[] blobToBytes(Blob blob) {
		BufferedInputStream is = null;
		try {
			is = new BufferedInputStream(blob.getBinaryStream());
			byte[] bytes = new byte[(int) blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read = 0;

			while (offset < len
					&& (read = is.read(bytes, offset, len - offset)) >= 0) {
				offset += read;
			}
			return bytes;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				return null;
			}
		}
	}
	
	/**
	 * 拼接两byte[]到一个byte[]并按四位字节对齐
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static byte[] spliceBytes(byte[] data1, byte[] data2) {
		if (data1 == null || data2 == null) {
			System.out.println("拼接byte数组方法传入参数为null");
			return null;
		}
		int len = data1.length + data2.length;
		byte[] res = new byte[len % 4 == 0 ? len : len + 4 - len % 4];
		System.arraycopy(data1, 0, res, 0, data1.length);
		System.arraycopy(data2, 0, res, data1.length, data2.length);
		return res;
	}
	
	
	/**
	 * 拼接两byte[]到一个byte[]不按四位字节对齐
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static byte[] spliceBytesNot4Bit(byte[] data1, byte[] data2) {
		if (data1 == null || data2 == null) {
			System.out.println("拼接byte数组方法传入参数为null");
			return null;
		}
		byte[] res = new byte[data1.length + data2.length];
		System.arraycopy(data1, 0, res, 0, data1.length);
		System.arraycopy(data2, 0, res, data1.length, data2.length);
		return res;
	}	
	  /**  
     * 对象转数组  
     * @param obj  
     * @return  
     */  
    public static byte[] toByteArray (Object obj) {      
        byte[] bytes = null;      
        ByteArrayOutputStream bos = new ByteArrayOutputStream();      
        try {        
            ObjectOutputStream oos = new ObjectOutputStream(bos);         
            oos.writeObject(obj);        
            oos.flush();         
            bytes = bos.toByteArray ();      
            oos.close();         
            bos.close();        
        } catch (IOException ex) {        
            ex.printStackTrace();   
        }      
        return bytes;    
    }  
	
//以下四个方法与Ｃ组算法一样	
	public static byte[] intToByte4(int number) {  
	    byte[] abyte = new byte[4];  
	    // "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。  
	    // ">>"右移位，若为正数则高位补0，若为负数则高位补1  
/*	    abyte[0] = (byte) (number >> 24); 
	    abyte[1] = (byte) (number >> 16);  
	    abyte[2] = (byte) (0xFF & (number >> 8));  
	    abyte[3] = (byte) (0xFF & number); 
	    */
	    //以下是Ｃ组算法处理
	    abyte[0] = (byte) (number/(256*256*256));
	    abyte[1] = (byte) (number/(256*256));
	    abyte[2] = (byte) ((number/256)& (0xFF));
	    abyte[3] = (byte) (number & (0xFF));
	    return abyte;  
	}
	
	
	
	public static byte[] intToByte2(int number) {  
	    byte[] abyte = new byte[2];  
	    abyte[0] = (byte) ((number/256));
	    abyte[1] = (byte) (number & (0xFF));
	    return abyte;  
	}
	
    public static byte[] changeByte(int data){  
        byte b4 = (byte)((data)>>24);  
        byte b3 = (byte)(((data)<<8)>>24);  
        byte b2= (byte)(((data)<<16)>>24);  
        byte b1 = (byte)(((data)<<24)>>24);  
        byte[] bytes = {b1,b2,b3,b4};  
        return bytes;  
    }  
    
	public static byte[] charToByte4(char charter) {  
	    byte[] abyte = new byte[4];  
	    abyte[0] = (byte) (charter & (0xFF));
	    return abyte;  
	}
	
	public static int byteToInt4(byte[] arrByte){

		return (int) (((arrByte[0]*256 + arrByte[1])*256 + arrByte[2])*256 + arrByte[3]) ;
	}
	
	public static int byteToInt2(byte[] arrByte){

		return (int) (arrByte[0]*256 + arrByte[1]) ;
	}
	
	/**
	 * 获取uuid，一般用于RAW(16)
	 * @return byte[16]
	 */
	public static byte[] getUuid(){
		return hexString2Bytes(UUID.randomUUID().toString().replaceAll("-", ""));
	}
	/**
	 * 将16进制字符串转为二进制byte数组
	 * @param hexString
	 * @return
	 */
	public static byte[] hexString2Bytes(String hexString) {  
	    if (hexString == null || hexString.equals("")) {  
	        return null;  
	    }
	    hexString = hexString.toUpperCase();  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (char2Byte(hexChars[pos]) << 4 | char2Byte(hexChars[pos + 1]));  
	    }  
	    return d;  
	}
	private static byte char2Byte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	}
	
}
