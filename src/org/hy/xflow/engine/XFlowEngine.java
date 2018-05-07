package org.hy.xflow.engine;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Help;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.ActivityInfo;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.Participant;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.Template;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.service.IFlowInfoService;
import org.hy.xflow.engine.service.IFlowProcessService;
import org.hy.xflow.engine.service.ITemplateService;





/**
 * 工作流引擎 
 *
 * @author      ZhengWei(HY)
 * @createDate  2017-03-10
 * @version     v1.0
 */
@Xjava
public class XFlowEngine
{
    
    @Xjava
    private ITemplateService    templateService;
    
    @Xjava
    private IFlowInfoService    flowInfoService;
    
    @Xjava
    private IFlowProcessService flowProcessService;
    
    
    
    public static XFlowEngine getInstance()
    {
        return (XFlowEngine)XJava.getObject("XFlowEngine");
    }
    
    
    
    /**
     * 按工作流模板名称创建工作流实例。
     * 
     * 将按模板名称查询版本号最大的有效的工作流模板，用它来创建工作流实例。
     * 
     * 创建的工作流实例，当前活动节点为  "开始" 节点。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-26
     * @version     v1.0
     *
     * @param i_User           创建人信息
     * @param i_TemplateName   工作流模板名称
     * @return                 成功时，返回工作流实例对象。
     *                         异常时，抛出错误。
     */
    public FlowInfo createByName(User i_User ,String i_TemplateName)
    {
        return createByName(i_User ,i_TemplateName ,"");
    }
    
    
    
    /**
     * 按工作流模板名称创建工作流实例。
     * 
     * 将按模板名称查询版本号最大的有效的工作流模板，用它来创建工作流实例。
     * 
     * 创建的工作流实例，当前活动节点为  "开始" 节点。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     *
     * @param i_User           创建人信息
     * @param i_TemplateName   工作流模板名称
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return                 成功时，返回工作流实例对象。
     *                         异常时，抛出错误。
     */
    public FlowInfo createByName(User i_User ,String i_TemplateName ,String i_ServiceDataID)
    {
        if ( i_User == null )
        {
            throw new NullPointerException("User is null.");
        }
        else if ( Help.isNull(i_User.getUserID()) )
        {
            throw new NullPointerException("UserID is null.");
        }
        else if ( Help.isNull(i_TemplateName) )
        {
            throw new NullPointerException("Template name is null.");
        }
        
        // 查询并判定工作流模板是否存在
        Template v_Template = this.templateService.queryByNameMaxVersionNo(i_TemplateName);
        if ( v_Template == null )
        {
            throw new VerifyError("Template[" + i_TemplateName + "] is not exists.");
        }
        v_Template = this.templateService.queryByID(v_Template);
        
        // 判定第三方使用系统的业务数据ID是否重复
        if ( !Help.isNull(i_ServiceDataID) )
        {
            FlowInfo v_FlowInfo = this.flowInfoService.queryByServiceDataID(i_ServiceDataID);
            
            if ( v_FlowInfo != null )
            {
                throw new VerifyError("ServiceDataID[" + i_ServiceDataID + "] is exists.");
            }
        }
        
        // 判定是否为参与人之一
        Participant v_Participant = v_Template.getActivityRouteTree().getStartActivity().isParticipant(i_User);
        if ( v_Participant == null )
        {
            throw new VerifyError("User is not participants.");
        }
        
        FlowInfo    v_Flow    = new FlowInfo(i_User ,v_Template ,i_ServiceDataID);
        FlowProcess v_Process = new FlowProcess(i_User ,v_Flow ,null ,v_Template.getActivityRouteTree().getStartActivity());
        boolean     v_Ret     = this.flowInfoService.createFlow(v_Flow ,v_Process);
        
        if ( v_Ret )
        {
            return v_Flow;
        }
        else
        {
            throw new RuntimeException("Create flow is error.");
        }
    }
    
    
    
    /**
     * 查询用户可以走的路由。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-02
     * @version     v1.0
     *
     * @param i_User    用户 
     * @param i_WorkID  工作流ID
     * @return
     */
    public List<ActivityRoute> queryNextRoutes(User i_User ,String i_WorkID)
    {
        if ( i_User == null )
        {
            throw new NullPointerException("User is null.");
        }
        else if ( Help.isNull(i_User.getUserID()) )
        {
            throw new NullPointerException("UserID is null.");
        }
        else if ( Help.isNull(i_WorkID) )
        {
            throw new NullPointerException("WorkID is null.");
        }
        
        FlowInfo v_FlowInfo = this.flowInfoService.queryByWorkID(i_WorkID);
        if ( v_FlowInfo == null || Help.isNull(v_FlowInfo.getWorkID()))
        {
            throw new NullPointerException("WorkID[" + i_WorkID + "] is not exists.");
        }
        
        Template v_Template = this.templateService.queryByID(v_FlowInfo.getFlowTemplateID());
        if ( v_Template == null )
        {
            throw new NullPointerException("Template[" + v_FlowInfo.getFlowTemplateID() + "] is not exists.");
        }
        
        List<FlowProcess> v_ProcessList = this.flowProcessService.queryByWorkID(i_WorkID);
        if ( Help.isNull(v_ProcessList) )
        {
            throw new NullPointerException("WorkID[" + i_WorkID + "] ProcessList is not exists.");
        }
        
        int         v_PIndex  = 0;
        FlowProcess v_Process = null;
        for (; v_PIndex < v_ProcessList.size(); v_PIndex++)
        {
            v_Process = v_ProcessList.get(v_PIndex);
            
            // 预留代码
            break;
        }
        if ( v_Process == null )
        {
            throw new NullPointerException("WorkID[" + i_WorkID + "] is not grant to User[" + i_User.getUserID() + "].");
        }
        
        ActivityInfo        v_Activity  = v_Template.getActivityRouteTree().getActivity(v_Process.getCurrentActivityID());
        List<ActivityRoute> v_Routes    = v_Activity.getRoutes();
        List<ActivityRoute> v_RetRoutes = new ArrayList<ActivityRoute>();
        for (ActivityRoute v_Route : v_Routes)
        {
            // 当路由上没有要求时，取活动节点上要求的参与人
            if ( Help.isNull(v_Route.getParticipants()) )
            {
                if ( v_Activity.isParticipant(i_User) != null )
                {
                    v_RetRoutes.add(v_Route);
                }
            }
            else
            {
                // 是否是路由上要求的参与人
                if ( v_Route.isParticipant(i_User) != null )
                {
                    v_RetRoutes.add(v_Route);
                }
            }
        }
        
        return v_RetRoutes;
    }
    
    
    
