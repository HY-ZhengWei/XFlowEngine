package org.hy.xflow.engine;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Date;
import org.hy.common.Help;
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
import org.hy.xflow.engine.bean.ProcessParticipant;
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
 *              v1.1  2018-08-21  添加：1.指定流程模板版本号情况下，创建流程实例的createByName()方法。
 *              v1.2  2018-09-05  添加：1.通过工作流流转信息，获取当前活动节点的信息
 *              v2.0  2019-09-12  添加：1.支持多路并行路由的流程
 *                                优化：2.统一所有 toNextByServiceDataID() 系列的方法，均从 toNext() 方法走。
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
     * @param i_ProcessExtra        流转的附加信息。非必填
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
        
        int                                      v_PIndex          = 0;
        FlowProcess                              v_Process         = null;  // 默认当前流转就在0下标的位置。但时间精度不高、操作及快时，会出现排序规则失效的情况，所以通过下面for处理
        ActivityInfo                             v_Activity        = null;
        PartitionMap<String ,ProcessParticipant> v_AllProcessParts = null;
        List<ActivityRoute>                      v_WhereTo         = null;
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
                                         ,v_WhereTo);
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
                             ,v_WhereTo);
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
     * @param i_ActivityRouteCode   走的路由编码
     * @return
     */
    public FlowProcess toNextByServiceDataID(User i_User ,String i_ServiceDataID ,FlowProcess i_Process ,String i_ActivityRouteCode)
    {
        return this.toNextByServiceDataID(i_User ,i_ServiceDataID ,i_Process ,i_ActivityRouteCode ,(List<UserParticipant>)null);
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
