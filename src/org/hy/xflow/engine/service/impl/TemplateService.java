package org.hy.xflow.engine.service.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.PartitionMap;
import org.hy.common.net.CommunicationListener;
import org.hy.common.net.data.CommunicationRequest;
import org.hy.common.net.data.CommunicationResponse;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.ActivityInfo;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.Participant;
import org.hy.xflow.engine.bean.ActivityRouteTree;
import org.hy.xflow.engine.bean.Template;
import org.hy.xflow.engine.common.BaseService;
import org.hy.xflow.engine.dao.IActivityInfoDAO;
import org.hy.xflow.engine.dao.IActivityParticipantsDAO;
import org.hy.xflow.engine.dao.IActivityRouteDAO;
import org.hy.xflow.engine.dao.IActivityRouteParticipantsDAO;
import org.hy.xflow.engine.dao.ITemplateDAO;
import org.hy.xflow.engine.service.ITemplateService;





/**
 * 工作流模板的服务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-19
 * @version     v1.0
 *              v2.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
 */
@Xjava
public class TemplateService extends BaseService implements ITemplateService ,CommunicationListener
{
    
    /** 已解释合成的模板实例的高速缓存。Map.key值是TemplateID */
    private static Map<String ,Template>  $CacheTemplates = new Hashtable<String ,Template>();
    
    
    
    @Xjava
    private ITemplateDAO                  templateDAO;
    
    @Xjava
    private IActivityInfoDAO              activityInfoDAO;
    
    @Xjava
    private IActivityRouteDAO             activityRouteDAO;
    
    @Xjava
    private IActivityParticipantsDAO      activityParticipantsDAO;
    
    @Xjava
    private IActivityRouteParticipantsDAO activityRouteParticipantsDAO;
    

    
    /**
     * 清空已解释合成的模板实例的高速缓存。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     *              v2.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
     */
    public static void clearCache()
    {
        $CacheTemplates.clear();
        
        TemplateService      v_This        = (TemplateService)XJava.getObject("TemplateService");
        CommunicationRequest v_RequestData = new CommunicationRequest();
        v_RequestData.setEventType(    v_This.getEventType());
        v_RequestData.setDataOperation("clearCache");
        v_RequestData.setDataXID(      Date.getNowTime().getFullMilli());
        v_This.clusterSyncFlowCache(v_RequestData);
    }
    
    
    
