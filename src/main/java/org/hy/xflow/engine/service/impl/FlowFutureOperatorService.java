package org.hy.xflow.engine.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.PartitionMap;
import org.hy.common.net.CommunicationListener;
import org.hy.common.net.data.CommunicationRequest;
import org.hy.common.net.data.CommunicationResponse;
import org.hy.common.xml.annotation.Xjava;
import org.hy.xflow.engine.bean.FlowProcess;
import org.hy.xflow.engine.bean.FutureOperator;
import org.hy.xflow.engine.bean.ProcessParticipant;
import org.hy.xflow.engine.bean.Template;
import org.hy.xflow.engine.bean.User;
import org.hy.xflow.engine.bean.UserRole;
import org.hy.xflow.engine.common.BaseService;
import org.hy.xflow.engine.dao.IFlowFutureOperatorDAO;
import org.hy.xflow.engine.enums.ParticipantTypeEnum;
import org.hy.xflow.engine.service.IFlowFutureOperatorService;
import org.hy.xflow.engine.service.ITemplateService;





/**
 * 工作流未来操作人(实时数据)的服务层接口
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-15
 * @version     v1.0
 *              v2.0  2019-09-12  1. 优化：从业务ID找工作流实例ID
 *                                2. 添加：支持多路并行路由的流程
 *              v3.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
 *              v4.0  2024-02-23  1. 添加：按人员信息查询待办时，可按流程模板名称过滤
 */
@Xjava
public class FlowFutureOperatorService extends BaseService implements IFlowFutureOperatorService ,CommunicationListener
{
    
    /**
     * 用人找实例的高速缓存。
     *   Map.key分区为参与人的形式的值：objectType:objectID
     *   Map.value元素为工作流实例ID：workID
     */
    private static PartitionMap<String ,FutureOperator> $FutureOperatorsByWorkID          = null;
    
    /**
     * 用人和流程模块找实例的高速缓存。
     *   Map.key分区为参与人的形式的值：objectType:objectID:templateName
     *   Map.value元素为工作流实例ID：workID
     */
    private static PartitionMap<String ,FutureOperator> $FutureOperatorsFroTemplateName   = null;
    
    /**
     * 用实例找人的高速缓存。
     *   Map.key分区为工作流实例ID：workID
     *   Map.value元素为参与人的形式的值：objectType:objectID
     */
    private static PartitionMap<String ,FutureOperator> $FutureOperators_KeyWorkID        = null;
    
    /**
     * 用业务ID找工作流ID的高速缓存。
     *   Map.key    为业务ID - serviceDataID
     *   Map.value  为实例ID - workID
     */
    private static Map<String ,String>                  $FutureOperators_SToWorkID        = null;
    
    
    
    @Xjava
    private IFlowFutureOperatorDAO futureOperatorDAO;
    
