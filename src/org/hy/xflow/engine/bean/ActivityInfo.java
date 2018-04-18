package org.hy.xflow.engine.bean;

import org.hy.common.Date;
import org.hy.common.xml.SerializableDef;





/**
 * 工作流活动组件(节点)信息表
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-17
 * @version     v1.0
 */
public class ActivityInfo extends SerializableDef
{
    private static final long serialVersionUID = -4724247321457107633L;
	
    
	/** 工作流活动ID */
    private String activityID;
    
	/** 工作流的模板ID */
    private String templateID;
    
	/** 活动名称 */
    private String activityName;
    
	/** 活动类型ID */
    private String activityTypeID;
    
	/** 位置x坐标值 */
    private Double x;
    
	/** 位置y坐标值 */
    private Double y;
    
	/** 位置z坐标值 */
    private Double z;
    
	/** 图标高度 */
    private Double height;
    
	/** 图标宽度 */
    private Double width;
    
	/** 图标路径 */
    private String iconURL;
    
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
    
	
	
	/**
     * 获取：工作流活动ID
     */
    public String getActivityID()
    {
        return this.activityID;
    }

    
    /**
     * 设置：工作流活动ID
     * 
     * @param i_ActivityID
     */
    public void setActivityID(String i_ActivityID)
    {
        this.activityID = i_ActivityID;
    }
	
	
	/**
     * 获取：工作流的模板ID
     */
    public String getTemplateID()
    {
        return this.templateID;
    }

    
    /**
     * 设置：工作流的模板ID
     * 
     * @param i_TemplateID
     */
    public void setTemplateID(String i_TemplateID)
    {
        this.templateID = i_TemplateID;
    }
	
	
	/**
     * 获取：活动名称
     */
    public String getActivityName()
    {
        return this.activityName;
    }

    
    /**
     * 设置：活动名称
     * 
     * @param i_ActivityName
     */
    public void setActivityName(String i_ActivityName)
    {
        this.activityName = i_ActivityName;
    }
	
	
	/**
     * 获取：活动类型ID
     */
    public String getActivityTypeID()
    {
        return this.activityTypeID;
    }

    
    /**
     * 设置：活动类型ID
     * 
     * @param i_ActivityTypeID
     */
    public void setActivityTypeID(String i_ActivityTypeID)
    {
        this.activityTypeID = i_ActivityTypeID;
    }
	
	
	/**
     * 获取：位置x坐标值
     */
    public Double getX()
    {
        return this.x;
    }

    
    /**
     * 设置：位置x坐标值
     * 
     * @param i_X
     */
    public void setX(Double i_X)
    {
        this.x = i_X;
    }
	
	
	/**
     * 获取：位置y坐标值
     */
    public Double getY()
    {
        return this.y;
    }

    
    /**
     * 设置：位置y坐标值
     * 
     * @param i_Y
     */
    public void setY(Double i_Y)
    {
        this.y = i_Y;
    }
	
	
	/**
     * 获取：位置z坐标值
     */
    public Double getZ()
    {
        return this.z;
    }

    
    /**
     * 设置：位置z坐标值
     * 
     * @param i_Z
     */
    public void setZ(Double i_Z)
    {
        this.z = i_Z;
    }
	
	
	/**
     * 获取：图标高度
     */
    public Double getHeight()
    {
        return this.height;
    }

    
    /**
     * 设置：图标高度
     * 
     * @param i_Height
     */
    public void setHeight(Double i_Height)
    {
        this.height = i_Height;
    }
	
	
	/**
     * 获取：图标宽度
     */
    public Double getWidth()
    {
        return this.width;
    }

    
    /**
     * 设置：图标宽度
     * 
     * @param i_Width
     */
    public void setWidth(Double i_Width)
    {
        this.width = i_Width;
    }
	
	
	/**
     * 获取：图标路径
     */
    public String getIconURL()
    {
        return this.iconURL;
    }

    
    /**
     * 设置：图标路径
     * 
     * @param i_IconURL
     */
    public void setIconURL(String i_IconURL)
    {
        this.iconURL = i_IconURL;
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

}
