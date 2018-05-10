package org.hy.xflow.engine.bean;

import java.util.List;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.PartitionMap;
import org.hy.common.TablePartition;
import org.hy.xflow.engine.common.BaseModel;
import org.hy.xflow.engine.common.IDHelp;





/**
 * 工作流信息表
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-17
 * @version     v1.0
 */
public class FlowInfo extends BaseModel
{
    private static final long serialVersionUID = -4724247321457107633L;
	
    
    /** 流转过程。（内存合成） */
    private List<FlowProcess> processes;
    
	/** 工作流实例ID */
    private String workID;
    
	/** 工作流的父实例ID，可为空 */
    private String workFatherID;
    
	/** 工作流的模板ID */
    private String flowTemplateID;
    
	/** 第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息 */
    private String serviceDataID;
    
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
    
	/** 最后修改人员ID */
    private String lastUserID;
    
	/** 最后修改人员名称 */
    private String lastUser;
    
	/** 最后修改时间 */
    private Date lastTime;
    
	/** 最后修改部门ID */
    private String lastOrgID;
    
	/** 最后修改部门名称 */
    private String lastOrg;
    
	/** 最后修改的过程ID */
    private String lastProcessID;
    
	/** 是否有效标记(0:无效；1:有效) */
    private Integer isValid;
    
	/** 是否删除标记(0:未删除；1:已删除) */
    private Integer isDelete;
    
    
    
    public FlowInfo()
    {
        
    }
    
    
    
    public FlowInfo(User i_User ,Template i_Template ,String i_ServiceDataID)
    {
        this.workID         = IDHelp.makeID();
        this.workFatherID   = "";
        this.flowTemplateID = i_Template.getTemplateID();
        this.serviceDataID  = Help.NVL(i_ServiceDataID);
        this.createrID      = i_User.getUserID();
        this.creater        = i_User.getUserName();
        this.createOrgID    = i_User.getOrgID();
        this.createOrg      = i_User.getOrgName();
        this.createTime     = new Date();
        this.lastUserID     = this.createrID;
        this.lastUser       = this.creater;
        this.lastTime       = this.createTime;
        this.lastOrgID      = this.createOrgID;
        this.lastOrg        = this.createOrg;
        this.lastProcessID  = IDHelp.makeID();
        this.isValid        = 1;
        this.isDelete       = 0;
    }
    
    
    
    /**
     * 获取按当时流转过程的当前活动ID为Map分区结构的流转信息。
     * 
     * 因流转信息是倒排的，所以同一活动ID的多次流转时，小分区中的首个元素即是最新的流转信息。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-10
     * @version     v1.0
     *
     * @return
     */
    public PartitionMap<String ,FlowProcess> getProcessActivityMap()
    {
        PartitionMap<String ,FlowProcess> v_Ret = new TablePartition<String ,FlowProcess>();
        
        if ( !Help.isNull(this.processes) )
        {
            for (FlowProcess v_Process : this.processes)
            {
                v_Ret.putRow(v_Process.getCurrentActivityID() ,v_Process);
            }
        }
        
        return v_Ret;
    }
	
    
	
    /**
     * 获取：流转过程。（内存合成）
     */
    public List<FlowProcess> getProcesses()
    {
        return processes;
    }

    
    /**
     * 设置：流转过程。（内存合成）
     * 
     * @param processes 
     */
    public void setProcesses(List<FlowProcess> processes)
    {
        this.processes = processes;
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
     * 获取：工作流的父实例ID，可为空
     */
    public String getWorkFatherID()
    {
        return this.workFatherID;
    }

    
    /**
     * 设置：工作流的父实例ID，可为空
     * 
     * @param i_WorkFatherID
     */
    public void setWorkFatherID(String i_WorkFatherID)
    {
        this.workFatherID = i_WorkFatherID;
    }
	
	
	/**
     * 获取：工作流的模板ID
     */
    public String getFlowTemplateID()
    {
        return this.flowTemplateID;
    }

    
    /**
     * 设置：工作流的模板ID
     * 
     * @param i_FlowTemplateID
     */
    public void setFlowTemplateID(String i_FlowTemplateID)
    {
        this.flowTemplateID = i_FlowTemplateID;
    }
	
	
	/**
     * 获取：第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     */
    public String getServiceDataID()
    {
        return this.serviceDataID;
    }

    
    /**
     * 设置：第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * 
     * @param i_ServiceDataID
     */
    public void setServiceDataID(String i_ServiceDataID)
    {
        this.serviceDataID = i_ServiceDataID;
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
     * 获取：最后修改部门ID
     */
    public String getLastOrgID()
    {
        return this.lastOrgID;
    }

    
    /**
     * 设置：最后修改部门ID
     * 
     * @param i_LastOrgID
     */
    public void setLastOrgID(String i_LastOrgID)
    {
        this.lastOrgID = i_LastOrgID;
    }
	
	
	/**
     * 获取：最后修改部门名称
     */
    public String getLastOrg()
    {
        return this.lastOrg;
    }

    
    /**
     * 设置：最后修改部门名称
     * 
     * @param i_LastOrg
     */
    public void setLastOrg(String i_LastOrg)
    {
        this.lastOrg = i_LastOrg;
    }
	
	
	/**
     * 获取：最后修改的过程ID
     */
    public String getLastProcessID()
    {
        return this.lastProcessID;
    }

    
    /**
     * 设置：最后修改的过程ID
     * 
     * @param i_LastProcessID
     */
    public void setLastProcessID(String i_LastProcessID)
    {
        this.lastProcessID = i_LastProcessID;
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
