package com.freeco.model.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.freeco.model.enums.AdvertType;

public class AdvertVO implements Serializable {
	
    private Integer id;

    private String name;

    private String title;
    
    private String website;

    private String gameName;

    private String serverName;

    private String templateName;

    private String flashName;

    private String href;
    
    private String exitUrl;

    private String channelInfoName;

    private String advertPlaceName;
    
    private String advertGroupCode;
    
    private int userSourceNatural;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;
    
    private String cdnUrl;
    
    private String type;

    /////
    /////

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
    
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getFlashName() {
        return flashName;
    }

    public void setFlashName(String flashName) {
        this.flashName = flashName;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getChannelInfoName() {
        return channelInfoName;
    }

    public void setChannelInfoName(String channelInfoName) {
        this.channelInfoName = channelInfoName;
    }

    public String getAdvertPlaceName() {
        return advertPlaceName;
    }

    public void setAdvertPlaceName(String advertPlaceName) {
        this.advertPlaceName = advertPlaceName;
    }

    public String getAdvertGroupCode() {
        return advertGroupCode;
    }

    public void setAdvertGroupCode(String advertGroupCode) {
        this.advertGroupCode = advertGroupCode;
    }
    
    public String getCreateUser() {
        return createUser;
    }

    public void setUserSourceNatural(int userSourceNatural) {
        this.userSourceNatural = userSourceNatural;
    }

    public int getUserSourceNatural() {
        return userSourceNatural;
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
    
    public String getExitUrl() {
		return exitUrl;
	}

	public void setExitUrl(String exitUrl) {
		this.exitUrl = exitUrl;
	}
	
	public boolean getIsExit(){
		return StringUtils.isNotBlank(exitUrl);
	}

	public String getCdnUrl() {
		return cdnUrl;
	}

	public void setCdnUrl(String cdnUrl) {
		this.cdnUrl = cdnUrl;
	}
	
	public AdvertType getAdvertType() {
		if(StringUtils.isNotBlank(type)){
			return AdvertType.valueOf(type);
		}
		return AdvertType.UNKOWN;
	}
	
	public String getType(){
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	//////////
	

	public String getHostHrefString(List<String> hostList) {
       StringBuilder builder = new StringBuilder();
       String copyHtml = "<a href='javascript:void(0);' onclick='doCopy($(this).prev()[0]);'>[复制]</a>";
       if(!(this.userSourceNatural==1 || (this.userSourceNatural==0 && "DUOWAN".equalsIgnoreCase(this.getTemplateName()) && "DUOWAN".equalsIgnoreCase(this.getFlashName()))))
        {
        	String adHtml = "http://download.game.yy.com/adcenter3/ad/" + this.id + ".html";
        	String confJs = "http://download.game.yy.com/adcenter3/conf/" + this.id + ".js";
        	String app = cdnUrl + "";
	        adHtml = String.format("<a target='_blank' href='%s'>%s</a> " + copyHtml, adHtml, adHtml);
	        confJs = String.format("<a target='_blank' href='%s'>%s</a> " + copyHtml, confJs, confJs);
	        app = String.format("<a target='_blank' href='%s'>%s</a> " + copyHtml, app, app);
	        String advertRedirect = "http://api.adcenter3.game.yy.com/advert/redirect/" + this.id + ".apk";
	        advertRedirect = String.format("<a target='_blank' href='%s'>%s</a> " + copyHtml, advertRedirect, advertRedirect);
	        builder.append(adHtml).append("</br>");
	        builder.append(confJs).append("</br>");
	        builder.append(app).append("</br>");
	        builder.append(advertRedirect).append("</br>");
	        for (String host : hostList) {
	            String url = "http://" + host + "/" + this.id + ".html";
	            String href = String.format("<a target='_blank' href='%s'>%s</a> " + copyHtml, url, url);
	            builder.append(href).append("</br>");
	        }
        }else
        {
        	String site=this.website.toLowerCase().startsWith("http://") ? this.website : "http://"+this.website;
        	if(this.userSourceNatural==0)
        		site = site +"?dsfrom=" +this.id;
        	String adHtml = "http://download.game.yy.com/adcenter3/ad/" + this.id + ".html";
        	String confJs = "http://download.game.yy.com/adcenter3/conf/" + this.id + ".js";
        	adHtml = String.format("<a target='_blank' href='%s'>%s</a> " + copyHtml, adHtml, adHtml);
	        confJs = String.format("<a target='_blank' href='%s'>%s</a> " + copyHtml, confJs, confJs);
        	String app = String.format("<a target='_blank' href='%s'>%s</a> " + copyHtml, cdnUrl, cdnUrl);
        	String advertRedirect = "http://api.adcenter3.game.yy.com/advert/redirect/" + this.id + ".apk";
	        advertRedirect = String.format("<a target='_blank' href='%s'>%s</a> " + copyHtml, advertRedirect, advertRedirect);
        	builder.append(site).append("</br>");
        	builder.append(adHtml).append("</br>");
	        builder.append(confJs).append("</br>");
	        builder.append(app).append("</br>");
	        builder.append(advertRedirect).append("</br>");
        }
        return builder.toString();
    }

    /**
     * For #CR201504_3.
     * If host in the hostList, output a string as <a target='_blank' href='%s'>%s</a>
     *
     * @param hostList
     * @param host
     * @return
     */
    public String getBdtyHrefString(List<String> hostList) {
    	String href = "";
    	if(!(this.userSourceNatural==1 || (this.userSourceNatural==0 && "DUOWAN".equalsIgnoreCase(this.getTemplateName()) && "DUOWAN".equalsIgnoreCase(this.getFlashName()))))
    	{
	    	String host = "bdty.yy.com";
	        if(hostList.contains(host)){
	            String url = "http://" + host + "/" + this.id + ".html";
	            href = String.format("<a target='_blank' href='%s'>%s</a> <span style='position: relative; display: inline-block; '><a href='#' class='copy-to-clipboard' data-content='%s'>[复制]</a></span>", url, url, url);
	        }
        }else
        {
        	String url=this.website.toLowerCase().startsWith("http://") ? this.website : "http://"+this.website;
        	if(this.userSourceNatural==0)
        		url = url +"?dsfrom=" +this.id;
	        href = String.format("<a target='_blank' href='%s'>%s</a> <span style='position: relative; display: inline-block; '><a href='#' class='copy-to-clipboard' data-content='%s'>[复制]</a></span>", url, url, url);
        }
        return href;
    }
}
