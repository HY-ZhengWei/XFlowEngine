package org.hy.xflow.engine.junit.S001_XX.J001_OnePeople;

import org.hy.common.Help;
import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.NextRoutes;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.bean.UserParticipant;
import org.hy.xflow.engine.common.BaseJunit;
import org.hy.xflow.engine.enums.ParticipantTypeEnum;
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
        
        String v_ServiceDataID = "F2018-09-26-001";
        
        NextRoutes v_NextRoutes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(v_Saler ,v_ServiceDataID);
        if ( Help.isNull(v_NextRoutes.getRoutes()) )
        {
            System.out.println("-- [" + v_Saler.getUserName()+ "]没有任何可操作的路由");
        }
        else
        {
            ActivityRoute v_Route  = v_NextRoutes.getRoutes().get(0);
            
            UserParticipant v_Participant = new UserParticipant();
            v_Participant.setObjectType(ParticipantTypeEnum.$Role);
            v_Participant.setObjectID("role-approval");
            v_Participant.setObjectName("选型审批人");
            v_Participant.setObjectNo(1);
            
            XFlowEngine.getInstance().toNextByServiceDataID(v_Saler ,v_ServiceDataID ,null ,v_Route.getActivityRouteCode() ,v_Participant);
            
            System.out.println("-- 申请成功。版本2.0.0转审批选型；版本1.0.0转受理");
        }
    }
    
    
    
    @Test
    public void test002()
    {
        User v_Saler = new User();
        v_Saler.setUserID("8a81b2b54b7b391b014b7d12b66400fc");
        v_Saler.setUserName("公用销售人员");
        v_Saler.addRole("004" ,"销售人员");
        
        User v_Approval = new User();
        v_Approval.setUserID("8a81b2aa4e4865b4014e535c6c900115");
        v_Approval.setUserName("郑伟");
        v_Approval.addRole("role-approval" ,"选型审批人");
        
        String v_ServiceDataID = "SID001";
        
        NextRoutes v_NextRoutes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(v_Saler ,v_ServiceDataID);
        v_NextRoutes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(v_Approval ,v_ServiceDataID);
        if ( Help.isNull(v_NextRoutes.getRoutes()) )
        {
            System.out.println("-- [" + v_Approval.getUserName()+ "]没有任何可操作的路由");
        }
        else
        {
            ActivityRoute v_Route = v_NextRoutes.getRoutes().get(0);
            
            XFlowEngine.getInstance().toNextByServiceDataID(v_Approval ,v_ServiceDataID ,null ,v_Route.getActivityRouteCode());
            
            System.out.println("-- 审批成功，转受理");
        }
    }
    
    
    
    @Test
    public void test003()
    {
        User v_Approval = new User();
        v_Approval.setUserID("8a81b2aa4e4865b4014e535c6c900115");
        v_Approval.setUserName("郑伟");
        v_Approval.addRole("role-approval" ,"选型审批人");
        
        User v_Manager = new User();
        v_Manager.setUserID("E10ADC3949BA59ABBE56E057F20F922E");
        v_Manager.setUserName("霍桂霞");
        v_Manager.addRole("001" ,"选型主管");
        
        String v_ServiceDataID = "SID001";
        
        NextRoutes v_NextRoutes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(v_Approval ,v_ServiceDataID);
        v_NextRoutes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(v_Manager ,v_ServiceDataID);
        if ( Help.isNull(v_NextRoutes.getRoutes()) )
        {
            System.out.println("-- [" + v_Manager.getUserName()+ "]没有任何可操作的路由");
        }
        else
        {
            ActivityRoute v_Route = v_NextRoutes.getRoutes().get(0);
            
            XFlowEngine.getInstance().toNextByServiceDataID(v_Manager ,v_ServiceDataID ,null ,v_Route.getActivityRouteCode());
            
            System.out.println("-- 受理成功，转评审");
        }
    }
    
}
