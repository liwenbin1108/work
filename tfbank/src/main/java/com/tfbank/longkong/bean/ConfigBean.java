package com.tfbank.longkong.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigBean {
	@Value("${project.monitor.batch}")
	private int monitorBatch;
	@Value("${project.perf.interval}")
	private long perfInterval;
	@Value("${project.event.interval}")
	private long eventInterval;
	@Value("${tarsier.kpi.exclepath}")
	private String exclepath;
	@Value("${tarsier.perf.url}")
	private String tPerfUrl;
	@Value("${tarsier.event.url}")
	private String tEventUrl;
	@Value("${tarsier.db.username}")
	private String tUserName;
	@Value("${tarsier.db.password}")
	private String tPassword;
	@Value("${tarsier.db.url}")
	private String tDbUrl;
	@Value("${tarsier.db.deviceClassName}")
	private String tDeviceClassName;
	@Value("${longkong.db.dbURL}")
	private String lDbUrl;
	@Value("${longkong.db.dbUserName}")
	private String lDbUserName;
	@Value("${longkong.db.dbUserPwd}")
	private String lDbUserPwd;
	@Value("${longkong.db.deviceClassName}")
	private String lDeviceCalssName;
	@Value("${longkong.webservice.url}")
	private String lWebserviceUrl;
	
	
	public ConfigBean() {
		super();
	}
	

	public String gettEventUrl() {
		return tEventUrl;
	}


	public void settEventUrl(String tEventUrl) {
		this.tEventUrl = tEventUrl;
	}


	public long getMonitorBatch() {
		return monitorBatch;
	}


	public void setMonitorBatch(int monitorBatch) {
		this.monitorBatch = monitorBatch;
	}


	public long getPerfInterval() {
		return perfInterval;
	}

	public void setPerfInterval(long perfInterval) {
		this.perfInterval = perfInterval;
	}

	public long getEventInterval() {
		return eventInterval;
	}

	public void setEventInterval(long eventInterval) {
		this.eventInterval = eventInterval;
	}

	public String getExclepath() {
		return exclepath;
	}
	public void setExclepath(String exclepath) {
		this.exclepath = exclepath;
	}
	public String gettPerfUrl() {
		return tPerfUrl;
	}
	public void settPerfUrl(String tPerfUrl) {
		this.tPerfUrl = tPerfUrl;
	}
	public String gettUserName() {
		return tUserName;
	}
	public void settUserName(String tUserName) {
		this.tUserName = tUserName;
	}
	public String gettPassword() {
		return tPassword;
	}
	public void settPassword(String tPassword) {
		this.tPassword = tPassword;
	}
	public String gettDbUrl() {
		return tDbUrl;
	}
	public void settDbUrl(String tDbUrl) {
		this.tDbUrl = tDbUrl;
	}
	public String gettDeviceClassName() {
		return tDeviceClassName;
	}
	public void settDeviceClassName(String tDeviceClassName) {
		this.tDeviceClassName = tDeviceClassName;
	}
	public String getlDbUrl() {
		return lDbUrl;
	}
	public void setlDbUrl(String lDbUrl) {
		this.lDbUrl = lDbUrl;
	}
	public String getlDbUserName() {
		return lDbUserName;
	}
	public void setlDbUserName(String lDbUserName) {
		this.lDbUserName = lDbUserName;
	}
	public String getlDbUserPwd() {
		return lDbUserPwd;
	}
	public void setlDbUserPwd(String lDbUserPwd) {
		this.lDbUserPwd = lDbUserPwd;
	}
	public String getlDeviceCalssName() {
		return lDeviceCalssName;
	}
	public void setlDeviceCalssName(String lDeviceCalssName) {
		this.lDeviceCalssName = lDeviceCalssName;
	}
	public String getlWebserviceUrl() {
		return lWebserviceUrl;
	}
	public void setlWebserviceUrl(String lWebserviceUrl) {
		this.lWebserviceUrl = lWebserviceUrl;
	}


	@Override
	public String toString() {
		return "ConfigBean [monitorBatch=" + monitorBatch + ", perfInterval=" + perfInterval + ", eventInterval="
				+ eventInterval + ", exclepath=" + exclepath + ", tPerfUrl=" + tPerfUrl + ", tEventUrl=" + tEventUrl
				+ ", tUserName=" + tUserName + ", tPassword=" + tPassword + ", tDbUrl=" + tDbUrl + ", tDeviceClassName="
				+ tDeviceClassName + ", lDbUrl=" + lDbUrl + ", lDbUserName=" + lDbUserName + ", lDbUserPwd="
				+ lDbUserPwd + ", lDeviceCalssName=" + lDeviceCalssName + ", lWebserviceUrl=" + lWebserviceUrl + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (eventInterval ^ (eventInterval >>> 32));
		result = prime * result + ((exclepath == null) ? 0 : exclepath.hashCode());
		result = prime * result + ((lDbUrl == null) ? 0 : lDbUrl.hashCode());
		result = prime * result + ((lDbUserName == null) ? 0 : lDbUserName.hashCode());
		result = prime * result + ((lDbUserPwd == null) ? 0 : lDbUserPwd.hashCode());
		result = prime * result + ((lDeviceCalssName == null) ? 0 : lDeviceCalssName.hashCode());
		result = prime * result + ((lWebserviceUrl == null) ? 0 : lWebserviceUrl.hashCode());
		result = prime * result + monitorBatch;
		result = prime * result + (int) (perfInterval ^ (perfInterval >>> 32));
		result = prime * result + ((tDbUrl == null) ? 0 : tDbUrl.hashCode());
		result = prime * result + ((tDeviceClassName == null) ? 0 : tDeviceClassName.hashCode());
		result = prime * result + ((tEventUrl == null) ? 0 : tEventUrl.hashCode());
		result = prime * result + ((tPassword == null) ? 0 : tPassword.hashCode());
		result = prime * result + ((tPerfUrl == null) ? 0 : tPerfUrl.hashCode());
		result = prime * result + ((tUserName == null) ? 0 : tUserName.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigBean other = (ConfigBean) obj;
		if (eventInterval != other.eventInterval)
			return false;
		if (exclepath == null) {
			if (other.exclepath != null)
				return false;
		} else if (!exclepath.equals(other.exclepath))
			return false;
		if (lDbUrl == null) {
			if (other.lDbUrl != null)
				return false;
		} else if (!lDbUrl.equals(other.lDbUrl))
			return false;
		if (lDbUserName == null) {
			if (other.lDbUserName != null)
				return false;
		} else if (!lDbUserName.equals(other.lDbUserName))
			return false;
		if (lDbUserPwd == null) {
			if (other.lDbUserPwd != null)
				return false;
		} else if (!lDbUserPwd.equals(other.lDbUserPwd))
			return false;
		if (lDeviceCalssName == null) {
			if (other.lDeviceCalssName != null)
				return false;
		} else if (!lDeviceCalssName.equals(other.lDeviceCalssName))
			return false;
		if (lWebserviceUrl == null) {
			if (other.lWebserviceUrl != null)
				return false;
		} else if (!lWebserviceUrl.equals(other.lWebserviceUrl))
			return false;
		if (monitorBatch != other.monitorBatch)
			return false;
		if (perfInterval != other.perfInterval)
			return false;
		if (tDbUrl == null) {
			if (other.tDbUrl != null)
				return false;
		} else if (!tDbUrl.equals(other.tDbUrl))
			return false;
		if (tDeviceClassName == null) {
			if (other.tDeviceClassName != null)
				return false;
		} else if (!tDeviceClassName.equals(other.tDeviceClassName))
			return false;
		if (tEventUrl == null) {
			if (other.tEventUrl != null)
				return false;
		} else if (!tEventUrl.equals(other.tEventUrl))
			return false;
		if (tPassword == null) {
			if (other.tPassword != null)
				return false;
		} else if (!tPassword.equals(other.tPassword))
			return false;
		if (tPerfUrl == null) {
			if (other.tPerfUrl != null)
				return false;
		} else if (!tPerfUrl.equals(other.tPerfUrl))
			return false;
		if (tUserName == null) {
			if (other.tUserName != null)
				return false;
		} else if (!tUserName.equals(other.tUserName))
			return false;
		return true;
	}


	
	
	
}
	
	
	


		