    @Xjava
    private ITemplateService       templateService;
    
    
    
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
     *              v2.0  2024-02-23  添加：流程模板名称的查询条件
     * 
     * @param i_User          流程用户
     * @param i_TemplateName  流程模板名称
     * @return
     */
    @Override
    public List<String> queryWorkIDs(User i_User ,String i_TemplateName)
    {
        return queryIDs(i_User ,i_TemplateName ,"workID");
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
     *              v2.0  2024-02-23  添加：流程模板名称的查询条件
     * 
     * @param i_User          流程用户
     * @param i_TemplateName  流程模板名称
     * @return
     */
    @Override
    public List<String> queryServiceDataIDs(User i_User ,String i_TemplateName)
    {
        return queryIDs(i_User ,i_TemplateName ,"serviceDataID");
    }
    
    
    
    /**
     * 获取用户可以处理（或叫待办）的工作流实例对应的实例ID、业务数据ID。
     * 
     *   1. 通过用户ID查询
     *   2. 通过部门ID查询
     *   3. 通过角色ID查询，支持多角色。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-11
     * @version     v1.0
     *              v2.0  2023-02-10  添加：三种角色的抄送功能
     *              v3.0  2023-06-01  删除：使用 "督办" 查询接口代替 "抄送"
     *              v4.0  2024-02-23  添加：流程模板名称的查询条件
     * 
     * @param i_User          流程用户
     * @param i_TemplateName  流程模板名称
     * @param i_IDName        实例ID或业务ID的属性名称。它决定着函数返回的是实例ID，还是业务ID。
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<String> queryIDs(User i_User ,String i_TemplateName ,String i_IDName)
    {
        List<String>         v_IDs  = new ArrayList<String>();
        List<FutureOperator> v_Temp = null;
        
        if ( Help.isNull(i_TemplateName) )
        {
            v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$User.getValue() + ":" + i_User.getUserID());
        }
        else
        {
            v_Temp = $FutureOperatorsFroTemplateName.get(ParticipantTypeEnum.$User.getValue() + ":" + i_User.getUserID() + ":" + i_TemplateName);
        }
        
        if ( !Help.isNull(v_Temp) )
        {
            v_IDs.addAll((List<String>)Help.toList(v_Temp ,i_IDName));
        }
        
        // 使用督办查询接口代替  2023-06-01 Del
        /*
        v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$UserSend.getValue() + ":" + i_User.getUserID());
        if ( !Help.isNull(v_Temp) )
        {
            v_IDs.addAll((List<String>)Help.toList(v_Temp ,i_IDName));
        }
        */
        
        if ( !Help.isNull(i_User.getOrgID()) )
        {
            if ( Help.isNull(i_TemplateName) )
            {
                v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$Org.getValue() + ":" + i_User.getOrgID());
            }
            else
            {
                v_Temp = $FutureOperatorsFroTemplateName.get(ParticipantTypeEnum.$Org.getValue() + ":" + i_User.getUserID() + ":" + i_TemplateName);
            }
            
            if ( !Help.isNull(v_Temp) )
            {
                v_IDs.addAll((List<String>)Help.toList(v_Temp ,i_IDName));
            }
            
            // 使用督办查询接口代替  2023-06-01 Del
            /*
            v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$OrgSend.getValue() + ":" + i_User.getOrgID());
            if ( !Help.isNull(v_Temp) )
            {
                v_IDs.addAll((List<String>)Help.toList(v_Temp ,i_IDName));
            }
            */
        }
        
