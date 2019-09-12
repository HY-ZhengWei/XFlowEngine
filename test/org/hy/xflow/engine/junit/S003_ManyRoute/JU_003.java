package org.hy.xflow.engine.junit.S003_ManyRoute;

import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.NextRoutes;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;





/**
 * 测试单元：多路由并发
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-09-12
 * @version     v1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class JU_003 extends BaseJunit
{
    
    private User   children;
    
    private User   father;
    
    private User   mother;
    
    private String serviceDataID = "能玩游戏吗001";
    
    
    
    public JU_003()
    {
        super();
        
        children = new User();
        children.setUserID("Children-01");
        children.setUserName("小明");
        children.addRole("Children-Role" ,"孩子的角色");
        
        father = new User();
        father.setUserID("Father-01");
        father.setUserName("小明的爸爸");
        father.addRole("Father-Role" ,"爸爸的角色");
        
        mother = new User();
        mother.setUserID("Mother-01");
        mother.setUserName("小明的妈妈");
        mother.addRole("Mother-Role" ,"妈妈的角色");
    }
    
    
    
    /**
     * 创建流程实例
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-12
     * @version     v1.0
     */
    @Test
    public void test_001_CreateFlow()
    {
        XFlowEngine.getInstance().createByName(children ,"多路由并发" ,serviceDataID);
    }
    
    
    
    /**
     * 查询当前活动节点有哪几条路由可走
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-12
     * @version     v1.0
     */
    @Test
    public void test_002_QueryNextRoutes()
    {
        NextRoutes v_NextRoutes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(children ,serviceDataID);
        
        System.out.println("当前所在的活动节点：" + v_NextRoutes.getCurrentActivity().getActivityName());
        
        int v_Index = 1;
        for (ActivityRoute v_Route : v_NextRoutes.getRoutes())
        {
            System.out.println("可走的路由：" + (v_Index++) + "：" + v_Route.getActivityRouteCode());
        }
    }
    
    
    
    /**
     * 向下一活动节点流转
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-12
     * @version     v1.0
     *
     */
    @Test
    public void test_003_ToNext()
    {
        XFlowEngine.getInstance().toNextByServiceDataID(children ,serviceDataID ,new String []{"问爸爸" ,"问妈妈"});
    }

}
