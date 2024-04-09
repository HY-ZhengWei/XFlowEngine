package org.hy.xflow.engine.service;

import org.hy.xflow.engine.bean.ProcessCounterSignatureLog;





/**
 * 工作流汇签的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-04-08
 * @version     v1.0
 */
public interface IProcessCounterSignatureService
{
    
    /**
     * 汇签记录
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-08
     * @version     v1.0
     *
     * @param io_CSLog  汇签记录
     * @return
     */
    public boolean saveCSLog(ProcessCounterSignatureLog io_CSLog);
    
    
    
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
    public ProcessCounterSignatureLog queryCSLog(ProcessCounterSignatureLog i_CSLog);
    
}
