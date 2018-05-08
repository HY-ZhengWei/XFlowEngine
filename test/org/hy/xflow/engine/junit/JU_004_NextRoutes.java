package org.hy.xflow.engine.junit;

import java.util.List;

import org.hy.common.Help;
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
            ActivityRoute v_Route  = v_Routes.get(0);
            
            XFlowEngine.getInstance().toNextByServiceDataID(v_Saler ,v_ServiceDataID ,v_Route.getActivityID() ,v_Route.getActivityRouteID());
            
            System.out.println("-- 转受理成功");
        }
        
        
        
        User v_Manager = new User();
        v_Manager.setUserID("E10ADC3949BA59ABBE56E057F20F922E");
        v_Manager.setUserName("霍桂霞");
        v_Manager.addRole("001" ,"选型主管");
        
        v_Routes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(v_Manager ,v_ServiceDataID);
        if ( Help.isNull(v_Routes) )
        {
            System.out.println("-- [" + v_Manager.getUserName()+ "]没有任何可操作的路由");
        }
        else
        {
            ActivityRoute v_Route = v_Routes.get(0);
            
            XFlowEngine.getInstance().toNextByServiceDataID(v_Manager ,v_ServiceDataID ,v_Route.getActivityID() ,v_Route.getActivityRouteID());
            
            System.out.println("-- 转评审成功");
        }
        
        
        
        User v_Review  = new User();
        v_Review.setUserID("8a81b2b54b7b391b014b7d14a00409ff");
        v_Review.setUserName("公用主管");
        v_Review.addRole("001" ,"选型主管");
        
        v_Routes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(v_Review ,v_ServiceDataID);
        if ( Help.isNull(v_Routes) )
        {
            System.out.println("-- [" + v_Review.getUserName()+ "]没有任何可操作的路由");
        }
        else
        {
            ActivityRoute v_Route = v_Routes.get(0);
            
            XFlowEngine.getInstance().toNextByServiceDataID(v_Review ,v_ServiceDataID ,v_Route.getActivityID() ,v_Route.getActivityRouteID());
            
            System.out.println("-- 评审后转向单人选型");
        }
    }
    
}
