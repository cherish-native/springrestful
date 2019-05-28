package com.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * FTP配置
 */
@XmlRootElement
public class FTPConfig {

    @XmlTransient
    public String ftpHostAddr;
    @XmlTransient
    public String ftpUserName;
    @XmlTransient
    public String ftpPassword;
    @XmlTransient
    public int ftpPort;

    @XmlElement(name = "ftpHostAddr")
    public String getFtpHostAddr() {
        return ftpHostAddr;
    }

    public void setFtpHostAddr(String ftpHostAddr) {
        this.ftpHostAddr = ftpHostAddr;
    }

    @XmlElement(name = "ftpUserName")
    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    @XmlElement(name = "ftpPassword")
    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    @XmlElement(name = "ftpPort")
    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }
}
