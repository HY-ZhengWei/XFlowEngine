package org.hy.xflow.engine.junit;

import java.util.HashMap;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.xml.XJava;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.Template;
import org.hy.xflow.engine.common.BaseJunit;
import org.hy.xflow.engine.dao.ITemplateDAO;
import org.hy.xflow.engine.service.ITemplateService;
import org.junit.Test;





/**
 * 测试单元：工作流模板测试
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-27
 * @version     v1.0
 */
public class JU_001_Template extends BaseJunit
{
    
    @Test
    public void test001()
    {
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
        
        
        Map<String ,Long> v_Map = new HashMap<String ,Long>(); 
        v_Map.put("templateID" ,1L);
        v_Template = ((ITemplateDAO)XJava.getObject("TemplateDAO")).queryByID(v_Map);
    }
    
    
    
    /**
     * 自动生成SQL语句
     */
    @Test
    public void test002()
    {
        System.out.println(Help.toSQLSelect(FlowProcess.class ,"A" ,false));
    }
    
}
