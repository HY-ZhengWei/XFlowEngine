package org.hy.xflow.engine.junit.S001_XX;

import java.util.List;

import org.hy.common.Help;
import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.Test;





/**
 * 测试单元：流转到完成
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-09
 * @version     v1.0
 */
public class JU_999_ToFinisth extends BaseJunit
{
    
    @Test
    public void test_001()
    {
        User v_Saler = new User();
        v_Saler.setUserID("8a81b2b54b7b391b014b7d12b66400fc");
        v_Saler.setUserName("公用销售人员");
        v_Saler.addRole("004" ,"销售人员");
        
        String v_ServiceDataID = "SID001";
        
        List<ActivityRoute> v_Routes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(v_Saler ,v_ServiceDataID);
        if ( Help.isNull(v_Routes) )
        {
            System.out.println("-- [" + v_Saler.getUserName()+ "]没有任何可操作的路由");
        }
        else
        {
            ActivityRoute v_Route = v_Routes.get(2);
            
            XFlowEngine.getInstance().toNextByServiceDataID(v_Saler ,v_ServiceDataID ,v_Route.getActivityID() ,v_Route.getActivityRouteID());
            
            System.out.println("-- 选型结果确认转结束");
        }
    }
    
}
