package com.util;

import com.config.FTPConfig;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * FTP工具类
 */
public class FTPUtil {

    private static boolean hasSetConfig = false;
    public static void setConfig(FTPConfig ftpConfig){
        if(!hasSetConfig){
            ftphostaddr = ftpConfig.getFtpHostAddr();
            ftpname = ftpConfig.getFtpUserName();
            ftppwd = ftpConfig.getFtpPassword();
            ftpport = ftpConfig.getFtpPort();
        }
    }

    private static String ftphostaddr = "192.168.1.132";//服务器地址
    private static String ftpname = "zchao";//服务器登录名
    private static String ftppwd = "123456";//登录密码
    private static int ftpport = 21;

    private final static String fileSeparator = System.getProperty("file.separator");

    private static Logger logger = Logger.getLogger(FTPUtil.class);

    /**
     * 从文件服务器上下载文件到本地
     * @param filename
     */
    public static byte[] downFile(String ftppath, String personId,String filename) {
        FTPClient ftp = initFtp();
        byte[] result = null;
        try{
            //4.指定要下载的目录
        	//String a =File.separator+ ftppath+File.separator+personId;
        	String a = ftppath+"/"+personId+"/";

        	ftp.changeWorkingDirectory(a);// 转移到FTP服务器目录
            //5.遍历下载的目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                //解决中文乱码问题，两次解码
                byte[] bytes=ff.getName().getBytes("iso-8859-1");
                String fn=new String(bytes,"utf8");
                if (fn.equals(filename)){
                    InputStream in = ftp.retrieveFileStream(ff.getName());
                    result = input2byte(in);
                    in.close();
                }
            }
            ftp.logout();
        }catch(Exception e){
            e.printStackTrace();
            new Exception("从服务器下载文件过程中发生错误");
        }finally{
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }
    /**
     * 从文件服务器上下载BMP文件到本地(BMP文件头+数据库捺印原图(去gafis头))
     * @param filename
     */
    public static byte[] downFileBMP(String ftppath, String personId,String filename) {
        FTPClient ftp = initFtp();
        byte[] result = null;
        try{
            //4.指定要下载的目录
        	//String a =File.separator+ ftppath+File.separator+personId;
        	String a = ftppath+"/"+personId+"/";

        	ftp.changeWorkingDirectory(a);// 转移到FTP服务器目录
            //5.遍历下载的目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                //解决中文乱码问题，两次解码
                byte[] bytes=ff.getName().getBytes("iso-8859-1");
                String fn=new String(bytes,"utf8");
                if (fn.equals(filename)){
                    InputStream in = ftp.retrieveFileStream(ff.getName());
                    result = input2byteBMP(in);//去除文件前gafis头  拼上BMP图片头
                    in.close();
                }
            }
            ftp.logout();
        }catch(Exception e){
            e.printStackTrace();
            new Exception("从服务器下载文件过程中发生错误");
        }finally{
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return result;
    }

    public static byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, buff.length)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        swapStream.close();
        return in2b;
    }
    
    public static byte[] input2byteBMP(InputStream inStream)
            throws IOException {
		 BMPImageHead bih = new BMPImageHead();
		 byte[] bmpHead = bih.toByteArray();

        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, buff.length)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();        
        swapStream.close();
		byte[] gafisHead = new byte[64];//gafis头长度
		byte[] bmpBody = new byte[in2b.length - gafisHead.length];
		System.arraycopy(in2b, gafisHead.length, bmpBody, 0, bmpBody.length);//去除gafis 只保留像素部分
        //输出bmp图像
		byte[] last = new byte[bmpHead.length + in2b.length];
		System.arraycopy(bmpHead, 0, last , 0, bmpHead.length);
		System.arraycopy(bmpBody, 0, last , bmpHead.length, bmpBody.length);
		
        return last;
    }

    public static FTPClient initFtp() {
        int reply;
        FTPClient ftp = new FTPClient();
        try {
            // 1.连接服务器
            //ftp.connect(ftphostaddr);
            ftp.connect(ftphostaddr, 21);
            // 2.登录服务器 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.login(ftpname, ftppwd);
            // 3.判断登陆是否成功
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Exception("服务器连接失败");
        }
        return ftp;
    }

}
