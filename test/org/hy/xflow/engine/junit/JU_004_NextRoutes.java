package org.hy.xflow.engine.junit;

import java.util.List;

import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.Test;





/**
 * 测试单元：下一活动
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-02
 * @version     v1.0
 */
public class JU_004_NextRoutes extends BaseJunit
{
    
    @Test
    public void test001()
    {
        User v_Manager = new User();
        v_Manager.setUserID("E10ADC3949BA59ABBE56E057F20F922E");
        v_Manager.setUserName("霍桂霞");
        v_Manager.setRoleID("001");
        v_Manager.setRoleName("选型主管");
        
        User v_Saler = new User();
        v_Saler.setUserID("8a81b2b54b7b391b014b7d12b66400fc");
        v_Saler.setUserName("公用销售人员");
        v_Saler.setRoleID("004");
        v_Saler.setRoleName("销售人员");
        
        List<ActivityRoute> v_Routes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(v_Saler ,"SID001");
        
        System.out.println(v_Routes.size());
    }
    
}
