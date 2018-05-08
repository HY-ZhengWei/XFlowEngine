package org.hy.xflow.engine.dao;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xsql;
import org.hy.xflow.engine.bean.ProcessParticipant;





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
     * 动态添加流转过程中的参与人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-08
     * @version     v1.0
     *
     * @param i_Participants
     * @return
     */
    @Xsql(id="XSQL_XFlow_ProcessParticipants_Insert")
    public int add(List<ProcessParticipant> i_Participants);
    
}
