package org.hy.xflow.engine.bean;

import org.hy.common.Help;
import org.hy.xflow.engine.common.BaseModel;
import org.hy.xflow.engine.enums.ParticipantTypeEnum;





/**
 * 与外界对接的参与人
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-08
 * @version     v1.0
 */
public class UserParticipant extends BaseModel
{

    private static final long serialVersionUID = -7249360500488445049L;
    
    
    /** 参与者类型（0:发起人；1:执行人；2:执行部门；3:执行角色；11:抄送人；12:抄送部门；13:抄送角色） */
    protected ParticipantTypeEnum objectType;
    
    /** 参与者ID */
    protected String              objectID;
    
    /** 参与者名称 */
    protected String              objectName;
    
    /** 参与者序号，表示前后顺序。系统自动生成，下标从1开始。当外界指定时，系统不在生成。 */
    protected Integer             objectNo;
    
    
    
    /**
     * 获取：参与者类型（0:发起人；1:执行人；2:执行部门；3:执行角色；11:抄送人；12:抄送部门；13:抄送角色）
     */
    public ParticipantTypeEnum getObjectType()
    {
        return objectType;
    }
    

    
    /**
     * 获取：参与者ID
     */
    public String getObjectID()
    {
        return objectID;
    }
    

    
    /**
     * 获取：参与者名称
     */
    public String getObjectName()
    {
        return Help.NVL(objectName);
    }
    

    
    /**
     * 设置：参与者类型（0:发起人；1:执行人；2:执行部门；3:执行角色；11:抄送人；12:抄送部门；13:抄送角色）
     * 
     * @param objectType 
     */
    public void setObjectType(ParticipantTypeEnum objectType)
    {
        this.objectType = objectType;
    }
    

    
    /**
     * 设置：参与者ID
     * 
     * @param objectID 
     */
    public void setObjectID(String objectID)
    {
        this.objectID = objectID;
    }
    

    
    /**
     * 设置：参与者名称
     * 
     * @param objectName 
     */
    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }


    
    /**
     * 获取：参与者序号，表示前后顺序。系统自动生成，下标从1开始
     * 
     *      当外界指定时，系统不在生成。
     */
    public Integer getObjectNo()
    {
        return objectNo;
    }
    


    /**
     * 设置：参与者序号，表示前后顺序。系统自动生成，下标从1开始。
     * 
     *      当外界指定时，系统不在生成。
     * 
     * @param objectNo 
     */
    public void setObjectNo(Integer objectNo)
    {
        this.objectNo = objectNo;
    }
    
}
