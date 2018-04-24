package org.hy.xflow.engine;

import org.hy.common.xml.XJava;
import org.hy.xflow.engine.bean.Template;
import org.hy.xflow.engine.config.InitConfig;
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
    
    public static void main(String [] args)
    {
        new InitConfig();
        
        ITemplateService v_TemplateService = (ITemplateService)XJava.getObject("TemplateService");
        Template         v_Template        = v_TemplateService.queryByID("T001");
        
        
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
