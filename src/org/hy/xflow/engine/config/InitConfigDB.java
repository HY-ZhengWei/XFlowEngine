package org.hy.xflow.engine.config;

import org.hy.common.xml.plugins.AppInitConfig;





/**
 * 初始化数据库信息
 * 
 * @author      ZhengWei(HY)
 * @createDate  2018-05-14
 * @version     v1.0  
 */
public final class InitConfigDB extends AppInitConfig
{
    
    private static boolean $Init = false;
    
    
    
    public InitConfigDB()
    {
        init();
    }
    
    
    
    private synchronized void init()
    {
        if ( !$Init )
        {
            $Init = true;
            
            try
            {
                this.init("sys.DB.Config.xml");
            }
            catch (Exception exce)
            {
                System.out.println(exce.getMessage());
                exce.printStackTrace();
            }
        }
    }
    
}
