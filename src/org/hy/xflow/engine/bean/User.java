package org.hy.xflow.engine.bean;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Help;
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
    
    /** 用户角色。支持多角色功能 */
    private List<UserRole> roles;
    
    
    
    /**
     * 添加多角色
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-08
     * @version     v1.0
     *
     * @param i_RoleID
     * @param i_RoleName
     */
    public synchronized void addRole(String i_RoleID ,String i_RoleName)
    {
        if ( this.roles == null )
        {
            this.roles = new ArrayList<UserRole>();
        }
        
        this.roles.add(new UserRole(i_RoleID ,i_RoleName));
    }
    
    
    
    /**
     * 获取：用户ID
     */
    public String getUserID()
    {
        return Help.NVL(userID);
    }
    
    
    /**
     * 获取：用户名称
     */
    public String getUserName()
    {
        return Help.NVL(userName);
    }
    

    /**
     * 获取：部门ID
     */
    public String getOrgID()
    {
        return Help.NVL(orgID);
    }

    
    /**
     * 获取：部门名称
     */
    public String getOrgName()
    {
        return Help.NVL(orgName);
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
     * 获取：用户角色。支持多角色功能
     */
    public List<UserRole> getRoles()
    {
        return roles;
    }
    
    
    
    /**
     * 设置：用户角色。支持多角色功能
     * 
     * @param roles 
     */
    public void setRoles(List<UserRole> roles)
    {
        this.roles = roles;
    }

}
