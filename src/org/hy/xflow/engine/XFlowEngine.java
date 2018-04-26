package org.hy.xflow.engine;

import org.hy.common.Help;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.Participant;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.Template;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.config.InitConfig;
import org.hy.xflow.engine.service.IFlowInfoService;
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
    private ITemplateService templateService;
    
    @Xjava
    private IFlowInfoService flowInfoService;
    
    
    
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
        if ( Help.isNull(i_TemplateName) )
        {
            throw new NullPointerException("Template name is null.");
        }
        else if ( i_User == null )
        {
            throw new NullPointerException("User is null.");
        }
        else if ( Help.isNull(i_User.getUserID()) )
        {
            throw new NullPointerException("UserID is null.");
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
    
    
    
    public static void main(String [] args)
    {
        new InitConfig();
        
        ITemplateService v_TemplateService = (ITemplateService)XJava.getObject("TemplateService");
        Template         v_Template        = v_TemplateService.queryByID("T001");
        
        System.out.println(Help.toSQLInsert(Process.class ,false));
        
        v_Template.getActivityRouteTree().getStartActivity();
        v_Template.getActivityRouteTree().getActivity("A001");  // 发起选型
        v_Template.getActivityRouteTree().getActivity("A002");  // 选型受理
        v_Template.getActivityRouteTree().getActivity("A003");  // 选型评审
        v_Template.getActivityRouteTree().getActivity("A004");  // 单人选型
        v_Template.getActivityRouteTree().getActivity("A005");  // 选型分包
        v_Template.getActivityRouteTree().getActivity("A006");  // 多人选型
        v_Template.getActivityRouteTree().getActivity("A007");  // 选型汇总
        v_Template.getActivityRouteTree().getActivity("A008");  // 选型结果确认
        
        
        
        User v_Saler = new User();
        v_Saler.setUserID("8a81b2b54b7b391b014b7d12b66400fc");
        v_Saler.setUserName("公用销售人员");
        v_Saler.setRoleID("004");
        v_Saler.setRoleName("销售人员");
        
        User v_Manager = new User();
        v_Manager.setUserID("E10ADC3949BA59ABBE56E057F20F922E");
        v_Manager.setUserName("霍桂霞");
        v_Manager.setRoleID("001");
        v_Manager.setRoleName("选型主管");
        
        XFlowEngine.getInstance().createByName(v_Saler ,"智能选型");
    }
    
}
