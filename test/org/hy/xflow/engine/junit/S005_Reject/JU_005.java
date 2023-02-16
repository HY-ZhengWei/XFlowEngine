package org.hy.xflow.engine.junit.S005_Reject;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.ProcessActivitys;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;





/**
 * 测试单元：自由驳回（未在工作流模板上预先配置驳回路由）
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-02-14
 * @version     v1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JU_005 extends BaseJunit
{
    private static final String $TemplateName = "自由驳回";
    
    /** 产品经理 */
    private User   manager;
    
    /** 检测喷涂工艺 */
    private User   checker01;
    
    /** X光探伤 */
    private User   checker02;
    
    /** 业务流转ID */
    private String serviceDataID = "20230214-002";
    
    
    
    public JU_005()
    {
        super();
        
        manager = new User();
        manager.setUserID("ZhengWei");
        manager.setUserName("ZhengWei");
        manager.addRole("产品经理的角色" ,"产品经理的角色");
        
        checker01 = new User();
        checker01.setUserID("WangLi");
        checker01.setUserName("WangLi");
        checker01.addRole("检测喷涂工艺的角色" ,"检测喷涂工艺的角色");
        
        checker02 = new User();
        checker02.setUserID("LiHao");
        checker02.setUserName("LiHao");
        checker02.addRole("X光探伤的角色" ,"X光探伤的角色");
    }
    
    
    
    /**
     * 查询查曾经流转过的节点
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-14
     * @version     v1.0
     *
     */
    @Test
    public void test_查曾经流转过的节点()
    {
        ProcessActivitys v_ProcessActivitys = XFlowEngine.getInstance().queryProcessActivitysByServiceDataID(checker02 ,serviceDataID);
        
        assertTrue(v_ProcessActivitys != null);
    }
    
    
    
    /**
     * 查询查曾经流转过的节点
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-15
     * @version     v1.0
     *
     */
    @Test
    public void test_驳回到上级()
    {
        FlowProcess v_RejectInfo = new FlowProcess();
        v_RejectInfo.setInfoComment("驳回的原因是：xxx xxx");
        
        ProcessActivitys  v_ProcessActivitys = XFlowEngine.getInstance().queryProcessActivitysByServiceDataID(checker02 ,serviceDataID);
        String            v_RejectACode      = v_ProcessActivitys.getActivitys().get(0).getCurrentActivityCode();
        List<FlowProcess> v_RejectRet        = XFlowEngine.getInstance().toRejectByServiceDataID(checker02 ,serviceDataID ,v_RejectInfo ,v_RejectACode);
        
        assertTrue(v_RejectRet != null);
    }
    
}
