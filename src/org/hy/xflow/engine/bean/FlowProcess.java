package org.hy.xflow.engine.bean;

import java.util.List;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.xflow.engine.common.BaseModel;
import org.hy.xflow.engine.common.IDHelp;
import org.hy.xflow.engine.enums.ParticipantTypeEnum;





/**
 * 工作流流转过程表
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-17
 * @version     v1.0
 */
public class FlowProcess extends BaseModel
{
    private static final long serialVersionUID = -4724247321457107633L;
    
    
    /** 工作流的动态参与人 */
	private List<ProcessParticipant> participants;
	
	/** 工作流的未来参与人（当有动态参与人时，取动态参与人。其它情况取工作流流程模板中定义的参与人） */
	private List<ProcessParticipant> futureParticipants;
	
	/** 工作流的过程ID */
    private String processID;
    
	/** 工作流实例ID */
    private String workID;
    
    /** 第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息 */
    private String serviceDataID;
    
	/** 分单前的过程ID。合单前持续记录ID值。不一定与previousProcessID同值 */
    private String splitProcessID;
    
	/** 工作流的过程编号。下标从1开始 */
    private Integer processNo;
    
	/** 当前活动ID */
    private String currentActivityID;
    
    /** 当前活动编码 */
    private String currentActivityCode;
    
	/** 当前活动名称 */
    private String currentActivityName;
    
	/** 上一过程ID */
    private String previousProcessID;
    
	/** 上一活动ID */
    private String previousActivityID;
    
    /** 上一活动编码 */
    private String previousActivityCode;
    
	/** 上一活动名称 */
    private String previousActivityName;
    
	/** 下一过程ID */
    private String nextProcessID;
    
	/** 下一活动ID */
    private String nextActivityID;
    
    /** 下一活动编码 */
    private String nextActivityCode;
    
	/** 下一活动名称 */
    private String nextActivityName;
    
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
    
	/** 限制操作人员ID */
    private String limitUserID;
    
	/** 限制操作部门ID */
    private String limitOrgID;
    
	/** 限制操作时间 */
    private Date limitTime;
    
	/** 操作时间 */
    private Date operateTime;
    
	/** 操作时长（单位：秒） */
    private Integer operateTimeLen;
    
	/** 操作类型ID */
    private String operateTypeID;
    
	/** 操作类型名称 */
    private String operateType;
    
	/** 操作人员ID */
    private String operateUserID;
    
	/** 操作人员名称 */
    private String operateUser;
    
	/** 操作部门ID */
    private String operateOrgID;
    
	/** 操作部门名称 */
    private String operateOrg;
    
	/** 操作文件信息，由第三方使用者定义其内容（拓展性数据） */
    private String operateFiles;
    
	/** 操作数据信息，由第三方使用者定义其内容（拓展性数据） */
    private String operateDatas;
    
	/** 备注说明 */
    private String infoComment;
    
	
    
    public FlowProcess init_CreateFlow(User i_User ,FlowInfo i_Flow ,ActivityInfo i_Activity)
    {
        this.processID            = i_Flow.getLastProcessID();
        this.serviceDataID        = i_Flow.getServiceDataID();
        this.workID               = i_Flow.getWorkID();
        this.splitProcessID       = "";
        this.currentActivityID    = i_Activity.getActivityID();
        this.currentActivityCode  = i_Activity.getActivityCode();
        this.currentActivityName  = i_Activity.getActivityName();
        this.processNo            = 1;
        this.previousProcessID    = "";
        this.previousActivityID   = "";
        this.previousActivityCode = "";
        this.previousActivityName = "";
        this.nextProcessID        = "";
        this.nextActivityID       = "";
        this.nextActivityCode     = "";
        this.nextActivityName     = "";
        this.createrID            = i_User.getUserID();
        this.creater              = i_User.getUserName();
        this.createOrgID          = i_User.getOrgID();
        this.createOrg            = i_User.getOrgName();
        this.createTime           = new Date();
        this.limitUserID          = "";
        this.limitOrgID           = "";
        this.limitTime            = new Date("2000-01-01 00:00:00");
        this.operateTime          = this.createTime;
        this.operateTimeLen       = 0;
        this.operateTypeID        = "";
        this.operateType          = "";
        this.operateUserID        = this.createrID;
        this.operateUser          = this.creater;
        this.operateOrgID         = this.createOrgID;
        this.operateOrg           = this.createOrg;
        this.operateFiles         = "";
        this.operateDatas         = "";
        this.infoComment          = "";
        
        return this;
    }
    
    
    
