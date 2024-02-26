package org.hy.xflow.engine.service.impl;

import java.util.List;

import org.hy.common.Help;
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
 *              v2.0  2024-02-23  添加：按人员信息查询已办时，可按流程模板名称过滤
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
     *              v2.0  2019-09-12  添加：历史单的查询
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    @Override
    public List<FlowProcess> queryByWorkID(String i_WorkID)
    {
        List<FlowProcess> v_Ret = this.flowProcessDAO.queryByWorkID(i_WorkID);
        
        if ( Help.isNull(v_Ret) )
        {
            v_Ret = this.flowProcessDAO.queryHistoryByWorkID(i_WorkID);
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 三方使用系统的业务数据ID，查询工作流流转过程信息。
     * 
     * 按时间倒排的。最新的，在首位。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-27
     * @version     v1.0
     *              v2.0  2019-09-12  添加：历史单的查询
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    @Override
    public List<FlowProcess> queryByServiceDataID(String i_ServiceDataID)
    {
        List<FlowProcess> v_Ret = this.flowProcessDAO.queryByServiceDataID(i_ServiceDataID);
        
        if ( Help.isNull(v_Ret) )
        {
            v_Ret = this.flowProcessDAO.queryHistoryByServiceDataID(i_ServiceDataID);
        }
        
        return v_Ret;
    }
    
    
    
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
    @Override
    public List<String> queryWorkIDsByDone(User i_User ,String i_TemplateName)
    {
        return this.flowProcessDAO.queryWorkIDsByDone(i_User.getUserID() ,i_TemplateName);
    }
    
    
    
    /**
     * 获取用户已处理过的工作流实例对应的第三方使用系统的业务数据ID。
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
    @Override
    public List<String> queryServiceDataIDsByDone(User i_User ,String i_TemplateName)
    {
        return this.flowProcessDAO.queryServiceDataIDsByDone(i_User.getUserID() ,i_TemplateName);
    }
    
    
    
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
    @Override
    public FlowProcess querySummary(FlowProcess i_Process)
    {
        return this.flowProcessDAO.querySummary(i_Process);
    }
    
    
    
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
    @Override
    public List<FlowProcess> querySummarysByWorkID(String i_WorkID)
    {
        List<FlowProcess> v_Ret = this.flowProcessDAO.querySummarysByWorkID(i_WorkID);
        
        if ( Help.isNull(v_Ret) )
        {
            v_Ret = this.flowProcessDAO.querySummarysByWorkIDHistory(i_WorkID);
        }
        
        return v_Ret;
    }
    
    
    
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
    @Override
    public List<FlowProcess> querySummarysByServiceDataID(String i_ServiceDataID)
    {
        List<FlowProcess> v_Ret = this.flowProcessDAO.querySummarysByServiceDataID(i_ServiceDataID);
        
        if ( Help.isNull(v_Ret) )
        {
            v_Ret = this.flowProcessDAO.querySummarysByServiceDataIDHistory(i_ServiceDataID);
        }
        
        return v_Ret;
    }
    
}
