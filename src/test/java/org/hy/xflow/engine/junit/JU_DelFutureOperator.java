package org.hy.xflow.engine.junit;

import org.hy.common.PartitionMap;
import org.hy.common.TablePartition;
import org.hy.xflow.engine.bean.FutureOperator;
import org.junit.Test;

public class JU_DelFutureOperator
{
    
    @Test
    public void del()
    {
        PartitionMap<String ,FutureOperator> v_Datas = new TablePartition<String ,FutureOperator>();
        
        
        FutureOperator v_D1 = new FutureOperator();
        v_D1.setWorkID("1");
        v_D1.setObjectID("2");
        v_D1.setObjectType(1);
        
        FutureOperator v_D2 = new FutureOperator();
        v_D2.setWorkID("1");
        v_D2.setObjectID("2");
        v_D2.setObjectType(1);
        
        v_Datas.putRow("1" ,v_D1);
        
        
        System.out.println(v_Datas.removeRow("1" ,v_D2));
    }
    
}