        if ( !Help.isNull(i_User.getRoles()) )
        {
            for (UserRole v_Role : i_User.getRoles())
            {
                if ( Help.isNull(i_TemplateName) )
                {
                    v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$Role.getValue() + ":" + v_Role.getRoleID());
                }
                else
                {
                    v_Temp = $FutureOperatorsFroTemplateName.get(ParticipantTypeEnum.$Role.getValue() + ":" + i_User.getUserID() + ":" + i_TemplateName);
                }
                
                if ( !Help.isNull(v_Temp) )
                {
                    v_IDs.addAll((List<String>)Help.toList(v_Temp ,i_IDName));
                }
                
                // 使用督办查询接口代替  2023-06-01 Del
                /*
                v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$RoleSend.getValue() + ":" + v_Role.getRoleID());
                if ( !Help.isNull(v_Temp) )
                {
                    v_IDs.addAll((List<String>)Help.toList(v_Temp ,i_IDName));
                }
                */
            }
        }
        
        return Help.toDistinct(v_IDs);
    }
    
    
    
    /**
     * 查询人员的参与类型，是执行类型还是抄送类型
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-10
     * @version     v1.0
     *
     * @param i_User    参与人
     * @param i_WorkID  工作流实例ID
     * @return
     */
    @Override
    public ParticipantTypeEnum queryParticipantType(User i_User ,String i_WorkID)
    {
        List<FutureOperator> v_Temp = null;
        
        v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$User.getValue() + ":" + i_User.getUserID());
        if ( !Help.isNull(v_Temp) )
        {
            for (FutureOperator v_FutureOpt : v_Temp)
            {
                if ( i_WorkID.equals(v_FutureOpt.getWorkID()) )
                {
                    return ParticipantTypeEnum.$User;
                }
            }
        }
        
        v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$CreateUser.getValue() + ":" + i_User.getUserID());
        if ( !Help.isNull(v_Temp) )
        {
            for (FutureOperator v_FutureOpt : v_Temp)
            {
                if ( i_WorkID.equals(v_FutureOpt.getWorkID()) )
                {
                    return ParticipantTypeEnum.$CreateUser;
                }
            }
        }
        
        v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$ActivityUser.getValue() + ":" + i_User.getUserID());
        if ( !Help.isNull(v_Temp) )
        {
            for (FutureOperator v_FutureOpt : v_Temp)
            {
                if ( i_WorkID.equals(v_FutureOpt.getWorkID()) )
                {
                    return ParticipantTypeEnum.$ActivityUser;
                }
            }
        }
        
        if ( !Help.isNull(i_User.getOrgID()) )
        {
            v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$Org.getValue() + ":" + i_User.getOrgID());
            if ( !Help.isNull(v_Temp) )
            {
                for (FutureOperator v_FutureOpt : v_Temp)
                {
                    if ( i_WorkID.equals(v_FutureOpt.getWorkID()) )
                    {
                        return ParticipantTypeEnum.$Org;
                    }
                }
            }
        }
        
        if ( !Help.isNull(i_User.getRoles()) )
        {
            for (UserRole v_Role : i_User.getRoles())
            {
                v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$Role.getValue() + ":" + v_Role.getRoleID());
                if ( !Help.isNull(v_Temp) )
                {
                    for (FutureOperator v_FutureOpt : v_Temp)
                    {
                        if ( i_WorkID.equals(v_FutureOpt.getWorkID()) )
                        {
                            return ParticipantTypeEnum.$Role;
                        }
                    }
                }
            }
        }
        
        // 先判定是否为执行人，当判定无果时，再判定是否为抄送人，即：执行类 > 抄送类
        v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$UserSend.getValue() + ":" + i_User.getUserID());
        if ( !Help.isNull(v_Temp) )
        {
            for (FutureOperator v_FutureOpt : v_Temp)
            {
                if ( i_WorkID.equals(v_FutureOpt.getWorkID()) )
                {
                    return ParticipantTypeEnum.$UserSend;
                }
            }
        }
        
        if ( !Help.isNull(i_User.getOrgID()) )
        {
            v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$OrgSend.getValue() + ":" + i_User.getOrgID());
            if ( !Help.isNull(v_Temp) )
            {
                for (FutureOperator v_FutureOpt : v_Temp)
                {
                    if ( i_WorkID.equals(v_FutureOpt.getWorkID()) )
                    {
                        return ParticipantTypeEnum.$OrgSend;
                    }
                }
            }
        }
        
        if ( !Help.isNull(i_User.getRoles()) )
        {
            for (UserRole v_Role : i_User.getRoles())
            {
                v_Temp = $FutureOperatorsByWorkID.get(ParticipantTypeEnum.$RoleSend.getValue() + ":" + v_Role.getRoleID());
                if ( !Help.isNull(v_Temp) )
                {
                    for (FutureOperator v_FutureOpt : v_Temp)
                    {
                        if ( i_WorkID.equals(v_FutureOpt.getWorkID()) )
                        {
                            return ParticipantTypeEnum.$RoleSend;
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    
    
    /**
     * 初始化缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     */
    @Override
    public synchronized void initCaches()
    {
        if ( $FutureOperatorsByWorkID == null )
        {
            this.queryAllByWorkID();
            this.queryAllByForTemplateName();
            this.queryAll_KeyWorkID();
            this.queryAll_SToWorkID();
        }
    }
    
    
    
    /**
     * 更新缓存中的未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *              v2.0  2024-02-23  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    @Override
    public void updateCache(FlowProcess i_Process ,Template i_Template)
    {
        this.delCache(i_Process ,i_Template);
        this.addCache(i_Process ,i_Template);
    }
    
    
    
    /**
     * 向缓存中添加未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *              v2.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
     *              v3.0  2024-02-23  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    @Override
    public void addCache(FlowProcess i_Process ,Template i_Template)
    {
        addCacheByTrue(i_Process ,i_Template);
        
        CommunicationRequest v_RequestData = new CommunicationRequest();
        v_RequestData.setEventType(    this.getEventType());
        v_RequestData.setDataOperation("addCache");
        v_RequestData.setDataXID(      i_Process.getWorkID());
        v_RequestData.setData(         i_Process);
        this.clusterSyncFlowCache(v_RequestData);
    }
    
    
    
    /**
     * 向缓存中添加未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *              v2.0  2024-02-23  添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    private synchronized void addCacheByTrue(FlowProcess i_Process ,Template i_Template)
    {
        if ( Help.isNull(i_Process.getServiceDataID()) )
        {
            for (ProcessParticipant v_Part : i_Process.getFutureParticipants())
            {
                String         v_ID = v_Part.getObjectType().getValue() + ":" + v_Part.getObjectID();
                FutureOperator v_FO = new FutureOperator();
                v_FO.setWorkID(        i_Process.getWorkID());
                v_FO.setServiceDataID( "");
                v_FO.setProcessID(     i_Process.getProcessID());
                v_FO.setSplitProcessID(i_Process.getSplitProcessID());
                v_FO.setObjectID(      v_Part.getObjectID());
                v_FO.setObjectType(    v_Part.getObjectType().getValue());
                
                if ( $FutureOperators_KeyWorkID.getRow(i_Process.getWorkID() ,v_FO) == null )
                {
                    $FutureOperators_KeyWorkID.putRow(i_Process.getWorkID() ,v_FO);
                }
                if ( $FutureOperatorsByWorkID.getRow(v_ID ,v_FO) == null )
                {
                    $FutureOperatorsByWorkID.putRow(v_ID ,v_FO);
                }
                
                String v_IDTemplate = v_ID + ":" + i_Template.getTemplateName();
                if ( $FutureOperatorsFroTemplateName.getRow(v_IDTemplate ,v_FO) == null )
                {
                    $FutureOperatorsFroTemplateName.putRow(v_IDTemplate ,v_FO);
                }
            }
        }
        else
        {
            $FutureOperators_SToWorkID.put(i_Process.getServiceDataID() ,i_Process.getWorkID());
            
            for (ProcessParticipant v_Part : i_Process.getFutureParticipants())
            {
                String         v_ID = v_Part.getObjectType().getValue() + ":" + v_Part.getObjectID();
                FutureOperator v_FO = new FutureOperator();
                v_FO.setWorkID(        i_Process.getWorkID());
                v_FO.setServiceDataID( i_Process.getServiceDataID());
                v_FO.setProcessID(     i_Process.getProcessID());
                v_FO.setSplitProcessID(i_Process.getSplitProcessID());
                v_FO.setObjectID(      v_Part.getObjectID());
                v_FO.setObjectType(    v_Part.getObjectType().getValue());
                
                if ( $FutureOperators_KeyWorkID.getRow(i_Process.getWorkID() ,v_FO) == null )
                {
                    $FutureOperators_KeyWorkID.putRow(i_Process.getWorkID() ,v_FO);
                }
                if ( $FutureOperatorsByWorkID.getRow(v_ID ,v_FO) == null )
                {
                    $FutureOperatorsByWorkID.putRow(v_ID ,v_FO);
                }
                
                String v_IDTemplate = v_ID + ":" + i_Template.getTemplateName();
                if ( $FutureOperatorsFroTemplateName.getRow(v_IDTemplate ,v_FO) == null )
                {
                    $FutureOperatorsFroTemplateName.putRow(v_IDTemplate ,v_FO);
                }
            }
        }
    }
    
    
    
    /**
     * 删除缓存中的未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *              v2.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
     *              v3.0  2024-02-23  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    @Override
    public void delCache(FlowProcess i_Process ,Template i_Template)
    {
        delCacheByTrue(i_Process ,i_Template);
        
        CommunicationRequest v_RequestData = new CommunicationRequest();
        v_RequestData.setEventType(    this.getEventType());
        v_RequestData.setDataOperation("delCache");
        v_RequestData.setDataXID(      i_Process.getWorkID());
        v_RequestData.setData(         i_Process);
        this.clusterSyncFlowCache(v_RequestData);
    }
    
    
    
    /**
     * 删除缓存中的未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *              v2.0  2024-02-23  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    private synchronized void delCacheByTrue(FlowProcess i_Process ,Template i_Template)
    {
        List<FutureOperator> v_FutureOperators = $FutureOperators_KeyWorkID.get(i_Process.getWorkID());
        
        if ( !Help.isNull(v_FutureOperators) )
        {
            if ( !Help.isNull(i_Process.getSplitProcessID()) )
            {
                // 删除分单情况下的旧缓存
                for (int i=v_FutureOperators.size()-1; i>=0; i--)
                {
                    FutureOperator v_FO = v_FutureOperators.get(i);
                    String         v_ID = v_FO.getObjectType() + ":" + v_FO.getObjectID();
                    
                    if ( i_Process.getWorkID().equals(v_FO.getWorkID()) && i_Process.getPreviousProcessID().equals(v_FO.getProcessID()) )
                    {
                        List<FutureOperator> v_DelDatas = $FutureOperatorsByWorkID.get(v_ID);
                        if ( !Help.isNull(v_DelDatas) )
                        {
                            for (int x=v_DelDatas.size() -1; x>=0; x--)
                            {
                                FutureOperator v_Del = v_DelDatas.get(x);
                                
                                // 未来参与人全相等时
                                if ( v_Del.equals(v_FO) && v_FO.getProcessID().equals(v_Del.getProcessID()) )
                                {
                                    v_Del = $FutureOperatorsByWorkID.removeRow(v_ID ,x);
                                }
                                // 缓存中的流转信息是上一个流转信息
                                else if ( !Help.isNull(i_Process.getPreviousProcessID())
                                       && i_Process.getPreviousProcessID().equals(v_Del.getProcessID()) )
                                {
                                    v_Del = $FutureOperatorsByWorkID.removeRow(v_ID ,x);
                                }
                            }
                        }
                        
                        // 删除人员、流程模板的缓存 Add 2024-02-26
                        v_ID      += ":" + i_Template.getTemplateName();
                        v_DelDatas = $FutureOperatorsFroTemplateName.get(v_ID);
                        if ( !Help.isNull(v_DelDatas) )
                        {
                            for (int x=v_DelDatas.size() -1; x>=0; x--)
                            {
                                FutureOperator v_Del = v_DelDatas.get(x);
                                
                                // 未来参与人全相等时
                                if ( v_Del.equals(v_FO) && v_FO.getProcessID().equals(v_Del.getProcessID()) )
                                {
                                    v_Del = $FutureOperatorsFroTemplateName.removeRow(v_ID ,x);
                                }
                                // 缓存中的流转信息是上一个流转信息
                                else if ( !Help.isNull(i_Process.getPreviousProcessID())
                                       && i_Process.getPreviousProcessID().equals(v_Del.getProcessID()) )
                                {
                                    v_Del = $FutureOperatorsFroTemplateName.removeRow(v_ID ,x);
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                for (int i=v_FutureOperators.size()-1; i>=0; i--)
                {
                    FutureOperator v_FO = v_FutureOperators.get(i);
                    String         v_ID = v_FO.getObjectType() + ":" + v_FO.getObjectID();
                    
                    List<FutureOperator> v_DelDatas = $FutureOperatorsByWorkID.get(v_ID);
                    if ( !Help.isNull(v_DelDatas) )
                    {
                        for (int x=v_DelDatas.size() -1; x>=0; x--)
                        {
                            FutureOperator v_Del = v_DelDatas.get(x);
                            
                            // 未来参与人全相等时
                            if ( v_Del.equals(v_FO) )
                            {
                                v_Del = $FutureOperatorsByWorkID.removeRow(v_ID ,x);
                            }
                            // 缓存中的流转信息是上一个流转信息
                            else if ( !Help.isNull(i_Process.getPreviousProcessID())
                                    && i_Process.getPreviousProcessID().equals(v_Del.getProcessID()) )
                            {
                                v_Del = $FutureOperatorsByWorkID.removeRow(v_ID ,x);
                            }
                        }
                    }
                    
                    // 删除人员、流程模板的缓存 Add 2024-02-26
                    v_ID      += ":" + i_Template.getTemplateName();
                    v_DelDatas = $FutureOperatorsFroTemplateName.get(v_ID);
                    if ( !Help.isNull(v_DelDatas) )
                    {
                        for (int x=v_DelDatas.size() -1; x>=0; x--)
                        {
                            FutureOperator v_Del = v_DelDatas.get(x);
                            
                            // 未来参与人全相等时
                            if ( v_Del.equals(v_FO) )
                            {
                                v_Del = $FutureOperatorsFroTemplateName.removeRow(v_ID ,x);
                            }
                            // 缓存中的流转信息是上一个流转信息
                            else if ( !Help.isNull(i_Process.getPreviousProcessID())
                                    && i_Process.getPreviousProcessID().equals(v_Del.getProcessID()) )
                            {
                                v_Del = $FutureOperatorsFroTemplateName.removeRow(v_ID ,x);
                            }
                        }
                    }
                }
            }
        }
        
        if ( !Help.isNull(i_Process.getSplitProcessID()) && !Help.isNull(v_FutureOperators) )
        {
            // 删除分单情况下的旧缓存
            for (int i=v_FutureOperators.size()-1; i>=0; i--)
            {
                FutureOperator v_FO = v_FutureOperators.get(i);
                
                if ( !Help.isNull(i_Process.getPreviousProcessID())
                   && i_Process.getPreviousProcessID().equals(v_FO.getProcessID()) )
                {
                    $FutureOperators_KeyWorkID.removeRow(i_Process.getWorkID() ,v_FO);
                }
            }
        }
        else
        {
            $FutureOperators_KeyWorkID.remove(i_Process.getWorkID());
        }
    }
    
    
    
    /**
     * 删除缓存中的当前工作流的所有未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-27
     * @version     v1.0
     *              v2.0  2024-02-26  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    @Override
    public void delCacheByAll(FlowProcess i_Process ,Template i_Template)
    {
        delCacheByAllTrue(i_Process ,i_Template);
        
        CommunicationRequest v_RequestData = new CommunicationRequest();
        v_RequestData.setEventType(    this.getEventType());
        v_RequestData.setDataOperation("delCacheByAll");
        v_RequestData.setDataXID(      i_Process.getWorkID());
        v_RequestData.setData(         i_Process);
        this.clusterSyncFlowCache(v_RequestData);
    }
    
    
    
    /**
     * 删除缓存中的当前工作流的所有未来操作人信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-02-27
     * @version     v1.0
     *              v2.0  2024-02-26  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    private synchronized void delCacheByAllTrue(FlowProcess i_Process ,Template i_Template)
    {
        List<FutureOperator> v_FutureOperators = $FutureOperators_KeyWorkID.get(i_Process.getWorkID());
        
        if ( !Help.isNull(v_FutureOperators) )
        {
            for (int i=v_FutureOperators.size()-1; i>=0; i--)
            {
                FutureOperator v_FO = v_FutureOperators.get(i);
                String         v_ID = v_FO.getObjectType() + ":" + v_FO.getObjectID();
                
                List<FutureOperator> v_DelDatas = $FutureOperatorsByWorkID.get(v_ID);
                if ( !Help.isNull(v_DelDatas) )
                {
                    for (int x=v_DelDatas.size() -1; x>=0; x--)
                    {
                        FutureOperator v_Del = v_DelDatas.get(x);
                        if ( v_FO.getWorkID().equals(v_Del.getWorkID()) )
                        {
                            v_Del = $FutureOperatorsByWorkID.removeRow(v_ID ,x);
                        }
                    }
                }
                
                // 删除人员、流程模板的缓存 Add 2024-02-26
                v_ID      += ":" + i_Template.getTemplateName();
                v_DelDatas = $FutureOperatorsFroTemplateName.get(v_ID);
                if ( !Help.isNull(v_DelDatas) )
                {
                    for (int x=v_DelDatas.size() -1; x>=0; x--)
                    {
                        FutureOperator v_Del = v_DelDatas.get(x);
                        if ( v_FO.getWorkID().equals(v_Del.getWorkID()) )
                        {
                            v_Del = $FutureOperatorsFroTemplateName.removeRow(v_ID ,x);
                        }
                    }
                }
            }
            
            $FutureOperators_KeyWorkID.remove(i_Process.getWorkID());
        }
    }
    
    
    
    /**
     * 删除缓存中的未来操作人信息。在转历史单时触发。
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-11
     * @version     v1.0
     *              v2.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
     *              v3.0  2024-02-23  1. 添加：流程模板信息，从中获取模板名称
     *
     * @param i_Process   实例流转信息
     * @param i_Template  流程模板信息
     */
    @Override
    public synchronized void delCacheToHistory(FlowProcess i_Process ,Template i_Template)
    {
        i_Process.setSplitProcessID("");
        
        delCache(i_Process ,i_Template);
        
        if ( !Help.isNull(i_Process.getServiceDataID()) )
        {
            $FutureOperators_SToWorkID.remove(i_Process.getServiceDataID());
            
            CommunicationRequest v_RequestData = new CommunicationRequest();
            v_RequestData.setEventType(    this.getEventType());
            v_RequestData.setDataOperation("delCacheToHistory");
            v_RequestData.setDataXID(      i_Process.getServiceDataID());
            this.clusterSyncFlowCache(v_RequestData);
        }
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
    @Override
    public PartitionMap<String ,FutureOperator> queryAllByWorkID()
    {
        if ( $FutureOperatorsByWorkID == null )
        {
            $FutureOperatorsByWorkID = this.futureOperatorDAO.queryAllByWorkID();
        }
        return $FutureOperatorsByWorkID;
    }
    
    
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询（按流程模板名称过滤）
     * 
     *   Map.key分区为参与人的形式的值：objectType:objectID
     *   Map.value元素为工作流实例ID：workID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-23
     * @version     v1.0
     *
     * @return
     */
    @Override
    public PartitionMap<String ,FutureOperator> queryAllByForTemplateName()
    {
        if ( $FutureOperatorsFroTemplateName == null )
        {
            $FutureOperatorsFroTemplateName = this.futureOperatorDAO.queryAllFroTemplateName();
        }
        return $FutureOperatorsFroTemplateName;
    }
    
    
    
    /**
     * 查询所有未来操作人，并分区保存，用于高速缓存查询
     * 
     *   Map.key    分区为参与人的形式的值：objectType:objectID
     *   Map.value  元素为工作流未来操作人对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-15
     * @version     v1.0
     *
     * @return
     */
    public PartitionMap<String ,FutureOperator> queryAll_KeyWorkID()
    {
        if ( $FutureOperators_KeyWorkID == null )
        {
            $FutureOperators_KeyWorkID = this.futureOperatorDAO.queryAll_KeyWorkID();
        }
        return $FutureOperators_KeyWorkID;
    }
    
    
    
    /**
     * 查询所有未来操作人，用业务ID找实例ID
     * 
     *   Map.key    为业务ID - serviceDataID
     *   Map.value  为实例ID - workID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-11
     * @version     v1.0
     *
     * @return
     */
    public Map<String ,String> queryAll_SToWorkID()
    {
        if ( $FutureOperators_SToWorkID == null )
        {
            $FutureOperators_SToWorkID = this.futureOperatorDAO.queryAll_SToWorkID();
        }
        return $FutureOperators_SToWorkID;
    }
    
    
    
    /**
     * 业务ID找工作流ID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-12
     * @version     v1.0
     *
     * @param i_ServiceDataID  业务ID
     * @return                 返回工作流ID
     */
    @Override
    public String querySToWorkID(String i_ServiceDataID)
    {
        return $FutureOperators_SToWorkID.get(i_ServiceDataID);
    }
    
    
    
    /**
     * 业务ID找工作流ID的高速缓存中添加新关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2019-09-12
     * @version     v1.0
     *              v2.0  2020-01-02  1. 添加：工作流引擎集群，同步引擎数据
     *
     * @param i_WorkID         工作流实例ID
     * @param i_ServiceDataID  业务ID
     */
    @Override
    public void pushSToWorkID(String i_WorkID ,String i_ServiceDataID)
    {
        $FutureOperators_SToWorkID.put(i_ServiceDataID ,i_WorkID);
        
        CommunicationRequest v_RequestData = new CommunicationRequest();
        v_RequestData.setEventType(    this.getEventType());
        v_RequestData.setDataOperation("pushSToWorkID");
        v_RequestData.setDataXID(      i_ServiceDataID);
        v_RequestData.setData(         i_WorkID);
        this.clusterSyncFlowCache(v_RequestData);
    }
    
    
    
    /**
     *  数据通讯的事件类型。即通知哪一个事件监听者来处理数据通讯（对应 ServerSocket.listeners 的分区标识）
     * 
     *  事件类型区分大小写
     * 
     * @author      ZhengWei(HY)
     * @createDate  2017-01-16
     * @version     v1.0
     *
     * @return
     */
    @Override
    public String getEventType()
    {
        return "CL_FlowFutureOperator";
    }
    
    
    
    /**
     * 数据通讯事件的执行动作
     * 
     * @author      ZhengWei(HY)
     * @createDate  2020-01-02
     * @version     v1.0
     *
     * @param i_RequestData
     * @return
     */
    @Override
    public CommunicationResponse communication(CommunicationRequest i_RequestData)
    {
        CommunicationResponse v_ResponseData = new CommunicationResponse();
        
        if ( Help.isNull(i_RequestData.getDataXID()) )
        {
            v_ResponseData.setResult(1);
            return v_ResponseData;
        }
        
        if ( "pushSToWorkID".equals(i_RequestData.getDataOperation()) )
        {
            if ( i_RequestData.getData() == null )
            {
                v_ResponseData.setResult(2);
                return v_ResponseData;
            }
            
            while ( $FutureOperators_SToWorkID == null )
            {
                try
                {
                    Thread.sleep(50);
                }
                catch (Exception exce)
                {
                    // Nothing.
                }
            }
            
            $FutureOperators_SToWorkID.put(i_RequestData.getDataXID() ,i_RequestData.getData().toString());
        }
        else if ( "delCacheToHistory".equals(i_RequestData.getDataOperation()) )
        {
            while ( $FutureOperators_SToWorkID == null )
            {
                try
                {
                    Thread.sleep(50);
                }
                catch (Exception exce)
                {
                    // Nothing.
                }
            }
            
            $FutureOperators_SToWorkID.remove(i_RequestData.getDataXID());
        }
        else if ( "addCache".equals(i_RequestData.getDataOperation()) )
        {
            if ( i_RequestData.getData() == null )
            {
                v_ResponseData.setResult(2);
                return v_ResponseData;
            }
            
            while ( $FutureOperatorsByWorkID == null )
            {
                try
                {
                    Thread.sleep(50);
                }
                catch (Exception exce)
                {
                    // Nothing.
                }
            }
            
            FlowProcess v_FlowProcess = (FlowProcess)i_RequestData.getData();
            Template    v_Template    = this.templateService.queryByIDByTrue(v_FlowProcess.getTemplateID());
            
            this.addCacheByTrue(v_FlowProcess ,v_Template);
        }
        else if ( "delCache".equals(i_RequestData.getDataOperation()) )
        {
            if ( i_RequestData.getData() == null )
            {
                v_ResponseData.setResult(2);
                return v_ResponseData;
            }
            
            while ( $FutureOperatorsByWorkID == null )
            {
                try
                {
                    Thread.sleep(50);
                }
                catch (Exception exce)
                {
                    // Nothing.
                }
            }
            
            FlowProcess v_FlowProcess = (FlowProcess)i_RequestData.getData();
            Template    v_Template    = this.templateService.queryByIDByTrue(v_FlowProcess.getTemplateID());
            
            this.delCacheByTrue(v_FlowProcess ,v_Template);
        }
        
        return v_ResponseData;
    }
    
}
