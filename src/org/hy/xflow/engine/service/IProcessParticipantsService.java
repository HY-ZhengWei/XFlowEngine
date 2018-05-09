package org.hy.xflow.engine.service;

import org.hy.common.PartitionMap;
import org.hy.xflow.engine.bean.ProcessParticipant;





/**
 * 工作流过程的动态参与人的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-08
 * @version     v1.0
 */
public interface IProcessParticipantsService
{
    
    
    /**
     * 工作流实例ID，查询工作流流转过程的动态参与人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-09
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    public PartitionMap<String ,ProcessParticipant> queryByWorkID(String i_WorkID);
    
    
    
    /**
     * 三方使用系统的业务数据ID，查询工作流流转过程的动态参与人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-09
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    public PartitionMap<String ,ProcessParticipant> queryByServiceDataID(String i_ServiceDataID);
    
}
