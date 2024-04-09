package org.hy.xflow.engine.service.impl;

import java.util.List;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.ProcessCounterSignatureLog;
import org.hy.xflow.engine.common.BaseService;
import org.hy.xflow.engine.dao.IProcessCounterSignatureDAO;
import org.hy.xflow.engine.service.IProcessCounterSignatureService;





/**
 * 工作流汇签的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-04-08
 * @version     v1.0
 */
@Xjava
public class ProcessCounterSignatureService extends BaseService implements IProcessCounterSignatureService
{
    
    
    @Xjava
    private IProcessCounterSignatureDAO counterSignatureDAO;
    
    
    
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
    @Override
    public boolean saveCSLog(ProcessCounterSignatureLog io_CSLog)
    {
        io_CSLog.setPcslID("PCSL" + StringHelp.getUUID());
        io_CSLog.setCsTime(Help.NVL(io_CSLog.getCsTime() ,new Date()));
        
        return this.counterSignatureDAO.saveCSLog(io_CSLog);
    }
    
    
    
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
    @Override
    public ProcessCounterSignatureLog queryCSInfo(ProcessCounterSignatureLog i_CSInfo)
    {
        return this.counterSignatureDAO.queryCSInfo(i_CSInfo);
    }
    
    
    
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
    @Override
    public ProcessCounterSignatureLog queryCSLog(ProcessCounterSignatureLog i_CSLog)
    {
        return this.counterSignatureDAO.queryCSLog(i_CSLog);
    }
    
    
    
    /**
     * 查询汇签信息及记录（包括：活动库、历史库）
     * 
     * 汇签要求按时间线倒排、汇签记录按时间线正排
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-09
     * @version     v1.0
     *
     * @param i_CSLog  汇签日志
     * @return
     */
    @Override
    public List<ProcessCounterSignatureLog> queryCSLogsByWorkID(ProcessCounterSignatureLog i_CSLog)
    {
        if ( i_CSLog == null || Help.isNull(i_CSLog.getWorkID()) )
        {
            return null;
        }
        
        i_CSLog.setServiceDataID(null);
        return this.counterSignatureDAO.queryCSLogs(i_CSLog);
    }
    
    
    
    /**
     * 查询汇签信息及记录（包括：活动库、历史库）
     * 
     * 汇签要求按时间线倒排、汇签记录按时间线正排
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-04-09
     * @version     v1.0
     *
     * @param i_CSLog  汇签日志
     * @return
     */
    @Override
    public List<ProcessCounterSignatureLog> queryCSLogsByServiceDataID(ProcessCounterSignatureLog i_CSLog)
    {
        if ( i_CSLog == null || Help.isNull(i_CSLog.getServiceDataID()) )
        {
            return null;
        }
        
        i_CSLog.setWorkID(null);
        return this.counterSignatureDAO.queryCSLogs(i_CSLog);
    }
    
}
