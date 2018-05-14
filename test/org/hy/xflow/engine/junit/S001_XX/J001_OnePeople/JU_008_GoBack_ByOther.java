package org.hy.xflow.engine.junit.S001_XX.J001_OnePeople;

import java.util.List;

import org.hy.common.Help;
import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.Test;





/**
 * 测试单元：由第三方人员驳回，如高层领导直接干预驳回。
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-11
 * @version     v1.0
 */
public class JU_008_GoBack_ByOther extends BaseJunit
{
    
    @Test
    public void test_001()
    {
        User v_Manager = new User();
        v_Manager.setUserID("E10ADC3949BA59ABBE56E057F20F922E");
        v_Manager.setUserName("霍桂霞");
        v_Manager.addRole("001" ,"选型主管");
        
        String v_ServiceDataID = "SID001";
        
        List<ActivityRoute> v_Routes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(v_Manager ,v_ServiceDataID);
        if ( Help.isNull(v_Routes) )
        {
            System.out.println("-- [" + v_Manager.getUserName()+ "]没有任何可操作的路由");
        }
        else
        {
            ActivityRoute v_Route = v_Routes.get(0);
            
            XFlowEngine.getInstance().toNextByServiceDataID(v_Manager ,v_ServiceDataID ,v_Route.getActivityCode() ,v_Route.getActivityRouteCode());
            
            System.out.println("-- 选型结果确认未通过转单人选型");
        }
    }
    
}
