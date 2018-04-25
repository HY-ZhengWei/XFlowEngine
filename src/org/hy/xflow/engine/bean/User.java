package org.hy.xflow.engine.bean;

import org.hy.xflow.engine.common.BaseModel;





/**
 * 与外界对接的用户信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-25
 * @version     v1.0
 */
public class User extends BaseModel
{
    
    private static final long serialVersionUID = 2225865648787794780L;
    

    /** 用户ID */
    private String userID;
    
    /** 用户名称 */
    private String userName;
    
    /** 部门ID */
    private String orgID;
    
    /** 部门名称 */
    private String orgName;
    
    /** 角色ID */
    private String roleID;
    
    /** 角色名称 */
    private String roleName;

    
    
    /**
     * 获取：用户ID
     */
    public String getUserID()
    {
        return userID;
    }
    
    
    /**
     * 获取：用户名称
     */
    public String getUserName()
    {
        return userName;
    }
    

    /**
     * 获取：部门ID
     */
    public String getOrgID()
    {
        return orgID;
    }

    
    /**
     * 获取：部门名称
     */
    public String getOrgName()
    {
        return orgName;
    }
    
    
    /**
     * 获取：角色ID
     */
    public String getRoleID()
    {
        return roleID;
    }
    
    
    /**
     * 获取：角色名称
     */
    public String getRoleName()
    {
        return roleName;
    }

    
    /**
     * 设置：用户ID
     * 
     * @param userID 
     */
    public void setUserID(String userID)
    {
        this.userID = userID;
    }
    
    
    /**
     * 设置：用户名称
     * 
     * @param userName 
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    
    /**
     * 设置：部门ID
     * 
     * @param orgID 
     */
    public void setOrgID(String orgID)
    {
        this.orgID = orgID;
    }

    
    /**
     * 设置：部门名称
     * 
     * @param orgName 
     */
    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }

    
    /**
     * 设置：角色ID
     * 
     * @param roleID 
     */
    public void setRoleID(String roleID)
    {
        this.roleID = roleID;
    }

    
    /**
     * 设置：角色名称
     * 
     * @param roleName 
     */
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }
    
}
