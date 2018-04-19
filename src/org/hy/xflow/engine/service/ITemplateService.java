package org.hy.xflow.engine.service;

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
     * 按模板ID查询模板信息
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
     * 按模板ID查询模板信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-19
     * @version     v1.0
     *
     * @param i_Template
     * @return
     */
    public Template queryByID(Template i_Template);
    
}
