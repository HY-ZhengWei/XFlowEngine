package org.hy.xflow.engine.dao;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.ActivityType;





/**
 * 工作流活动(节点)类型 
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-18
 * @version     v1.0
 */
@Xjava(value=XType.XSQL)
public interface IActivityTypeDAO
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
    @Xsql("XSQL_XFlow_ActivityType_Query")
    public List<ActivityType> query();
    
}
