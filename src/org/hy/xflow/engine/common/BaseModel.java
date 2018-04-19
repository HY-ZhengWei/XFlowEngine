package org.hy.xflow.engine.common;

import org.hy.common.xml.SerializableDef;





/**
 * 基础值对象类
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-01-29
 * @version     v1.0
 */
public class BaseModel extends SerializableDef
{
	private static final long serialVersionUID = -4175348701996946560L;
	
	
    
    /**
     * 通用设置属性的方法
     *
     * @author      ZhengWei(HY)
     * @createDate  2015-07-23
     * @version     v1.0
     *
     * @param i_PropertyIndex
     * @param i_Value
     *
     */
    public void propertyValue(int i_PropertyIndex ,Object i_Value)
    {
        super.setPropertyValue(i_PropertyIndex ,i_Value);
    }
    
}
