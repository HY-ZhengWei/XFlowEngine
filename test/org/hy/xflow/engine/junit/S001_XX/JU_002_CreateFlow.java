package org.hy.xflow.engine.junit.S001_XX;

import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.Test;





/**
 * 测试单元：创建工作流实例
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-27
 * @version     v1.0
 */
public class JU_002_CreateFlow extends BaseJunit
{
    
    @Test
    public void test001()
    {
        User v_Saler = new User();
        v_Saler.setUserID("8a81b2b54b7b391b014b7d12b66400fc");
        v_Saler.setUserName("公用销售人员");
        v_Saler.addRole("004" ,"销售人员");
        
        XFlowEngine.getInstance().createByName(v_Saler ,"智能选型" ,"SID002");
    }
    
}
