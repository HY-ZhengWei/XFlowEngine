package org.hy.xflow.engine.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Help;
import org.hy.common.PartitionMap;
import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.ProcessParticipant;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.bean.UserRole;
import org.hy.xflow.engine.common.BaseService;
import org.hy.xflow.engine.dao.IFlowFutureOperatorDAO;
import org.hy.xflow.engine.enums.ParticipantTypeEnum;
import org.hy.xflow.engine.service.IFlowFutureOperatorService;





/**
 * 工作流未来操作人(实时数据)的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-15
 * @version     v1.0
 */
@Xjava
public class FlowFutureOperatorService extends BaseService implements IFlowFutureOperatorService
{
    
    /** 
     * 高速缓存。
     *   Map.key分区为参与人的形式的值：objectType:objectID 
     *   Map.value元素为工作流实例ID：workID
     */
    public static PartitionMap<String ,String> $FutureOperatorsByWorkID        = null;
    
    /**
     * 高速缓存。
     *   Map.key分区为参与人的形式的值：objectType:objectID 
     *   Map.value元素为工作流实例第三方使用系统的业务数据ID：serviceDataID
     */
    public static PartitionMap<String ,String> $FutureOperatorsByServiceDataID = null;
    
    /** 
     * 高速缓存。
     *   Map.key分区为工作流实例ID：workID
     *   Map.value元素为参与人的形式的值：objectType:objectID 
     */
    public static PartitionMap<String ,String> $FutureOperators_KeyWorkID        = null;
    
    /**
     * 高速缓存。
     *   Map.key分区为工作流实例第三方使用系统的业务数据ID：serviceDataID
     *   Map.value元素为参与人的形式的值：objectType:objectID 
     */
    public static PartitionMap<String ,String> $FutureOperators_KeyServiceDataID = null;
    
    
    
    @Xjava
    private IFlowFutureOperatorDAO futureOperatorDAO;
    
    
    
    /**
     * 获取用户可以处理（或叫待办）的工作流实例ID。
     * 
     *   1. 通过用户ID查询
     *   2. 通过部门ID查询
     *   3. 通过角色ID查询，支持多角色。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    public List<String> queryWorkIDs(User i_User)
    {
        List<String> v_IDs  = new ArrayList<String>();
        List<String> v_Temp = null;
        
        v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$User.getValue() + ":" + i_User.getUserID());
        if ( !Help.isNull(v_Temp) )
        {
            v_IDs.addAll(v_Temp);
        }
        
        if ( !Help.isNull(i_User.getOrgID()) )
        {
            v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$Org.getValue() + ":" + i_User.getOrgID());
            if ( !Help.isNull(v_Temp) )
            {
                v_IDs.addAll(v_Temp);
            }
        }
        
        if ( !Help.isNull(i_User.getRoles()) )
        {
            for (UserRole v_Role : i_User.getRoles())
            {
                v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$Role.getValue() + ":" + v_Role.getRoleID());
                if ( !Help.isNull(v_Temp) )
                {
                    v_IDs.addAll(v_Temp);
                }
            }
        }
        
        return v_IDs;
    }
    
    
    
    /**
     * 获取用户可以处理（或叫待办）的工作流实例对应的第三方使用系统的业务数据ID。
     * 
     *   1. 通过用户ID查询
     *   2. 通过部门ID查询
     *   3. 通过角色ID查询，支持多角色。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @param i_User
     * @return
     */
    public List<String> queryServiceDataIDs(User i_User)
    {
        List<String> v_IDs  = new ArrayList<String>();
        List<String> v_Temp = null;
        
        v_Temp = $FutureOperatorsByServiceDataID.get(ParticipantTypeEnum.$User.getValue() + ":" + i_User.getUserID());
        if ( !Help.isNull(v_Temp) )
        {
            v_IDs.addAll(v_Temp);
        }
        
        if ( !Help.isNull(i_User.getOrgID()) )
        {
            v_Temp = $FutureOperatorsByServiceDataID.get(ParticipantTypeEnum.$Org.getValue() + ":" + i_User.getOrgID());
            if ( !Help.isNull(v_Temp) )
            {
                v_IDs.addAll(v_Temp);
            }
        }
        
        if ( !Help.isNull(i_User.getRoles()) )
        {
            for (UserRole v_Role : i_User.getRoles())
            {
                v_Temp = $FutureOperatorsByServiceDataID.get(ParticipantTypeEnum.$Role.getValue() + ":" + v_Role.getRoleID());
                if ( !Help.isNull(v_Temp) )
                {
                    v_IDs.addAll(v_Temp);
                }
            }
        }
        
        return v_IDs;
    }
    
    
    
