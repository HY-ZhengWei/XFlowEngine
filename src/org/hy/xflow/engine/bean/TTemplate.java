package org.hy.xflow.engine.bean;

import org.hy.common.Date;
import org.hy.common.xml.SerializableDef;





/**
 * 工作流模板表
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-17
 * @version     v1.0
 */
public class TTemplate extends SerializableDef
{
    private static final long serialVersionUID = -4724247321457107633L;
	
    
	/** 模板ID */
    private String templateID;
    
	/** 模板版本号 */
    private String version;
    
	/** 模板名称 */
    private String templateName;
    
	/** 备注说明 */
    private String infoComment;
    
	/** 创建人员ID */
    private String createrID;
    
	/** 创建人员名称 */
    private String creater;
    
	/** 创建时间 */
    private Date createTime;
    
	/** 最后修改人员ID */
    private String lastUserID;
    
	/** 最后修改人员名称 */
    private String lastUser;
    
	/** 最后修改时间 */
    private Date lastTime;
    
	/** 是否有效标记(0:无效；1:有效) */
    private Integer isValid;
    
	/** 是否删除标记(0:未删除；1:已删除) */
    private Integer isDelete;
    
	
	
	/**
     * 获取：模板ID
     */
    public String getTemplateID()
    {
        return this.templateID;
    }

    
    /**
     * 设置：模板ID
     * 
     * @param i_TemplateID
     */
    public void setTemplateID(String i_TemplateID)
    {
        this.templateID = i_TemplateID;
    }
	
	
	/**
     * 获取：模板版本号
     */
    public String getVersion()
    {
        return this.version;
    }

    
    /**
     * 设置：模板版本号
     * 
     * @param i_Version
     */
    public void setVersion(String i_Version)
    {
        this.version = i_Version;
    }
	
	
	/**
     * 获取：模板名称
     */
    public String getTemplateName()
    {
        return this.templateName;
    }

    
    /**
     * 设置：模板名称
     * 
     * @param i_TemplateName
     */
    public void setTemplateName(String i_TemplateName)
    {
        this.templateName = i_TemplateName;
    }
	
	
	/**
     * 获取：备注说明
     */
    public String getInfoComment()
    {
        return this.infoComment;
    }

    
    /**
     * 设置：备注说明
     * 
     * @param i_InfoComment
     */
    public void setInfoComment(String i_InfoComment)
    {
        this.infoComment = i_InfoComment;
    }
	
	
	/**
     * 获取：创建人员ID
     */
    public String getCreaterID()
    {
        return this.createrID;
    }

    
    /**
     * 设置：创建人员ID
     * 
     * @param i_CreaterID
     */
    public void setCreaterID(String i_CreaterID)
    {
        this.createrID = i_CreaterID;
    }
	
	
	/**
     * 获取：创建人员名称
     */
    public String getCreater()
    {
        return this.creater;
    }

    
    /**
     * 设置：创建人员名称
     * 
     * @param i_Creater
     */
    public void setCreater(String i_Creater)
    {
        this.creater = i_Creater;
    }
	
	
	/**
     * 获取：创建时间
     */
    public Date getCreateTime()
    {
        return this.createTime;
    }

    
    /**
     * 设置：创建时间
     * 
     * @param i_CreateTime
     */
    public void setCreateTime(Date i_CreateTime)
    {
        this.createTime = i_CreateTime;
    }
	
	
	/**
     * 获取：最后修改人员ID
     */
    public String getLastUserID()
    {
        return this.lastUserID;
    }

    
    /**
     * 设置：最后修改人员ID
     * 
     * @param i_LastUserID
     */
    public void setLastUserID(String i_LastUserID)
    {
        this.lastUserID = i_LastUserID;
    }
	
	
	/**
     * 获取：最后修改人员名称
     */
    public String getLastUser()
    {
        return this.lastUser;
    }

    
    /**
     * 设置：最后修改人员名称
     * 
     * @param i_LastUser
     */
    public void setLastUser(String i_LastUser)
    {
        this.lastUser = i_LastUser;
    }
	
	
	/**
     * 获取：最后修改时间
     */
    public Date getLastTime()
    {
        return this.lastTime;
    }

    
    /**
     * 设置：最后修改时间
     * 
     * @param i_LastTime
     */
    public void setLastTime(Date i_LastTime)
    {
        this.lastTime = i_LastTime;
    }
	
	
	/**
     * 获取：是否有效标记(0:无效；1:有效)
     */
    public Integer getIsValid()
    {
        return this.isValid;
    }

    
    /**
     * 设置：是否有效标记(0:无效；1:有效)
     * 
     * @param i_IsValid
     */
    public void setIsValid(Integer i_IsValid)
    {
        this.isValid = i_IsValid;
    }
	
	
	/**
     * 获取：是否删除标记(0:未删除；1:已删除)
     */
    public Integer getIsDelete()
    {
        return this.isDelete;
    }

    
    /**
     * 设置：是否删除标记(0:未删除；1:已删除)
     * 
     * @param i_IsDelete
     */
    public void setIsDelete(Integer i_IsDelete)
    {
        this.isDelete = i_IsDelete;
    }

}
