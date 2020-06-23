package org.hy.xflow.engine.junit.S004_ManyRoute;

import org.hy.common.Date;
import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;





/**
 * 测试单元：多路由动态并发
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-06-23
 * @version     v1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
public class JU_004 extends BaseJunit
{
    private static final String $TemplateName = "多路由动态并发";
    
    /** 销售人员 */
    private User   seller;
    
    /** 合同评审 */
    private User   reviewer;
    
    /** 商务人员 */
    private User   businessman;
    
    /** 采购人员 */
    private User   purchaser;
    
    /** 计划人员 */
    private User   planner;
    
    /** 总评审 */
    private User   reviewerSum;
    
    /** 业务流转ID */
    private String serviceDataID = "SID" + Date.getNowTime().getYMD_ID();
    
    
    
    public JU_004()
    {
        super();
        
        seller = new User();
        seller.setUserID("销售-01");
        seller.setUserName("销售-刘一");
        seller.addRole("Role销售部" ,"销售部");
        
        reviewer = new User();
        reviewer.setUserID("合同评审-01");
        reviewer.setUserName("合同评审-王二");
        reviewer.addRole("Role合同评审" ,"合同评审");
        
        businessman = new User();
        businessman.setUserID("商务部-01");
        businessman.setUserName("商务部-张三");
        businessman.addRole("Role商务部" ,"商务部");
        
        purchaser = new User();
        purchaser.setUserID("采购部-01");
        purchaser.setUserName("采购部-李四");
        purchaser.addRole("Role采购部" ,"采购部");
        
        planner = new User();
        planner.setUserID("计划部-01");
        planner.setUserName("计划部-赵五");
        planner.addRole("Role计划部" ,"计划部");
        
        reviewerSum = new User();
        reviewerSum.setUserID("总评审-01");
        reviewerSum.setUserName("总评审-钱六");
        reviewerSum.addRole("Role总评审" ,"总评审");
    }
    
    
    
    /**
     * 销售部 -> 合同评审 -> (商务部、采购部、计划部)【三部全都评审】 -> 总评审 -> 完成
     * 
     * @author      ZhengWei(HY)
     * @createDate  2020-06-23
     * @version     v1.0
     *
     */
    @Test
    public void test_1_2_345_F()
    {
        // XFlowEngine.getInstance().createByName(seller ,$TemplateName ,serviceDataID);
        
        // XFlowEngine.getInstance().toNextByServiceDataID(seller ,serviceDataID ,null ,"销售部-合同评审");
        
        XFlowEngine.getInstance().toNextByServiceDataID(reviewer ,serviceDataID ,null ,new String []{"商务部" ,"采购部" ,"计划部"});
        
        FlowProcess v_Process = new FlowProcess();
        v_Process.setInfoComment("同意");
        v_Process.setSummary(34D);
        
        XFlowEngine.getInstance().toNextByServiceDataID(businessman ,serviceDataID ,v_Process ,"总评审");
        XFlowEngine.getInstance().toNextByServiceDataID(purchaser   ,serviceDataID ,v_Process ,"总评审");
        XFlowEngine.getInstance().toNextByServiceDataID(planner     ,serviceDataID ,v_Process ,"总评审");
        
        XFlowEngine.getInstance().toNextByServiceDataID(reviewerSum ,serviceDataID ,v_Process ,"完成");
    }
    
}
