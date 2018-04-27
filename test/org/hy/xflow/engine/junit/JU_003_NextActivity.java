package org.hy.xflow.engine.junit;

import org.hy.common.xml.XJava;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.hy.xflow.engine.service.IFlowInfoService;
import org.junit.Test;





/**
 * 测试单元：下一个活动
 */
public class JU_003_NextActivity extends BaseJunit
{
    
    @Test
    public void test001()
    {
        User v_Manager = new User();
        v_Manager.setUserID("E10ADC3949BA59ABBE56E057F20F922E");
        v_Manager.setUserName("霍桂霞");
        v_Manager.setRoleID("001");
        v_Manager.setRoleName("选型主管");
        
        
        IFlowInfoService v_FlowInfoService = (IFlowInfoService)XJava.getObject("FlowInfoService");
        
        FlowInfo v_Flow = v_FlowInfoService.queryByWorkID("0BB55D07C25943309DBAD806FA8132BB");
        v_Flow = v_FlowInfoService.queryByServiceDataID("SID001");
        
        v_Flow.getWorkID();
    }
    
}
