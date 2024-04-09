package org.hy.xflow.engine.dao;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.ProcessCounterSignatureLog;





/**
 * 工作流汇签的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-04-08
 * @version     v1.0
 */
@Xjava(id="ProcessCounterSignatureDAO" ,value=XType.XSQL)
public interface IProcessCounterSignatureDAO
{
    
    /**
     * 汇签记录
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-09
     * @version     v1.0
     *
     * @param i_CSLog  汇签记录
     * @return
     */
    @Xsql("GXSQL_XFlow_ProcessCounterSignatureLog")
    public boolean saveCSLog(@Xparam("csLog") ProcessCounterSignatureLog i_CSLog);
    
    
    
    /**
     * 查询汇签要求（非历史库）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-08
     * @version     v1.0
     *
     * @param i_CSInfo  汇签信息（workID、processID两字段查询）
     * @return
     */
    @Xsql(id="XSQL_XFlow_ProcessCounterSignature_QueryByProcessID" ,returnOne=true)
    public ProcessCounterSignatureLog queryCSInfo(ProcessCounterSignatureLog i_CSInfo);
    
    
    
    /**
     * 查询用户是否有汇签记录（非历史库）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-09
     * @version     v1.0
     *
     * @param i_CSLog  汇签日志（workID、processID、csUserID三字段查询）
     * @return
     */
    @Xsql(id="XSQL_XFlow_ProcessCounterSignatureLog_QueryByUserID" ,returnOne=true)
    public ProcessCounterSignatureLog queryCSLog(ProcessCounterSignatureLog i_CSLog);
    
}
