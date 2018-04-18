package org.hy.xflow.engine.dao;

import org.hy.common.PartitionMap;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.ActivityRoute;





/**
 * 工作流活动组件(节点)的路由表的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-18
 * @version     v1.0
 */
@Xjava(id="ActivityRouteDAO" ,value=XType.XSQL)
public interface IActivityRouteDAO
{
    
    /**
     * 查询所有活动(节点)类型
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-18
     * @version     v1.0
     *
     * @return
     */
    @Xsql("XSQL_XFlow_ActivityRoute_QueryByTemplateID")
    public PartitionMap<String ,ActivityRoute> query();
    
}
