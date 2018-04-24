package org.hy.xflow.engine.bean;

import org.hy.xflow.engine.common.BaseModel;





/**
 * 参与人类型 
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-24
 * @version     v1.0
 */
public class ParticipantType extends BaseModel
{
    
    private static final long serialVersionUID = -5096344239749759330L;

    
    /** 参与人类型ID */
    private String  participantTypeID;
    
	/** 参与人类型名称 */
    private String  participantType;
    
	/** 备注说明 */
    private String  infoComment;
    
    /** 排列顺序 */
    private Integer orderNo;
    
	
    
    /**
     * 获取：参与人类型ID
     */
    public String getParticipantTypeID()
    {
        return participantTypeID;
    }
    
    
    /**
     * 获取：参与人类型名称
     */
    public String getParticipantType()
    {
        return participantType;
    }
    
    
    /**
     * 设置：参与人类型ID
     * 
     * @param participantTypeID 
     */
    public void setParticipantTypeID(String participantTypeID)
    {
        this.participantTypeID = participantTypeID;
    }

    
    /**
     * 设置：参与人类型名称
     * 
     * @param participantType 
     */
    public void setParticipantType(String participantType)
    {
        this.participantType = participantType;
    }
    

    /**
     * 获取：备注说明
     */
    public String getInfoComment()
    {
        return this.infoComment;
    }

    
    /**
     * 设置：备注说明
     * 
     * @param i_InfoComment
     */
    public void setInfoComment(String i_InfoComment)
    {
        this.infoComment = i_InfoComment;
    }

    
    /**
     * 获取：排列顺序
     */
    public Integer getOrderNo()
    {
        return orderNo;
    }

    
    /**
     * 设置：排列顺序
     * 
     * @param orderNo 
     */
    public void setOrderNo(Integer orderNo)
    {
        this.orderNo = orderNo;
    }

}
