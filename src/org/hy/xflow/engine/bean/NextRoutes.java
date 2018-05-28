package org.hy.xflow.engine.bean;

import java.util.List;

import org.hy.common.PartitionMap;
import org.hy.xflow.engine.common.BaseModel;





/**
 * 查询用户可以走的路由的数据结构。
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-28
 * @version     v1.0
 */
public class NextRoutes extends BaseModel
{
    private static final long serialVersionUID = -8064344771494245788L;

    
    
    /** 工作流实例。应包含所有流转信息，即flow.processes有值 */
    private FlowInfo                                 flow;
    
    /** 当前流转信息 */
    private FlowProcess                              currentProcess;
    
    /** 当前活动节点 */
    private ActivityInfo                             currentActivity;
    
    /** 
     * 工作流流转过程的动态参与人信息（所有流转过程的所有动态参与人）
     * Map.key 是流转ID
     */
    private PartitionMap<String ,ProcessParticipant> flowParticipants;
    
    /** 查询用户可以走的路由 */
    private List<ActivityRoute>                      routes;
    
    
    
    public NextRoutes()
    {
        
    }
    
    
    
    public NextRoutes(FlowInfo                                 i_Flow 
                     ,FlowProcess                              i_CurrentProcess 
                     ,ActivityInfo                             i_CurrentActivity 
                     ,PartitionMap<String ,ProcessParticipant> i_FlowParticipants
                     ,List<ActivityRoute>                      i_Routes)
    {
        this.flow             = i_Flow;
        this.currentProcess   = i_CurrentProcess;
        this.currentActivity  = i_CurrentActivity;
        this.flowParticipants = i_FlowParticipants;
        this.routes           = i_Routes;
    }
    

    
    /**
     * 获取：工作流实例。应包含所有流转信息，即flow.processes有值
     */
    public FlowInfo getFlow()
    {
        return flow;
    }
    

    
    /**
     * 获取：当前流转信息
     */
    public FlowProcess getCurrentProcess()
    {
        return currentProcess;
    }
    

    
    /**
     * 获取：当前活动节点
     */
    public ActivityInfo getCurrentActivity()
    {
        return currentActivity;
    }
    

    
    /**
     * 设置：工作流实例。应包含所有流转信息，即flow.processes有值
     * 
     * @param flow 
     */
    public void setFlow(FlowInfo flow)
    {
        this.flow = flow;
    }
    

    
    /**
     * 设置：当前流转信息
     * 
     * @param currentProcess 
     */
    public void setCurrentProcess(FlowProcess currentProcess)
    {
        this.currentProcess = currentProcess;
    }
    

    
    /**
     * 设置：当前活动节点
     * 
     * @param currentActivity 
     */
    public void setCurrentActivity(ActivityInfo currentActivity)
    {
        this.currentActivity = currentActivity;
    }


    
    /**
     * 获取：查询用户可以走的路由
     */
    public List<ActivityRoute> getRoutes()
    {
        return routes;
    }
    


    /**
     * 设置：查询用户可以走的路由
     * 
     * @param routes 
     */
    public void setRoutes(List<ActivityRoute> routes)
    {
        this.routes = routes;
    }


    
    /**
     * 获取：工作流流转过程的动态参与人信息（所有流转过程的所有动态参与人）
     *      Map.key 是流转ID
     */
    public PartitionMap<String ,ProcessParticipant> getFlowParticipants()
    {
        return flowParticipants;
    }
    

    
    /**
     * 设置：工作流流转过程的动态参与人信息（所有流转过程的所有动态参与人）
     *      Map.key 是流转ID
     * 
     * @param flowParticipants 
     */
    public void setFlowParticipants(PartitionMap<String ,ProcessParticipant> flowParticipants)
    {
        this.flowParticipants = flowParticipants;
    }
    
}
