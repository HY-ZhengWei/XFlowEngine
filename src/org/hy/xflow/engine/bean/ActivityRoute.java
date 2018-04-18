package org.hy.xflow.engine.bean;

import org.hy.common.Date;
import org.hy.common.xml.SerializableDef;





/**
 * 工作流活动组件(节点)的路由表
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-17
 * @version     v1.0
 */
public class ActivityRoute extends SerializableDef
{
    private static final long serialVersionUID = -4724247321457107633L;
	
    
	/** 主键ID */
    private String arID;
    
	/** 工作流活动ID */
    private String activityID;
    
	/** 工作流的模板ID */
    private String templateID;
    
	/** 下一活动ID。可以为不同工作流模板的活动。即支持子流程 */
    private String nextActivityID;
    
	/** 有条件判定时，执行的XJava对象ID */
    private String conditionXJavaID;
    
	/** 有条件判定时，执行的对象方法名称 */
    private String conditionMethod;
    
	/** 有条件判定时，方法执行结果的对比值 */
    private String conditionValue;
    
	/** 有条件判定时，对比值的数据类型（1:数字；2:布尔值；3:文本） */
    private Integer conditionVType;
    
	/** 标题 */
    private String title;
    
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
     * 获取：主键ID
     */
    public String getArID()
    {
        return this.arID;
    }

    
    /**
     * 设置：主键ID
     * 
     * @param i_ArID
     */
    public void setArID(String i_ArID)
    {
        this.arID = i_ArID;
    }
	
	
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
     * 获取：下一活动ID。可以为不同工作流模板的活动。即支持子流程
     */
    public String getNextActivityID()
    {
        return this.nextActivityID;
    }

    
    /**
     * 设置：下一活动ID。可以为不同工作流模板的活动。即支持子流程
     * 
     * @param i_NextActivityID
     */
    public void setNextActivityID(String i_NextActivityID)
    {
        this.nextActivityID = i_NextActivityID;
    }
	
	
	/**
     * 获取：有条件判定时，执行的XJava对象ID
     */
    public String getConditionXJavaID()
    {
        return this.conditionXJavaID;
    }

    
    /**
     * 设置：有条件判定时，执行的XJava对象ID
     * 
     * @param i_ConditionXJavaID
     */
    public void setConditionXJavaID(String i_ConditionXJavaID)
    {
        this.conditionXJavaID = i_ConditionXJavaID;
    }
	
	
	/**
     * 获取：有条件判定时，执行的对象方法名称
     */
    public String getConditionMethod()
    {
        return this.conditionMethod;
    }

    
    /**
     * 设置：有条件判定时，执行的对象方法名称
     * 
     * @param i_ConditionMethod
     */
    public void setConditionMethod(String i_ConditionMethod)
    {
        this.conditionMethod = i_ConditionMethod;
    }
	
	
	/**
     * 获取：有条件判定时，方法执行结果的对比值
     */
    public String getConditionValue()
    {
        return this.conditionValue;
    }

    
    /**
     * 设置：有条件判定时，方法执行结果的对比值
     * 
     * @param i_ConditionValue
     */
    public void setConditionValue(String i_ConditionValue)
    {
        this.conditionValue = i_ConditionValue;
    }
	
	
	/**
     * 获取：有条件判定时，对比值的数据类型（1:数字；2:布尔值；3:文本）
     */
    public Integer getConditionVType()
    {
        return this.conditionVType;
    }

    
    /**
     * 设置：有条件判定时，对比值的数据类型（1:数字；2:布尔值；3:文本）
     * 
     * @param i_ConditionVType
     */
    public void setConditionVType(Integer i_ConditionVType)
    {
        this.conditionVType = i_ConditionVType;
    }
	
	
	/**
     * 获取：标题
     */
    public String getTitle()
    {
        return this.title;
    }

    
    /**
     * 设置：标题
     * 
     * @param i_Title
     */
    public void setTitle(String i_Title)
    {
        this.title = i_Title;
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