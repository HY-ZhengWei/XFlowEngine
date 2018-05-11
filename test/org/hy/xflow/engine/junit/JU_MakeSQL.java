package org.hy.xflow.engine.junit;

import org.hy.common.Help;
import org.hy.xflow.engine.bean.FlowInfo;
import org.hy.xflow.engine.bean.ProcessParticipant;
import org.hy.xflow.engine.common.BaseJunit;
import org.junit.Test;





/**
 * 自动 生成SQL语句
 *
 * @author      ZhengWei(HY)
 * @createDate  2018-05-11
 * @version     v1.0
 */
public class JU_MakeSQL extends BaseJunit
{
    
    /**
     * 自动生成SQL语句
     */
    @Test
    public void test001()
    {
        System.out.println(Help.toSQLSelect(FlowInfo.class ,"A" ,false));
        System.out.println("\n\n");
        
        System.out.println(Help.toSQLInsert(ProcessParticipant.class ,false));
        System.out.println("\n\n");
        
        System.out.println(Help.toSQLUpdate(FlowInfo.class ,false));
        System.out.println("\n\n");
    }
    
}