    public FlowProcess init_ToNext(User i_User ,FlowInfo i_Flow ,FlowProcess io_Previous ,ActivityRoute i_Route)
    {
        this.processID               = IDHelp.makeID();
        this.serviceDataID           = i_Flow.getServiceDataID();
        this.workID                  = i_Flow.getWorkID();
        this.splitProcessID          = "";
        this.currentActivityID       = i_Route.getNextActivity().getActivityID();
        this.currentActivityCode     = i_Route.getNextActivity().getActivityCode();
        this.currentActivityName     = i_Route.getNextActivity().getActivityName();
        
        this.processNo               = -1;  //////////////////////////////////////
        this.previousProcessID       = io_Previous.getProcessID();
        this.previousActivityID      = io_Previous.getCurrentActivityID();
        this.previousActivityCode    = io_Previous.getCurrentActivityCode();
        this.previousActivityName    = io_Previous.getCurrentActivityName();
        
        io_Previous.nextProcessID    = this.processID;
        io_Previous.nextActivityID   = this.currentActivityID;
        io_Previous.nextActivityCode = this.currentActivityCode;
        io_Previous.nextActivityName = this.currentActivityName;
        
        this.nextProcessID           = "";
        this.nextActivityID          = "";
        this.nextActivityName        = "";
        this.createrID               = i_User.getUserID();
        this.creater                 = i_User.getUserName();
        this.createOrgID             = i_User.getOrgID();
        this.createOrg               = i_User.getOrgName();
        this.createTime              = new Date();
        this.limitUserID             = "";
        this.limitOrgID              = "";
        this.limitTime               = new Date("2000-01-01 00:00:00");
        this.operateTime             = this.createTime;
        this.operateTimeLen          = 0;
        this.operateTypeID           = "";
        this.operateType             = "";
        this.operateUserID           = this.createrID;
        this.operateUser             = this.creater;
        this.operateOrgID            = this.createOrgID;
        this.operateOrg              = this.createOrg;
        
        io_Previous.operateTime      = this.createTime;
        io_Previous.operateTimeLen   = 0;
        io_Previous.operateTypeID    = i_Route.getRouteType().getRouteTypeID().getValue();
        io_Previous.operateType      = i_Route.getRouteType().getRouteType();
        io_Previous.operateUserID    = this.createrID;
        io_Previous.operateUser      = this.creater;
        io_Previous.operateOrgID     = this.createOrgID;
        io_Previous.operateOrg       = this.createOrg;
        
        this.operateFiles            = "";
        this.operateDatas            = "";
        this.infoComment             = "";
        
        return this;
    }
    
    
    
