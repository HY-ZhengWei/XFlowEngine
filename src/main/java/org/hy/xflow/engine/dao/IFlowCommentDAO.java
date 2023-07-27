package org.hy.xflow.engine.dao;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.FlowComment;





/**
 * 工作流备注的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-07-27
 * @version     v1.0
 */
@Xjava(id="FlowCommentDAO" ,value=XType.XSQL)
public interface IFlowCommentDAO
{
    
    /**
     * 工作流实例ID，查询工作流备注信息（活动及历史的均查询）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-07-27
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    @Xsql(id="XSQL_XFlow_FlowComment_QueryByWorkID_ServiceDataID")
    public List<FlowComment> queryByWorkID(@Xparam(id="workID" ,notNull=true) String i_WorkID);
    
    
    
    /**
     * 按第三方使用系统的业务数据ID，查询工作流备注信息（活动及历史的均查询）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-07-27
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    @Xsql(id="XSQL_XFlow_FlowComment_QueryByWorkID_ServiceDataID")
    public List<FlowComment> queryByServiceDataID(@Xparam(id="serviceDataID" ,notNull=true) String i_ServiceDataID);
    
    
    
    /**
     * 添加工作流备注信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-07-27
     * @version     v1.0
     *
     * @param i_FlowComment
     * @return
     */
    @Xsql("XSQL_XFlow_FlowComment_Insert")
    public int addFlowComment(FlowComment i_FlowComment);
    
}
