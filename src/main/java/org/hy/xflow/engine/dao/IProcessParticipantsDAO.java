package org.hy.xflow.engine.dao;

import java.util.List;

import org.hy.common.PartitionMap;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.FlowComment;
import org.hy.xflow.engine.bean.FlowData;
import org.hy.xflow.engine.bean.ProcessParticipant;





/**
 * 工作流过程的动态参与人的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-08
 * @version     v1.0
 *              v2.0  2024-02-23  添加：按人员信息查询督查时，可按流程模板名称过滤
 *                                添加：按人员信息查询督办时，可按流程模板名称过滤
 *              v3.0  2024-04-10  添加：添加参与人
 */
@Xjava(id="ProcessParticipantsDAO" ,value=XType.XSQL)
public interface IProcessParticipantsDAO
{
    
    /**
     * 工作流实例ID，查询工作流流转过程的动态参与人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-09
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return          Map.分区为：工作流的过程ID
     */
    @Xsql("XSQL_XFlow_ProcessParticipants_QueryByWorkID_ServiceDataID")
    public PartitionMap<String ,ProcessParticipant> queryByWorkID(@Xparam(id="workID" ,notNull=true) String i_WorkID);
    
    
    
    /**
     * 三方使用系统的业务数据ID，查询工作流流转过程的动态参与人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-09
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return                 Map.分区为：工作流的过程ID
     */
    @Xsql("XSQL_XFlow_ProcessParticipants_QueryByWorkID_ServiceDataID")
    public PartitionMap<String ,ProcessParticipant> queryByServiceDataID(@Xparam(id="serviceDataID" ,notNull=true) String i_ServiceDataID);
    
    
    
    /**
     * 查询工作流流转过程抄送人相关的流程信息（督办）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-01
     * @version     v1.0
     *              v2.0  2024-02-23  添加：按人员信息查询督办时，可按流程模板名称过滤
     *
     * @param i_FlowData  工作流接口数据
     * @return
     */
    @Xsql("XSQL_XFlow_ProcessParticipants_QueryBySupervise")
    public List<ProcessParticipant> queryBySupervise(FlowData i_FlowData);
    
    
    
    /**
     * 查询历史工作流流转过程抄送人相关的流程信息（督查）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-01
     * @version     v1.0
     *              v2.0  2024-02-23  添加：按人员信息查询督查时，可按流程模板名称过滤
     *
     * @param i_FlowData  工作流接口数据
     * @return
     */
    @Xsql("XSQL_XFlow_ProcessParticipants_QueryBySupervision")
    public List<ProcessParticipant> queryBySupervision(FlowData i_FlowData);
    
    
    
    /**
     * 查询人员在工作流实例中的最大参与者角色是什么
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-07-27
     * @version     v1.0
     *
     * @param i_FlowComment
     * @return
     */
    @Xsql(id="XSQL_XFlow_ProcessParticipants_QueryByMinObjectType" ,returnOne=true)
    public ProcessParticipant queryByMinObjectType(FlowComment i_FlowComment);
    
    
    
    /**
     * 添加参与人（仅用于汇签过期时，添加系统为参与人，执行汇签完成）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-10
     * @version     v1.0
     *
     * @param i_ProcessParticipant  参与人信息
     * @return
     */
    @Xsql(id="XSQL_XFlow_ProcessParticipants_Insert" ,batch=true)
    public int insert(ProcessParticipant i_ProcessParticipant);
    
}
