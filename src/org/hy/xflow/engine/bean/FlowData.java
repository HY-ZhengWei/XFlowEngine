package org.hy.xflow.engine.bean;

import java.util.List;

import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.bean.UserParticipant;
import org.hy.xflow.engine.common.BaseModel;





/**
 * 工作流接口的数据类型
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-17
 * @version     v1.0
 */
public class FlowData extends BaseModel
{

    private static final long serialVersionUID = -8362950623214901037L;
    
    
    
    /** 用户信息 */
    private User                  user;
    
    /** 工作流模板名称 */
    private String                templateName;
    
    /** 第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息 */
    private String                serviceDataID;
    
    /** 工作流实例ID */
    private String                workID;
    
    /** 路由编码 */
    private String                activityRouteCode;
    
    /** 指定下一活动的动态参与人 */
    private List<UserParticipant> participants;

    
    
    /**
     * 获取：用户信息
     */
    public User getUser()
    {
        return user;
    }
    

    
    /**
     * 获取：工作流模板名称
     */
    public String getTemplateName()
    {
        return templateName;
    }
    

    
    /**
     * 获取：第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     */
    public String getServiceDataID()
    {
        return serviceDataID;
    }
    

    
    /**
     * 获取：工作流实例ID
     */
    public String getWorkID()
    {
        return workID;
    }
    

    
    /**
     * 获取：路由编码
     */
    public String getActivityRouteCode()
    {
        return activityRouteCode;
    }
    

    
    /**
     * 获取：指定下一活动的动态参与人
     */
    public List<UserParticipant> getParticipants()
    {
        return participants;
    }
    

    
    /**
     * 设置：用户信息
     * 
     * @param user 
     */
    public void setUser(User user)
    {
        this.user = user;
    }
    

    
    /**
     * 设置：工作流模板名称
     * 
     * @param templateName 
     */
    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
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
     * 设置：工作流实例ID
     * 
     * @param workID 
     */
    public void setWorkID(String workID)
    {
        this.workID = workID;
    }
    

    
    /**
     * 设置：路由编码
     * 
     * @param activityRouteCode 
     */
    public void setActivityRouteCode(String activityRouteCode)
    {
        this.activityRouteCode = activityRouteCode;
    }
    

    
    /**
     * 设置：指定下一活动的动态参与人
     * 
     * @param participants 
     */
    public void setParticipants(List<UserParticipant> participants)
    {
        this.participants = participants;
    }
    
}
