package org.hy.xflow.engine.dao;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.FlowProcess;





/**
 * 工作流流转过程的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-27
 * @version     v1.0
 */
@Xjava(id="FlowProcessDAO" ,value=XType.XSQL)
public interface IFlowProcessDAO
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
    @Xsql("XSQL_XFlow_Process_QueryByWorkID_ServiceDataID")
    public List<FlowProcess> queryByWorkID(@Xparam(id="workID" ,notNull=true) String i_WorkID);
    
    
    
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
    @Xsql("XSQL_XFlow_Process_QueryByWorkID_ServiceDataID")
    public List<FlowProcess> queryByServiceDataID(@Xparam(id="serviceDataID" ,notNull=true) String i_ServiceDataID);
    
}
