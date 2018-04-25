package org.hy.xflow.engine.service.impl;

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
     * 按模板ID查询模板信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-19
     * @version     v1.0
     *
     * @param i_TemplateID
     * @return
     */
    public Template queryByID(String i_TemplateID)
    {
        Template v_Template = this.templateDAO.queryByID(i_TemplateID);
        
        if ( v_Template == null )
        {
            return v_Template;
        }
        
        Map<String ,ActivityInfo>           v_AllActivitys       = this.activityInfoDAO             .queryByTemplateID(v_Template);
        PartitionMap<String ,ActivityRoute> v_AllRoutes          = this.activityRouteDAO            .queryByTemplateID(v_Template);
        PartitionMap<String ,Participant>   v_AllActivityPs      = this.activityRouteParticipantsDAO.queryByTemplateID(v_Template);
        PartitionMap<String ,Participant>   v_AllActivityRoutePs = this.activityRouteParticipantsDAO.queryByTemplateID(v_Template);
        ActivityRouteTree                   v_ARouteTree         = new ActivityRouteTree(v_AllActivitys ,v_AllRoutes ,v_AllActivityPs ,v_AllActivityRoutePs);
        
        v_Template.setActivityRouteTree(v_ARouteTree);
        
        return v_Template;
    }
    
    
    
    /**
     * 按模板ID查询模板信息
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
    
}
