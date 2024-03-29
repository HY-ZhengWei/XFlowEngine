package org.hy.xflow.engine.service;

import java.util.List;

import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.User;





/**
 * 工作流流转过程的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-27
 * @version     v1.0
 *              v2.0  2024-02-23  添加：按人员信息查询已办时，可按流程模板名称过滤
 */
public interface IFlowProcessService
{
    
    /**
     * 工作流实例ID，查询工作流流转过程信息。
     * 
     * 按时间倒排的。最新的，在首位。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-27
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    public List<FlowProcess> queryByWorkID(String i_WorkID);
    
    
    
    /**
     * 三方使用系统的业务数据ID，查询工作流流转过程信息。
     * 
     * 按时间倒排的。最新的，在首位。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-27
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    public List<FlowProcess> queryByServiceDataID(String i_ServiceDataID);
    
    
    
    /**
     * 获取用户已处理过的工作流实例ID。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-06-11
     * @version     v1.0
     *              v2.0  2024-02-23  添加：流程模板名称的查询条件
     * 
     * @param i_User          流程用户
     * @param i_TemplateName  流程模板名称
     * @return
     */
    public List<String> queryWorkIDsByDone(User i_User ,String i_TemplateName);
    
    
    
    /**
     * 获取用户已处理过的工作流实例对应的第三方使用系统的业务数据ID。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-06-11
     *              v2.0  2024-02-23  添加：流程模板名称的查询条件
     * 
     * @param i_User          流程用户
     * @param i_TemplateName  流程模板名称
     * @return
     */
    public List<String> queryServiceDataIDsByDone(User i_User ,String i_TemplateName);
    
    
    
    /**
     * 汇总“汇总值”
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-16
     * @version     v1.0
     *
     * @param i_Process
     * @return
     */
    public FlowProcess querySummary(FlowProcess i_Process);
    
    
    
    /**
     * 查询历次的汇总情况。首次为最新的流转（即按时间顺序倒排的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-18
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    public List<FlowProcess> querySummarysByWorkID(String i_WorkID);
    
    
    
    /**
     * 查询历次的汇总情况。首次为最新的流转（即按时间顺序倒排的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-18
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    public List<FlowProcess> querySummarysByServiceDataID(String i_ServiceDataID);
    
}
