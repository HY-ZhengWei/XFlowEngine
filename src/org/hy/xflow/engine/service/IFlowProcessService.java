package org.hy.xflow.engine.service;

import java.util.List;

import org.hy.xflow.engine.bean.FlowProcess;





/**
 * 工作流流转过程的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-27
 * @version     v1.0
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
    
}
