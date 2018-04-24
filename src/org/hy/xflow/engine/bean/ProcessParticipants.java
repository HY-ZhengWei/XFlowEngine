package org.hy.xflow.engine.bean;





/**
 * 工作流过程的实际参与人
 *
 * @author      ZhengWei
 * @createDate  2018-04-17
 * @version     v1.0
 */
public class ProcessParticipants extends Participants
{
    private static final long serialVersionUID = -4724247321457107633L;
	
    
	/** 主键ID */
    private String pwpID;
    
	/** 工作流的过程ID */
    private String processID;
    
	/** 工作流实例ID */
    private String workID;
    
	/** 创建部门ID */
    private String createOrgID;
    
	/** 创建部门名称 */
    private String createOrg;
    
	
	
	/**
     * 获取：主键ID
     */
    public String getPwpID()
    {
        return this.pwpID;
    }

    
    /**
     * 设置：主键ID
     * 
     * @param i_PwpID
     */
    public void setPwpID(String i_PwpID)
    {
        this.pwpID = i_PwpID;
    }
	
	
	/**
     * 获取：工作流的过程ID
     */
    public String getProcessID()
    {
        return this.processID;
    }

    
    /**
     * 设置：工作流的过程ID
     * 
     * @param i_ProcessID
     */
    public void setProcessID(String i_ProcessID)
    {
        this.processID = i_ProcessID;
    }
	
	
	/**
     * 获取：工作流实例ID
     */
    public String getWorkID()
    {
        return this.workID;
    }

    
    /**
     * 设置：工作流实例ID
     * 
     * @param i_WorkID
     */
    public void setWorkID(String i_WorkID)
    {
        this.workID = i_WorkID;
    }
	
	
	/**
     * 获取：创建部门ID
     */
    public String getCreateOrgID()
    {
        return this.createOrgID;
    }

    
    /**
     * 设置：创建部门ID
     * 
     * @param i_CreateOrgID
     */
    public void setCreateOrgID(String i_CreateOrgID)
    {
        this.createOrgID = i_CreateOrgID;
    }
	
	
	/**
     * 获取：创建部门名称
     */
    public String getCreateOrg()
    {
        return this.createOrg;
    }

    
    /**
     * 设置：创建部门名称
     * 
     * @param i_CreateOrg
     */
    public void setCreateOrg(String i_CreateOrg)
    {
        this.createOrg = i_CreateOrg;
    }

}
