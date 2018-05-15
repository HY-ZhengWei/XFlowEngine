package org.hy.xflow.engine.dao;

import org.hy.common.PartitionMap;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xsql;





/**
 * 工作流未来操作人(实时数据)的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-15
 * @version     v1.0
 */
@Xjava(id="FlowFutureOperatorDAO" ,value=XType.XSQL)
public interface IFlowFutureOperatorDAO
{
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询
     * 
     *   Map.key分区为参与人的形式的值：objectType:objectID 
     *   Map.value元素为工作流实例ID：workID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @return
     */
    @Xsql(id="XSQL_XFlow_TFlowFutureOperator_QueryAllByWorkID" ,cacheID="$FutureOperatorsByWorkID")
    public PartitionMap<String ,String> queryAllByWorkID();
    
    
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询
     * 
     *   Map.key分区为参与人的形式的值：objectType:objectID 
     *   Map.value元素为工作流实例第三方使用系统的业务数据ID：serviceDataID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @return
     */
    @Xsql(id="XSQL_XFlow_TFlowFutureOperator_QueryAllByServiceDataID" ,cacheID="$FutureOperatorsByServiceDataID")
    public PartitionMap<String ,String> queryAllByServiceDataID();
    
    
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询
     * 
     *   Map.key分区为工作流实例ID：workID
     *   Map.value元素为参与人的形式的值：objectType:objectID 
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @return
     */
    @Xsql(id="XSQL_XFlow_TFlowFutureOperator_QueryAll_KeyWorkID" ,cacheID="$FutureOperators_KeyWorkID")
    public PartitionMap<String ,String> queryAll_KeyWorkID();
    
    
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询
     * 
     *   Map.key分区为工作流实例第三方使用系统的业务数据ID：serviceDataID
     *   Map.value元素为参与人的形式的值：objectType:objectID 
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @return
     */
    @Xsql(id="XSQL_XFlow_TFlowFutureOperator_QueryAll_KeyServiceDataID" ,cacheID="$FutureOperators_KeyServiceDataID")
    public PartitionMap<String ,String> queryAll_KeyServiceDataID();
    
}
