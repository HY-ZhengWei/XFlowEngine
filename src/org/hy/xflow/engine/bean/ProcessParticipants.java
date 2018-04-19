package org.hy.xflow.engine.bean;

import org.hy.common.Date;
import org.hy.xflow.engine.common.BaseModel;





/**
 * 工作流过程的实际参与人
 *
 * @author      ZhengWei
 * @createDate  2018-04-17
 * @version     v1.0
 */
public class ProcessParticipants extends BaseModel
{
    private static final long serialVersionUID = -4724247321457107633L;
	
    
	/** 主键ID */
    private String pwpID;
    
	/** 工作流的过程ID */
    private String processID;
    
	/** 工作流实例ID */
    private String workID;
    
	/** 参与者类型（1:任务执行人；2:任务执行部门；3:任务执行角色；11:抄送人；12:抄送部门；13:抄送角色） */
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
    
	/** 创建部门ID */
    private String createOrgID;
    
	/** 创建部门名称 */
    private String createOrg;
    
	/** 创建时间 */
    private Date createTime;
    
	
	
	/**
     * 获取：主键ID
     */
    public String getPwpID()
    {
        return this.pwpID;
    }

    
    /**
     * 设置：主键ID
     * 
     * @param i_PwpID
     */
    public void setPwpID(String i_PwpID)
    {
        this.pwpID = i_PwpID;
    }
	
	
	/**
     * 获取：工作流的过程ID
     */
    public String getProcessID()
    {
        return this.processID;
    }

    
    /**
     * 设置：工作流的过程ID
     * 
     * @param i_ProcessID
     */
    public void setProcessID(String i_ProcessID)
    {
        this.processID = i_ProcessID;
    }
	
	
	/**
     * 获取：工作流实例ID
     */
    public String getWorkID()
    {
        return this.workID;
    }

    
    /**
     * 设置：工作流实例ID
     * 
     * @param i_WorkID
     */
    public void setWorkID(String i_WorkID)
    {
        this.workID = i_WorkID;
    }
	
	
	/**
     * 获取：参与者类型（1:任务执行人；2:任务执行部门；3:任务执行角色；11:抄送人；12:抄送部门；13:抄送角色）
     */
    public Integer getObjectType()
    {
        return this.objectType;
    }

    
    /**
     * 设置：参与者类型（1:任务执行人；2:任务执行部门；3:任务执行角色；11:抄送人；12:抄送部门；13:抄送角色）
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
     * 获取：创建部门ID
     */
    public String getCreateOrgID()
    {
        return this.createOrgID;
    }

    
    /**
     * 设置：创建部门ID
     * 
     * @param i_CreateOrgID
     */
    public void setCreateOrgID(String i_CreateOrgID)
    {
        this.createOrgID = i_CreateOrgID;
    }
	
	
	/**
     * 获取：创建部门名称
     */
    public String getCreateOrg()
    {
        return this.createOrg;
    }

    
    /**
     * 设置：创建部门名称
     * 
     * @param i_CreateOrg
     */
    public void setCreateOrg(String i_CreateOrg)
    {
        this.createOrg = i_CreateOrg;
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
