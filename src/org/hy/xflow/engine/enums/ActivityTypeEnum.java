package org.hy.xflow.engine.enums;

import org.hy.xflow.engine.common.BaseEnum;





/**
 * 活动类型
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-11
 * @version     v1.0
 */
public enum ActivityTypeEnum implements BaseEnum<String>
{
    
    $Start("AT000"  ,"开始"),
    
    $Normal("AT001" ,"常规"),
    
    $Finish("AT999" ,"结束");
    
    
    
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
    public static ActivityTypeEnum get(String i_Value)
    {
        if ( i_Value == null )
        {
            return null;
        }
        
        for (ActivityTypeEnum v_Enum : ActivityTypeEnum.values()) 
        {
            if ( v_Enum.value.equals(i_Value.trim()) ) 
            {
                return v_Enum;
            }
        }
        
        return null;
    }
    
    
    
    ActivityTypeEnum(String i_Value ,String i_Desc)
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
