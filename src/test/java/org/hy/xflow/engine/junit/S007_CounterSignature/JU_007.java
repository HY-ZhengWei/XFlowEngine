package org.hy.xflow.engine.junit.S007_CounterSignature;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.xml.log.Logger;
import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.ActivityRouteTree;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.ProcessCounterSignatureLog;
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
    private String serviceDataID = "202404-004";
    
    
    
    public JU_007()
    {
        super();
        
        teacher = new User();
        teacher.setUserID("目老师");
        teacher.setUserName("目老师");
        teacher.setOrgID("math");
        teacher.setOrgID("数学系");
        teacher.addRole("教师的角色" ,"教师的角色");
        
        student01 = new User();
        student01.setUserID("ZhengWei");
        student01.setUserName("阿伟");
        student01.setOrgID("IT");
        student01.setOrgID("计算机系");
        student01.addRole("学生的角色" ,"学生的角色");
        
        student02 = new User();
        student02.setUserID("HY");
        student02.setUserName("开心");
        student02.setOrgID("IT");
        student02.setOrgID("计算机系");
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
    
    
    
    /**
     * 教师部署家庭作业（汇签场景01：汇签下发）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-03
     * @version     v1.0
     */
    @Test
    public void test_布置作业()
    {
        FlowProcess v_FlowProcess = null;
        
        try
        {
            FlowProcess v_ProcessExtra = new FlowProcess();
            v_ProcessExtra.setCounterSignature(new ProcessCounterSignatureLog());
            v_ProcessExtra.getCounterSignature().setCsMaxUserCount(0);
            v_ProcessExtra.getCounterSignature().setCsMinUserCount(0);
            v_ProcessExtra.getCounterSignature().setCsExpireTime(new Date().getDate(1));
            
            v_FlowProcess = XFlowEngine.getInstance().toNextByServiceDataID(teacher ,serviceDataID ,v_ProcessExtra ,ActivityRouteTree.$AutoActivityRouteCode);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        assertTrue(v_FlowProcess != null);
    }
    
    
    
    /**
     * 学生提交作业（汇签场景02：汇签记录）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-08
     * @version     v1.0
     */
    @Test
    public void test_提交作业_学生A()
    {
        FlowProcess v_FlowProcess = null;
        
        try
        {
            FlowProcess v_ProcessExtra = new FlowProcess();
            v_ProcessExtra.setCounterSignature(new ProcessCounterSignatureLog());
            v_ProcessExtra.getCounterSignature().setCsType  ("完成作业");
            v_ProcessExtra.getCounterSignature().setCsTypeID("Finish");
            
            v_FlowProcess = XFlowEngine.getInstance().toNextByServiceDataID(student01 ,serviceDataID ,v_ProcessExtra ,ActivityRouteTree.$AutoActivityRouteCode);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        assertTrue(v_FlowProcess != null);
    }
    
    
    
    /**
     * 学生提交作业（汇签场景02：汇签记录）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-08
     * @version     v1.0
     */
    @Test
    public void test_提交作业_学生B()
    {
        FlowProcess v_FlowProcess = null;
        
        try
        {
            FlowProcess v_ProcessExtra = new FlowProcess();
            v_ProcessExtra.setCounterSignature(new ProcessCounterSignatureLog());
            v_ProcessExtra.getCounterSignature().setCsType  ("完成作业");
            v_ProcessExtra.getCounterSignature().setCsTypeID("Finish");
            
            v_FlowProcess = XFlowEngine.getInstance().toNextByServiceDataID(student02 ,serviceDataID ,v_ProcessExtra ,ActivityRouteTree.$AutoActivityRouteCode);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        assertTrue(v_FlowProcess != null);
    }
    
    
    
    /**
     * 学生A提交作业之前，可以查询到待办信息
     * 学生A提交作业之后，不应查询到待办信息。哪怕未来操作是按角色下发的汇签任务，也不应查询到
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-09
     * @version     v1.0
     */
    @Test
    public void test_查询待办_学生A()
    {
        List<String> v_WorkIDs = null;
        try
        {
            v_WorkIDs = XFlowEngine.getInstance().queryWorkIDs(student01 ,$TemplateName);
            Help.print(v_WorkIDs);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        assertTrue(v_WorkIDs != null);
    }
    
    
    
    /**
     * 查询汇签
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-09
     * @version     v1.0
     */
    @Test
    public void test_查询汇签()
    {
        List<ProcessCounterSignatureLog> v_CSInfos = null;
        
        try
        {
            ProcessCounterSignatureLog v_CSLogParam = new ProcessCounterSignatureLog();
            v_CSLogParam.setServiceDataID(serviceDataID);
            
            v_CSInfos = XFlowEngine.getInstance().queryCSLogsByServiceDataID(v_CSLogParam);
            if ( v_CSInfos != null )
            {
                for (ProcessCounterSignatureLog v_CSInfo : v_CSInfos)
                {
                    System.out.println(v_CSInfo.getCreater() + " 汇签下发 " + v_CSInfo.getCsMaxUserCount() + "人，最少汇签 " + v_CSInfo.getCsMinUserCount() + "人，完成于：" + v_CSInfo.getCsFinishTime());
                    
                    for (ProcessCounterSignatureLog v_CSLog : v_CSInfo.getLogs())
                    {
                        System.out.println(v_CSLog.getCsTime() + " " + v_CSLog.getCsUser() + " - " + v_CSLog.getCsType());
                    }
                }
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        assertTrue(v_CSInfos != null);
    }
    
}
