package org.hy.xflow.engine.service;

import java.util.List;

import org.hy.common.PartitionMap;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.FutureOperator;
import org.hy.xflow.engine.bean.ProcessParticipant;
import org.hy.xflow.engine.bean.Template;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.enums.ParticipantTypeEnum;





/**
 * 工作流未来操作人(实时数据)的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-15
 * @version     v1.0
 *              v2.0  2024-02-23  添加：按人员信息查询待办时，可按流程模板名称过滤
 *              v3.0  2024-04-10  添加：为汇签过期，自动完成汇签而暂时添加未来参与人
 *              v4.0  2024-05-06  添加：待办查询：可按活动节点Code查询
 */
public interface IFlowFutureOperatorService
{
    
    
    /**
     * 获取用户可以处理（或叫待办）的工作流实例ID。
     * 
     *   1. 通过用户ID查询
     *   2. 通过部门ID查询
     *   3. 通过角色ID查询，支持多角色。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *              v2.0  2024-02-23  添加：流程模板名称的查询条件
     * 
     * @param i_User          流程用户
     * @param i_TemplateName  流程模板名称
     * @return
     */
    public List<String> queryWorkIDs(User i_User ,String i_TemplateName);
    
    
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询（按流程模板名称过滤）
     * 
     *   Map.key分区为参与人的形式的值：objectType:objectID
     *   Map.value元素为工作流实例ID：workID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-23
     * @version     v1.0
     *
     * @return
     */
    public PartitionMap<String ,FutureOperator> queryAllByForTemplateName();
    
    
    
    /**
     * 获取用户可以处理（或叫待办）的工作流实例对应的第三方使用系统的业务数据ID。
     * 
     *   1. 通过用户ID查询
     *   2. 通过部门ID查询
     *   3. 通过角色ID查询，支持多角色。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *              v2.0  2024-02-23  添加：流程模板名称的查询条件
     * 
     * @param i_User          流程用户
     * @param i_TemplateName  流程模板名称
     * @return
     */
    public List<String> queryServiceDataIDs(User i_User ,String i_TemplateName);
    
    
    
    /**
     * 获取用户可以处理（或叫待办）的工作流实例ID。
     * 
     *   1. 通过用户ID查询
     *   2. 通过部门ID查询
     *   3. 通过角色ID查询，支持多角色。
     *   4. 通过模板、活动Code查询
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-06
     * @version     v1.0
     * 
     * @param i_User          流程用户
     * @param i_TemplateName  流程模板名称
     * @param i_ActivityCode  工作流活动Code。作为与外界交互的编码。同一版本的工作流下是惟一的，不同版本的同类工作流可以相同（非空、必填）
     * @return
     */
    public List<String> queryWorkIDs(User i_User ,String i_TemplateName ,String i_ActivityCode);
    
    
    
    /**
     * 获取用户可以处理（或叫待办）的工作流实例对应的第三方使用系统的业务数据ID。
     * 
     *   1. 通过用户ID查询
     *   2. 通过部门ID查询
     *   3. 通过角色ID查询，支持多角色。
     *   4. 通过模板、活动Code查询
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-05-06
     * @version     v1.0
     * 
     * @param i_User          流程用户
     * @param i_TemplateName  流程模板名称
     * @param i_ActivityCode  工作流活动Code。作为与外界交互的编码。同一版本的工作流下是惟一的，不同版本的同类工作流可以相同（非空、必填）
     * @return
     */
    public List<String> queryServiceDataIDs(User i_User ,String i_TemplateName ,String i_ActivityCode);
    
    
    
    /**
     * 查询人员的参与类型，是执行类型还是抄送类型
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-10
     * @version     v1.0
     *
     * @param i_User    参与人
     * @param i_WorkID  工作流实例ID
     * @return
     */
    public ParticipantTypeEnum queryParticipantType(User i_User ,String i_WorkID);
    
    
    
    /**
     * 初始化缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     */
    public void initCaches();
    
    
    
    /**
     * 更新缓存中的未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *              v2.0  2024-02-23  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    public void updateCache(FlowProcess i_Process ,Template i_Template);
    
    
    
    /**
     * 为汇签过期，自动完成汇签而暂时添加未来参与人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-10
     * @version     v1.0
     *
     * @param i_Part  未来参与人
     */
    public void addCacheByCounterSignatureExpire(ProcessParticipant i_Part);
    
    
    
    /**
     * 向缓存中添加未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *              v2.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
     *              v3.0  2024-02-23  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    public void addCache(FlowProcess i_Process ,Template i_Template);
    
    
    
    /**
     * 删除缓存中的未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *              v2.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
     *              v3.0  2024-02-23  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    public void delCache(FlowProcess i_Process ,Template i_Template);
    
    
    
    /**
     * 删除缓存中的当前工作流的所有未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-27
     * @version     v1.0
     *              v2.0  2024-02-26  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    public void delCacheByAll(FlowProcess i_Process ,Template i_Template);
    
    
    
    /**
     * 删除缓存中的未来操作人信息。在转历史单时触发。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-11
     * @version     v1.0
     *              v2.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
     *              v3.0  2024-02-23  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    public void delCacheToHistory(FlowProcess i_Process ,Template i_Template);
    
    
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询
     * 
     *   Map.key    分区为参与人的形式的值：objectType:objectID
     *   Map.value  元素为工作流未来操作人对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @return
     */
    public PartitionMap<String ,FutureOperator> queryAllByWorkID();
    
    
    
    /**
     * 业务ID找工作流ID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-12
     * @version     v1.0
     *
     * @param i_ServiceDataID  业务ID
     * @return                 返回工作流实例ID
     */
    public String querySToWorkID(String i_ServiceDataID);
    
    
    
    /**
     * 业务ID找工作流ID的高速缓存中添加新关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-12
     * @version     v1.0
     *
     * @param i_WorkID         工作流实例ID
     * @param i_ServiceDataID  业务ID
     */
    public void pushSToWorkID(String i_WorkID ,String i_ServiceDataID);
    
}
