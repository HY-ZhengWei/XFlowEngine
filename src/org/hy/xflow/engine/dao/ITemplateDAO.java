package org.hy.xflow.engine.dao;

import java.util.Map;

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
    @Xsql(id="XSQL_XFlow_Template_QueryByID_NameVersionNo" ,returnOne=true)
    public Template queryByID(Map<String ,Long> i_TemplateIDs);
    
    
    
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
    @Xsql(id="XSQL_XFlow_Template_QueryByID_NameVersionNo" ,returnOne=true)
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
    @Xsql(id="XSQL_XFlow_Template_QueryByID_NameVersionNo" ,returnOne=true)
    public Template queryByID(@Xparam(notNulls={"templateID"}) Template i_Template);
    
    
    
    /**
     * 按模板名称及版本号查询模板信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-18
     * @version     v1.0
     *
     * @param i_TemplateID  模板ID
     * @param i_VersionNo   模板版本号（数值递增型）
     * @return
     */
    @Xsql(id="XSQL_XFlow_Template_QueryByID_NameVersionNo" ,returnOne=true)
    public Template queryByNameVersion(@Xparam(id="templateName" ,notNull=true) String  i_TemplateID
                                      ,@Xparam(id="versionNo"    ,notNull=true) Integer i_VersionNo);
    
    
    
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
    @Xsql(id="XSQL_XFlow_Template_QueryByID_NameVersionNo" ,returnOne=true)
    public Template queryByNameVersion(@Xparam(notNulls={"templateName" ,"versionNo"}) Template i_Template);
    
    
    
    /**
     * 按模板名称查询版本号最大的有效的模板信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     *
     * @param i_TemplateName  模板名称
     * @param i_VersionNo     模板版本号（其数值是递增型）（可选的）
     * @return
     */
    @Xsql(id="XSQL_XFlow_Template_QueryByName_MaxVersionNo" ,returnOne=true)
    public Template queryByNameMaxVersionNo(@Xparam(id="templateName" ,notNull=true) String  i_TemplateName
                                           ,@Xparam("versionNo")                     Integer i_VersionNo);
    
    
    
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
    @Xsql(id="XSQL_XFlow_Template_QueryByName_MaxVersionNo" ,returnOne=true)
    public Template queryByNameMaxVersionNo(@Xparam(notNulls={"templateName"}) Template i_Template);
    
}
