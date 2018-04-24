package org.hy.xflow.engine.dao;

import org.hy.common.PartitionMap;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.Participants;
import org.hy.xflow.engine.bean.Template;





/**
 * 工作流活动组件(节点)路由的参与人的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-24
 * @version     v1.0
 */
@Xjava(id="ActivityRouteParticipantsDAO" ,value=XType.XSQL)
public interface IActivityRouteParticipantsDAO
{
    
    /**
     * 查询某一工作流模板的活动组件(节点)的路由的参与人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-24
     * @version     v1.0
     *
     * @param i_TemplateID  模板ID
     * @return
     */
    @Xsql("XSQL_XFlow_ActivityRouteParticipants_QueryByTemplateID")
    public PartitionMap<String ,Participants> queryByTemplateID(@Xparam(id="templateID" ,notNull=true)String i_TemplateID);
    
    
    
    /**
     * 查询某一工作流模板的活动组件(节点)的路由的参与人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-24
     * @version     v1.0
     *
     * @param i_Template  模板对象
     * @return
     */
    @Xsql("XSQL_XFlow_ActivityRouteParticipants_QueryByTemplateID")
    public PartitionMap<String ,Participants> queryByTemplateID(@Xparam(notNulls={"templateID"}) Template i_Template);
    
}
