package com.freeco.model.form;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class AdvertInfoForm {

	@NotBlank(message = "请填写advertId")
	@Pattern(regexp = "\\d+", message = "advertId只能是数字")
	private String advertId;

	@NotBlank(message = "请填写version")
	private String version;

	@NotBlank(message = "请填写timestamp")
	@Pattern(regexp = "\\d+", message = "timestamp只能是数字")
	private String timestamp;//客户端请求时间，精确到毫秒1436803638

	@NotBlank(message = "签名不能为空")
	private String sign;//签名  md5(advertId + timestamp+ version +key)

	public String getAdvertId() {
		return advertId;
	}

	public void setAdvertId(String advertId) {
		this.advertId = advertId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
