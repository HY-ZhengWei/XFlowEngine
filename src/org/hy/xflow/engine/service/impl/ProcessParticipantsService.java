package org.hy.xflow.engine.service.impl;

import org.hy.common.PartitionMap;
import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.ProcessParticipant;
import org.hy.xflow.engine.common.BaseService;
import org.hy.xflow.engine.dao.IProcessParticipantsDAO;
import org.hy.xflow.engine.service.IProcessParticipantsService;





/**
 * 工作流过程的动态参与人的服务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-08
 * @version     v1.0
 */
@Xjava
public class ProcessParticipantsService extends BaseService implements IProcessParticipantsService
{
    
    @Xjava
    private IProcessParticipantsDAO processParticipantsDAO;
    
    
    
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
    public PartitionMap<String ,ProcessParticipant> queryByWorkID(String i_WorkID)
    {
        return this.processParticipantsDAO.queryByWorkID(i_WorkID);
    }
    
    
    
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
    public PartitionMap<String ,ProcessParticipant> queryByServiceDataID(String i_ServiceDataID)
    {
        return this.processParticipantsDAO.queryByServiceDataID(i_ServiceDataID);
    }
    
}
