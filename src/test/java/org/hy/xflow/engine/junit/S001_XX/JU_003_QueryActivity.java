package org.hy.xflow.engine.junit.S001_XX;

import org.hy.common.xml.XJava;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.common.BaseJunit;
import org.hy.xflow.engine.service.IFlowInfoService;
import org.junit.Test;





/**
 * 测试单元：查询活动
 * 
 * @author      ZhengWei(HY)
 * @createDate  2018-04-28
 * @version     v1.0
 */
public class JU_003_QueryActivity extends BaseJunit
{
    
    @Test
    public void test001()
    {
        IFlowInfoService v_FlowInfoService = (IFlowInfoService)XJava.getObject("FlowInfoService");
        
        FlowInfo v_Flow = v_FlowInfoService.queryByWorkID("XFA117B5368FE84675B7E23FE2CE724B0F");
        v_Flow = v_FlowInfoService.queryByServiceDataID("SID001");
        
        System.out.println("工作流ID：" + v_Flow.getWorkID());
    }
    
}
