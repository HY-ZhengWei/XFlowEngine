package org.hy.xflow.engine.junit.S001_XX.J001_OnePeople;

import java.util.List;

import org.hy.common.Help;
import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.bean.UserParticipant;
import org.hy.xflow.engine.common.BaseJunit;
import org.hy.xflow.engine.enums.ParticipantTypeEnum;
import org.junit.Test;





/**
 * 测试单元：下一活动，并动态指定参与人
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-08
 * @version     v1.0
 */
public class JU_005_NextParticipants extends BaseJunit
{
    
    @Test
    public void test_001()
    {
        User v_Review  = new User();
        v_Review.setUserID("8a81b2b54b7b391b014b7d14a00409ff");
        v_Review.setUserName("公用主管");
        v_Review.addRole("001" ,"选型主管");
        
        String v_ServiceDataID = "SID001";
        
        List<ActivityRoute> v_Routes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(v_Review ,v_ServiceDataID);
        if ( Help.isNull(v_Routes) )
        {
            System.out.println("-- [" + v_Review.getUserName()+ "]没有任何可操作的路由");
        }
        else
        {
            UserParticipant v_UserPart = new UserParticipant();
            
            v_UserPart.setObjectID("8a81b2b54b7b391b014b7d143a7400fe");
            v_UserPart.setObjectName("公用选型负责人");
            v_UserPart.setObjectType(ParticipantTypeEnum.$User);
            
            ActivityRoute v_Route = v_Routes.get(0);
            
            XFlowEngine.getInstance().toNextByServiceDataID(v_Review ,v_ServiceDataID ,v_Route.getActivityCode() ,v_Route.getActivityRouteCode() ,v_UserPart);
            
            System.out.println("-- 评审后转向单人选型");
        }
    }
    
}
