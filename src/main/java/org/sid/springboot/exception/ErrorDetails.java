package org.sid.springboot.exception;

import java.util.Date;

public class ErrorDetails {
	private Date timestemps;
	private String message;
	private String details;
	
	
	public ErrorDetails(Date timestemps, String message, String details) {
		super();
		this.timestemps = timestemps;
		this.message = message;
		this.details = details;
	}
	public Date getTimestemps() {
		return timestemps;
	}
	public void setTimestemps(Date timestemps) {
		this.timestemps = timestemps;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
	
}
