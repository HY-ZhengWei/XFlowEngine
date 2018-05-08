package org.hy.xflow.engine.service.impl;

import java.util.List;

import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.ProcessParticipant;
import org.hy.xflow.engine.common.BaseService;
import org.hy.xflow.engine.dao.IProcessParticipantsDAO;
import org.hy.xflow.engine.service.IProcessParticipantsService;





/**
 * 工作流过程的动态参与人的服务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-08
 * @version     v1.0
 */
@Xjava
public class ProcessParticipantsService extends BaseService implements IProcessParticipantsService
{
    
    @Xjava
    private IProcessParticipantsDAO processParticipantsDAO;
    
    
    
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
    public boolean add(List<ProcessParticipant> i_Participants)
    {
        return this.processParticipantsDAO.add(i_Participants) == i_Participants.size();
    }
    
}
