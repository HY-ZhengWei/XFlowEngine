package org.hy.xflow.engine.junit.S005_Reject;

import org.hy.common.Date;
import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.ProcessActivitys;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;





/**
 * 测试单元：自由驳回
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
    private String serviceDataID = "SID" + Date.getNowTime().getYMD_ID();
    
    
    
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
     * 
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-14
     * @version     v1.0
     *
     */
    @Test
    public void test_查曾经流转过的节点()
    {
        ProcessActivitys v_ProcessActivitys = XFlowEngine.getInstance().queryProcessActivitysByServiceDataID(checker02 ,"20230214-002");
    }
    
}
