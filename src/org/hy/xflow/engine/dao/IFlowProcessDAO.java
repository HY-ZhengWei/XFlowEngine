package org.hy.xflow.engine.dao;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.User;





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
    @Xsql("XSQL_XFlow_Process_queryWorkIDsByDone")
    public List<String> queryWorkIDsByDone(User i_User);
    
    
    
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
    @Xsql("XSQL_XFlow_Process_queryServiceDataIDsByDone")
    public List<String> queryServiceDataIDsByDone(User i_User);
    
    
    
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
    @Xsql(id="XSQL_XFlow_Process_Summary" ,returnOne=true)
    public FlowProcess querySummary(FlowProcess i_Process);
    
}
