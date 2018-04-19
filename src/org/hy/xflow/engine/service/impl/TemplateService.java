package org.hy.xflow.engine.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.PartitionMap;
import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.ActivityInfo;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.ActivityRouteTree;
import org.hy.xflow.engine.bean.Template;
import org.hy.xflow.engine.common.BaseService;
import org.hy.xflow.engine.dao.IActivityInfoDAO;
import org.hy.xflow.engine.dao.IActivityRouteDAO;
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
    private ITemplateDAO      templateDAO;
    
    @Xjava
    private IActivityInfoDAO  activityInfoDAO;
    
    @Xjava
    private IActivityRouteDAO activityRouteDAO;
    
    
    
    public Template queryByID(String i_TemplateID)
    {
        Template v_Template = this.templateDAO.queryByID(i_TemplateID);
        if ( v_Template == null )
        {
            return v_Template;
        }
        
        Map<String ,ActivityInfo>           v_AllActivitys = this.activityInfoDAO .queryByTemplateID(v_Template);
        PartitionMap<String ,ActivityRoute> v_AllRoutes    = this.activityRouteDAO.queryByTemplateID(v_Template);
        ActivityRouteTree                   v_ARouteTree   = new ActivityRouteTree(v_AllActivitys ,v_AllRoutes);
        
        return v_Template;
    }
    
    
}
