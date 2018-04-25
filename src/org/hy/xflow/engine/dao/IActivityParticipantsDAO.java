package org.hy.xflow.engine.dao;

import org.hy.common.PartitionMap;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.Participant;
import org.hy.xflow.engine.bean.Template;





/**
 * 工作流活动组件(节点)的参与人的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-25
 * @version     v1.0
 */
@Xjava(id="ActivityParticipantsDAO" ,value=XType.XSQL)
public interface IActivityParticipantsDAO
{
    
    /**
     * 查询某一工作流模板的活动组件(节点)的参与人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     *
     * @param i_TemplateID  模板ID
     * @return
     */
    @Xsql("XSQL_XFlow_ActivityParticipants_QueryByTemplateID")
    public PartitionMap<String ,Participant> queryByTemplateID(@Xparam(id="templateID" ,notNull=true)String i_TemplateID);
    
    
    
    /**
     * 查询某一工作流模板的活动组件(节点)的参与人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     *
     * @param i_Template  模板对象
     * @return
     */
    @Xsql("XSQL_XFlow_ActivityParticipants_QueryByTemplateID")
    public PartitionMap<String ,Participant> queryByTemplateID(@Xparam(notNulls={"templateID"}) Template i_Template);
    
}
