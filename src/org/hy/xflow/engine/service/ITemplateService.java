package org.hy.xflow.engine.service;

import java.util.List;

import org.hy.xflow.engine.bean.ActivityInfo;
import org.hy.xflow.engine.bean.ActivityRoute;
import org.hy.xflow.engine.bean.Template;





/**
 * 工作流模板的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-19
 * @version     v1.0
 */
public interface ITemplateService
{
    
    /**
     * 查询所有工作流模板信息。内部组合生成关系数据网。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-10-31
     * @version     v1.0
     *
     * @return
     */
    public List<Template> queryAll();
    
    
    
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
    public Template queryByID(String i_TemplateID);
    
    
    
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
    public Template queryByID(Template i_Template);
    
    
    
    /**
     * 按模板名称查询版本号最大的有效的模板信息。内部组合生成关系数据网。
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
    public Template queryByNameMaxVersionNo(String i_TemplateName ,Integer i_VersionNo);
    
    
    
    /**
     * 按模板名称查询版本号最大的有效的模板信息。内部组合生成关系数据网。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     *
     * @param i_Template  模板对象
     *
     * @return
     */
    public Template queryByNameMaxVersionNo(Template i_Template);
    
    
    
    /**
     * 数据库更新成功后，应即时更新高速缓存
     *
     * @author      ZhengWei(HY)
     * @createDate  2018-11-17
     * @version     v1.0
     *
     * @param i_TemplateID
     * @return
     */
    public boolean refreshCache(String i_TemplateID);
    
    
    
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
    public boolean saves(String i_TemplateID ,List<ActivityInfo> i_Activitys ,List<ActivityRoute> i_Routes);
    
}
