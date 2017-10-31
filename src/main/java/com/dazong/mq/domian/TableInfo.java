package com.dazong.mq.domian;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author huqichao
 * @create 2017-10-30 19:29
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper=true)
public class TableInfo implements Serializable {

    private String dbName;

    private String tableName;

    private String tableDesc;

    public int getVersion(){
        String[] str = tableDesc.split("-");
        return Integer.valueOf(str[1]);
    }

    public String getComment(int verion){
        String[] str = tableDesc.split("-");
        return str[0] + "-" + verion;
    }
}
