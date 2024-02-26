package org.hy.xflow.engine.service;

import java.util.List;

import org.hy.common.PartitionMap;
import org.hy.xflow.engine.bean.FlowComment;
import org.hy.xflow.engine.bean.FlowData;
import org.hy.xflow.engine.bean.ProcessParticipant;





/**
 * 工作流过程的动态参与人的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-08
 * @version     v1.0
 *              v2.0  2024-02-23  添加：按人员信息查询督查时，可按流程模板名称过滤
 *                                添加：按人员信息查询督办时，可按流程模板名称过滤
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
     * @return          Map.分区为：工作流的过程ID
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
     * @return                 Map.分区为：工作流的过程ID
     */
    public PartitionMap<String ,ProcessParticipant> queryByServiceDataID(String i_ServiceDataID);
    
    
    
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
    public ProcessParticipant queryByMinObjectType(FlowComment i_FlowComment);
    
}
