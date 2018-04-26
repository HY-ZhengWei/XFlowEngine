package org.hy.xflow.engine.dao;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.FlowProcess;





/**
 * 工作流模板表的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-25
 * @version     v1.0
 */
@Xjava(id="FlowInfoDAO" ,value=XType.XSQL)
public interface IFlowInfoDAO
{
    
    /**
     * 按第三方使用系统的业务数据ID，查询工作流实例
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-25
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return
     */
    @Xsql(id="XSQL_XFlow_FlowInfo_QueryByServiceDataID" ,returnOne=true)
    public FlowInfo queryByServiceDataID(@Xparam(id="serviceDataID" ,notNull=true) String i_ServiceDataID);
    
    
    
    /**
     * 创建的工作流实例，当前活动节点为  "开始" 节点。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-04-26
     * @version     v1.0
     *
     * @param i_FlowInfo
     * @param i_Process
     * @return
     */
    @Xsql("GXSQL_Flow_Create")
    public boolean createFlow(@Xparam("flow")    FlowInfo    i_FlowInfo 
                             ,@Xparam("process") FlowProcess i_Process);
    
}
