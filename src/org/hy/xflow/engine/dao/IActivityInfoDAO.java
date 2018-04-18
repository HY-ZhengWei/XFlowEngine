package org.hy.xflow.engine.dao;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.ActivityInfo;
import org.hy.xflow.engine.bean.Template;





/**
 * 工作流活动组件(节点)信息表
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-18
 * @version     v1.0
 */
@Xjava(id="ActivityInfoDAO" ,value=XType.XSQL)
public interface IActivityInfoDAO
{
    
    /**
     * 查询某一工作流模板的活动组件(节点)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-18
     * @version     v1.0
     *
     * @param i_TemplateID  模板ID
     * @return
     */
    @Xsql("XSQL_XFlow_ActivityInfo_QueryByTemplateID")
    public List<ActivityInfo> queryByTemplateID(@Xparam(id="templateID" ,notNull=true)String i_TemplateID);
    
    
    
    /**
     * 查询某一工作流模板的活动组件(节点)
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-18
     * @version     v1.0
     *
     * @param i_Template  模板对象
     * @return
     */
    @Xsql("XSQL_XFlow_ActivityInfo_QueryByTemplateID")
    public List<ActivityInfo> queryByTemplateID(@Xparam(notNulls={"templateID"}) Template i_Template); 
    
}
