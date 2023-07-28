package org.hy.xflow.engine.junit.S006_Rejects;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hy.common.Help;
import org.hy.common.xml.log.Logger;
import org.hy.xflow.engine.XFlowEngine;
import org.hy.xflow.engine.bean.FlowComment;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.NextRoutes;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseJunit;
import org.hy.xflow.engine.enums.RejectModeEnum;
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
public class JU_006 extends BaseJunit
{
    private static final Logger $Logger       = new Logger(JU_006.class ,true);
    
    private static final String $TemplateName = "自由驳回";
    
    /** 产品经理 */
    private User   manager;
    
    /** 检测喷涂工艺 */
    private User   checker01;
    
    /** X光探伤 */
    private User   checker02;
    
    /** 业务流转ID */
    private String serviceDataID = "20230214-003";
    
    
    
    public JU_006()
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
     * 创建流程实例
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-09-05
     * @version     v1.0
     */
    @Test
    public void test_创建流程实例()
    {
        FlowInfo v_FlowInfo = null;
        
        try
        {
            v_FlowInfo = XFlowEngine.getInstance().createByName(manager ,$TemplateName ,serviceDataID);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        assertTrue(v_FlowInfo != null);
    }
    
    
    
    /**
     * 添加实例备注
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-07-27
     * @version     v1.0
     *
     */
    @Test
    public void test_添加实例备注()
    {
        boolean     v_Ret         = false;
        FlowComment v_FlowComment = new FlowComment();
        
        v_FlowComment.setServiceDataID("HY20230601-001");
        v_FlowComment.setCreaterID("99099");
        v_FlowComment.setCreater("张博超");
        v_FlowComment.setComment("测试");
        
        try
        {
            XFlowEngine.getInstance().addComment(v_FlowComment);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        assertTrue(v_Ret);
    }
    
    
    
    @Test
    public void test_查询实例备注()
    {
        List<FlowComment> v_FComments = null;
        
        v_FComments = XFlowEngine.getInstance().queryCommentByWorkID       ("XFBC8926C89E6B421C8AE941B53BCFBF0C");
        v_FComments = XFlowEngine.getInstance().queryCommentByServiceDataID("HY20230601-001");
        
        $Logger.info(v_FComments);
    }
    
    
    
    /**
     * 执行喷涂工艺
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-16
     * @version     v1.0
     */
    @Test
    public void test_执行喷涂工艺()
    {
        FlowProcess v_FlowProcess = null;
        
        try
        {
            v_FlowProcess = XFlowEngine.getInstance().toNextByServiceDataID(manager ,serviceDataID ,null ,"R-S005-001-R001");
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        assertTrue(v_FlowProcess != null);
    }
    
    
    
    /**
     * 启动流程实例
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-16
     * @version     v1.0
     */
    @Test
    public void test_执行X光探伤()
    {
        FlowProcess v_FlowProcess = null;
        
        try
        {
            v_FlowProcess = XFlowEngine.getInstance().toNextByServiceDataID(checker01 ,serviceDataID ,null ,"R-S005-001-R002");
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
        }
        
        assertTrue(v_FlowProcess != null);
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
        NextRoutes v_NextRoutes = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(checker02 ,serviceDataID);
        
        assertTrue(!Help.isNull(v_NextRoutes.getActivitys()));
    }
    
    
    
    /**
     * 驳回到上级
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
        
        NextRoutes        v_NextRoutes  = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(checker02 ,serviceDataID);
        String            v_RejectACode = v_NextRoutes.getActivitys().get(0).getCurrentActivityCode();
        List<FlowProcess> v_RejectRet   = XFlowEngine.getInstance().toRejectByServiceDataID(checker02 ,serviceDataID ,RejectModeEnum.$Auto ,v_RejectInfo ,v_RejectACode);
        
        assertTrue(v_RejectRet != null);
    }
    
    
    
    /**
     * 驳回到上级的上级
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-15
     * @version     v1.0
     *
     */
    @Test
    public void test_驳回到上级的上级()
    {
        FlowProcess v_RejectInfo = new FlowProcess();
        v_RejectInfo.setInfoComment("驳回的原因是：yyy yyy");
        
        NextRoutes        v_NextRoutes  = XFlowEngine.getInstance().queryNextRoutesByServiceDataID(checker02 ,serviceDataID);
        String            v_RejectACode = v_NextRoutes.getActivitys().get(1).getCurrentActivityCode();
        List<FlowProcess> v_RejectRet   = XFlowEngine.getInstance().toRejectByServiceDataID(checker02 ,serviceDataID ,RejectModeEnum.$Auto ,v_RejectInfo ,v_RejectACode);
        
        assertTrue(v_RejectRet != null);
    }
    
}
