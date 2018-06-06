package org.hy.xflow.engine.junit;

import org.hy.common.xml.XJava;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.Test;





/**
 * 测试单元：Insert...Select语句 
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-06-06
 * @version     v1.0
 */
public class JU_XSQL_Insert_Select extends BaseJunit
{
    
    @Test
    public void test()
    {
        FlowInfo v_Flow = new FlowInfo();
        
        v_Flow.setServiceDataID("1234567890");
        
        System.out.println(XJava.getXSQL("XSQL_XFlow_FlowInfo_ToHistory").getContent().getSQL(v_Flow));
    }
    
}
