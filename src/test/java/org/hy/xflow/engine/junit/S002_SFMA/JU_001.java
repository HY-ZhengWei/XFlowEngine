package org.hy.xflow.engine.junit.S002_SFMA;

import org.hy.common.Date;
import org.hy.common.StringHelp;
import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.ActivityInfo;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.NextRoutes;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;





/**
 * 测试单元：《主动颈椎屈曲》的全流程测试
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-09-05
 * @version     v1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class JU_001 extends BaseJunit
{
    
    private User   doctor;
    
    private String serviceDataID = "检查者本次测试的ID-002";
    
    
    
    public JU_001()
    {
        super();
        
        doctor = new User();
        doctor.setUserID("Doctor-00001");
        doctor.setUserName("苗医生");
        doctor.addRole("Doctor-Role" ,"医生角色");
    }
    
    
    
    /**
     * 创建流程实例
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-09-05
     * @version     v1.0
     */
    @Test
    public void test_001_CreateFlow()
    {
        XFlowEngine.getInstance().createByName(doctor ,"主动颈椎屈曲" ,serviceDataID);
    }
    
    
    
    /**
     * 查询当前活动节点有哪几条路由可走
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-09-05
     * @version     v1.0
     */
    @Test
    public void test_002_QueryNextRoutes()
    {
        NextRoutes v_NextRoutes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(doctor ,serviceDataID);
        
        System.out.println("当前所在的活动节点：" + v_NextRoutes.getCurrentActivity().getActivityName());
        
        int v_Index = 1;
        for (ActivityRoute v_Route : v_NextRoutes.getRoutes())
        {
            System.out.println("可走的路由：" + (v_Index++) + v_Route.getActivityRouteCode());
        }
    }
    
    
    
    /**
     * 向下一活动节点流转
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-09-05
     * @version     v1.0
     *
     */
    @Test
    public void test_003_ToNext()
    {
        XFlowEngine.getInstance().toNextByServiceDataID(doctor ,serviceDataID ,null ,"FN");
    }
    
    
    
    /**
     * 全流程流转测试
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-09-05
     * @version     v1.0
     *
     */
    @Test
    public void test_004_ToFinish()
    {
        String [][] v_Routes = {{"DN" ,"DN" ,"DN"} 
                               ,{"DP" ,"DP" ,"DP"}
                               ,{"FP" ,"FP" ,"FP"}
                               ,{"DN" ,"FN"}
                               ,{"FN"}};
        
        for (int i=0; i<v_Routes.length; i++)
        {
            this.serviceDataID = Date.getNowTime().getFullMilli_ID();
            
            XFlowEngine.getInstance().createByName(doctor ,"主动颈椎屈曲" ,serviceDataID);
            
            FlowProcess v_LastProcess = null;
            for (String v_Route : v_Routes[i])
            {
                v_LastProcess = XFlowEngine.getInstance().toNextByServiceDataID(doctor ,serviceDataID ,null ,v_Route);
            }
            
            ActivityInfo v_Activity = XFlowEngine.getInstance().getCurrentActivity(v_LastProcess);
            
            System.out.println("检测" + (i+1) + "：" + StringHelp.toString(v_Routes[i]) + " 的结果是：" + v_Activity.getInfoComment());
        }
    }
    
}
