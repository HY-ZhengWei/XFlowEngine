package org.hy.xflow.engine.service.impl;

import java.util.List;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.FlowComment;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.common.BaseService;
import org.hy.xflow.engine.dao.IFlowCommentDAO;
import org.hy.xflow.engine.dao.IFlowInfoDAO;
import org.hy.xflow.engine.enums.RouteTypeEnum;
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
    private IFlowInfoDAO    flowInfoDAO;
    
    @Xjava
    private IFlowCommentDAO flowCommendDAO;
    
    
    
    /**
     * 查询某一工作流模板下的所有活动的工作流实例。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-08-29
     * @version     v1.0
     *
     * @param i_TemplateID  工作流模板ID
     * @return
     */
    @Override
    public List<FlowInfo> queryActivitys(String i_TemplateID)
    {
        return this.flowInfoDAO.queryActivitys(i_TemplateID);
    }
    
    
    
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
    @Override
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
    @Override
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
    @Override
    public boolean createFlow(FlowInfo i_FlowInfo ,FlowProcess i_Process)
    {
        return this.flowInfoDAO.createFlow(i_FlowInfo ,i_Process);
    }
    
    
    
    /**
     * 工作流流转，并更新前一个流转信息。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-07
     * @version     v1.0
     *
     * @param i_Flow          工作流实例对象
     * @param i_ProcessList   新流转的过程集合信息（分单时会有多个，正常情况下均只有一个元素）
     * @param i_Previous      前一个流转的过程信息
     * @return
     */
    @Override
    public boolean toNext(FlowInfo i_Flow ,List<FlowProcess> i_ProcessList ,FlowProcess i_Previous)
    {
        if ( i_ProcessList.size() == 1 )
        {
            if ( RouteTypeEnum.$ToSum.equals(RouteTypeEnum.get(i_Previous.getOperateTypeID())) )
            {
                return this.flowInfoDAO.toNextSummary(i_Flow ,i_ProcessList.get(0) ,i_Previous);
            }
            else if ( RouteTypeEnum.$Reject_Team.equals(RouteTypeEnum.get(i_Previous.getOperateTypeID())) )
            {
                return this.flowInfoDAO.toNextRejectTeam(i_Flow ,i_ProcessList.get(0) ,i_Previous);
            }
            else
            {
                return this.flowInfoDAO.toNext(i_Flow ,i_ProcessList.get(0) ,i_Previous);
            }
        }
        else
        {
            return this.flowInfoDAO.toNextSplit(i_Flow ,i_ProcessList ,i_Previous);
        }
    }
    
    
    
    /**
     * 工作流实例ID，全流转结束后转历史
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-11
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    @Override
    public boolean toHistory(String i_WorkID)
    {
        return this.flowInfoDAO.toHistory(i_WorkID);
    }
    
    
    
    /**
     * 按第三方使用系统的业务数据ID，全流转结束后转历史
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-11
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    @Override
    public boolean toHistoryByServiceDataID(String i_ServiceDataID)
    {
        return this.flowInfoDAO.toHistoryByServiceDataID(i_ServiceDataID);
    }
    
    
    
    /**
     * 工作流实例ID，查询工作流备注信息（活动及历史的均查询）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-07-27
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    @Override
    public List<FlowComment> queryCommentByWorkID(String i_WorkID)
    {
        return this.flowCommendDAO.queryByWorkID(i_WorkID);
    }
    
    
    
    /**
     * 按第三方使用系统的业务数据ID，查询工作流备注信息（活动及历史的均查询）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-07-27
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    @Override
    public List<FlowComment> queryCommentByServiceDataID(String i_ServiceDataID)
    {
        return this.flowCommendDAO.queryByServiceDataID(i_ServiceDataID);
    }
    
    
    
    /**
     * 添加工作流备注信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-07-27
     * @version     v1.0
     *
     * @param io_FlowComment
     * @return
     */
    @Override
    public boolean addComment(FlowComment io_FlowComment)
    {
        if ( Help.isNull(io_FlowComment.getFcID()) )
        {
            io_FlowComment.setFcID(StringHelp.getUUID());
        }
        
        io_FlowComment.setCreateTime(new Date());
        return this.flowCommendDAO.addFlowComment(io_FlowComment) == 1;
    }
    
    
    
    /**
     * 添加工作流备注信息（转历史的）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-08-04
     * @version     v1.0
     *
     * @param i_FlowComment
     * @return
     */
    @Override
    public boolean addCommentToHistory(FlowComment io_FlowComment)
    {
        if ( Help.isNull(io_FlowComment.getFcID()) )
        {
            io_FlowComment.setFcID(StringHelp.getUUID());
        }
        
        io_FlowComment.setCreateTime(new Date());
        return this.flowCommendDAO.addFlowCommentToHistory(io_FlowComment) == 1;
    }
    
}
