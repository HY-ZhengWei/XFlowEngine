package org.hy.xflow.engine;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Help;
import org.hy.common.PartitionMap;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.ActivityInfo;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.Participant;
import org.hy.xflow.engine.bean.ProcessParticipant;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.NextRoutes;
import org.hy.xflow.engine.bean.Template;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.bean.UserParticipant;
import org.hy.xflow.engine.config.InitConfig;
import org.hy.xflow.engine.enums.ActivityTypeEnum;
import org.hy.xflow.engine.enums.ParticipantTypeEnum;
import org.hy.xflow.engine.enums.RouteTypeEnum;
import org.hy.xflow.engine.service.IFlowFutureOperatorService;
import org.hy.xflow.engine.service.IFlowInfoService;
import org.hy.xflow.engine.service.IFlowProcessService;
import org.hy.xflow.engine.service.IProcessParticipantsService;
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
    private ITemplateService            templateService;
    
    @Xjava
    private IFlowInfoService            flowInfoService;
    
    @Xjava
    private IFlowProcessService         flowProcessService;
    
    @Xjava
    private IProcessParticipantsService processParticipantsService;
    
    @Xjava
    private IFlowFutureOperatorService  futureOperatorService;
    
    
    
    /**
     * 在执行任何操作前，应当初始配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-17
     * @version     v1.0
     *
     */
    public static void init()
    {
        new InitConfig();
    }
    
    
    
    public static XFlowEngine getInstance()
    {
        XFlowEngine v_XFlowEngine = (XFlowEngine)XJava.getObject("XFlowEngine");
        
        v_XFlowEngine.futureOperatorService.initCaches();
        
        return v_XFlowEngine;
    }
    
    
    
    /**
     * 判定用户参与人之一。
     * 
     * 联合动态参与人、活动实际操作人、发起人、活动节点、活动路由一起判定。
     * 
     * 各种参与人在活动节点上的级别划分如下：
     *   指定动态参与人 》 活动实际操作人  》  发起人 》 活动参与人
     *  
     * 路由可以不设定参与人的要求，但当路由设定参与人要求时，无论哪种参与人类型，均要符合路由对参与人的要求。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-08
     * @version     v1.0
     *
     * @param i_User      用户
     * @param i_Flow      工作流实例。内部有此实例的所有流转信息
     * @param io_Process  流转过程。它上可指定动态参与人。
     * @param i_Activity  模板的活动
     * @return
     */
    public static Participant isParticipant(User i_User ,FlowInfo i_Flow ,FlowProcess io_Process ,ActivityRoute i_Route)
    {
        boolean                           v_IsCreater     = i_User.getUserID().equals(i_Flow.getCreaterID());
        PartitionMap<String ,FlowProcess> v_OldProcessMap = i_Flow.getProcessActivityMap();
        Participant                       v_Participant   = null;
        
        if ( Help.isNull(i_Route.getParticipants()) )
        {
            // 是之前工作流流转过程的活动实际操作人时
            v_Participant = whereTo_ParticipantTypeEnum_ActivityUser(i_User ,i_Route.getActivity() ,v_OldProcessMap);
            if ( v_Participant == null )
            {
                // 为发起人时
                if ( i_Route.getActivity().getParticipantByCreater() != null && v_IsCreater )
                {
                    v_Participant = i_Route.getActivity().getParticipantByCreater();
                }
                else
                {
                    v_Participant = i_Route.getActivity().isParticipant(i_User);
                }
            }
        }
        // 路由级别高于活动节点，当路由上有参与人要求时，按路由的要求走
        else
        {
            // 是之前工作流流转过程的活动实际操作人时
            v_Participant = whereTo_ParticipantTypeEnum_ActivityUser(i_User ,i_Route ,v_OldProcessMap);
            if ( v_Participant == null )
            {
                // 为发起人时
                if ( i_Route.getParticipantByCreater() != null && v_IsCreater )
                {
                    v_Participant = i_Route.getParticipantByCreater();
                }
                else
                {
                    v_Participant = i_Route.isParticipant(i_User);
                }
            }
        }
        
        return v_Participant;
    }
    
    
    
    /**
     * 去哪？当前流转活动节点下、当前模板的活动节点下，允许当前用户走的路由。
     * 
     * 各种参与人在活动节点上的级别划分如下：
     *   指定动态参与人 》 活动实际操作人  》  发起人 》 活动参与人
     *  
     * 路由可以不设定参与人的要求，但当路由设定参与人要求时，无论哪种参与人类型，均要符合路由对参与人的要求。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-09
     * @version     v1.0
     *
     * @param i_User      用户
     * @param i_Flow      工作流实例。内部有此实例的所有流转信息
     * @param io_Process  流转过程。它上可指定动态参与人。
     * @param i_Activity  当前活动
     * @return
     */
    public static List<ActivityRoute> whereTo(User i_User ,FlowInfo i_Flow ,FlowProcess io_Process ,ActivityInfo i_Activity)
    {
        List<ActivityRoute> v_Routes    = i_Activity.getRoutes();
        List<ActivityRoute> v_RetRoutes = new ArrayList<ActivityRoute>();
        boolean             v_IsCreater = i_User.getUserID().equals(i_Flow.getCreaterID());
        
        if ( Help.isNull(v_Routes) )
        {
            return v_RetRoutes;
        }
        
        // 指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
        // 指定动态参与人时，同时也受限于路由上设定的参与人。
        //                 但当路由上没有设定参与人时，动态参与人将畅通无阻
        if ( !Help.isNull(io_Process.getParticipants()) )
        {
            if ( io_Process.isParticipant(i_User) == null )
            {
                return v_RetRoutes;
            }
            
            for (ActivityRoute v_Route : v_Routes)
            {
                // 当路由上没有要求时，默认认为参与人可以走此路由
                if ( Help.isNull(v_Route.getParticipants()) )
                {
                    v_RetRoutes.add(v_Route);
                }
                else
                {
                    // 是发起人时
                    if ( v_IsCreater && v_Route.getParticipantByCreater() != null )
                    {
                        v_RetRoutes.add(v_Route);
                    }
                    // 是否是路由上要求的参与人
                    else if ( v_Route.isParticipant(i_User) != null )
                    {
                        v_RetRoutes.add(v_Route);
                    }
                }
            }
        }
        // 情况1. 活动节点的参与人是之前另一个活动的实际操作人 
        // 情况2. 活动节点的参与人有发起人的情况
        // 情况3. 常规工作流模板定义的参与人
        else
        {
            PartitionMap<String ,FlowProcess> v_OldProcessMap = i_Flow.getProcessActivityMap();
            
            for (ActivityRoute v_Route : v_Routes)
            {
                // 当路由上没有要求时，取活动节点上要求的参与人
                if ( Help.isNull(v_Route.getParticipants()) )
                {
                    // 是之前工作流流转过程的活动实际操作人时
                    if ( whereTo_ParticipantTypeEnum_ActivityUser(i_User ,i_Activity ,v_OldProcessMap) != null )
                    {
                        v_RetRoutes.add(v_Route);
                    }
                    // 是发起人时
                    else if ( i_Activity.getParticipantByCreater() != null && v_IsCreater )
                    {
                        v_RetRoutes.add(v_Route);
                    }
                    else if ( i_Activity.isParticipant(i_User) != null )
                    {
                        v_RetRoutes.add(v_Route);
                    }
                }
                else
                {
                    // 是之前工作流流转过程的活动实际操作人时
                    if ( whereTo_ParticipantTypeEnum_ActivityUser(i_User ,v_Route ,v_OldProcessMap) != null )
                    {
                        v_RetRoutes.add(v_Route);
                    }
                    // 是发起人时
                    else if ( v_Route.getParticipantByCreater() != null && v_IsCreater )
                    {
                        v_RetRoutes.add(v_Route);
                    }
                    // 是否是路由上要求的参与人
                    else if ( v_Route.isParticipant(i_User) != null )
                    {
                        v_RetRoutes.add(v_Route);
                    }
                }
            }
        }
        
        return v_RetRoutes;
    }
    
    
    
    /**
     * 判定用户是否为之前工作流流转过程的活动实际操作人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-10
     * @version     v1.0
     *
     * @param i_User           用户
     * @param i_Activity       当前活动
     * @param i_OldProcessMap  按当时流转过程的当前活动ID为Map分区结构的流转信息
     * @return
     */
    private static Participant whereTo_ParticipantTypeEnum_ActivityUser(User i_User ,ActivityInfo i_Activity ,PartitionMap<String ,FlowProcess> i_OldProcessMap)
    {
        if ( !Help.isNull(i_Activity.getParticipantByActivitys()) )
        {
            for (Participant v_ActivityPart : i_Activity.getParticipantByActivitys().values())
            {
                List<FlowProcess> v_OldProcesses = i_OldProcessMap.get(v_ActivityPart.getObjectID());
                
                if ( !Help.isNull(v_OldProcesses) )
                {
                    if ( i_User.getUserID().equals(v_OldProcesses.get(0).getOperateUserID()) )
                    {
                        return v_ActivityPart;
                    }
                }
            }
        }
        
        return null;
    }
    
    
    
    /**
     * 判定用户是否为之前工作流流转过程的活动实际操作人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-10
     * @version     v1.0
     *
     * @param i_User           用户
     * @param i_Route          准备判定要走的路由
     * @param i_OldProcessMap  按当时流转过程的当前活动ID为Map分区结构的流转信息
     * @return
     */
    private static Participant whereTo_ParticipantTypeEnum_ActivityUser(User i_User ,ActivityRoute i_Route ,PartitionMap<String ,FlowProcess> i_OldProcessMap)
    {
        if ( !Help.isNull(i_Route.getParticipantByActivitys()) )
        {
            for (Participant v_ActivityPart : i_Route.getParticipantByActivitys().values())
            {
                List<FlowProcess> v_OldProcesses = i_OldProcessMap.get(v_ActivityPart.getObjectID());
                
                if ( !Help.isNull(v_OldProcesses) )
                {
                    if ( i_User.getUserID().equals(v_OldProcesses.get(0).getOperateUserID()) )
                    {
                        return v_ActivityPart;
                    }
                }
            }
        }
        
        return null;
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
            throw new VerifyError("User[" + i_User.getUserID() + "] is not participants for TemplateName[" + i_TemplateName + "].");
        }
        
        FlowInfo    v_Flow    = new FlowInfo(i_User ,v_Template ,i_ServiceDataID);
        FlowProcess v_Process = new FlowProcess().init_CreateFlow(i_User ,v_Flow ,v_Template.getActivityRouteTree().getStartActivity());
        boolean     v_Ret     = this.flowInfoService.createFlow(v_Flow ,v_Process);
        
        if ( v_Ret )
        {
            return v_Flow;
        }
        else
        {
            throw new RuntimeException("ServiceDataID[" + i_ServiceDataID + "] create flow is error. User[" + i_User.getUserID() + "] TemplateName[" + i_TemplateName + "]");
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
    public NextRoutes queryNextRoutes(User i_User ,String i_WorkID)
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
        
        v_FlowInfo.setProcesses(this.flowProcessService.queryByWorkID(i_WorkID));
        if ( Help.isNull(v_FlowInfo.getProcesses()) )
        {
            throw new NullPointerException("WorkID[" + i_WorkID + "] ProcessList is not exists.");
        }
        
        int         v_PIndex  = 0;
        FlowProcess v_Process = null;
        for (; v_PIndex < v_FlowInfo.getProcesses().size(); v_PIndex++)
        {
            v_Process = v_FlowInfo.getProcesses().get(v_PIndex);
            
            // 预留代码
            break;
        }
        if ( v_Process == null )
        {
            throw new NullPointerException("WorkID[" + i_WorkID + "] is not grant to User[" + i_User.getUserID() + "].");
        }
        
        
        ActivityInfo v_Activity = v_Template.getActivityRouteTree().getActivity(v_Process.getCurrentActivityCode());
        
        // 查询动态参与人
        PartitionMap<String ,ProcessParticipant> v_AllProcessParts = processParticipantsService.queryByWorkID(i_WorkID);
        v_Process.setParticipants(v_AllProcessParts.get(v_Process.getProcessID()));
        
        return new NextRoutes(v_FlowInfo
                             ,v_Process
                             ,v_Activity
                             ,v_AllProcessParts
                             ,whereTo(i_User ,v_FlowInfo ,v_Process ,v_Activity));
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
    public NextRoutes queryNextRoutesByServiceDataID(User i_User ,String i_ServiceDataID)
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
        
        v_FlowInfo.setProcesses(this.flowProcessService.queryByServiceDataID(i_ServiceDataID));
        if ( Help.isNull(v_FlowInfo.getProcesses()) )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] ProcessList is not exists.");
        }
        
        int         v_PIndex  = 0;
        FlowProcess v_Process = null;
        for (; v_PIndex < v_FlowInfo.getProcesses().size(); v_PIndex++)
        {
            v_Process = v_FlowInfo.getProcesses().get(v_PIndex);
            
            // 预留代码
            break;
        }
        if ( v_Process == null )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not grant to User[" + i_User.getUserID() + "].");
        }
        
        ActivityInfo v_Activity = v_Template.getActivityRouteTree().getActivity(v_Process.getCurrentActivityCode());
        
        // 查询动态参与人
        PartitionMap<String ,ProcessParticipant> v_AllProcessParts = processParticipantsService.queryByServiceDataID(i_ServiceDataID);
        v_Process.setParticipants(v_AllProcessParts.get(v_Process.getProcessID()));
        
        return new NextRoutes(v_FlowInfo 
                             ,v_Process 
                             ,v_Activity 
                             ,v_AllProcessParts
                             ,whereTo(i_User ,v_FlowInfo ,v_Process ,v_Activity));
    }
    
    
    
    /**
     * 向下一个活动节点流转
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-08
     * @version     v1.0
     *
     * @param i_User               操作用户 
     * @param i_WorkID             工作流ID
     * @param i_ActivityRouteCode  走的路由编码
     * @return
     */
    public FlowProcess toNext(User i_User ,String i_WorkID ,String i_ActivityRouteCode)
    {
        return this.toNext(i_User ,i_WorkID ,i_ActivityRouteCode ,(List<UserParticipant>)null);
    }
    
    
    
    /**
     * 向下一个活动节点流转
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-08
     * @version     v1.0
     *
     * @param i_User                操作用户 
     * @param i_WorkID              工作流ID
     * @param i_ActivityRouteCode   走的路由编码
     * @param i_Participant         指定下一活动的动态参与人，可选项。
     *                              指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                              指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                             但当路由上没有设定参与人时，动态参与人将畅通无阻
     * @return
     */
    public FlowProcess toNext(User i_User ,String i_ServiceDataID ,String i_ActivityRouteCode ,UserParticipant i_Participant)
    {
        List<UserParticipant> v_Participants = null;
        
        if ( i_Participant != null )
        {
            v_Participants = new ArrayList<UserParticipant>();
            v_Participants.add(i_Participant);
        }
        
        return this.toNext(i_User ,i_ServiceDataID ,i_ActivityRouteCode ,v_Participants);
    }
    
    
    
    /**
     * 向下一个活动节点流转
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-07
     * @version     v1.0
     *
     * @param i_User                操作用户 
     * @param i_WorkID              工作流ID
     * @param i_ActivityRouteCode   走的路由编码
     * @param i_Participants        指定下一活动的动态参与人，可选项。
     *                              指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                              指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                             但当路由上没有设定参与人时，动态参与人将畅通无阻
     * @return
     */
    public FlowProcess toNext(User i_User ,String i_WorkID ,String i_ActivityRouteCode ,List<UserParticipant> i_Participants)
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
        else if ( Help.isNull(i_ActivityRouteCode) )
        {
            throw new NullPointerException("ActivityRouteCode is null.");
        }
        
        NextRoutes v_NextRoutes = this.queryNextRoutes(i_User ,i_WorkID);
        Template   v_Template   = this.templateService.queryByID(v_NextRoutes.getFlow().getFlowTemplateID());
        if ( v_Template == null )
        {
            throw new NullPointerException("Template[" + v_NextRoutes.getFlow().getFlowTemplateID() + "] is not exists. WorkID[" + i_WorkID + "] for User[" + i_User.getUserID() + "]");
        }
        
        ActivityRoute v_Route = v_Template.getActivityRouteTree().getActivityRoute(i_ActivityRouteCode);
        if ( v_Route == null )
        {
            throw new NullPointerException("ActivityRouteCode[" + i_ActivityRouteCode + "] is not exists. WorkID[" + i_WorkID + "] or User[" + i_User.getUserID() + "]");
        }
        
        // 判定是否允许用户走这条路由
        boolean v_AllowRoute = false;
        for (ActivityRoute v_MaybeRoute : v_NextRoutes.getRoutes())
        {
            if ( v_MaybeRoute.getActivityRouteID().equals(v_Route.getActivityRouteID()) )
            {
                v_AllowRoute = true;
                break;
            }
        }
        if ( !v_AllowRoute )
        {
            throw new VerifyError("WorkID[" + i_WorkID + "] to next process is not Route. ActivityCode[" + v_Route.getActivityCode() + "]  ActivityRouteCode[" + i_ActivityRouteCode + "] User[" + i_User.getUserID() + "]");
        }
        
        // 设置动态参与人
        FlowProcess v_Previous = v_NextRoutes.getCurrentProcess();
        v_Previous.setParticipants(v_NextRoutes.getFlowParticipants().get(v_Previous.getProcessID()));
        
        // 判定是否为参与人
        Participant v_Participant = isParticipant(i_User ,v_NextRoutes.getFlow() ,v_Previous ,v_Route);
        if ( v_Participant == null )
        {
            throw new NullPointerException("WorkID[" + i_WorkID + "] is not grant to User[" + i_User.getUserID() + "].");
        }
        
        FlowProcess v_Process = new FlowProcess();
        v_Process.init_ToNext(i_User ,v_NextRoutes.getFlow() ,v_Previous ,v_Route);
        
        // 生成参与人信息
        if ( !Help.isNull(i_Participants) )
        {
            v_Process.setParticipants(new ArrayList<ProcessParticipant>());
            for (int v_Index=0; v_Index<i_Participants.size(); v_Index++)
            {
                UserParticipant v_UserPart = i_Participants.get(v_Index);
                if ( v_UserPart == null )
                {
                    throw new NullPointerException("WorkID[" + i_WorkID + "] participant[" + v_Index + "] is null to User[" + i_User.getUserID() + "].");
                }
                else if ( Help.isNull(v_UserPart.getObjectID()) )
                {
                    throw new NullPointerException("WorkID[" + i_WorkID + "] participant[" + v_Index + "] ObjectID is null to User[" + i_User.getUserID() + "].");
                }
                else if ( v_UserPart.getObjectType() == null )
                {
                    throw new NullPointerException("WorkID[" + i_WorkID + "] participant[" + v_Index + "] ObjectType is null to User[" + i_User.getUserID() + "].");
                }
                
                if ( v_UserPart.getObjectNo() == null )
                {
                    v_UserPart.setObjectNo(v_Index + 1);
                }
                
                ProcessParticipant v_PartTemp = new ProcessParticipant();
                v_PartTemp.init(i_User ,v_Process ,i_Participants.get(v_Index));
                v_Process.getParticipants().add(v_PartTemp);
            }
        }
        
        List<FlowProcess> v_OldProcesses = v_NextRoutes.getFlow().getProcessActivityMap().get(v_Route.getNextActivity().getActivityCode());
        
        // 驳回的路由
        if ( v_Route.getRouteTypeID() == RouteTypeEnum.$Reject )
        {
            if ( Help.isNull(v_OldProcesses) )
            {
                throw new VerifyError("WorkID[" + i_WorkID + "] to next process is not reject. ActivityCode[" + v_Route.getActivityCode() + "]  ActivityRouteCode[" + i_ActivityRouteCode + "] User[" + i_User.getUserID() + "]");
            }
            
            if ( Help.isNull(v_Process.getParticipants()) )
            {
                v_Process.setParticipants(new ArrayList<ProcessParticipant>());
            }
            
            // 指定原操作人，为驳回后的操作人
            ProcessParticipant v_RejectPart = new ProcessParticipant();
            v_RejectPart.init_ToReject(i_User ,v_Process ,v_OldProcesses.get(0));
            v_Process.getParticipants().add(v_RejectPart);
        }
        
        
        // 未来操作人：是工作流模板中定义的参与人
        if ( Help.isNull(v_Process.getParticipants()) )
        {
            v_Process.setFutureParticipants(new ArrayList<ProcessParticipant>());
            
            for (Participant v_PartItem : v_Route.getNextActivity().getParticipants())
            {
                ProcessParticipant v_FuturePart = new ProcessParticipant();
                
                if ( v_PartItem.getObjectType() == ParticipantTypeEnum.$CreateUser )
                {
                    v_FuturePart.init(i_User ,v_Process ,v_PartItem.toCreater(v_NextRoutes.getFlow()));
                }
                else if ( v_PartItem.getObjectType() == ParticipantTypeEnum.$ActivityUser )
                {
                    if ( !Help.isNull(v_OldProcesses) )
                    {
                        v_FuturePart.init(i_User ,v_Process ,v_PartItem.toOperater(v_OldProcesses.get(0)));
                    }
                    else
                    {
                        throw new VerifyError("WorkID[" + i_WorkID + "] to next process is not find $ActivityUser. ActivityCode[" + v_Route.getActivityCode() + "]  ActivityRouteCode[" + i_ActivityRouteCode + "] User[" + i_User.getUserID() + "]");
                    }
                }
                else
                {
                    v_FuturePart.init(i_User ,v_Process ,v_PartItem);
                }
                
                v_Process.getFutureParticipants().add(v_FuturePart);
            }
        }
        // 未来操作人：是动态参与人
        else
        {
            v_Process.setFutureParticipants(v_Process.getParticipants());
        }
        
        boolean v_Ret = this.flowInfoService.toNext(v_Process ,v_Previous);
        
        if ( v_Ret )
        {
            if ( RouteTypeEnum.$Finish    == v_Route.getRouteTypeID()
              || ActivityTypeEnum.$Finish == v_Route.getNextActivity().getActivityTypeID() )
            {
                this.flowInfoService.toHistory(i_WorkID);
                this.futureOperatorService.delCache(v_Process);
            }
            else
            {
                this.futureOperatorService.updateCache(v_Process);
            }
            
            return v_Process;
        }
        else
        {
            throw new RuntimeException("WorkID[" + i_WorkID + "] to next process is error. ActivityCode[" + v_Route.getActivityCode() + "]  ActivityRouteCode[" + i_ActivityRouteCode + "] User[" + i_User.getUserID() + "]");
        }
    }
    
    
    
    /**
     * 按第三方使用系统的业务数据ID，向下一个活动节点流转
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-08
     * @version     v1.0
     *
     * @param i_User                操作用户 
     * @param i_ServiceDataID       第三方使用系统的业务数据ID
     * @param i_ActivityRouteCode   走的路由编码
     * @return
     */
    public FlowProcess toNextByServiceDataID(User i_User ,String i_ServiceDataID ,String i_ActivityRouteCode)
    {
        return this.toNextByServiceDataID(i_User ,i_ServiceDataID ,i_ActivityRouteCode ,(List<UserParticipant>)null);
    }
    
    
    
    /**
     * 按第三方使用系统的业务数据ID，向下一个活动节点流转
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-08
     * @version     v1.0
     *
     * @param i_User                操作用户 
     * @param i_ServiceDataID       第三方使用系统的业务数据ID
     * @param i_ActivityRouteCode   走的路由编码
     * @param i_Participant         指定下一活动的动态参与人，可选项。
     *                              指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                              指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                             但当路由上没有设定参与人时，动态参与人将畅通无阻
     * @return
     */
    public FlowProcess toNextByServiceDataID(User i_User ,String i_ServiceDataID ,String i_ActivityRouteCode ,UserParticipant i_Participant)
    {
        List<UserParticipant> v_Participants = null;
        
        if ( i_Participant != null )
        {
            v_Participants = new ArrayList<UserParticipant>();
            v_Participants.add(i_Participant);
        }
        
        return this.toNextByServiceDataID(i_User ,i_ServiceDataID ,i_ActivityRouteCode ,v_Participants);
    }
    
    
    
    /**
     * 按第三方使用系统的业务数据ID，向下一个活动节点流转
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-07
     * @version     v1.0
     *
     * @param i_User                操作用户 
     * @param i_ServiceDataID       第三方使用系统的业务数据ID
     * @param i_ActivityRouteCode   走的路由编码
     * @param i_Participants        指定下一活动的动态参与人，可选项。
     *                              指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                              指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                             但当路由上没有设定参与人时，动态参与人将畅通无阻。
     * @return
     */
    public FlowProcess toNextByServiceDataID(User i_User ,String i_ServiceDataID ,String i_ActivityRouteCode ,List<UserParticipant> i_Participants)
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
        else if ( Help.isNull(i_ActivityRouteCode) )
        {
            throw new NullPointerException("ActivityRouteCode is null.");
        }
        
        NextRoutes v_NextRoutes = this.queryNextRoutesByServiceDataID(i_User ,i_ServiceDataID);
        Template   v_Template   = this.templateService.queryByID(v_NextRoutes.getFlow().getFlowTemplateID());
        if ( v_Template == null )
        {
            throw new NullPointerException("Template[" + v_NextRoutes.getFlow().getFlowTemplateID() + "] is not exists. ServiceDataID[" + i_ServiceDataID + "] or User[" + i_User.getUserID() + "]");
        }
        
        ActivityRoute v_Route = v_Template.getActivityRouteTree().getActivityRoute(i_ActivityRouteCode);
        if ( v_Route == null )
        {
            throw new NullPointerException("ActivityRouteCode[" + i_ActivityRouteCode + "] is not exists. ServiceDataID[" + i_ServiceDataID + "] or User[" + i_User.getUserID() + "]");
        }
        
        // 判定是否允许用户走这条路由
        boolean v_AllowRoute = false;
        for (ActivityRoute v_MaybeRoute : v_NextRoutes.getRoutes())
        {
            if ( v_MaybeRoute.getActivityRouteID().equals(v_Route.getActivityRouteID()) )
            {
                v_AllowRoute = true;
                break;
            }
        }
        if ( !v_AllowRoute )
        {
            throw new VerifyError("ServiceDataID[" + i_ServiceDataID + "] to next process is not Route. ActivityCode[" + v_Route.getActivityCode() + "]  ActivityRouteCode[" + i_ActivityRouteCode + "] User[" + i_User.getUserID() + "]");
        }
        
        
        // 设置动态参与人
        FlowProcess v_Previous = v_NextRoutes.getCurrentProcess();
        v_Previous.setParticipants(v_NextRoutes.getFlowParticipants().get(v_Previous.getProcessID()));
        
        // 判定是否为参与人
        Participant v_Participant = isParticipant(i_User ,v_NextRoutes.getFlow() ,v_Previous ,v_Route);
        if ( v_Participant == null )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not grant to User[" + i_User.getUserID() + "].");
        }
        
        FlowProcess v_Process = new FlowProcess();
        v_Process.init_ToNext(i_User ,v_NextRoutes.getFlow() ,v_Previous ,v_Route);
        
        // 生成参与人信息
        if ( !Help.isNull(i_Participants) )
        {
            v_Process.setParticipants(new ArrayList<ProcessParticipant>());
            for (int v_Index=0; v_Index<i_Participants.size(); v_Index++)
            {
                UserParticipant v_UserPart = i_Participants.get(v_Index);
                if ( v_UserPart == null )
                {
                    throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] participant[" + v_Index + "] is null to User[" + i_User.getUserID() + "].");
                }
                else if ( Help.isNull(v_UserPart.getObjectID()) )
                {
                    throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] participant[" + v_Index + "] ObjectID is null to User[" + i_User.getUserID() + "].");
                }
                else if ( v_UserPart.getObjectType() == null )
                {
                    throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] participant[" + v_Index + "] ObjectType is null to User[" + i_User.getUserID() + "].");
                }
                
                if ( v_UserPart.getObjectNo() == null )
                {
                    v_UserPart.setObjectNo(v_Index + 1);
                }
                
                ProcessParticipant v_PartTemp = new ProcessParticipant();
                v_PartTemp.init(i_User ,v_Process ,i_Participants.get(v_Index));
                v_Process.getParticipants().add(v_PartTemp);
            }
        }
        
        List<FlowProcess> v_OldProcesses = v_NextRoutes.getFlow().getProcessActivityMap().get(v_Route.getNextActivity().getActivityCode());
        
        // 驳回的路由
        if ( v_Route.getRouteTypeID() == RouteTypeEnum.$Reject )
        {
            if ( Help.isNull(v_OldProcesses) )
            {
                throw new VerifyError("ServiceDataID[" + i_ServiceDataID + "] to next process is not reject. ActivityCode[" + v_Route.getActivityCode() + "]  ActivityRouteCode[" + i_ActivityRouteCode + "] User[" + i_User.getUserID() + "]");
            }
            
            if ( Help.isNull(v_Process.getParticipants()) )
            {
                v_Process.setParticipants(new ArrayList<ProcessParticipant>());
            }
            
            // 指定原操作人，为驳回后的操作人
            ProcessParticipant v_RejectPart = new ProcessParticipant();
            v_RejectPart.init_ToReject(i_User ,v_Process ,v_OldProcesses.get(0));
            v_Process.getParticipants().add(v_RejectPart);
        }
        
        
        // 未来操作人：是工作流模板中定义的参与人
        if ( Help.isNull(v_Process.getParticipants()) )
        {
            v_Process.setFutureParticipants(new ArrayList<ProcessParticipant>());
            
            for (Participant v_PartItem : v_Route.getNextActivity().getParticipants())
            {
                ProcessParticipant v_FuturePart = new ProcessParticipant();
                
                if ( v_PartItem.getObjectType() == ParticipantTypeEnum.$CreateUser )
                {
                    v_FuturePart.init(i_User ,v_Process ,v_PartItem.toCreater(v_NextRoutes.getFlow()));
                }
                else if ( v_PartItem.getObjectType() == ParticipantTypeEnum.$ActivityUser )
                {
                    if ( !Help.isNull(v_OldProcesses) )
                    {
                        v_FuturePart.init(i_User ,v_Process ,v_PartItem.toOperater(v_OldProcesses.get(0)));
                    }
                    else
                    {
                        throw new VerifyError("ServiceDataID[" + i_ServiceDataID + "] to next process is not find $ActivityUser. ActivityCode[" + v_Route.getActivityCode() + "]  ActivityRouteCode[" + i_ActivityRouteCode + "] User[" + i_User.getUserID() + "]");
                    }
                }
                else
                {
                    v_FuturePart.init(i_User ,v_Process ,v_PartItem);
                }
                
                v_Process.getFutureParticipants().add(v_FuturePart);
            }
        }
        // 未来操作人：是动态参与人
        else
        {
            v_Process.setFutureParticipants(v_Process.getParticipants());
        }
        
        boolean v_Ret = this.flowInfoService.toNext(v_Process ,v_Previous);
        
        if ( v_Ret )
        {
            if ( RouteTypeEnum.$Finish    == v_Route.getRouteTypeID()
              || ActivityTypeEnum.$Finish == v_Route.getNextActivity().getActivityTypeID() )
            {
                this.flowInfoService.toHistoryByServiceDataID(i_ServiceDataID);
                this.futureOperatorService.delCache(v_Process);
            }
            else
            {
                this.futureOperatorService.updateCache(v_Process);
            }
            
            return v_Process;
        }
        else
        {
            throw new RuntimeException("ServiceDataID[" + i_ServiceDataID + "] to next process is error. ActivityCode[" + v_Route.getActivityCode() + "]  ActivityRouteCode[" + i_ActivityRouteCode + "] User[" + i_User.getUserID() + "]");
        }
    }
    
    
    
    /**
     * 获取用户可以处理（或叫待办）的工作流实例ID。
     * 
     *   1. 通过用户ID查询
     *   2. 通过部门ID查询
     *   3. 通过角色ID查询，支持多角色。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    public List<String> queryWorkIDs(User i_User)
    {
        return this.futureOperatorService.queryWorkIDs(i_User);
    }
    
    
    
    /**
     * 获取用户可以处理（或叫待办）的工作流实例对应的第三方使用系统的业务数据ID。
     * 
     *   1. 通过用户ID查询
     *   2. 通过部门ID查询
     *   3. 通过角色ID查询，支持多角色。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    public List<String> queryServiceDataIDs(User i_User)
    {
        return this.futureOperatorService.queryServiceDataIDs(i_User);
    }
    
}
