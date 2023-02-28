package org.hy.xflow.engine.common;

import org.hy.xflow.engine.config.InitConfig;
import org.hy.xflow.engine.config.InitConfigDB;





/**
 * 基础测试类
 * 
 * @author      ZhengWei(HY)
 * @createDate  2014-09-23
 * @version     v1.0
 */
public class BaseJunit extends Base
{
    private static boolean $IsInit = false;
    
    
    
    public BaseJunit()
    {
        synchronized ( this )
        {
            if ( !$IsInit )
            {
                $IsInit = true;
                
                new InitConfigDB();
                new InitConfig(false);
            }
        }
    }
    
}
