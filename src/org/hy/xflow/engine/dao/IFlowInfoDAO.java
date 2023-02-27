package org.hy.xflow.engine.dao;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.FlowProcess;





/**
 * 工作流模板表的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-25
 * @version     v1.0
 *              v2.0  2019-09-12  添加：支持多路并行路由的流程
 */
@Xjava(id="FlowInfoDAO" ,value=XType.XSQL)
public interface IFlowInfoDAO
{
    
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
    @Xsql(id="XSQL_XFlow_FlowInfo_QueryByTemplateID_ForActivity")
    public List<FlowInfo> queryActivitys(@Xparam(id="flowTemplateID") String i_TemplateID);
    
    
    
    /**
     * 工作流实例ID，查询工作流实例（活动及历史的均查询）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-27
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return
     */
    @Xsql(id="XSQL_XFlow_FlowInfo_QueryByWorkID_ServiceDataID" ,returnOne=true)
    public FlowInfo queryByWorkID(@Xparam(id="workID" ,notNull=true) String i_WorkID);
    
    
    
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
    @Xsql(id="XSQL_XFlow_FlowInfo_QueryByWorkID_ServiceDataID" ,returnOne=true)
    public FlowInfo queryByServiceDataID(@Xparam(id="serviceDataID" ,notNull=true) String i_ServiceDataID);
    
    
    
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
    @Xsql("GXSQL_Flow_Create")
    public boolean createFlow(@Xparam("flow")    FlowInfo    i_FlowInfo
                             ,@Xparam("process") FlowProcess i_Process);
    
    
    
    /**
     * 工作流流转，并更新前一个流转信息。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-07
     * @version     v1.0
     *
     * @param i_Flow      工作流实例对象
     * @param i_Process   新流转的过程集合信息（分单时会有多个，正常情况下均只有一个元素）
     * @param i_Previous  前一个流转的过程信息
     * @return
     */
    @Xsql("GXSQL_Flow_ToNext")
    public boolean toNext(@Xparam("flow")     FlowInfo    i_Flow
                         ,@Xparam("process")  FlowProcess i_Process
                         ,@Xparam("previous") FlowProcess i_Previous);
    
    
    
    
    /**
     * 分单多路的工作流流转，并更新前一个流转信息。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-16
     * @version     v1.0
     *
     * @param i_Flow          工作流实例对象
     * @param i_ProcessList   新流转的过程集合信息（分单时会有多个，正常情况下均只有一个元素）
     * @param i_Previous      前一个流转的过程信息
     * @return
     */
    @Xsql("GXSQL_Flow_ToNext_Split")
    public boolean toNextSplit(@Xparam("flow")        FlowInfo          i_Flow
                              ,@Xparam("processList") List<FlowProcess> i_ProcessList
                              ,@Xparam("previous")    FlowProcess       i_Previous);
    
    
    
    /**
     * 分单多路后的汇总，工作流流转，并更新前一个流转信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-16
     * @version     v1.0
     *
     * @param i_Flow      工作流实例对象
     * @param i_Process   新流转的过程集合信息（分单时会有多个，正常情况下均只有一个元素）
     * @param i_Previous  前一个流转的过程信息
     * @return
     */
    @Xsql("GXSQL_Flow_ToNext_Summary")
    public boolean toNextSummary(@Xparam("flow")     FlowInfo    i_Flow
                                ,@Xparam("process")  FlowProcess i_Process
                                ,@Xparam("previous") FlowProcess i_Previous);
    
    
    
    /**
     * 分单多路后的自由驳回（协同模式），驳回到指定节点，更新之前所有多路流转路由
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-27
     * @version     v1.0
     *
     * @param i_Flow      工作流实例对象
     * @param i_Process   新流转的过程集合信息（分单时会有多个，正常情况下均只有一个元素）
     * @param i_Previous  前一个流转的过程信息
     * @return
     */
    @Xsql("GXSQL_Flow_ToNext_RejectTeam")
    public boolean toNextRejectTeam(@Xparam("flow")     FlowInfo    i_Flow
                                   ,@Xparam("process")  FlowProcess i_Process
                                   ,@Xparam("previous") FlowProcess i_Previous);
    
    
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
    @Xsql("GXSQL_Flow_ToHistory")
    public boolean toHistory(@Xparam(id="workID" ,notNull=true) String i_WorkID);
    
    
    
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
    @Xsql("GXSQL_Flow_ToHistory")
    public boolean toHistoryByServiceDataID(@Xparam(id="serviceDataID" ,notNull=true) String i_ServiceDataID);
    
}
