package org.hy.xflow.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.ListMap;
import org.hy.common.PartitionMap;
import org.hy.common.StringHelp;
import org.hy.common.TablePartition;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.ActivityInfo;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.NextRoutes;
import org.hy.xflow.engine.bean.Participant;
import org.hy.xflow.engine.bean.ProcessActivitys;
import org.hy.xflow.engine.bean.ProcessParticipant;
import org.hy.xflow.engine.bean.Template;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.bean.UserParticipant;
import org.hy.xflow.engine.config.InitConfig;
import org.hy.xflow.engine.enums.ActivityTypeEnum;
import org.hy.xflow.engine.enums.ParticipantTypeEnum;
import org.hy.xflow.engine.enums.RejectModeEnum;
import org.hy.xflow.engine.enums.RouteTypeEnum;
import org.hy.xflow.engine.service.IFlowFutureOperatorService;
import org.hy.xflow.engine.service.IFlowInfoService;
import org.hy.xflow.engine.service.IFlowProcessService;
import org.hy.xflow.engine.service.IProcessParticipantsService;
import org.hy.xflow.engine.service.ITemplateService;





/**
 * 工作流引擎
 * 
 * 待办：要做什么
 * 已办：做过什么
 * 督办：监督、协同他人正在做的。抄送一次，全流程随时查
 * 督查：复查督办全流程已完结的
 *
 * @author      ZhengWei(HY)
 * @createDate  2017-03-10
 * @version     v1.0
 *              v1.1  2018-08-21  添加：1.指定流程模板版本号情况下，创建流程实例的createByName()方法。
 *              v1.2  2018-09-05  添加：1.通过工作流流转信息，获取当前活动节点的信息
 *              v2.0  2019-09-12  添加：1.支持多路并行路由的流程
 *                                优化：2.统一所有 toNextByServiceDataID() 系列的方法，均从 toNext() 方法走。
 *              v3.0  2023-02-02  添加：1.查询用户可以走的路由 queryNextRoutes 方法，当未动态指定参与人时，返回模板上定义的参与人
 *              v4.0  2023-06-01  添加：1.督办的相关查询接口
 *                                     2.督查的相关查询接口
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
     * @createDate  2023-02-15
     * @version     v1.0
     *
     * @param i_User      用户
     * @param i_Flow      工作流实例。内部有此实例的所有流转信息
     * @param i_Activity  模板的活动
     * @return
     */
    public static Participant isParticipant(User i_User ,FlowInfo i_Flow ,ActivityInfo i_Activity)
    {
        boolean                           v_IsCreater     = i_User.getUserID().equals(i_Flow.getCreaterID());
        PartitionMap<String ,FlowProcess> v_OldProcessMap = i_Flow.getProcessActivityMap();
        Participant                       v_Participant   = null;
        
        // 是之前工作流流转过程的活动实际操作人时
        v_Participant = whereTo_ParticipantTypeEnum_ActivityUser(i_User ,i_Activity ,v_OldProcessMap);
        if ( v_Participant == null )
        {
            // 为发起人时
            if ( i_Activity.getParticipantByCreater() != null && v_IsCreater )
            {
                v_Participant = i_Activity.getParticipantByCreater();
            }
            else
            {
                v_Participant = i_Activity.isParticipant(i_User);
            }
        }
        
        return v_Participant;
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
     * @param i_Route     活动路由
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
     *              v2.0  2023-02-02  添加：查询用户可以走的路由 queryNextRoutes 方法，当未动态指定参与人时，返回模板上定义的参与人
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
            Map<String ,Object>               v_ObjectKeys    = new HashMap<String ,Object>();  // 防止重复添加参与人
            PartitionMap<String ,FlowProcess> v_OldProcessMap = i_Flow.getProcessActivityMap();
            io_Process.setParticipants(new ArrayList<ProcessParticipant>());
            
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
                    
                    // 将模板上设定的参与人给 工作流流转过程 对象
                    if ( !Help.isNull(i_Activity.getParticipants()) )
                    {
                        for (Participant v_ActivityParticipant : i_Activity.getParticipants())
                        {
                            String v_ObjectKey = v_ActivityParticipant.getObjectType() + "@" + v_ActivityParticipant.getObjectID();
                            if ( !v_ObjectKeys.containsKey(v_ObjectKey) )
                            {
                                io_Process.getParticipants().add(new ProcessParticipant().init(v_ActivityParticipant));
                                v_ObjectKeys.put(v_ObjectKey ,v_ObjectKey);
                            }
                        }
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
                    
                    // 将模板上设定的参与人给 工作流流转过程 对象
                    if ( !Help.isNull(v_Route.getParticipants()) )
                    {
                        for (Participant v_RouteParticipant : v_Route.getParticipants())
                        {
                            String v_ObjectKey = v_RouteParticipant.getObjectType() + "@" + v_RouteParticipant.getObjectID();
                            if ( !v_ObjectKeys.containsKey(v_ObjectKey) )
                            {
                                io_Process.getParticipants().add(new ProcessParticipant().init(v_RouteParticipant));
                                v_ObjectKeys.put(v_ObjectKey ,v_ObjectKey);
                            }
                        }
                    }
                }
            }
            
            v_ObjectKeys.clear();
            v_ObjectKeys = null;
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
     * 通过工作流流转信息，获取当前活动节点的信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-09-05
     * @version     v1.0
     *
     * @param i_Process  工作流流转信息
     * @return
     */
    public ActivityInfo getCurrentActivity(FlowProcess i_Process)
    {
        if ( i_Process == null )
        {
            throw new NullPointerException("FlowProcess is null.");
        }
        if ( Help.isNull(i_Process.getTemplateID()) )
        {
            throw new NullPointerException("TemplateID is null.");
        }
        if ( Help.isNull(i_Process.getCurrentActivityCode()) )
        {
            throw new NullPointerException("CurrentActivityCode is null.");
        }
        
        Template v_Template = this.templateService.queryByID(i_Process.getTemplateID());
        if ( v_Template == null )
        {
            throw new RuntimeException("TemplateID[" + i_Process.getTemplateID() + "] is not exists.");
        }
        
        ActivityInfo v_Activity = v_Template.getActivityRouteTree().getActivity(i_Process.getCurrentActivityCode());
        
        return v_Activity;
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
        return createByName(i_User ,i_TemplateName ,"" ,null);
    }
    
    
    
    /**
     * 按工作流模板名称创建工作流实例。
     * 
     * 将按模板名称查询版本号最大的有效的工作流模板，用它来创建工作流实例。
     * 
     * 创建的工作流实例，当前活动节点为  "开始" 节点。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-08-21
     * @version     v1.0
     *
     * @param i_User           创建人信息
     * @param i_TemplateName   工作流模板名称
     * @param i_VersionNo      模板版本号（其数值是递增型）
     * @return                 成功时，返回工作流实例对象。
     *                         异常时，抛出错误。
     */
    public FlowInfo createByName(User i_User ,String i_TemplateName ,Integer i_VersionNo)
    {
        return createByName(i_User ,i_TemplateName ,"" ,i_VersionNo);
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
        return createByName(i_User ,i_TemplateName ,i_ServiceDataID ,null);
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
     * @param i_VersionNo      模板版本号（其数值是递增型）
     * @return                 成功时，返回工作流实例对象。
     *                         异常时，抛出错误。
     */
    public FlowInfo createByName(User i_User ,String i_TemplateName ,String i_ServiceDataID ,Integer i_VersionNo)
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
        Template v_Template = this.templateService.queryByNameMaxVersionNo(i_TemplateName ,i_VersionNo);
        if ( v_Template == null )
        {
            throw new RuntimeException("Template[" + i_TemplateName + "] is not exists.");
        }
        v_Template = this.templateService.queryByID(v_Template);
        
        // 判定第三方使用系统的业务数据ID是否重复
        if ( !Help.isNull(i_ServiceDataID) )
        {
            FlowInfo v_FlowInfo = this.flowInfoService.queryByServiceDataID(i_ServiceDataID);
            
            if ( v_FlowInfo != null )
            {
                throw new RuntimeException("ServiceDataID[" + i_ServiceDataID + "] is exists.");
            }
        }
        
        // 判定是否为参与人之一
        Participant v_Participant = v_Template.getActivityRouteTree().getStartActivity().isParticipant(i_User);
        if ( v_Participant == null )
        {
            throw new RuntimeException("User[" + i_User.getUserID() + "] is not participants for TemplateName[" + i_TemplateName + "].");
        }
        
        FlowInfo    v_Flow    = new FlowInfo(i_User ,v_Template ,i_ServiceDataID);
        FlowProcess v_Process = new FlowProcess().init_CreateFlow(i_User ,v_Flow ,v_Template.getActivityRouteTree().getStartActivity());
        boolean     v_Ret     = this.flowInfoService.createFlow(v_Flow ,v_Process);
        
        if ( v_Ret )
        {
            if ( !Help.isNull(i_ServiceDataID) )
            {
                this.futureOperatorService.pushSToWorkID(v_Flow.getWorkID() ,i_ServiceDataID);
            }
            
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
     * @param i_User                用户
     * @param i_WorkID              工作流ID
     * @return                      是否有可走的路由，要通过 NextRoutes.getRoutes() 来判定
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
        
        int                                      v_PIndex                 = 0;
        FlowProcess                              v_Process                = null;  // 默认当前流转就在0下标的位置。但时间精度不高、操作及快时，会出现排序规则失效的情况，所以通过下面for处理
        ActivityInfo                             v_Activity               = null;
        PartitionMap<String ,ProcessParticipant> v_AllProcessParts        = null;
        List<ActivityRoute>                      v_WhereTo                = null;
        List<FlowProcess>                        v_OnceTo                 = null;
        ParticipantTypeEnum                      v_QueryerParticipantType = this.futureOperatorService.queryParticipantType(i_User ,i_WorkID);
        for (; v_PIndex < v_FlowInfo.getProcesses().size(); v_PIndex++)
        {
            if ( Help.isNull(v_FlowInfo.getProcesses().get(v_PIndex).getNextProcessID()) )
            {
                v_Process  = v_FlowInfo.getProcesses().get(v_PIndex);
                v_Activity = v_Template.getActivityRouteTree().getActivity(v_Process.getCurrentActivityCode());
                
                // 查询动态参与人
                v_AllProcessParts = processParticipantsService.queryByWorkID(i_WorkID);
                v_Process.setParticipants(v_AllProcessParts.get(v_Process.getProcessID()));
                
                v_WhereTo = whereTo(i_User ,v_FlowInfo ,v_Process ,v_Activity);
                v_OnceTo  = onceTo (i_User ,v_FlowInfo ,v_Process ,v_Activity ,v_Template);
                
                if ( !Help.isNull(v_WhereTo) )
                {
                    if ( !Help.isNull(v_Process.getPreviousOperateTypeID()) )
                    {
                        // 判定：当前流转是否是从“汇总路由”过来的
                        if ( RouteTypeEnum.$ToSum.equals(RouteTypeEnum.get(v_Process.getPreviousOperateTypeID())) )
                        {
                            FlowProcess v_HistorySummary = this.flowProcessService.querySummary(v_Process);
                            String      v_PassType       = v_Activity.getPassType();
                            boolean     v_IsSummaryPass  = false;
                            boolean     v_IsCounterPass  = false;
                            
                            if ( v_HistorySummary.getSummaryPass().doubleValue() > 0 )
                            {
                                if ( v_HistorySummary.getSummary().doubleValue() >= v_HistorySummary.getSummaryPass().doubleValue() )
                                {
                                    v_IsSummaryPass = true;
                                }
                            }
                            
                            if ( v_HistorySummary.getCounterPass().intValue() > 0 )
                            {
                                if ( v_HistorySummary.getCounter().intValue() >= v_HistorySummary.getCounterPass().intValue() )
                                {
                                    v_IsCounterPass = true;
                                }
                            }
                            
                            v_Process.setSummary(    v_HistorySummary.getSummary());
                            v_Process.setSummaryPass(v_HistorySummary.getSummaryPass());
                            v_Process.setCounter(    v_HistorySummary.getCounter());
                            v_Process.setCounterPass(v_HistorySummary.getCounterPass());
                            
                            if ( ("AND".equalsIgnoreCase(v_PassType) && (v_IsSummaryPass && v_IsCounterPass))
                              || ("OR" .equalsIgnoreCase(v_PassType) && (v_IsSummaryPass || v_IsCounterPass)) )
                            {
                                // 汇总通过
                                v_Process.setIsPass(1);
                            }
                            else
                            {
                                // 汇总未通过
                                v_Process.setIsPass(-1);
                                continue;
                                /*
                                return new NextRoutes(v_FlowInfo
                                                     ,v_Process
                                                     ,v_Activity
                                                     ,null
                                                     ,new ArrayList<ActivityRoute>());
                                */
                            }
                        }
                    }
                    
                    return new NextRoutes(v_FlowInfo
                                         ,v_Process
                                         ,v_Activity
                                         ,v_AllProcessParts
                                         ,v_WhereTo
                                         ,v_OnceTo
                                         ,v_QueryerParticipantType);
                }
            }
        }
        if ( v_Process == null )
        {
            throw new NullPointerException("WorkID[" + i_WorkID + "] is not grant to User[" + i_User.getUserID() + "].");
        }
        
        return new NextRoutes(v_FlowInfo
                             ,v_Process
                             ,v_Activity
                             ,v_AllProcessParts
                             ,v_WhereTo
                             ,v_OnceTo
                             ,v_QueryerParticipantType);
    }
    
    
    
    /**
     * 按第三方使用系统的业务数据ID，查询用户可以走的路由。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-02
     * @version     v1.0
     *
     * @param i_User                操作用户
     * @param i_ServiceDataID       第三方使用系统的业务数据ID
     * @return
     */
    public NextRoutes queryNextRoutesByServiceDataID(User i_User ,String i_ServiceDataID)
    {
        String v_WorkID = this.futureOperatorService.querySToWorkID(i_ServiceDataID);
        if ( Help.isNull(v_WorkID) )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not find WorkID.");
        }
        
        return this.queryNextRoutes(i_User ,v_WorkID);
    }
    
    
    
    /**
     * 查询工作流实例曾经流转过的节点
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-14
     * @version     v1.0
     *              v2.0  2023-02-16  删除：与queryNextRoutes()方法整合为一个方法，减少接口数量，减轻开发用户的负担
     *
     * @param i_User    用户
     * @param i_WorkID  工作流ID
     * @return
     */
    @Deprecated
    public ProcessActivitys queryProcessActivitysByServiceDataID(User i_User ,String i_ServiceDataID)
    {
        String v_WorkID = this.futureOperatorService.querySToWorkID(i_ServiceDataID);
        if ( Help.isNull(v_WorkID) )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not find WorkID.");
        }
        
        return this.queryProcessActivitys(i_User ,v_WorkID);
    }
    
    
    
    /**
     * 查询工作流实例曾经流转过的节点
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-01-31
     * @version     v1.0
     *              v2.0  2023-02-16  删除：与queryNextRoutes()方法整合为一个方法，减少接口数量，减轻开发用户的负担
     *
     * @param i_User    用户
     * @param i_WorkID  工作流ID
     * @return
     */
    @Deprecated
    public ProcessActivitys queryProcessActivitys(User i_User ,String i_WorkID)
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
        
        int                                      v_PIndex                 = 0;
        FlowProcess                              v_Process                = null;  // 默认当前流转就在0下标的位置。但时间精度不高、操作及快时，会出现排序规则失效的情况，所以通过下面for处理
        ActivityInfo                             v_Activity               = null;
        PartitionMap<String ,ProcessParticipant> v_AllProcessParts        = null;
        List<FlowProcess>                        v_OnceTo                 = null;
        ParticipantTypeEnum                      v_QueryerParticipantType = this.futureOperatorService.queryParticipantType(i_User ,i_WorkID);
        for (; v_PIndex < v_FlowInfo.getProcesses().size(); v_PIndex++)
        {
            if ( Help.isNull(v_FlowInfo.getProcesses().get(v_PIndex).getNextProcessID()) )
            {
                v_Process  = v_FlowInfo.getProcesses().get(v_PIndex);
                v_Activity = v_Template.getActivityRouteTree().getActivity(v_Process.getCurrentActivityCode());
                
                // 查询动态参与人
                v_AllProcessParts = processParticipantsService.queryByWorkID(i_WorkID);
                v_Process.setParticipants(v_AllProcessParts.get(v_Process.getProcessID()));
                
                v_OnceTo = onceTo(i_User ,v_FlowInfo ,v_Process ,v_Activity ,v_Template);
                
                if ( !Help.isNull(v_OnceTo) )
                {
                    return new ProcessActivitys(v_FlowInfo
                                               ,v_Process
                                               ,v_Activity
                                               ,v_AllProcessParts
                                               ,v_OnceTo
                                               ,v_QueryerParticipantType);
                }
            }
        }
        if ( v_Process == null )
        {
            throw new NullPointerException("WorkID[" + i_WorkID + "] is not grant to User[" + i_User.getUserID() + "].");
        }
        
        return new ProcessActivitys(v_FlowInfo
                                   ,v_Process
                                   ,v_Activity
                                   ,v_AllProcessParts
                                   ,v_OnceTo
                                   ,v_QueryerParticipantType);
    }
    
    
    
    /**
     * 曾经去过哪？配合驳回功能的使用
     * 
     * 不包含：当前活动节点，否则会出现自己流转自己的情况
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-01
     * @version     v1.0
     *
     * @param i_User      用户
     * @param i_Flow      工作流实例。内部有此实例的所有流转信息
     * @param io_Process  流转过程。它上可指定动态参与人。
     * @param i_Activity  当前活动节点
     * @param i_Template  工作流模板
     * @return
     */
    public static List<FlowProcess> onceTo(User i_User ,FlowInfo i_Flow ,FlowProcess io_Process ,ActivityInfo i_Activity ,Template i_Template)
    {
        ListMap<String ,FlowProcess> v_Activitys         = new ListMap<String ,FlowProcess>();
        boolean                      v_IsFindA           = false;                         // 是否已定位到当前活动节点
        String                       v_PreviousProcessID = null;                          // 上一过程ID
        
        for (int v_PIndex=0; v_PIndex < i_Flow.getProcesses().size(); v_PIndex++)         // 时间倒序：最后操作的排在前
        {
            FlowProcess v_FProcess = i_Flow.getProcesses().get(v_PIndex);
            
            // 先定位当前活动节点
            if ( !v_IsFindA )
            {
                if ( i_Activity.getActivityID().equals(v_FProcess.getCurrentActivityID()) )
                {
                    v_PreviousProcessID = v_FProcess.getPreviousProcessID();
                    v_IsFindA           = true;
                }
                
                continue;
            }
            
            // 路由链逐一依次向上查找
            if ( v_PreviousProcessID.equals(v_FProcess.getProcessID()) )
            {
                v_PreviousProcessID = v_FProcess.getPreviousProcessID();
                
                if ( !Help.isNull(v_FProcess.getNextActivityID())                             // 不包含：待办节点
                  && !i_Activity.getActivityID().equals(v_FProcess.getCurrentActivityID()) )  // 不包含：当前活动节点
                {
                    if ( !v_Activitys.containsKey(v_FProcess.getCurrentActivityID()) )        // 防止重复
                    {
                        ActivityInfo v_Activity = i_Template.getActivityRouteTree().getActivity(v_FProcess.getCurrentActivityCode());
                        if ( v_Activity != null )
                        {
                            v_Activitys.put(v_FProcess.getCurrentActivityID() ,v_FProcess);
                        }
                    }
                }
            }
        }
        
        return v_Activitys.getValues();
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
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityRouteCode   走的路由编码
     * @return
     */
    public FlowProcess toNext(User i_User ,String i_WorkID ,FlowProcess i_ProcessExtra ,String i_ActivityRouteCode)
    {
        return this.toNext(i_User ,i_WorkID ,i_ProcessExtra ,i_ActivityRouteCode ,(List<UserParticipant>)null);
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
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityRouteCode   走的路由编码
     * @param i_Participant         指定下一活动的动态参与人，可选项。
     *                              指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                              指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                             但当路由上没有设定参与人时，动态参与人将畅通无阻
     * @return
     */
    public FlowProcess toNext(User i_User ,String i_ServiceDataID ,FlowProcess i_ProcessExtra ,String i_ActivityRouteCode ,UserParticipant i_Participant)
    {
        List<UserParticipant> v_Participants = null;
        
        if ( i_Participant != null )
        {
            v_Participants = new ArrayList<UserParticipant>();
            v_Participants.add(i_Participant);
        }
        
        return this.toNext(i_User ,i_ServiceDataID ,i_ProcessExtra ,i_ActivityRouteCode ,v_Participants);
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
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityRouteCode   走的路由编码
     * @param i_Participants        指定下一活动的动态参与人，可选项。
     *                              指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                              指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                             但当路由上没有设定参与人时，动态参与人将畅通无阻
     * @return
     */
    public FlowProcess toNext(User i_User ,String i_WorkID ,FlowProcess i_ProcessExtra ,String i_ActivityRouteCode ,List<UserParticipant> i_Participants)
    {
        TablePartition<String ,UserParticipant> v_ActivityRouteCodes = new TablePartition<String ,UserParticipant>();
        
        v_ActivityRouteCodes.put(i_ActivityRouteCode ,i_Participants);
        
        List<FlowProcess> v_ProcessList = this.toNext(i_User ,i_WorkID ,i_ProcessExtra ,v_ActivityRouteCodes);
        
        return v_ProcessList.get(0);
    }
    
    
    
    /**
     * 向下一个活动节点流转（支持多路由并发执行）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-12
     * @version     v1.0
     *
     * @param i_User                操作用户
     * @param i_WorkID              工作流ID
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityRouteCodes  走的多个发并路由编码
     * @return
     */
    public FlowProcess toNext(User i_User ,String i_WorkID ,FlowProcess i_ProcessExtra ,String [] i_ActivityRouteCodes)
    {
        TablePartition<String ,UserParticipant> v_ActivityRouteCodes = new TablePartition<String ,UserParticipant>();
        
        for (String v_ActivityRouteCode : i_ActivityRouteCodes)
        {
            v_ActivityRouteCodes.put(v_ActivityRouteCode ,new ArrayList<UserParticipant>(0));
        }
        
        List<FlowProcess> v_ProcessList = this.toNext(i_User ,i_WorkID ,i_ProcessExtra ,v_ActivityRouteCodes);
        
        return v_ProcessList.get(0);
    }
    
    
    
    /**
     * 向下一个活动节点流转（支持多路由并发执行）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-10
     * @version     v1.0
     *
     * @param i_User                操作用户
     * @param i_WorkID              工作流ID
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityRouteCodes  Map.Partition  走的路由编码，
     *                              Map.value      指定每个路由的下一活动的动态参与人，可选项。
     *                                             指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                                             指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                             但当路由上没有设定参与人时，动态参与人将畅通无阻
     * @return
     */
    public List<FlowProcess> toNext(User i_User ,String i_WorkID ,FlowProcess i_ProcessExtra ,PartitionMap<String ,UserParticipant> i_ActivityRouteCodes)
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
        else if ( Help.isNull(i_ActivityRouteCodes) )
        {
            throw new NullPointerException("ActivityRouteCodes is null.");
        }
        
        NextRoutes v_NextRoutes = this.queryNextRoutes(i_User ,i_WorkID);
        if ( Help.isNull(v_NextRoutes.getRoutes()) )
        {
            if ( v_NextRoutes.getCurrentProcess().getIsPass() != null && v_NextRoutes.getCurrentProcess().getIsPass().intValue() == -1 )
            {
                // 未满足汇总条件
                throw new RuntimeException("WorkID[" + i_WorkID + "] is not pass summary to User[" + i_User.getUserID() + "].");
            }
            else
            {
                // 没有可走的路由
                throw new RuntimeException("WorkID[" + i_WorkID + "] is not find route to User[" + i_User.getUserID() + "].");
            }
        }
        Template v_Template = this.templateService.queryByID(v_NextRoutes.getFlow().getFlowTemplateID());
        if ( v_Template == null )
        {
            throw new NullPointerException("Template[" + v_NextRoutes.getFlow().getFlowTemplateID() + "] is not exists. WorkID[" + i_WorkID + "] for User[" + i_User.getUserID() + "]");
        }
        
        FlowProcess         v_Previous    = v_NextRoutes.getCurrentProcess();
        List<FlowProcess>   v_ProcessList = new ArrayList<FlowProcess>();
        List<ActivityRoute> v_RouteList   = new ArrayList<ActivityRoute>();
        for (String v_ActivityRouteCode : i_ActivityRouteCodes.keySet())
        {
            ActivityRoute v_Route = v_Template.getActivityRouteTree().getActivityRoute(v_NextRoutes.getCurrentActivity().getActivityCode() ,v_ActivityRouteCode);
            if ( v_Route == null )
            {
                throw new NullPointerException("ActivityRouteCode[" + v_ActivityRouteCode + "] is not exists. WorkID[" + i_WorkID + "] or User[" + i_User.getUserID() + "]");
            }
            
            if ( i_ActivityRouteCodes.size() > 1 )
            {
                if ( RouteTypeEnum.$ToMany != v_Route.getRouteTypeID() )
                {
                    // 路由类型不是：分派路由时，不允许并发多路路由
                    throw new RuntimeException("ActivityRouteCode[" + v_ActivityRouteCode + "] routeType is not RouteTypeEnum.$ToMany. WorkID[" + i_WorkID + "] or User[" + i_User.getUserID() + "]");
                }
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
                throw new RuntimeException("WorkID[" + i_WorkID + "] to next process is not Route. ActivityCode[" + v_Route.getActivityCode() + "]  ActivityRouteCode[" + v_ActivityRouteCode + "] User[" + i_User.getUserID() + "]");
            }
            
            // 设置动态参与人
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
            List<UserParticipant> v_Participants = i_ActivityRouteCodes.get(v_ActivityRouteCode);
            if ( !Help.isNull(v_Participants) )
            {
                v_Process.setParticipants(new ArrayList<ProcessParticipant>());
                for (int v_Index=0; v_Index<v_Participants.size(); v_Index++)
                {
                    UserParticipant v_UserPart = v_Participants.get(v_Index);
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
                    v_PartTemp.init(i_User ,v_Process ,v_Participants.get(v_Index));
                    v_Process.getParticipants().add(v_PartTemp);
                }
            }
            
            // 驳回的路由
            List<FlowProcess> v_OldProcesses = v_NextRoutes.getFlow().getProcessActivityMap().get(v_Route.getNextActivity().getActivityCode());
            if ( v_Route.getRouteTypeID() == RouteTypeEnum.$Reject )
            {
                if ( Help.isNull(v_OldProcesses) )
                {
                    throw new RuntimeException("WorkID[" + i_WorkID + "] to next process is not reject. ActivityCode[" + v_Route.getActivityCode() + "]  ActivityRouteCode[" + v_ActivityRouteCode + "] User[" + i_User.getUserID() + "]");
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
                
                if ( !Help.isNull(v_Route.getNextActivity().getParticipants()) )
                {
                    String v_SplitProcessID = i_ActivityRouteCodes.size() > 1 ? v_Previous.getProcessID() : "";
                    
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
                                throw new RuntimeException("WorkID[" + i_WorkID + "] to next process is not find $ActivityUser. ActivityCode[" + v_Route.getActivityCode() + "]  ActivityRouteCode[" + v_ActivityRouteCode + "] User[" + i_User.getUserID() + "]");
                            }
                        }
                        else
                        {
                            v_FuturePart.init(i_User ,v_Process ,v_PartItem);
                        }
                        
                        v_FuturePart.setSplitProcessID(v_SplitProcessID);
                        v_Process.getFutureParticipants().add(v_FuturePart);
                    }
                    
                    // 未指定参与人时，默认为工作流模板中定义的参与人
                    v_Process.setParticipants(v_Process.getFutureParticipants());
                }
                else
                {
                    // 走到最后的结束节点，会执行此。
                }
            }
            // 未来操作人：是动态参与人
            else
            {
                v_Process.setFutureParticipants(v_Process.getParticipants());
            }
            
            v_ProcessList.add(v_Process);
            v_RouteList  .add(v_Route);
        }
        
        if ( i_ProcessExtra != null )
        {
            v_Previous.setOperateFiles(Help.NVL(i_ProcessExtra.getOperateFiles()));
            v_Previous.setOperateDatas(Help.NVL(i_ProcessExtra.getOperateDatas()));
            v_Previous.setInfoComment( Help.NVL(i_ProcessExtra.getInfoComment()));
        }
        
        
        // 分单时，对分的多个路由信息用逗号分隔的处理
        if ( v_ProcessList.size() > 1 )
        {
            if ( !Help.isNull(v_Previous.getSplitProcessID()) )
            {
                // 已有多路并行的情况，未闭环前，不能再次发新的多路并行路流转。
                throw new RuntimeException("WorkID[" + i_WorkID + "] is have many SplitProcessID[" + v_Previous.getSplitProcessID() + "] to User[" + i_User.getUserID() + "].");
            }
            
            v_Previous.setNextProcessID   ("");
            v_Previous.setNextActivityID  ("");
            v_Previous.setNextActivityCode("");
            v_Previous.setNextActivityName("");
            
            for (int i=0; i<v_ProcessList.size(); i++)
            {
                FlowProcess v_Process = v_ProcessList.get(i);
                String      v_Split   = i == 0 ? "" : ",";
                
                v_Process.setSplitProcessID(v_Previous.getProcessID());
                
                v_Previous.setNextProcessID   (v_Previous.getNextProcessID()    + v_Split + v_Process.getProcessID());
                v_Previous.setNextActivityID  (v_Previous.getNextActivityID()   + v_Split + v_Process.getCurrentActivityID());
                v_Previous.setNextActivityCode(v_Previous.getNextActivityCode() + v_Split + v_Process.getCurrentActivityCode());
                v_Previous.setNextActivityName(v_Previous.getNextActivityName() + v_Split + v_Process.getCurrentActivityName());
            }
        }
        else
        {
            v_ProcessList.get(0).setSplitProcessID(v_Previous.getSplitProcessID());
            
            if ( !Help.isNull(v_Previous.getSplitProcessID()) )
            {
                ActivityInfo v_SummaryActivity = null;
                if ( RouteTypeEnum.$ToSum.equals(RouteTypeEnum.get(v_Previous.getPreviousOperateTypeID())) )
                {
                    // 汇总活动向下流转，在当前节点就有汇总的条件、约束信息
                    v_SummaryActivity = v_RouteList.get(0).getActivity();
                }
                else
                {
                    // 汇总路由的流转过程中，找下一活动节点即有汇总的条件、约束信息
                    v_SummaryActivity = v_RouteList.get(0).getNextActivity();
                }
                
                // 每个分支路由的汇总值，均记录在操作时间、操作类型、操作动作的那行记录上。
                v_Previous.setSummary(i_ProcessExtra == null ? 0 : Help.NVL(i_ProcessExtra.getSummary()));
                v_Previous.setCounter(i_ProcessExtra == null ? 1 : Help.NVL(i_ProcessExtra.getCounter()));
                v_Previous.setCounter(v_Previous.getCounter() <= 0 ? 1 : v_Previous.getCounter());         // 最小为1，因为操作人也是提交人
                v_Previous.setSummaryPass(v_SummaryActivity.getSummaryPass());
                v_Previous.setCounterPass(v_SummaryActivity.getCounterPass());
                
                FlowProcess v_HistorySummary = this.flowProcessService.querySummary(v_Previous);
                String      v_PassType       = v_SummaryActivity.getPassType();
                boolean     v_IsSummaryPass  = false;
                boolean     v_IsCounterPass  = false;
                
                if ( v_SummaryActivity.getSummaryPass().doubleValue() > 0 )
                {
                    if ( v_HistorySummary.getSummary().doubleValue() + v_Previous.getSummary().doubleValue() >= v_SummaryActivity.getSummaryPass().doubleValue() )
                    {
                        v_IsSummaryPass = true;
                    }
                }
                
                if ( v_SummaryActivity.getCounterPass().intValue() > 0 )
                {
                    if ( v_HistorySummary.getCounter().intValue() + v_Previous.getCounter().intValue() >= v_SummaryActivity.getCounterPass().intValue() )
                    {
                        v_IsCounterPass = true;
                    }
                }
                
                if ( ("AND".equalsIgnoreCase(v_PassType) && (v_IsSummaryPass && v_IsCounterPass))
                  || ("OR" .equalsIgnoreCase(v_PassType) && (v_IsSummaryPass || v_IsCounterPass)) )
                {
                    // 汇总通过
                    v_Previous.setIsPass(1);
                    
                    if ( RouteTypeEnum.$ToSum.equals(RouteTypeEnum.get(v_Previous.getPreviousOperateTypeID())) )
                    {
                        // 汇总节点向后继续流转时
                        v_Previous.setSummary(    v_HistorySummary.getSummary());
                        v_Previous.setSummaryPass(v_HistorySummary.getSummaryPass());
                        v_Previous.setCounter(    v_HistorySummary.getCounter());
                        v_Previous.setCounterPass(v_HistorySummary.getCounterPass());
                        
                        v_ProcessList.get(0).setSplitProcessID("");
                    }
                }
                else
                {
                    // 只有转流的路由是“汇总”路由时
                    if ( RouteTypeEnum.$ToSum.equals(v_RouteList.get(0).getRouteTypeID()) )
                    {
                        // 汇总未通过时，删除未来操作人
                        for (FlowProcess v_FPItem : v_ProcessList)
                        {
                            v_FPItem.setFutureParticipants(new ArrayList<ProcessParticipant>());
                        }
                    }
                }
            }
        }
        
        
        FlowInfo v_Flow = new FlowInfo();
        
        v_Flow.setWorkID(       v_Previous.getWorkID());
        v_Flow.setLastProcessID(v_ProcessList.get(0).getProcessID());
        v_Flow.setLastTime(new Date());
        v_Flow.setLastUserID(       i_User.getUserID());
        v_Flow.setLastUser(Help.NVL(i_User.getUserName()));
        v_Flow.setLastOrgID(        i_User.getOrgID());
        v_Flow.setLastOrg(Help.NVL( i_User.getOrgName()));
        
        boolean v_Ret = this.flowInfoService.toNext(v_Flow ,v_ProcessList ,v_Previous);
        
        if ( v_Ret )
        {
            if ( Help.isNull(v_Previous.getSplitProcessID()) || (v_Previous.getIsPass() != null && 1 == v_Previous.getIsPass().intValue()) )
            {
                for (ActivityRoute v_Route : v_RouteList)
                {
                    if ( RouteTypeEnum.$Finish    == v_Route.getRouteTypeID()
                      || ActivityTypeEnum.$Finish == v_Route.getNextActivity().getActivityTypeID() )
                    {
                        this.flowInfoService.toHistory(i_WorkID);
                        this.futureOperatorService.delCacheToHistory(v_ProcessList.get(0));
                        
                        return v_ProcessList;
                    }
                }
            }
            
            for (FlowProcess v_Process : v_ProcessList)
            {
                this.futureOperatorService.updateCache(v_Process);
            }
            
            return v_ProcessList;
        }
        else
        {
            throw new RuntimeException("WorkID[" + i_WorkID + "] to next process is error. ActivityCode["
                                     + StringHelp.toString(Help.toList(v_RouteList ,"activityCode"))
                                     + "]  ActivityRouteCode[" + StringHelp.toString(Help.toListKeys(i_ActivityRouteCodes))
                                     + "] User[" + i_User.getUserID() + "]");
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
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityRouteCode   走的路由编码
     * @return
     */
    public FlowProcess toNextByServiceDataID(User i_User ,String i_ServiceDataID ,FlowProcess i_ProcessExtra ,String i_ActivityRouteCode)
    {
        return this.toNextByServiceDataID(i_User ,i_ServiceDataID ,i_ProcessExtra ,i_ActivityRouteCode ,(List<UserParticipant>)null);
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
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityRouteCode   走的路由编码
     * @param i_Participant         指定下一活动的动态参与人，可选项。
     *                              指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                              指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                             但当路由上没有设定参与人时，动态参与人将畅通无阻
     * @return
     */
    public FlowProcess toNextByServiceDataID(User i_User ,String i_ServiceDataID ,FlowProcess i_ProcessExtra ,String i_ActivityRouteCode ,UserParticipant i_Participant)
    {
        String v_WorkID = this.futureOperatorService.querySToWorkID(i_ServiceDataID);
        if ( Help.isNull(v_WorkID) )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not find WorkID.");
        }
        
        return this.toNext(i_User ,v_WorkID ,i_ProcessExtra ,i_ActivityRouteCode ,i_Participant);
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
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityRouteCode   走的路由编码
     * @param i_Participants        指定下一活动的动态参与人，可选项。
     *                              指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                              指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                             但当路由上没有设定参与人时，动态参与人将畅通无阻。
     * @return
     */
    public FlowProcess toNextByServiceDataID(User i_User ,String i_ServiceDataID ,FlowProcess i_ProcessExtra ,String i_ActivityRouteCode ,List<UserParticipant> i_Participants)
    {
        String v_WorkID = this.futureOperatorService.querySToWorkID(i_ServiceDataID);
        if ( Help.isNull(v_WorkID) )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not find WorkID.");
        }
        
        return this.toNext(i_User ,v_WorkID ,i_ProcessExtra ,i_ActivityRouteCode ,i_Participants);
    }
    
    
    
    /**
     * 向下一个活动节点流转（支持多路由并发执行）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-12
     * @version     v1.0
     *
     * @param i_User                操作用户
     * @param i_ServiceDataID       第三方使用系统的业务数据ID
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityRouteCodes  走的多个发并路由编码
     * @return
     */
    public FlowProcess toNextByServiceDataID(User i_User ,String i_ServiceDataID ,FlowProcess i_ProcessExtra ,String [] i_ActivityRouteCodes)
    {
        String v_WorkID = this.futureOperatorService.querySToWorkID(i_ServiceDataID);
        if ( Help.isNull(v_WorkID) )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not find WorkID.");
        }
        
        return this.toNext(i_User ,v_WorkID ,i_ProcessExtra ,i_ActivityRouteCodes);
    }
    
    
    
    /**
     * 向下一个活动节点流转（支持多路由并发执行）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-10
     * @version     v1.0
     *
     * @param i_User                操作用户
     * @param i_ServiceDataID       第三方使用系统的业务数据ID
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityRouteCodes  Map.Partition  走的路由编码，
     *                              Map.value      指定每个路由的下一活动的动态参与人，可选项。
     *                                             指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                                             指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                             但当路由上没有设定参与人时，动态参与人将畅通无阻
     * @return
     */
    public List<FlowProcess> toNextByServiceDataID(User i_User ,String i_ServiceDataID ,FlowProcess i_ProcessExtra ,PartitionMap<String ,UserParticipant> i_ActivityRouteCodes)
    {
        String v_WorkID = this.futureOperatorService.querySToWorkID(i_ServiceDataID);
        if ( Help.isNull(v_WorkID) )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not find WorkID.");
        }
        
        return this.toNext(i_User ,v_WorkID ,i_ProcessExtra ,i_ActivityRouteCodes);
    }
    
    
    
    /**
     * 自由驳回（未在工作流模板上预先配置驳回路由）（支持多路由并发执行）
     * 
     * 模板上预先配置驳回路由的方式：可用toNext()方法。
     * 自由驳回是对toNext()方法的专项定制扩展，允许未在模板上定义驳回路由
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-15
     * @version     v1.0
     *
     * @param i_User                操作用户
     * @param i_WorkID              工作流ID
     * @param i_RejectMode          驳回模式
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityCode        驳回到的活动节点的编码
     * @return
     */
    public List<FlowProcess> toReject(User i_User ,String i_WorkID ,RejectModeEnum i_RejectMode ,FlowProcess i_ProcessExtra ,String i_ActivityCode)
    {
        TablePartition<String ,UserParticipant> v_ActivityCodes = new TablePartition<String ,UserParticipant>();
        
        v_ActivityCodes.putRow(i_ActivityCode ,null);
        
        return this.toReject(i_User ,i_WorkID ,i_RejectMode ,i_ProcessExtra ,v_ActivityCodes);
    }
    
    
    
    /**
     * 自由驳回（未在工作流模板上预先配置驳回路由）（支持多路由并发执行）
     * 
     * 模板上预先配置驳回路由的方式：可用toNext()方法。
     * 自由驳回是对toNext()方法的专项定制扩展，允许未在模板上定义驳回路由
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-15
     * @version     v1.0
     *
     * @param i_User                操作用户
     * @param i_ServiceDataID       第三方使用系统的业务数据ID
     * @param i_RejectMode          驳回模式
     * @param i_ProcessExtra        流转的附加信息。非必填
     * @param i_ActivityCode        驳回到的活动节点的编码
     * @return
     */
    public List<FlowProcess> toRejectByServiceDataID(User i_User ,String i_ServiceDataID ,RejectModeEnum i_RejectMode ,FlowProcess i_ProcessExtra ,String i_ActivityCode)
    {
        String v_WorkID = this.futureOperatorService.querySToWorkID(i_ServiceDataID);
        if ( Help.isNull(v_WorkID) )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not find WorkID.");
        }
        
        return toReject(i_User ,v_WorkID ,i_RejectMode ,i_ProcessExtra ,i_ActivityCode);
    }
    
    
    
    /**
     * 自由驳回（未在工作流模板上预先配置驳回路由）（支持多路由并发执行）
     * 
     * 模板上预先配置驳回路由的方式：可用toNext()方法。
     * 自由驳回是对toNext()方法的专项定制扩展，允许未在模板上定义驳回路由
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-15
     * @version     v1.0
     *
     * @param i_User           操作用户
     * @param i_ServiceDataID  第三方使用系统的业务数据ID
     * @param i_RejectMode     驳回模式
     * @param i_ProcessExtra   流转的附加信息。非必填
     * @param i_ActivityUsers  Map.Partition  曾经走过的活动节点的编码，也是准备驳回到活动节点
     *                         Map.value      指定每个路由的下一活动的动态参与人，可选项。
     *                                        指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                                        指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                        但当路由上没有设定参与人时，动态参与人将畅通无阻
     * @return
     */
    public List<FlowProcess> toRejectByServiceDataID(User i_User ,String i_ServiceDataID ,RejectModeEnum i_RejectMode ,FlowProcess i_ProcessExtra ,PartitionMap<String ,UserParticipant> i_ActivityUsers)
    {
        String v_WorkID = this.futureOperatorService.querySToWorkID(i_ServiceDataID);
        if ( Help.isNull(v_WorkID) )
        {
            throw new NullPointerException("ServiceDataID[" + i_ServiceDataID + "] is not find WorkID.");
        }
        
        return toReject(i_User ,v_WorkID ,i_RejectMode ,i_ProcessExtra ,i_ActivityUsers);
    }
    
    
    
    /**
     * 自由驳回（未在工作流模板上预先配置驳回路由）（支持多路由并发执行）
     * 
     * 模板上预先配置驳回路由的方式：可用toNext()方法。
     * 自由驳回是对toNext()方法的专项定制扩展，允许未在模板上定义驳回路由
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-15
     * @version     v1.0
     *
     * @param i_User           操作用户
     * @param i_WorkID         工作流ID
     * @param i_RejectMode     驳回模式
     * @param i_ProcessExtra   流转的附加信息。非必填
     * @param i_ActivityUsers  Map.Partition  曾经走过的活动节点的编码，也是准备驳回到活动节点
     *                         Map.value      指定每个路由的下一活动的动态参与人，可选项。
     *                                        指定动态参与人时，其级别高于活动设定的参与人，即活动上设定的参与人将失效。
     *                                        指定动态参与人时，同时也受限于路由上设定的参与人。
     *                                        但当路由上没有设定参与人时，动态参与人将畅通无阻
     * @return
     */
    public List<FlowProcess> toReject(User i_User ,String i_WorkID ,RejectModeEnum i_RejectMode ,FlowProcess i_ProcessExtra ,PartitionMap<String ,UserParticipant> i_ActivityUsers)
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
        else if ( Help.isNull(i_ActivityUsers) )
        {
            throw new NullPointerException("ActivityUsers is null.");
        }
        
        NextRoutes v_NextRoutes = this.queryNextRoutes(i_User ,i_WorkID);
        if ( Help.isNull(v_NextRoutes.getActivitys()) )
        {
            throw new RuntimeException("WorkID[" + i_WorkID + "] is not any reject activity for User [" + i_User.getUserID() + "].");
        }
        Template v_Template = this.templateService.queryByID(v_NextRoutes.getFlow().getFlowTemplateID());
        if ( v_Template == null )
        {
            throw new NullPointerException("Template[" + v_NextRoutes.getFlow().getFlowTemplateID() + "] is not exists. WorkID[" + i_WorkID + "] for User[" + i_User.getUserID() + "]");
        }
        
        // 判定当前接口操作人，是否为参与人
        Participant v_Participant = isParticipant(i_User ,v_NextRoutes.getFlow() ,v_NextRoutes.getCurrentActivity());
        if ( v_Participant == null )
        {
            throw new NullPointerException("WorkID[" + i_WorkID + "] is not grant to User[" + i_User.getUserID() + "].");
        }
        
        List<FlowProcess> v_ProcessList = new ArrayList<FlowProcess>();
        boolean           v_RejectTeam  = false;
        for (String v_ActivityCode : i_ActivityUsers.keySet())
        {
            ActivityInfo v_Activity = v_Template.getActivityRouteTree().getActivity(v_ActivityCode);
            if ( v_Activity == null )
            {
                // 没有找到活动节点
                throw new RuntimeException("WorkID[" + i_WorkID + "] is not find activity[" + v_ActivityCode + "] for User [" + i_User.getUserID() + "].");
            }
            
            FlowProcess v_Process     = null;
            boolean     v_IsPassMany  = false;    // 驳回目地节点是否经过分派节点
            boolean     v_IsMany      = false;    // 驳回目地节点是否就为分派节点
            for (FlowProcess v_PItem : v_NextRoutes.getActivitys())
            {
                boolean v_ToMany = false;
                if ( !v_IsPassMany && RouteTypeEnum.$ToMany.getValue().equalsIgnoreCase(v_PItem.getOperateTypeID()) )
                {
                    v_IsPassMany  = true;
                    v_ToMany      = true;
                }
                
                if ( v_ActivityCode.equals(v_PItem.getCurrentActivityCode()) )
                {
                    v_Process = v_PItem;
                    v_IsMany  = v_ToMany;
                    break;
                }
            }
            
            if ( v_Process == null )
            {
                // 没有找到曾经流转过的活动节点
                throw new RuntimeException("WorkID[" + i_WorkID + "] is not allow reject activity[" + v_ActivityCode + "] for User [" + i_User.getUserID() + "].");
            }
            
            List<UserParticipant> v_UserParticipants = i_ActivityUsers.get(v_ActivityCode);
            if ( !Help.isNull(v_UserParticipants) )
            {
                // TODO: 放在之后实现，看是否有真正需求而定再开发
            }
            
            FlowProcess v_NewProcess = new FlowProcess();
            v_NewProcess.init_ToReject(i_User ,v_NextRoutes.getFlow() ,v_NextRoutes.getCurrentProcess() ,v_Activity);
            
            // 未来操作人：是驳回节点的当时的实际操作人
            UserParticipant    v_UserParticipant = new UserParticipant();
            ProcessParticipant v_FuturePart      = new ProcessParticipant();
            
            v_UserParticipant.setObjectID(  v_Process.getOperateUserID());   // 用当时操作人为本次驳回的未来操作人：即驳回给当初操作人处理
            v_UserParticipant.setObjectName(v_Process.getOperateUser());
            v_UserParticipant.setObjectType(ParticipantTypeEnum.$User);
            v_UserParticipant.setObjectNo(0);
            v_FuturePart.init(i_User ,v_NewProcess ,v_UserParticipant);
            
            v_NewProcess.setFutureParticipants(new ArrayList<ProcessParticipant>());
            v_NewProcess.getFutureParticipants().add(v_FuturePart);
            
            // 驳回目地节点是否经过分派节点
            if ( v_IsPassMany )
            {
                // 协同多条路由线路一同驳回
                // 情况1：接口要求协同模式
                // 情况2：回目地节点经过分派节点，无论是否为协同模式均要按协同模式处理
                // 情况3：当前节点为汇签，无论是否为协同模式均要按协同模式处理
                if ( RejectModeEnum.$Team == i_RejectMode
                  || !v_IsMany
                  || RouteTypeEnum.$ToSum.getValue().equalsIgnoreCase(v_NextRoutes.getCurrentProcess().getPreviousOperateTypeID()) )
                {
                    v_NewProcess.setSplitProcessID("");  // 清除之前的分单ID
                    v_NewProcess.setPreviousOperateTypeID(            RouteTypeEnum.$Reject_Team.getValue());
                    v_NextRoutes.getCurrentProcess().setOperateTypeID(RouteTypeEnum.$Reject_Team.getValue());
                    v_NextRoutes.getCurrentProcess().setOperateType(  RouteTypeEnum.$Reject_Team.getDesc());
                    v_ProcessList.clear();               // 清空之前的添加的流转信息
                    v_ProcessList.add(v_NewProcess);
                    v_RejectTeam = true;
                    break;
                }
            }
            
            v_ProcessList.add(v_NewProcess);
        }
        
        
        // 驳回原因或附加数据，应当记录到当前流转中，而不是下一条流转中
        if ( i_ProcessExtra != null )
        {
            v_NextRoutes.getCurrentProcess().setOperateFiles(Help.NVL(i_ProcessExtra.getOperateFiles()));
            v_NextRoutes.getCurrentProcess().setOperateDatas(Help.NVL(i_ProcessExtra.getOperateDatas()));
            v_NextRoutes.getCurrentProcess().setInfoComment( Help.NVL(i_ProcessExtra.getInfoComment()));
        }
        
        
        FlowInfo v_Flow = new FlowInfo();
        
        v_Flow.setWorkID(       v_NextRoutes.getCurrentProcess().getWorkID());
        v_Flow.setLastProcessID(v_NextRoutes.getCurrentProcess().getProcessID());
        v_Flow.setLastTime(new Date());
        v_Flow.setLastUserID(       i_User.getUserID());
        v_Flow.setLastUser(Help.NVL(i_User.getUserName()));
        v_Flow.setLastOrgID(        i_User.getOrgID());
        v_Flow.setLastOrg(Help.NVL( i_User.getOrgName()));
        
        boolean v_Ret = this.flowInfoService.toNext(v_Flow ,v_ProcessList ,v_NextRoutes.getCurrentProcess());
        
        if ( v_Ret )
        {
            if ( v_RejectTeam )
            {
                this.futureOperatorService.delCacheByAll(v_ProcessList.get(0));
                this.futureOperatorService.addCache(     v_ProcessList.get(0));
            }
            else
            {
                for (FlowProcess v_Process : v_ProcessList)
                {
                    this.futureOperatorService.updateCache(v_Process);
                }
            }
            
            return v_ProcessList;
        }
        else
        {
            throw new RuntimeException("WorkID[" + i_WorkID + "] to reject process is error. CurrentActivityCode["
                                     + v_NextRoutes.getCurrentActivity().getActivityCode()
                                     + "]  RejectActivityCode[" + StringHelp.toString(Help.toListKeys(i_ActivityUsers))
                                     + "] User[" + i_User.getUserID() + "]");
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
    
    
    
    /**
     * 获取用户已处理过的工作流实例ID。
     * 
     *   1. 通过用户ID查询
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-06-11
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    public List<String> queryWorkIDsByDone(User i_User)
    {
        return this.flowProcessService.queryWorkIDsByDone(i_User);
    }
    
    
    
    /**
     * 获取用户已处理过的工作流实例对应的第三方使用系统的业务数据ID。
     * 
     *   1. 通过用户ID查询
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-06-11
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    public List<String> queryServiceDataIDsByDone(User i_User)
    {
        return this.flowProcessService.queryServiceDataIDsByDone(i_User);
    }
    
    
    
    /**
     * 获取用户督办（抄送的）的工作流实例ID。
     * 
     *   1. 通过用户ID查询抄送
     *   2. 通过部门ID查询抄送
     *   3. 通过角色ID查询抄送，支持多角色。
     * 
     * 思路：
     *     1. 查流程模板上配置的抄送信息，即默认抄送信息
     *     2. 查流程实例上指定的抄送信息，即指定抄送信息
     *     3. 指定抄送>默认抄送，整合为流程实例的"全流程抄送"信息
     *     4. 通过请求参数查询"全流程抄送"信息
     *     5. 任意节点抄送一次即为督办
     *     6. 督办的生命周期是：开始于抄送，完成在流程实例的结束
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-01
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> querySuperviseWorkIDs(User i_User)
    {
        return (List<String>) Help.toList(this.processParticipantsService.queryBySupervise(i_User) ,"workID");
    }
    
    
    
    /**
     * 获取用户督办（抄送的）的工作流实例对应的第三方使用系统的业务数据ID。
     * 
     *   1. 通过用户ID查询抄送
     *   2. 通过部门ID查询抄送
     *   3. 通过角色ID查询抄送，支持多角色。。
     * 
     * 思路：
     *     1. 查流程模板上配置的抄送信息，即默认抄送信息
     *     2. 查流程实例上指定的抄送信息，即指定抄送信息
     *     3. 指定抄送>默认抄送，整合为流程实例的"全流程抄送"信息
     *     4. 通过请求参数查询"全流程抄送"信息
     *     5. 任意节点抄送一次即为督办
     *     6. 督办的生命周期是：开始于抄送，完成在流程实例的结束
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-01
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> querySuperviseServiceDataIDs(User i_User)
    {
        return (List<String>) Help.toList(this.processParticipantsService.queryBySupervise(i_User) ,"serviceDataID");
    }
    
    
    
    /**
     * 获取用户历史督办（抄送的）的已完结的工作流实例ID。
     * 
     *   1. 通过用户ID查询抄送
     *   2. 通过部门ID查询抄送
     *   3. 通过角色ID查询抄送，支持多角色。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-01
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> queryBySupervisionWorkIDs(User i_User)
    {
        return (List<String>) Help.toList(this.processParticipantsService.queryBySupervision(i_User) ,"workID");
    }
    
    
    
    /**
     * 获取用户历史督办（抄送的）的已完结的工作流实例ID对应的第三方使用系统的业务数据ID。
     * 
     *   1. 通过用户ID查询抄送
     *   2. 通过部门ID查询抄送
     *   3. 通过角色ID查询抄送，支持多角色。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-01
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<String> queryBySupervisionServiceDataIDs(User i_User)
    {
        return (List<String>) Help.toList(this.processParticipantsService.queryBySupervision(i_User) ,"serviceDataID");
    }
    
    
    
    /**
     * 查询某一工作流模板下的所有活动的工作流实例。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-08-29
     * @version     v1.0
     *
     * @param i_TemplateID  工作流模板ID
     * @return
     */
    public List<FlowInfo> queryActivitys(String i_TemplateID)
    {
        return this.flowInfoService.queryActivitys(i_TemplateID);
    }
    
    
    
    /**
     * 查询历次的汇总情况。首次为最新的流转（即按时间顺序倒排的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-18
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    public List<FlowProcess> querySummarysByWorkID(String i_WorkID)
    {
        return this.flowProcessService.querySummarysByWorkID(i_WorkID);
    }
    
    
    
    /**
     * 查询历次的汇总情况。首次为最新的流转（即按时间顺序倒排的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-18
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    public List<FlowProcess> querySummarysByServiceDataID(String i_ServiceDataID)
    {
        return this.flowProcessService.querySummarysByServiceDataID(i_ServiceDataID);
    }
    
}
