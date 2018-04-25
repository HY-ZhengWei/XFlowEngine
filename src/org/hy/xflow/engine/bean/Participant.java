package org.hy.xflow.engine.bean;

import org.hy.common.Date;
import org.hy.xflow.engine.common.BaseModel;





/**
 * 1. 工作流活动组件(节点)的参与人
 * 2. 工作流活动组件(节点)路由的参与人
 *
 * @author      ZhengWei
 * @createDate  2018-04-24
 * @version     v1.0
 */
public class Participant extends BaseModel
{
    private static final long serialVersionUID = -4724247321457107633L;
	
    
	/** 主键ID */
    private String participantID;
    
	/** 工作流的模板ID */
    private String templateID;
    
    /** 活动ID */
    private String activityID;
    
	/** 活动路由ID */
    private String activityRouteID;
    
    /** 参与者类型（内存合成） */
    private ParticipantType participantType;
    
	/** 参与者类型（1:执行人；2:执行部门；3:执行角色；11:抄送人；12:抄送部门；13:抄送角色） */
    private Integer objectType;
    
	/** 参与者ID */
    private String objectID;
    
	/** 参与者名称 */
    private String objectName;
    
	/** 参与者序号，表示前后顺序。系统自动生成，下标从1开始 */
    private Integer objectNo;
    
	/** 创建人员ID */
    private String createrID;
    
	/** 创建人员名称 */
    private String creater;
    
	/** 创建时间 */
    private Date createTime;
    
	
    
    /**
     * 获取：主键ID
     */
    public String getParticipantID()
    {
        return participantID;
    }
    
    
    /**
     * 设置：主键ID
     * 
     * @param participantID 
     */
    public void setParticipantID(String participantID)
    {
        this.participantID = participantID;
    }
    
    
    /**
     * 获取：活动ID
     */
    public String getActivityID()
    {
        return activityID;
    }

    
    /**
     * 设置：活动ID
     * 
     * @param activityID 
     */
    public void setActivityID(String activityID)
    {
        this.activityID = activityID;
    }
    

    /**
     * 获取：活动路由ID
     */
    public String getActivityRouteID()
    {
        return activityRouteID;
    }

    
    /**
     * 设置：活动路由ID
     * 
     * @param activityRouteID 
     */
    public void setActivityRouteID(String activityRouteID)
    {
        this.activityRouteID = activityRouteID;
    }


    /**
     * 获取：工作流的模板ID
     */
    public String getTemplateID()
    {
        return templateID;
    }

    
    /**
     * 设置：工作流的模板ID
     * 
     * @param templateID 
     */
    public void setTemplateID(String templateID)
    {
        this.templateID = templateID;
    }
    
    
    /**
     * 获取：参与者类型（内存合成）
     */
    public ParticipantType getParticipantType()
    {
        return participantType;
    }

    
    /**
     * 设置：参与者类型（内存合成）
     * 
     * @param participantType 
     */
    public void setParticipantType(ParticipantType participantType)
    {
        this.participantType = participantType;
    }


    /**
     * 获取：参与者类型（1:执行人；2:执行部门；3:执行角色；11:抄送人；12:抄送部门；13:抄送角色）
     */
    public Integer getObjectType()
    {
        return this.objectType;
    }

    
    /**
     * 设置：参与者类型（1:执行人；2:执行部门；3:执行角色；11:抄送人；12:抄送部门；13:抄送角色）
     * 
     * @param i_ObjectType
     */
    public void setObjectType(Integer i_ObjectType)
    {
        this.objectType = i_ObjectType;
    }
	
	
	/**
     * 获取：参与者ID
     */
    public String getObjectID()
    {
        return this.objectID;
    }

    
    /**
     * 设置：参与者ID
     * 
     * @param i_ObjectID
     */
    public void setObjectID(String i_ObjectID)
    {
        this.objectID = i_ObjectID;
    }
	
	
	/**
     * 获取：参与者名称
     */
    public String getObjectName()
    {
        return this.objectName;
    }

    
    /**
     * 设置：参与者名称
     * 
     * @param i_ObjectName
     */
    public void setObjectName(String i_ObjectName)
    {
        this.objectName = i_ObjectName;
    }
	
	
	/**
     * 获取：参与者序号，表示前后顺序。系统自动生成，下标从1开始
     */
    public Integer getObjectNo()
    {
        return this.objectNo;
    }

    
    /**
     * 设置：参与者序号，表示前后顺序。系统自动生成，下标从1开始
     * 
     * @param i_ObjectNo
     */
    public void setObjectNo(Integer i_ObjectNo)
    {
        this.objectNo = i_ObjectNo;
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

}
