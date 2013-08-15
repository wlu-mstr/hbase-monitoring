package com.microstrategy.database.hbase.model;

public class LogFileInfo {
	private String server;
	private String url;
	private String check_point;
	private String update_time;
	private String status;

	public LogFileInfo() {

	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCheck_point() {
		return check_point;
	}

	public void setCheck_point(String check_point) {
		this.check_point = check_point;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "LogFileInfo [server=" + server + ", url=" + url
				+ ", check_point=" + check_point + ", update_time="
				+ update_time + ", status=" + status + "]";
	}

}
