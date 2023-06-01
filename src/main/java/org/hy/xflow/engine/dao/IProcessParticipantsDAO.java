package org.hy.xflow.engine.dao;

import java.util.List;

import org.hy.common.PartitionMap;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.ProcessParticipant;
import org.hy.xflow.engine.bean.User;





/**
 * 工作流过程的动态参与人的DAO
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-08
 * @version     v1.0
 */
@Xjava(id="ProcessParticipantsDAO" ,value=XType.XSQL)
public interface IProcessParticipantsDAO
{
    
    /**
     * 工作流实例ID，查询工作流流转过程的动态参与人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-09
     * @version     v1.0
     *
     * @param i_WorkID  工作流实例ID
     * @return          Map.分区为：工作流的过程ID
     */
    @Xsql("XSQL_XFlow_ProcessParticipants_QueryByWorkID_ServiceDataID")
    public PartitionMap<String ,ProcessParticipant> queryByWorkID(@Xparam(id="workID" ,notNull=true) String i_WorkID);
    
    
    
    /**
     * 三方使用系统的业务数据ID，查询工作流流转过程的动态参与人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-09
     * @version     v1.0
     *
     * @param i_ServiceDataID  第三方使用系统的业务数据ID。即支持用第三方ID也能找到工作流信息
     * @return                 Map.分区为：工作流的过程ID
     */
    @Xsql("XSQL_XFlow_ProcessParticipants_QueryByWorkID_ServiceDataID")
    public PartitionMap<String ,ProcessParticipant> queryByServiceDataID(@Xparam(id="serviceDataID" ,notNull=true) String i_ServiceDataID);
    
    
    
    /**
     * 查询工作流流转过程抄送人相关的流程信息（督办）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-01
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    @Xsql("XSQL_XFlow_ProcessParticipants_QueryBySupervise")
    public List<ProcessParticipant> queryBySupervise(User i_User);
    
    
    
    /**
     * 查询历史工作流流转过程抄送人相关的流程信息（督查）
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-01
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    @Xsql("XSQL_XFlow_ProcessParticipants_QueryBySupervision")
    public List<ProcessParticipant> queryBySupervision(User i_User);
    
}
