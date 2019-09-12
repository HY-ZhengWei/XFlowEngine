package org.hy.xflow.engine.service;

import java.util.List;

import org.hy.common.PartitionMap;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.FutureOperator;
import org.hy.xflow.engine.bean.User;





/**
 * 工作流未来操作人(实时数据)的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-15
 * @version     v1.0
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
     *
     * @param i_User
     * @return
     */
    public List<String> queryWorkIDs(User i_User);
    
    
    
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
     *
     * @param i_User
     * @return
     */
    public List<String> queryServiceDataIDs(User i_User);
    
    
    
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
     *
     * @param i_Process
     */
    public void updateCache(FlowProcess i_Process);
    
    
    
    /**
     * 向缓存中添加未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @param i_Process
     */
    public void addCache(FlowProcess i_Process);
    
    
    
    /**
     * 删除缓存中的未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @param i_Process
     */
    public void delCache(FlowProcess i_Process);
    
    
    
    /**
     * 删除缓存中的未来操作人信息。在转历史单时触发。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-11
     * @version     v1.0
     *
     * @param i_Process
     */
    public void delCacheToHistory(FlowProcess i_Process);
    
    
    
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
