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
 *              v2.0  2024-02-23  添加：按人员信息查询已办时，可按流程模板名称过滤
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
     * 工作流实例ID，查询工作流流转过程信息（历史单）。
     * 
     * 按时间倒排的。最新的，在首位。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-17
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    @Xsql("XSQL_XFlow_ProcessHistory_QueryByWorkID_ServiceDataID")
    public List<FlowProcess> queryHistoryByWorkID(@Xparam(id="workID" ,notNull=true) String i_WorkID);
    
    
    
    /**
     * 三方使用系统的业务数据ID，查询工作流流转过程信息（历史单）。
     * 
     * 按时间倒排的。最新的，在首位。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-17
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    @Xsql("XSQL_XFlow_ProcessHistory_QueryByWorkID_ServiceDataID")
    public List<FlowProcess> queryHistoryByServiceDataID(@Xparam(id="serviceDataID" ,notNull=true) String i_ServiceDataID);
    
    
    
    /**
     * 获取用户已处理过的工作流实例ID。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-06-11
     * @version     v1.0
     *              v2.0  2024-02-23  添加：流程模板名称的查询条件
     * 
     * @param i_UserID        流程用户ID
     * @param i_TemplateName  流程模板名称
     * @return
     */
    @Xsql("XSQL_XFlow_Process_queryWorkIDsByDone")
    public List<String> queryWorkIDsByDone(@Xparam(id="userID")       String i_UserID
                                          ,@Xparam(id="templateName") String i_TemplateName);
    
    
    
    /**
     * 获取用户已处理过的工作流实例对应的第三方使用系统的业务数据ID。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-06-11
     * @version     v1.0
     *              v2.0  2024-02-23  添加：流程模板名称的查询条件
     * 
     * @param i_UserID        流程用户ID
     * @param i_TemplateName  流程模板名称
     * @return
     */
    @Xsql("XSQL_XFlow_Process_queryServiceDataIDsByDone")
    public List<String> queryServiceDataIDsByDone(@Xparam(id="userID")       String i_UserID
                                                 ,@Xparam(id="templateName") String i_TemplateName);
    
    
    
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
    @Xsql("XSQL_XFlow_Process_Query_SummaryList")
    public List<FlowProcess> querySummarysByWorkID(@Xparam(id="workID" ,notNull=true) String i_WorkID);
    
    
    
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
    @Xsql("XSQL_XFlow_Process_Query_SummaryList")
    public List<FlowProcess> querySummarysByServiceDataID(@Xparam(id="serviceDataID" ,notNull=true) String i_ServiceDataID);
    
    
    
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
    @Xsql("XSQL_XFlow_Process_Query_SummaryList_History")
    public List<FlowProcess> querySummarysByWorkIDHistory(@Xparam(id="workID" ,notNull=true) String i_WorkID);
    
    
    
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
    @Xsql("XSQL_XFlow_Process_Query_SummaryList_History")
    public List<FlowProcess> querySummarysByServiceDataIDHistory(@Xparam(id="serviceDataID" ,notNull=true) String i_ServiceDataID);
    
}
