package org.hy.xflow.engine.junit.S007_CounterSignature;

import static org.junit.Assert.assertTrue;

import org.hy.common.xml.log.Logger;
import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.ActivityRouteTree;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;





/**
 * 测试单元：汇签 & 自动流转
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-04-03
 * @version     v1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JU_007 extends BaseJunit
{
    private static final Logger $Logger       = new Logger(JU_007.class ,true);
    
    private static final String $TemplateName = "汇签";
    
    
    
    /** 教师 */
    private User   teacher;
    
    /** 学生01 */
    private User   student01;
    
    /** 学生02 */
    private User   student02;
    
    /** 业务流转ID */
    private String serviceDataID = "202404-001";
    
    
    
    public JU_007()
    {
        super();
        
        teacher = new User();
        teacher.setUserID("目老师");
        teacher.setUserName("目老师");
        teacher.addRole("教师的角色" ,"教师的角色");
        
        student01 = new User();
        student01.setUserID("ZhengWei");
        student01.setUserName("阿伟");
        student01.addRole("学生的角色" ,"学生的角色");
        
        student02 = new User();
        student02.setUserID("HY");
        student02.setUserName("开心");
        student02.addRole("学生的角色" ,"学生的角色");
    }
    
    
    
    /**
     * 创建流程实例
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-03
     * @version     v1.0
     */
    @Test
    public void test_创建流程实例()
    {
        FlowInfo v_FlowInfo = null;
        
        try
        {
            v_FlowInfo = XFlowEngine.getInstance().createByName(teacher ,$TemplateName ,serviceDataID);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        assertTrue(v_FlowInfo != null);
    }
    
    
    
    /**
     * 上课学习完成，下课
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-03
     * @version     v1.0
     */
    @Test
    public void test_下课()
    {
        FlowProcess v_FlowProcess = null;
        
        try
        {
            v_FlowProcess = XFlowEngine.getInstance().toNextByServiceDataID(teacher ,serviceDataID ,null ,ActivityRouteTree.$AutoActivityRouteCode);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        assertTrue(v_FlowProcess != null);
    }
    
}
