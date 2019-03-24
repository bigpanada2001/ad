package com.freeco.model;

import com.freeco.model.annotation.Ordinary;
import com.freeco.model.annotation.Primary;
import java.io.Serializable;
import java.util.Date;

public class Advert implements Serializable, Cloneable {

    @Primary
    private Integer id;

    @Ordinary
    private String name;

    @Ordinary
    private String title;

    @Ordinary
    private String website;

    @Ordinary
    private String gameId;
    
    @Ordinary
    private String serverType;

    @Ordinary
    private String serverId;

    @Ordinary
    private Integer templateId;

    @Ordinary
    private Integer flashId;

    @Ordinary
    private String href;

    @Ordinary
    private String extra;

    @Ordinary
    private String code;

    @Ordinary
    private Integer advertPlaceId;
    
    @Ordinary
    private Integer appPackageId;

    @Ordinary
    private String createUser;

    @Ordinary
    private Date createTime;

    @Ordinary
    private String updateUser;

    @Ordinary
    private Date updateTime;
    
    @Ordinary
    private String cdnUrl;
    
    @Ordinary
    private String type;

    //////////

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getFlashId() {
        return flashId;
    }

    public void setFlashId(Integer flashId) {
        this.flashId = flashId;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getAdvertPlaceId() {
        return advertPlaceId;
    }

    public void setAdvertPlaceId(Integer advertPlaceId) {
        this.advertPlaceId = advertPlaceId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public Integer getAppPackageId() {
		return appPackageId;
	}

	public String getCdnUrl() {
		return cdnUrl;
	}

	public void setCdnUrl(String cdnUrl) {
		this.cdnUrl = cdnUrl;
	}

	public void setAppPackageId(Integer appPackageId) {
		this.appPackageId = appPackageId;
	}
	
	@Override 
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
