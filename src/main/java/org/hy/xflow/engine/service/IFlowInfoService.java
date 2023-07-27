package org.hy.xflow.engine.service;

import java.util.List;

import org.hy.xflow.engine.bean.FlowComment;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.FlowProcess;





/**
 * 工作流实例的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-25
 * @version     v1.0
 *              v2.0  2019-09-12  添加：支持多路并行路由的流程
 */
public interface IFlowInfoService
{
    
    /**
     * 查询某一工作流模板下的所有活动的工作流实例。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-08-29
     * @version     v1.0
     *
     * @param i_TemplateID  工作流模板ID
     * @return
     */
    public List<FlowInfo> queryActivitys(String i_TemplateID);
    
    
    
    /**
     * 工作流实例ID，查询工作流实例
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-27
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    public FlowInfo queryByWorkID(String i_WorkID);
    
    
    
    /**
     * 按第三方使用系统的业务数据ID，查询工作流实例
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    public FlowInfo queryByServiceDataID(String i_ServiceDataID);
    
    
    
    /**
     * 创建的工作流实例，当前活动节点为  "开始" 节点。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-26
     * @version     v1.0
     *
     * @param i_FlowInfo
     * @param i_Process
     * @return
     */
    public boolean createFlow(FlowInfo i_FlowInfo ,FlowProcess i_Process);
    
    
    
    /**
     * 工作流流转，并更新前一个流转信息。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-07
     * @version     v1.0
     *
     * @param i_Flow          工作流实例对象
     * @param i_ProcessList   新流转的过程集合信息（分单时会有多个，正常情况下均只有一个元素）
     * @param i_Previous      前一个流转的过程信息
     * @return
     */
    public boolean toNext(FlowInfo i_Flow ,List<FlowProcess> i_ProcessList ,FlowProcess i_Previous);
    
    
    
    /**
     * 工作流实例ID，全流转结束后转历史
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-11
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    public boolean toHistory(String i_WorkID);
    
    
    
    /**
     * 按第三方使用系统的业务数据ID，全流转结束后转历史
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-11
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    public boolean toHistoryByServiceDataID(String i_ServiceDataID);
    
    
    
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
    public List<FlowComment> queryCommentByWorkID(String i_WorkID);
    
    
    
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
    public List<FlowComment> queryCommentByServiceDataID(String i_ServiceDataID);
    
    
    
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
    public boolean addComment(FlowComment i_FlowComment);
    
}
