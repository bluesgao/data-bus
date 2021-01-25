package com.wwyt.databus.core.binlog;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BinlogItemWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private BinlogItem binlogItem;
    private Map<String, Object> data;

    public static List<BinlogItemWrapper> getBinlogItems(Binlog binlog) {
        List<BinlogItemWrapper> binlogItemWrappers = new ArrayList<>(8);
        for (int i = 0; i < binlog.getData().size(); i++) {
            BinlogItem binlogItem = new BinlogItem();
            binlogItem.setData(binlog.getData().get(i));
            binlogItem.setDatabase(binlog.getDatabase());
            binlogItem.setTable(binlog.getTable());
            binlogItem.setMysqlType(binlog.getMysqlType());
            binlogItem.setOld(binlog.getOld().get(i));
            binlogItem.setPkNames(binlog.getPkNames());
            binlogItem.setType(binlog.getType());
            binlogItem.setMysqlType(binlog.getMysqlType());

            BinlogItemWrapper itemWrapper = new BinlogItemWrapper();
            itemWrapper.setBinlogItem(binlogItem);
            itemWrapper.setData(new HashMap<>(8));
            binlogItemWrappers.add(itemWrapper);
        }
        return binlogItemWrappers;
    }
}