    /**
     * 按第三方使用系统的业务数据ID，查询用户可以走的路由。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-02
     * @version     v1.0
     *
     * @param i_User
     * @param i_ServiceDataID
     * @return
     */
    public List<ActivityRoute> queryNextRoutesByServiceDataID(User i_User ,String i_ServiceDataID)
    {
        if ( i_User == null )
        {
            throw new NullPointerException("User is null.");
        }
        else if ( Help.isNull(i_User.getUserID()) )
        {
            throw new NullPointerException("UserID is null.");
        }
        else if ( Help.isNull(i_ServiceDataID) )
        {
            throw new NullPointerException("ServiceDataID is null.");
        }
        
        FlowInfo v_FlowInfo = this.flowInfoService.queryByServiceDataID(i_ServiceDataID);
        if ( v_FlowInfo == null || Help.isNull(v_FlowInfo.getWorkID()))
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not exists.");
        }
        
        Template v_Template = this.templateService.queryByID(v_FlowInfo.getFlowTemplateID());
        if ( v_Template == null )
        {
            throw new NullPointerException("Template[" + v_FlowInfo.getFlowTemplateID() + "] is not exists.");
        }
        
        List<FlowProcess> v_ProcessList = this.flowProcessService.queryByServiceDataID(i_ServiceDataID);
        if ( Help.isNull(v_ProcessList) )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] ProcessList is not exists.");
        }
        
        int         v_PIndex  = 0;
        FlowProcess v_Process = null;
        for (; v_PIndex < v_ProcessList.size(); v_PIndex++)
        {
            v_Process = v_ProcessList.get(v_PIndex);
            
            // 预留代码
            break;
        }
        if ( v_Process == null )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not grant to User[" + i_User.getUserID() + "].");
        }
        
        ActivityInfo        v_Activity  = v_Template.getActivityRouteTree().getActivity(v_Process.getCurrentActivityID());
        List<ActivityRoute> v_Routes    = v_Activity.getRoutes();
        List<ActivityRoute> v_RetRoutes = new ArrayList<ActivityRoute>();
        for (ActivityRoute v_Route : v_Routes)
        {
            // 当路由上没有要求时，取活动节点上要求的参与人
            if ( Help.isNull(v_Route.getParticipants()) )
            {
                if ( v_Activity.isParticipant(i_User) != null )
                {
                    v_RetRoutes.add(v_Route);
                }
            }
            else
            {
                // 是否是路由上要求的参与人
                if ( v_Route.isParticipant(i_User) != null )
                {
                    v_RetRoutes.add(v_Route);
                }
            }
        }
        
        return v_RetRoutes;
    }
    
    
    
    public FlowProcess toNext(User i_User ,String i_WorkID ,String i_ActivityID ,String i_ActivityRouteID)
    {
        if ( i_User == null )
        {
            throw new NullPointerException("User is null.");
        }
        else if ( Help.isNull(i_User.getUserID()) )
        {
            throw new NullPointerException("UserID is null.");
        }
        else if ( Help.isNull(i_WorkID) )
        {
            throw new NullPointerException("WorkID is null.");
        }
        else if ( Help.isNull(i_ActivityID) )
        {
            throw new NullPointerException("ActivityID is null.");
        }
        else if ( Help.isNull(i_ActivityRouteID) )
        {
            throw new NullPointerException("ActivityRouteID is null.");
        }
        
        FlowInfo v_FlowInfo = this.flowInfoService.queryByWorkID(i_WorkID);
        if ( v_FlowInfo == null || Help.isNull(v_FlowInfo.getWorkID()))
        {
            throw new NullPointerException("WorkID[" + i_WorkID + "] is not exists.");
        }
        
        Template v_Template = this.templateService.queryByID(v_FlowInfo.getFlowTemplateID());
        if ( v_Template == null )
        {
            throw new NullPointerException("Template[" + v_FlowInfo.getFlowTemplateID() + "] is not exists.");
        }
        
        ActivityRoute v_Route = v_Template.getActivityRouteTree().getActivityRoute(i_ActivityID ,i_ActivityRouteID);
        if ( v_Route == null )
        {
            throw new NullPointerException("ActivityID[" + i_ActivityID + "] and ActivityRouteID[" + i_ActivityRouteID + "] is not exists.");
        }
        
        FlowProcess v_Process = new FlowProcess();
        
        return v_Process;
    }
    
}
