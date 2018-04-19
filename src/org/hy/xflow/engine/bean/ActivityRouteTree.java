package org.hy.xflow.engine.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.PartitionMap;





/**
 * 活动路由树。它体现的是某一模板的活动路由树信息。
 * 
 * 通过活动表、路由表组合生成的。
 * 
 * 此类自身就是整个活动路由树的 “开始”活动节点。
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-19
 * @version     v1.0
 */
public class ActivityRouteTree extends ActivityInfo
{
    
    private static final long serialVersionUID = 5366730038571520921L;
    

    /** 模板的所有活动 */
    private Map<String ,ActivityInfo>           allActivitys;
    
    /** 模板的所有路由 */
    private PartitionMap<String ,ActivityRoute> allRoutes;
    
    
    
    public ActivityRouteTree(Map<String ,ActivityInfo> i_AllActivitys ,PartitionMap<String ,ActivityRoute> i_AllRoutes)
    {
        this.allActivitys = i_AllActivitys;
        this.allRoutes    = i_AllRoutes;
        
        this.makeActivityRouteTree();
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
        if ( Help.isNull(allActivitys) || Help.isNull(allRoutes) )
        {
            return;
        }
        
        this.makeActivityRouteTree(this);
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
        List<ActivityRoute> v_CurrentARoutes = allRoutes.get(io_CurrentActivity.getActivityID());
        
        if ( !Help.isNull(v_CurrentARoutes) )
        {
            io_CurrentActivity.setNextActivitys(new ArrayList<ActivityInfo>());
            
            for (ActivityRoute v_CurrentARoute : v_CurrentARoutes)
            {
                if ( !Help.isNull(v_CurrentARoute.getNextActivityID()) )
                {
                    ActivityInfo v_NextActivity = allActivitys.get(v_CurrentARoute.getNextActivityID());
                    
                    if ( v_NextActivity != null )
                    {
                        io_CurrentActivity.getNextActivitys().add(v_NextActivity);
                        
                        makeActivityRouteTree(v_NextActivity);
                    }
                }
            }
        }
    }
    
}
