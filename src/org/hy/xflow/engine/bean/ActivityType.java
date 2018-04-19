package org.hy.xflow.engine.bean;

import org.hy.xflow.engine.common.BaseModel;





/**
 * 工作流活动(节点)类型 
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-17
 * @version     v1.0
 */
public class ActivityType extends BaseModel
{
    private static final long serialVersionUID = -4724247321457107633L;
	
    
	/** 工作流活动类型ID */
    private String  activityTypeID;
    
	/** 工作流活动类型名称 */
    private String  activityType;
    
	/** 备注说明 */
    private String  infoComment;
    
    /** 排列顺序 */
    private Integer orderNo;
    
	
	
	/**
     * 获取：工作流活动类型ID
     */
    public String getActivityTypeID()
    {
        return this.activityTypeID;
    }

    
    /**
     * 设置：工作流活动类型ID
     * 
     * @param i_ActivityTypeID
     */
    public void setActivityTypeID(String i_ActivityTypeID)
    {
        this.activityTypeID = i_ActivityTypeID;
    }
	
	
	/**
     * 获取：工作流活动类型名称
     */
    public String getActivityType()
    {
        return this.activityType;
    }

    
    /**
     * 设置：工作流活动类型名称
     * 
     * @param i_ActivityType
     */
    public void setActivityType(String i_ActivityType)
    {
        this.activityType = i_ActivityType;
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
