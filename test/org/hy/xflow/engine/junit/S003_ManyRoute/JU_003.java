package org.hy.xflow.engine.junit.S003_ManyRoute;

import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.FlowProcess;
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
    
    private User   father_Else;
    
    private User   mother;
    
    private String serviceDataID = "能玩游戏吗017";
    
    
    
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
        
        father_Else = new User();
        father_Else.setUserID("father_Else-02");
        father_Else.setUserName("其它小朋友的爸爸");
        father_Else.addRole("Father-Role" ,"爸爸的角色");
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
     * 查询当前活动节点有哪几条路由可走。即，孩子可以有哪些路可以走？
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
            System.out.println(children.getUserName() + "可走的路由：" + (v_Index++) + "：" + v_Route.getActivityRouteCode());
        }
    }
    
    
    
    /**
     * 向下一活动节点流转。即，孩子问爸爸、问妈妈（发起分单，多路并行流程）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-12
     * @version     v1.0
     *
     */
    @Test
    public void test_003_ToNext()
    {
        XFlowEngine.getInstance().toNextByServiceDataID(children ,serviceDataID ,null ,new String []{"问爸爸" ,"问妈妈"});
    }
    
    
    
    /**
     * 查询当前活动节点有哪几条路由可走。即，爸爸可以有哪些路可以走？
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-17
     * @version     v1.0
     */
    @Test
    public void test_004_QueryNextRoutes_ToFather()
    {
        NextRoutes v_NextRoutes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(father_Else ,serviceDataID);
        
        System.out.println("当前所在的活动节点：" + v_NextRoutes.getCurrentActivity().getActivityName());
        
        int v_Index = 1;
        for (ActivityRoute v_Route : v_NextRoutes.getRoutes())
        {
            System.out.println(father.getUserName() + "可走的路由：" + (v_Index++) + "：" + v_Route.getActivityRouteCode());
        }
    }
    
    
    
    /**
     * 查询当前活动节点有哪几条路由可走。即，妈妈可以有哪些路可以走？
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-17
     * @version     v1.0
     */
    @Test
    public void test_004_QueryNextRoutes_ToMother()
    {
        NextRoutes v_NextRoutes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(mother ,serviceDataID);
        
        System.out.println("当前所在的活动节点：" + v_NextRoutes.getCurrentActivity().getActivityName());
        
        int v_Index = 1;
        for (ActivityRoute v_Route : v_NextRoutes.getRoutes())
        {
            System.out.println(mother.getUserName() + "可走的路由：" + (v_Index++) + "：" + v_Route.getActivityRouteCode());
        }
    }
    
    
    
    /**
     * 向下一活动节点流转。即，爸爸回答孩子
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-15
     * @version     v1.0
     *
     */
    @Test
    public void test_004_ToNextByFather()
    {
        FlowProcess v_Process = new FlowProcess();
        
        v_Process.setInfoComment("要学完英语才能玩");
        v_Process.setOperateFiles("这里是英语资料");
        v_Process.setOperateDatas("学的快，还有奖励");
        v_Process.setSummary(100D);
        
        XFlowEngine.getInstance().toNextByServiceDataID(father ,serviceDataID ,v_Process ,"爸爸答复");
    }
    
    
    
    /**
     * 向下一活动节点流转。即，妈妈回答孩子
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-16
     * @version     v1.0
     *
     */
    @Test
    public void test_004_ToNextByMother()
    {
        FlowProcess v_Process = new FlowProcess();
        
        v_Process.setInfoComment("要做完作业才能玩");
        v_Process.setSummary(50D);
        
        XFlowEngine.getInstance().toNextByServiceDataID(mother ,serviceDataID ,v_Process ,"妈妈答复");
    }
    
    
    
    /**
     * 向下一活动节点流转。即，孩子执行询问结果
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-16
     * @version     v1.0
     *
     */
    @Test
    public void test_005_ToNextByExecute()
    {
        XFlowEngine.getInstance().toNextByServiceDataID(children ,serviceDataID ,null ,"执行");
    }
    
    
    
    /**
     * 完成流转
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-17
     * @version     v1.0
     *
     */
    @Test
    public void test_006_ToFinisth()
    {
        XFlowEngine.getInstance().toNextByServiceDataID(children ,serviceDataID ,null ,"完成");
    }
    
}
