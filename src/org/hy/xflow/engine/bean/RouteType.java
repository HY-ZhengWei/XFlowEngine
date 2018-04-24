package org.hy.xflow.engine.bean;

import org.hy.xflow.engine.common.BaseModel;





/**
 * 工作流路由类型 
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-24
 * @version     v1.0
 */
public class RouteType extends BaseModel
{
    private static final long serialVersionUID = -4724247321457107633L;
	
    
	/** 工作流路由类型ID */
    private String  routeTypeID;
    
	/** 工作流路由类型名称 */
    private String  routeType;
    
	/** 备注说明 */
    private String  infoComment;
    
    /** 排列顺序 */
    private Integer orderNo;
    
	
	
    /**
     * 获取：工作流路由类型ID
     */
    public String getRouteTypeID()
    {
        return routeTypeID;
    }

    
    /**
     * 获取：工作流路由类型名称
     */
    public String getRouteType()
    {
        return routeType;
    }

    
    /**
     * 设置：工作流路由类型ID
     * 
     * @param routeTypeID 
     */
    public void setRouteTypeID(String routeTypeID)
    {
        this.routeTypeID = routeTypeID;
    }

    
    /**
     * 设置：工作流路由类型名称
     * 
     * @param routeType 
     */
    public void setRouteType(String routeType)
    {
        this.routeType = routeType;
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