    /**
     * 判定用户信息是否当前动态的参与人之一
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-09
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    public Participant isParticipant(User i_User)
    {
        if ( i_User == null )
        {
            return null;
        }
        if ( Help.isNull(this.participants) )
        {
            return null;
        }
        
        for (Participant v_Participant : this.participants)
        {
            if ( ParticipantTypeEnum.$User     == v_Participant.getObjectType()
              || ParticipantTypeEnum.$UserSend == v_Participant.getObjectType() )
            {
                if ( v_Participant.getObjectID().equals(i_User.getUserID()) )
                {
                    return v_Participant;
                }
            }
            else if ( ParticipantTypeEnum.$Org     == v_Participant.getObjectType()
                   || ParticipantTypeEnum.$OrgSend == v_Participant.getObjectType() )
            {
                if ( v_Participant.getObjectID().equals(i_User.getOrgID()) )
                {
                    return v_Participant;
                } 
            }
            else if ( ParticipantTypeEnum.$Role     == v_Participant.getObjectType()
                   || ParticipantTypeEnum.$RoleSend == v_Participant.getObjectType() )
            {
                if ( Help.isNull(i_User.getRoles()) )
                {
                    return null;
                }
                
                for (UserRole v_Role : i_User.getRoles())
                {
                    if ( v_Participant.getObjectID().equals(v_Role.getRoleID()) )
                    {
                        return v_Participant;
                    }
                }
            }
        }
        
        return null;
    }
    
    
	
    /**
     * 获取：工作流的动态参与人
     */
    public List<ProcessParticipant> getParticipants()
    {
        return participants;
    }

    
    /**
     * 设置：工作流的动态参与人
     * 
     * @param participants 
     */
    public void setParticipants(List<ProcessParticipant> participants)
    {
        this.participants = participants;
    }

    
    /**
     * 获取：工作流的未来参与人（当有动态参与人时，取动态参与人。其它情况取工作流流程模板中定义的参与人）
     */
    public List<ProcessParticipant> getFutureParticipants()
    {
        return futureParticipants;
    }
    
    
    /**
     * 设置：工作流的未来参与人（当有动态参与人时，取动态参与人。其它情况取工作流流程模板中定义的参与人）
     * 
     * @param futureParticipants 
     */
    public void setFutureParticipants(List<ProcessParticipant> futureParticipants)
    {
        this.futureParticipants = futureParticipants;
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
     * 获取：第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     */
    public String getServiceDataID()
    {
        return serviceDataID;
    }
    
    
    /**
     * 设置：第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * 
     * @param serviceDataID 
     */
    public void setServiceDataID(String serviceDataID)
    {
        this.serviceDataID = serviceDataID;
    }
    

    /**
     * 获取：分单前的过程ID。合单前持续记录ID值。不一定与previousProcessID同值
     */
    public String getSplitProcessID()
    {
        return this.splitProcessID;
    }

    
    /**
     * 设置：分单前的过程ID。合单前持续记录ID值。不一定与previousProcessID同值
     * 
     * @param i_SplitProcessID
     */
    public void setSplitProcessID(String i_SplitProcessID)
    {
        this.splitProcessID = i_SplitProcessID;
    }
	
	
	/**
     * 获取：工作流的过程编号。下标从1开始
     */
    public Integer getProcessNo()
    {
        return this.processNo;
    }

    
    /**
     * 设置：工作流的过程编号。下标从1开始
     * 
     * @param i_ProcessNo
     */
    public void setProcessNo(Integer i_ProcessNo)
    {
        this.processNo = i_ProcessNo;
    }
	
	
	/**
     * 获取：当前活动ID
     */
    public String getCurrentActivityID()
    {
        return this.currentActivityID;
    }

    
    /**
     * 设置：当前活动ID
     * 
     * @param i_CurrentActivityID
     */
    public void setCurrentActivityID(String i_CurrentActivityID)
    {
        this.currentActivityID = i_CurrentActivityID;
    }
	
	
    /**
     * 获取：当前活动编码
     */
    public String getCurrentActivityCode()
    {
        return currentActivityCode;
    }
    
    
    /**
     * 设置：当前活动编码
     * 
     * @param currentActivityCode 
     */
    public void setCurrentActivityCode(String currentActivityCode)
    {
        this.currentActivityCode = currentActivityCode;
    }


    /**
     * 获取：当前活动名称
     */
    public String getCurrentActivityName()
    {
        return this.currentActivityName;
    }

    
    /**
     * 设置：当前活动名称
     * 
     * @param i_CurrentActivityName
     */
    public void setCurrentActivityName(String i_CurrentActivityName)
    {
        this.currentActivityName = i_CurrentActivityName;
    }
	
	
	/**
     * 获取：上一过程ID
     */
    public String getPreviousProcessID()
    {
        return this.previousProcessID;
    }

    
    /**
     * 设置：上一过程ID
     * 
     * @param i_PreviousProcessID
     */
    public void setPreviousProcessID(String i_PreviousProcessID)
    {
        this.previousProcessID = i_PreviousProcessID;
    }
	
	
	/**
     * 获取：上一活动ID
     */
    public String getPreviousActivityID()
    {
        return this.previousActivityID;
    }

    
    /**
     * 设置：上一活动ID
     * 
     * @param i_PreviousActivityID
     */
    public void setPreviousActivityID(String i_PreviousActivityID)
    {
        this.previousActivityID = i_PreviousActivityID;
    }
	
	
    /**
     * 获取：下一活动编码
     */
    public String getNextActivityCode()
    {
        return nextActivityCode;
    }
    
    
    /**
     * 设置：下一活动编码
     * 
     * @param nextActivityCode 
     */
    public void setNextActivityCode(String nextActivityCode)
    {
        this.nextActivityCode = nextActivityCode;
    }


    /**
     * 获取：上一活动名称
     */
    public String getPreviousActivityName()
    {
        return this.previousActivityName;
    }

    
    /**
     * 设置：上一活动名称
     * 
     * @param i_PreviousActivityName
     */
    public void setPreviousActivityName(String i_PreviousActivityName)
    {
        this.previousActivityName = i_PreviousActivityName;
    }
	
	
	/**
     * 获取：下一过程ID
     */
    public String getNextProcessID()
    {
        return this.nextProcessID;
    }

    
    /**
     * 设置：下一过程ID
     * 
     * @param i_NextProcessID
     */
    public void setNextProcessID(String i_NextProcessID)
    {
        this.nextProcessID = i_NextProcessID;
    }
	
	
	/**
     * 获取：下一活动ID
     */
    public String getNextActivityID()
    {
        return this.nextActivityID;
    }

    
    /**
     * 设置：下一活动ID
     * 
     * @param i_NextActivityID
     */
    public void setNextActivityID(String i_NextActivityID)
    {
        this.nextActivityID = i_NextActivityID;
    }
	
	
    /**
     * 获取：上一活动编码
     */
    public String getPreviousActivityCode()
    {
        return previousActivityCode;
    }

    
    /**
     * 设置：上一活动编码
     * 
     * @param previousActivityCode 
     */
    public void setPreviousActivityCode(String previousActivityCode)
    {
        this.previousActivityCode = previousActivityCode;
    }


    /**
     * 获取：下一活动名称
     */
    public String getNextActivityName()
    {
        return this.nextActivityName;
    }

    
    /**
     * 设置：下一活动名称
     * 
     * @param i_NextActivityName
     */
    public void setNextActivityName(String i_NextActivityName)
    {
        this.nextActivityName = i_NextActivityName;
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
     * 获取：限制操作人员ID
     */
    public String getLimitUserID()
    {
        return this.limitUserID;
    }

    
    /**
     * 设置：限制操作人员ID
     * 
     * @param i_LimitUserID
     */
    public void setLimitUserID(String i_LimitUserID)
    {
        this.limitUserID = i_LimitUserID;
    }
	
	
	/**
     * 获取：限制操作部门ID
     */
    public String getLimitOrgID()
    {
        return this.limitOrgID;
    }

    
    /**
     * 设置：限制操作部门ID
     * 
     * @param i_LimitOrgID
     */
    public void setLimitOrgID(String i_LimitOrgID)
    {
        this.limitOrgID = i_LimitOrgID;
    }
	
	
	/**
     * 获取：限制操作时间
     */
    public Date getLimitTime()
    {
        return this.limitTime;
    }

    
    /**
     * 设置：限制操作时间
     * 
     * @param i_LimitTime
     */
    public void setLimitTime(Date i_LimitTime)
    {
        this.limitTime = i_LimitTime;
    }
	
	
	/**
     * 获取：操作时间
     */
    public Date getOperateTime()
    {
        return this.operateTime;
    }

    
    /**
     * 设置：操作时间
     * 
     * @param i_OperateTime
     */
    public void setOperateTime(Date i_OperateTime)
    {
        this.operateTime = i_OperateTime;
    }
	
	
	/**
     * 获取：操作时长（单位：秒）
     */
    public Integer getOperateTimeLen()
    {
        return this.operateTimeLen;
    }

    
    /**
     * 设置：操作时长（单位：秒）
     * 
     * @param i_OperateTimeLen
     */
    public void setOperateTimeLen(Integer i_OperateTimeLen)
    {
        this.operateTimeLen = i_OperateTimeLen;
    }
	
	
	/**
     * 获取：操作类型ID
     */
    public String getOperateTypeID()
    {
        return this.operateTypeID;
    }

    
    /**
     * 设置：操作类型ID
     * 
     * @param i_OperateTypeID
     */
    public void setOperateTypeID(String i_OperateTypeID)
    {
        this.operateTypeID = i_OperateTypeID;
    }
	
	
	/**
     * 获取：操作类型名称
     */
    public String getOperateType()
    {
        return this.operateType;
    }

    
    /**
     * 设置：操作类型名称
     * 
     * @param i_OperateType
     */
    public void setOperateType(String i_OperateType)
    {
        this.operateType = i_OperateType;
    }
	
	
	/**
     * 获取：操作人员ID
     */
    public String getOperateUserID()
    {
        return this.operateUserID;
    }

    
    /**
     * 设置：操作人员ID
     * 
     * @param i_OperateUserID
     */
    public void setOperateUserID(String i_OperateUserID)
    {
        this.operateUserID = i_OperateUserID;
    }
	
	
	/**
     * 获取：操作人员名称
     */
    public String getOperateUser()
    {
        return this.operateUser;
    }

    
    /**
     * 设置：操作人员名称
     * 
     * @param i_OperateUser
     */
    public void setOperateUser(String i_OperateUser)
    {
        this.operateUser = i_OperateUser;
    }
	
	
	/**
     * 获取：操作部门ID
     */
    public String getOperateOrgID()
    {
        return this.operateOrgID;
    }

    
    /**
     * 设置：操作部门ID
     * 
     * @param i_OperateOrgID
     */
    public void setOperateOrgID(String i_OperateOrgID)
    {
        this.operateOrgID = i_OperateOrgID;
    }
	
	
	/**
     * 获取：操作部门名称
     */
    public String getOperateOrg()
    {
        return this.operateOrg;
    }

    
    /**
     * 设置：操作部门名称
     * 
     * @param i_OperateOrg
     */
    public void setOperateOrg(String i_OperateOrg)
    {
        this.operateOrg = i_OperateOrg;
    }
	
	
	/**
     * 获取：操作文件信息，由第三方使用者定义其内容（拓展性数据）
     */
    public String getOperateFiles()
    {
        return this.operateFiles;
    }

    
    /**
     * 设置：操作文件信息，由第三方使用者定义其内容（拓展性数据）
     * 
     * @param i_OperateFiles
     */
    public void setOperateFiles(String i_OperateFiles)
    {
        this.operateFiles = i_OperateFiles;
    }
	
	
	/**
     * 获取：操作数据信息，由第三方使用者定义其内容（拓展性数据）
     */
    public String getOperateDatas()
    {
        return this.operateDatas;
    }

    
    /**
     * 设置：操作数据信息，由第三方使用者定义其内容（拓展性数据）
     * 
     * @param i_OperateDatas
     */
    public void setOperateDatas(String i_OperateDatas)
    {
        this.operateDatas = i_OperateDatas;
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

}
