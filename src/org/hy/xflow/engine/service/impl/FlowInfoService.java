package org.hy.xflow.engine.service.impl;

import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.common.BaseService;
import org.hy.xflow.engine.dao.IFlowInfoDAO;
import org.hy.xflow.engine.service.IFlowInfoService;





/**
 * 工作流模板的服务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-25
 * @version     v1.0
 */
@Xjava
public class FlowInfoService extends BaseService implements IFlowInfoService
{
    
    @Xjava
    private IFlowInfoDAO flowInfoDAO;
    
    
    
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
    public FlowInfo queryByWorkID(String i_WorkID)
    {
        return this.flowInfoDAO.queryByWorkID(i_WorkID);
    }
    
    
    
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
    public FlowInfo queryByServiceDataID(String i_ServiceDataID)
    {
        return this.flowInfoDAO.queryByServiceDataID(i_ServiceDataID);
    }
    
    
    
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
    public boolean createFlow(FlowInfo i_FlowInfo ,FlowProcess i_Process)
    {
        return this.flowInfoDAO.createFlow(i_FlowInfo ,i_Process);
    }
    
}
