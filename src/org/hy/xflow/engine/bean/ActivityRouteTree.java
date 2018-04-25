package org.hy.xflow.engine.bean;

import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.PartitionMap;
import org.hy.xflow.engine.common.BaseModel;





/**
 * 活动路由树。它体现的是某一模板的活动路由树信息。
 * 
 * 通过活动表、路由表组合生成的。
 * 
 * @author      ZhengWei(HY)
 * @createDate  2018-04-19
 * @version     v1.0
 */
public class ActivityRouteTree extends BaseModel
{
    
    private static final long serialVersionUID = 5366730038571520921L;
    

    /** 模板的所有活动 */
    private Map<String ,ActivityInfo>           allActivitys;
    
    /** 模板的所有路由 */
    private PartitionMap<String ,ActivityRoute> allRoutes;
    
    /** 模板的活动路由的所有参与人 */
    private PartitionMap<String ,Participant>   allActivityPs;
    
    /** 模板的活动路由的所有参与人 */
    private PartitionMap<String ,Participant>   allActivityRoutePs;
    
    /** 整个活动路由树的 "开始" 活动节点 */
    private ActivityInfo                        startActivity;
    
    /** 整个活动路由树的 "结束" 活动节点 */
    private ActivityInfo                        endActivity;
    
    
    
    public ActivityRouteTree(Map<String ,ActivityInfo>           i_AllActivitys 
                            ,PartitionMap<String ,ActivityRoute> i_AllRoutes
                            ,PartitionMap<String ,Participant>   i_AllActivityPs
                            ,PartitionMap<String ,Participant>   i_AllActivityRoutePs)
    {
        this.allActivitys       = i_AllActivitys;
        this.allRoutes          = i_AllRoutes;
        this.allActivityPs      = i_AllActivityPs;
        this.allActivityRoutePs = i_AllActivityRoutePs;
        
        if ( !Help.isNull(this.allActivitys) )
        {
            this.startActivity = this.allActivitys.values().iterator().next();
            for (ActivityInfo v_Activity : this.allActivitys.values())
            {
                this.endActivity = v_Activity;
            }
        }
        
        this.makeActivityRouteTree();
    }
    
    
    
    /**
     * 整个活动路由树的 "开始" 活动节点
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-24
     * @version     v1.0
     *
     * @return
     */
    public ActivityInfo getStartActivity()
    {
        return this.startActivity;
    }
    
    
    
    /**
     * 整个活动路由树的 "结束" 活动节点
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-24
     * @version     v1.0
     *
     * @return
     */
    public ActivityInfo getEndActivity()
    {
        return this.endActivity;
    }
    
    
    
    /**
     * 按活动ID获取活动对象。通过它可进入步获取下一步的所有节点 
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-19
     * @version     v1.0
     *
     * @param i_ActivityID
     * @return
     */
    public ActivityInfo getActivity(String i_ActivityID)
    {
        return this.allActivitys.get(i_ActivityID);
    }
    
    
    
    /**
     * 按活动路由生成活动路由树
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-19
     * @version     v1.0
     *
     */
    public void makeActivityRouteTree()
    {
        if ( Help.isNull(allActivitys) 
          || Help.isNull(allRoutes) 
          || Help.isNull(allActivityPs)
          || Help.isNull(allActivityRoutePs)
          || this.startActivity == null 
          || this.endActivity   == null )
        {
            return;
        }
        
        this.makeActivityRouteTree(this.startActivity);
    }
    
    
    
    /**
     * 按活动路由生成活动路由树（递归）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-19
     * @version     v1.0
     *
     * @param io_CurrentActivity  当前活动
     */
    private void makeActivityRouteTree(ActivityInfo io_CurrentActivity)
    {
        io_CurrentActivity.setParticipants(allActivityPs.get(io_CurrentActivity.getActivityID()));
        List<ActivityRoute> v_CurrentARoutes = allRoutes.get(io_CurrentActivity.getActivityID());
        
        System.out.println(log(io_CurrentActivity));
        
        if ( !Help.isNull(v_CurrentARoutes) )
        {
            io_CurrentActivity.setRoutes(v_CurrentARoutes);
            
            for (ActivityRoute v_CurrentARoute : v_CurrentARoutes)
            {
                v_CurrentARoute.setActivity(io_CurrentActivity);
                v_CurrentARoute.setParticipants(allActivityRoutePs.get(v_CurrentARoute.getActivityRouteID()));
                
                if ( !Help.isNull(v_CurrentARoute.getNextActivityID()) )
                {
                    ActivityInfo v_NextActivity = allActivitys.get(v_CurrentARoute.getNextActivityID());
                    
                    if ( v_NextActivity != null )
                    {
                        v_CurrentARoute.setNextActivity(v_NextActivity);
                        
                        System.out.println(log(v_CurrentARoute));
                        
                        if ( !Help.isNull(v_NextActivity.getRoutes()) )
                        {
                            // 已经有路由信息的将不再递归，一般为自循环活动或驳回的活动。防止死循环。
                        }
                        else if ( v_NextActivity == this.startActivity )
                        {
                            // 不等于开始活动才递归。防止死循环
                        }
                        else if ( v_NextActivity == this.endActivity )
                        {
                            // 不等于结束活动才递归。防止死循环
                        }
                        else
                        {
                            makeActivityRouteTree(v_NextActivity);
                        }
                    }
                }
            }
        }
    }
    
    
    
    /**
     * 输出日志
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     *
     * @param i_Activity
     * @return
     */
    private String log(ActivityInfo i_Activity)
    {
        StringBuilder v_Log = new StringBuilder();
        
        v_Log.append("\n").append(i_Activity.getActivityName());
        
        if ( !Help.isNull(i_Activity.getParticipants()) )
        {
            for (int i=0; i<i_Activity.getParticipants().size(); i++)
            {
                Participant v_Participant = i_Activity.getParticipants().get(i);
                v_Log.append("\n\t参与人").append(i+1).append("  ").append(v_Participant.getParticipantType().getParticipantType()).append(":").append(v_Participant.getObjectName());
            }
            
            v_Log.append("\n");
        }
        
        return v_Log.toString();
    }
    
    
    
    /**
     * 输出日志
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-24
     * @version     v1.0
     *
     * @param i_CurrentARoute
     * @return
     */
    private String log(ActivityRoute i_CurrentARoute)
    {
        StringBuilder v_Log = new StringBuilder();
        
        v_Log.append("【").append(i_CurrentARoute.getActivity().getActivityName()).append("】");
        v_Log.append("  ---").append(i_CurrentARoute.getActivityRouteName()).append("--->  ");
        v_Log.append("【").append(i_CurrentARoute.getNextActivity().getActivityName()).append("】");
        
        if ( !Help.isNull(i_CurrentARoute.getParticipants()) )
        {
            for (int i=0; i<i_CurrentARoute.getParticipants().size(); i++)
            {
                Participant v_Participant = i_CurrentARoute.getParticipants().get(i);
                v_Log.append("\n\t参与人").append(i+1).append("  ").append(v_Participant.getParticipantType().getParticipantType()).append(":").append(v_Participant.getObjectName());
            }
            
            v_Log.append("\n");
        }
        
        return v_Log.toString();
    }
    
}