    /**
     * 初始化缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     */
    public void initCaches()
    {
        if ( $FutureOperatorsByWorkID == null )
        {
            this.queryAllByWorkID();
            this.queryAllByServiceDataID();
            this.queryAll_KeyWorkID();
            this.queryAll_KeyServiceDataID();
        }
    }
    
    
    
    /**
     * 更新缓存中的未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @param i_Process
     */
    public synchronized void updateCache(FlowProcess i_Process)
    {
        this.delCache(i_Process);
        this.addCache(i_Process);
    }
    
    
    
    /**
     * 向缓存中添加未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @param i_Process
     */
    public synchronized void addCache(FlowProcess i_Process)
    {
        if ( Help.isNull(i_Process.getServiceDataID()) )
        {
            for (ProcessParticipant v_Part : i_Process.getFutureParticipants())
            {
                String v_ID = v_Part.getObjectType().getValue() + ":" + v_Part.getObjectID();
                $FutureOperators_KeyWorkID.putRow(i_Process.getWorkID() ,v_ID);
                $FutureOperatorsByWorkID  .putRow(i_Process.getWorkID() ,v_ID);
            }
        }
        else
        {
            for (ProcessParticipant v_Part : i_Process.getFutureParticipants())
            {
                String v_ID = v_Part.getObjectType().getValue() + ":" + v_Part.getObjectID();
                $FutureOperators_KeyWorkID       .putRow(i_Process.getWorkID()        ,v_ID);
                $FutureOperators_KeyServiceDataID.putRow(i_Process.getServiceDataID() ,v_ID);
                $FutureOperatorsByWorkID         .putRow(i_Process.getWorkID()        ,v_ID);
                $FutureOperatorsByServiceDataID  .putRow(i_Process.getServiceDataID() ,v_ID);
            }
        }
    }
    
    
    
    /**
     * 删除缓存中的未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @param i_Process
     */
    public synchronized void delCache(FlowProcess i_Process)
    {
        List<String> v_OldObjectTypeIDs = $FutureOperators_KeyWorkID.get(i_Process.getWorkID());
        
        if ( !Help.isNull(v_OldObjectTypeIDs) )
        {
            for (String v_OldID : v_OldObjectTypeIDs)
            {
                $FutureOperatorsByWorkID       .removeRow(v_OldID ,i_Process.getWorkID());
                $FutureOperatorsByServiceDataID.removeRow(v_OldID ,i_Process.getServiceDataID());
            }
        }
        
        $FutureOperators_KeyWorkID       .remove(i_Process.getWorkID());
        $FutureOperators_KeyServiceDataID.remove(i_Process.getServiceDataID());
    }
    
    
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询
     * 
     *   Map.key分区为参与人的形式的值：objectType:objectID 
     *   Map.value元素为工作流实例ID：workID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @return
     */
    public PartitionMap<String ,String> queryAllByWorkID()
    {
        if ( $FutureOperatorsByWorkID == null )
        {
            $FutureOperatorsByWorkID = this.futureOperatorDAO.queryAllByWorkID();
        }
        return $FutureOperatorsByWorkID;
    }
    
    
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询
     * 
     *   Map.key分区为参与人的形式的值：objectType:objectID 
     *   Map.value元素为工作流实例第三方使用系统的业务数据ID：serviceDataID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @return
     */
    public PartitionMap<String ,String> queryAllByServiceDataID()
    {
        if ( $FutureOperatorsByServiceDataID == null )
        {
            $FutureOperatorsByServiceDataID = this.futureOperatorDAO.queryAllByServiceDataID();
        }
        return $FutureOperatorsByServiceDataID;
    }
    
    
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询
     * 
     *   Map.key分区为工作流实例ID：workID
     *   Map.value元素为参与人的形式的值：objectType:objectID 
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @return
     */
    public PartitionMap<String ,String> queryAll_KeyWorkID()
    {
        if ( $FutureOperators_KeyWorkID == null )
        {
            $FutureOperators_KeyWorkID = this.futureOperatorDAO.queryAll_KeyWorkID();
        }
        return $FutureOperators_KeyWorkID;
    }
    
    
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询
     * 
     *   Map.key分区为工作流实例第三方使用系统的业务数据ID：serviceDataID
     *   Map.value元素为参与人的形式的值：objectType:objectID 
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @return
     */
    public PartitionMap<String ,String> queryAll_KeyServiceDataID()
    {
        if ( $FutureOperators_KeyServiceDataID == null )
        {
            $FutureOperators_KeyServiceDataID = this.futureOperatorDAO.queryAll_KeyServiceDataID();
        }
        return $FutureOperators_KeyServiceDataID;
    }
    
}
