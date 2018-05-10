package org.hy.xflow.engine.enums;

import org.hy.xflow.engine.common.BaseEnum;





/**
 * 路由类型
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-10
 * @version     v1.0
 */
public enum RouteTypeEnum implements BaseEnum<String>
{
    
    $Normal("RT001" ,"转派"),
    
    $ToMany("RT002" ,"分派"),
    
    $ToSum("RT003"  ,"汇签"),
    
    $Reject("RT020" ,"驳回"),
    
    $Finish("RT999" ,"结束");
    
    
    
    /**
     * 值
     */
    private String value;
    
    /**
     * 描述
     */
    private String  desc;
    
    
    
    /**
     * 数值转为常量
     * 
     * @author      ZhengWei(HY)
     * @createDate  2018-05-08
     * @version     v1.0
     *
     * @param i_Value
     * @return
     */
    public static RouteTypeEnum get(String i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (RouteTypeEnum v_Enum : RouteTypeEnum.values()) 
        {
            if ( v_Enum.value.equals(i_Value.trim()) ) 
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    RouteTypeEnum(String i_Value ,String i_Desc)
    {
        this.value = i_Value;
        this.desc  = i_Desc;
    }

    
    
    @Override
    public String getValue()
    {
        return this.value;
    }
    
    
    
    public String getDesc()
    {
        return this.desc;
    }
    
    

    @Override
    public String toString()
    {
        return this.value;
    }
    
}
