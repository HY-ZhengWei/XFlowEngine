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
 * @createDate  2019-10-22
 * @version     v1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class JU_00302 extends BaseJunit
{
    
    private User   creater;
    
    private User   operaterA;
    
    private User   operaterB;
    
    private String serviceDataID = "测试并行002";
    
    
    
    public JU_00302()
    {
        super();
        
        creater = new User();
        creater.setUserID("Create-01");
        creater.setUserName("程志华");
        creater.addRole("107" ,"申请人角色");
        
        operaterA = new User();
        operaterA.setUserID("Operater-A");
        operaterA.setUserName("赵娟");
        operaterA.addRole("111" ,"赵娟的角色");
        
        operaterB = new User();
        operaterB.setUserID("Operater-A");
        operaterB.setUserName("郑伟");
        operaterB.addRole("112" ,"郑伟的角色");
    }
    
    
    
    /**
     * 创建流程实例
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-10-22
     * @version     v1.0
     */
    @Test
    public void test_001_CreateFlow()
    {
        XFlowEngine.getInstance().createByName(creater ,"测试并行" ,serviceDataID);
    }
    
    
    
    /**
     * 向下一活动节点流转。即，孩子问爸爸、问妈妈（发起分单，多路并行流程）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-10-22
     * @version     v1.0
     *
     */
    @Test
    public void test_003_ToNext()
    {
        XFlowEngine.getInstance().toNextByServiceDataID(creater ,serviceDataID ,null ,new String []{"走线路一" ,"走线路二"});
    }
    
    
    
    /**
     * 向下一活动节点流转。即，爸爸回答孩子
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-10-22
     * @version     v1.0
     *
     */
    @Test
    public void test_004_ToNextByA()
    {
        FlowProcess v_Process = new FlowProcess();
        
        v_Process.setInfoComment("赵娟答复");
        
        XFlowEngine.getInstance().toNextByServiceDataID(operaterA ,serviceDataID ,v_Process ,"线路一走向结束");
    }
    
    
    
    /**
     * 向下一活动节点流转。即，妈妈回答孩子
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-10-22
     * @version     v1.0
     *
     */
    @Test
    public void test_005_ToNextByB()
    {
        FlowProcess v_Process = new FlowProcess();
        
        v_Process.setInfoComment("郑伟答复");
        
        XFlowEngine.getInstance().toNextByServiceDataID(operaterB ,serviceDataID ,v_Process ,"线路二走向结束");
    }
    
}
