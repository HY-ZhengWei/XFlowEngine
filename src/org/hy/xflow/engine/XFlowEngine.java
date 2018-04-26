package org.hy.xflow.engine;

import org.hy.common.Help;
import org.hy.common.xml.XJava;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.Participant;
import org.hy.xflow.engine.bean.Process;
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
public class XFlowEngine
{
    
    /**
     * 按工作流模板名称创建工作流实例。
     * 
     * 将按模板名称查询版本号最大的有效的模板信息。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     *
     * @param i_TemplateName   工作流模板名称
     * @param i_User           创建人信息
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return                 成功时，返回工作流实例对象。
     *                         异常时，抛出错误。
     */
    public static FlowInfo createByName(String i_TemplateName ,User i_User ,String i_ServiceDataID)
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
        ITemplateService v_TemplateService = (ITemplateService)XJava.getObject("TemplateService");
        Template         v_Template        = v_TemplateService.queryByNameMaxVersionNo(i_TemplateName);
        if ( v_Template == null )
        {
            throw new VerifyError("Template[" + i_TemplateName + "] is not exists.");
        }
        
        // 判定第三方使用系统的业务数据ID是否重复
        if ( !Help.isNull(i_ServiceDataID) )
        {
            IFlowInfoService v_FlowInfoService = (IFlowInfoService)XJava.getObject("FlowInfoService");
            FlowInfo         v_FlowInfo        = v_FlowInfoService.queryByServiceDataID(i_ServiceDataID);
            
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
        
        FlowInfo v_Flow = new FlowInfo(v_Template ,i_User ,i_ServiceDataID);
        
        
        return v_Flow;
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
    }
    
}
