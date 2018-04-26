package org.hy.xflow.engine.common;

import org.hy.common.StringHelp;





/**
 * ID生成器 
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-04-25
 * @version     v1.0
 */
public class IDHelp
{
    
    public static String makeID()
    {
        return StringHelp.getUUID();
    }
    
}