    /**
     * 查询所有工作流模板信息。内部组合生成关系数据网。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-10-31
     * @version     v1.0
     *
     * @return
     */
    public synchronized List<Template> queryAll()
    {
        List<Template> v_Tempaltes = this.templateDAO.queryAll();
        List<Template> v_Ret       = new ArrayList<Template>();
        
        if ( !Help.isNull(v_Tempaltes) )
        {
            for (Template v_Template : v_Tempaltes)
            {
                v_Ret.add(this.queryByID(v_Template));
            }
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 按模板ID查询模板信息。内部组合生成关系数据网。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-19
     * @version     v1.0
     *              v2.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
     *
     * @param i_TemplateID
     * @return
     */
    public Template queryByID(String i_TemplateID)
    {
        Template v_Template = queryByIDByTrue(i_TemplateID);
        
        CommunicationRequest v_RequestData = new CommunicationRequest();
        v_RequestData.setEventType(    this.getEventType());
        v_RequestData.setDataOperation("queryByID");
        v_RequestData.setDataXID(      i_TemplateID);
        this.clusterSyncFlowCache(v_RequestData);
        
        return v_Template;
    }
    
    
    
    /**
     * 按模板ID查询模板信息。内部组合生成关系数据网。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-19
     * @version     v1.0
     *
     * @param i_TemplateID
     * @return
     */
    private synchronized Template queryByIDByTrue(String i_TemplateID)
    {
        Template v_Template = $CacheTemplates.get(i_TemplateID);
        if ( v_Template != null )
        {
            return v_Template;
        }
        
        v_Template = this.templateDAO.queryByID(i_TemplateID);
        if ( v_Template == null )
        {
            return v_Template;
        }
        
        Map<String ,ActivityInfo>           v_AllActivitys       = this.activityInfoDAO             .queryByTemplateID(v_Template);
        PartitionMap<String ,ActivityRoute> v_AllRoutes          = this.activityRouteDAO            .queryByTemplateID(v_Template);
        PartitionMap<String ,Participant>   v_AllActivityPs      = this.activityParticipantsDAO     .queryByTemplateID(v_Template);
        PartitionMap<String ,Participant>   v_AllActivityRoutePs = this.activityRouteParticipantsDAO.queryByTemplateID(v_Template);
        ActivityRouteTree                   v_ARouteTree         = new ActivityRouteTree(v_AllActivitys ,v_AllRoutes ,v_AllActivityPs ,v_AllActivityRoutePs);
        
        v_Template.setActivityRouteTree(v_ARouteTree);
        
        $CacheTemplates.put(v_Template.getTemplateID() ,v_Template);
        
        return v_Template;
    }
    
    
    
    /**
     * 按模板ID查询模板信息。内部组合生成关系数据网。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-19
     * @version     v1.0
     *
     * @param i_Template
     * @return
     */
    public Template queryByID(Template i_Template)
    {
        return this.queryByID(i_Template.getTemplateID());
    }
    
    
    
    /**
     * 按模板名称查询版本号最大的有效的模板信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     * 
     * @param i_TemplateName  模板名称
     * @param i_VersionNo     模板版本号（其数值是递增型）（可选的）
     *
     * @return
     */
    public Template queryByNameMaxVersionNo(String i_TemplateName ,Integer i_VersionNo)
    {
        return this.templateDAO.queryByNameMaxVersionNo(i_TemplateName ,i_VersionNo);
    }
    
    
    
    /**
     * 按模板名称查询版本号最大的有效的模板信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     *
     * @param i_Template  模板对象
     *
     * @return
     */
    public Template queryByNameMaxVersionNo(Template i_Template)
    {
        return this.templateDAO.queryByNameMaxVersionNo(i_Template);
    }
    
    
    
    /**
     * 数据库更新成功后，应即时更新高速缓存
     *
     * @author      ZhengWei(HY)
     * @createDate  2018-11-17
     * @version     v1.0
     *              v2.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
     *
     * @param i_TemplateID
     * @return
     */
    public boolean refreshCache(String i_TemplateID)
    {
        try
        {
            $CacheTemplates.remove(i_TemplateID);
            
            CommunicationRequest v_RequestData = new CommunicationRequest();
            v_RequestData.setEventType(    this.getEventType());
            v_RequestData.setDataOperation("refreshCache");
            v_RequestData.setDataXID(      i_TemplateID);
            this.clusterSyncFlowCache(v_RequestData);
            
            
            Template v_Template = this.queryByID(i_TemplateID);
            if ( v_Template != null )
            {
                return true;
            }
        }
        catch (Exception exce)
        {
            exce.printStackTrace();
        }
        
        return false;
    }
    

    
    /**
     * 保存活动节点 及 活动路由
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-11-02
     * @version     v1.0
     *              v2.0  2018-11-17  添加：活动路由的保存
     * 
     * @param i_TemplateID  模板ID
     * @param i_Activitys   活动节点的集合
     * @param i_Routes      活动路由的集合
     * @return
     */
    public boolean saves(String i_TemplateID ,List<ActivityInfo> i_Activitys ,List<ActivityRoute> i_Routes)
    {
        Help.setValues(i_Activitys ,"templateID" ,i_TemplateID);
        Help.setValues(i_Routes    ,"templateID" ,i_TemplateID);
        
        boolean v_Ret = this.activityInfoDAO.saves(i_Activitys ,i_Routes);
        if ( !v_Ret )
        {
            return false;
        }
        
        this.refreshCache(i_TemplateID);
        
        return true;
    }
    
    
    
    /**
     *  数据通讯的事件类型。即通知哪一个事件监听者来处理数据通讯（对应 ServerSocket.listeners 的分区标识）
     *  
     *  事件类型区分大小写
     * 
     * @author      ZhengWei(HY)
     * @createDate  2017-01-16
     * @version     v1.0
     *
     * @return
     */
    public String getEventType()
    {
        return "CL_Template";
    }
    
    
    
    /**
     * 数据通讯事件的执行动作
     * 
     * @author      ZhengWei(HY)
     * @createDate  2020-01-02
     * @version     v1.0
     *
     * @param i_RequestData
     * @return
     */
    public CommunicationResponse communication(CommunicationRequest i_RequestData)
    {
        CommunicationResponse v_ResponseData = new CommunicationResponse();
        
        if ( Help.isNull(i_RequestData.getDataXID()) )
        {
            v_ResponseData.setResult(1);
            return v_ResponseData;
        }
        
        if ( "clearCache".equals(i_RequestData.getDataOperation()) )
        {
            $CacheTemplates.clear();
        }
        else if ( "refreshCache".equals(i_RequestData.getDataOperation()) )
        {
            $CacheTemplates.remove(i_RequestData.getDataXID());
            this.queryByID(i_RequestData.getDataXID());
        }
        else if ( "queryByID".equals(i_RequestData.getDataOperation()) )
        {
            this.queryByIDByTrue(i_RequestData.getDataXID());
        }
        
        return v_ResponseData;
    }
    
}
