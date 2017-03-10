package org.hy.xflow.engine.config;

import java.util.List;

import org.hy.common.app.Param;
import org.hy.common.thread.ThreadPool;
import org.hy.common.xml.XJava;
import org.hy.common.xml.plugins.AppInitConfig;





/**
 * 初始化信息
 * 
 * @author      ZhengWei(HY)
 * @createDate  2017-03-10
 * @version     v1.0  
 */
public final class InitConfig extends AppInitConfig
{
    
    private static boolean $Init = false;
    
    
    
    public InitConfig()
    {
        this(true);
    }
    
    
    
    public InitConfig(boolean i_IsStartJobs)
    {
        init(i_IsStartJobs);
    }
    
    
    
    @SuppressWarnings("unchecked")
    private synchronized void init(boolean i_IsStartJobs)
    {
        if ( !$Init )
        {
            $Init = true;
            
            try
            {
                this.init("sys.Config.xml");
                this.init("startup.Config.xml");
                this.init((List<Param>)XJava.getObject("StartupConfig"));
                this.init(((Param)XJava.getObject("RootPackageName")).getValue());
                
                if ( i_IsStartJobs )
                {
                    init_TPool();
                    this.init("job.Config.xml");
                }
            }
            catch (Exception exce)
            {
                System.out.println(exce.getMessage());
                exce.printStackTrace();
            }
        }
    }
    
    
    
    private void init_TPool()
    {
        ThreadPool.setMaxThread(    this.getIntConfig("TPool_MaxThread"));
        ThreadPool.setMinThread(    this.getIntConfig("TPool_MinThread"));
        ThreadPool.setMinIdleThread(this.getIntConfig("TPool_MinIdleThread"));
        ThreadPool.setIntervalTime( this.getIntConfig("TPool_IntervalTime"));
        ThreadPool.setIdleTimeKill( this.getIntConfig("TPool_IdleTimeKill"));
        ThreadPool.setWatch(        Boolean.parseBoolean(XJava.getParam("TPool_IsWatch").getValue()));
    }
    
    
    
    private int getIntConfig(String i_XJavaID)
    {
        return Integer.parseInt(XJava.getParam(i_XJavaID).getValue());
    }
    
}
