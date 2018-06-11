package org.hy.xflow.engine.service.impl;

import java.util.List;

import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.common.BaseService;
import org.hy.xflow.engine.dao.IFlowProcessDAO;
import org.hy.xflow.engine.service.IFlowProcessService;





/**
 * 工作流流转过程的服务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-27
 * @version     v1.0
 */
@Xjava
public class FlowProcessService extends BaseService implements IFlowProcessService
{
    
    @Xjava
    private IFlowProcessDAO flowProcessDAO; 
    
    
    
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
    public List<FlowProcess> queryByWorkID(String i_WorkID)
    {
        return this.flowProcessDAO.queryByWorkID(i_WorkID);
    }
    
    
    
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
    public List<FlowProcess> queryByServiceDataID(String i_ServiceDataID)
    {
        return this.flowProcessDAO.queryByServiceDataID(i_ServiceDataID);
    }
    
    
    
    /**
     * 获取用户已处理过的工作流实例ID。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-06-11
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    public List<String> queryWorkIDsByDone(User i_User)
    {
        return this.flowProcessDAO.queryWorkIDsByDone(i_User);
    }
    
    
    
    /**
     * 获取用户已处理过的工作流实例对应的第三方使用系统的业务数据ID。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-06-11
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    public List<String> queryServiceDataIDsByDone(User i_User)
    {
        return this.flowProcessDAO.queryServiceDataIDsByDone(i_User);
    }
    
}
