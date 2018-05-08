package org.hy.xflow.engine.bean;

import org.hy.common.Help;
import org.hy.xflow.engine.common.BaseModel;





/**
 * 与外界对接的用户角色。支持一位用户多重角色。
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-08
 * @version     v1.0
 */
public class UserRole extends BaseModel
{

    private static final long serialVersionUID = -2600611110914991230L;
    
    
    /** 角色ID */
    private String roleID;
    
    /** 角色名称 */
    private String roleName;
    
    
    
    public UserRole()
    {
        
    }
    
    
    
    public UserRole(String i_RoleID ,String i_RoleName)
    {
        this.roleID   = i_RoleID;
        this.roleName = i_RoleName;
    }
    
    
    
    /**
     * 获取：角色ID
     */
    public String getRoleID()
    {
        return Help.NVL(roleID);
    }
    
    
    /**
     * 获取：角色名称
     */
    public String getRoleName()
    {
        return Help.NVL(roleName);
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
