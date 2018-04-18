package org.hy.xflow.engine.dao;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.Template;





/**
 * 工作流模板表的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-18
 * @version     v1.0
 */
@Xjava(id="TemplateDAO" ,value=XType.XSQL)
public interface ITemplateDAO
{
    
    /**
     * 按模板ID查询模板信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-18
     * @version     v1.0
     *
     * @param i_TemplateID  模板ID
     * @return
     */
    @Xsql(id="XSQL_XFlow_Template_QueryByID_NameVersion" ,returnOne=true)
    public Template queryByID(@Xparam("templateID") String i_TemplateID);
    
    
    
    /**
     * 按模板ID查询模板信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-18
     * @version     v1.0
     *
     * @param i_Template  模板对象
     * @return
     */
    @Xsql(id="XSQL_XFlow_Template_QueryByID_NameVersion" ,returnOne=true)
    public Template queryByID(@Xparam(notNulls={"templateID"}) Template i_Template);
    
    
    
    /**
     * 按模板名称及版本号查询模板信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-18
     * @version     v1.0
     *
     * @param i_TemplateID  模板ID
     * @param i_Version     版本号
     * @return
     */
    @Xsql(id="XSQL_XFlow_Template_QueryByID_NameVersion" ,returnOne=true)
    public Template queryByNameVersion(@Xparam(id="templateName" ,notNull=true) String i_TemplateID
                                      ,@Xparam(id="version"      ,notNull=true) String i_Version);
    
    
    
    /**
     * 按模板名称及版本号查询模板信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-18
     * @version     v1.0
     *
     * @param i_Template  模板对象 
     * @return
     */
    @Xsql(id="XSQL_XFlow_Template_QueryByID_NameVersion" ,returnOne=true)
    public Template queryByNameVersion(@Xparam(notNulls={"templateName" ,"version"}) Template i_Template);
    
}
