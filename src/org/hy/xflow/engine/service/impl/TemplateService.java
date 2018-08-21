package org.hy.xflow.engine.service.impl;

import java.util.Hashtable;
import java.util.Map;

import org.hy.common.PartitionMap;
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
 */
@Xjava
public class TemplateService extends BaseService implements ITemplateService
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
     *
     */
    public static void clearCache()
    {
        $CacheTemplates.clear();
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
    public synchronized Template queryByID(String i_TemplateID)
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
    
}
