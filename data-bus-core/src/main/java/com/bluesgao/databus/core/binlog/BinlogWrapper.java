package com.bluesgao.databus.core.binlog;

import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BinlogWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private Binlog binlog;
    private Retry retry;
    private Map<String, Object> data;

    public static BinlogWrapper warp(Binlog binlog) {
        return warp(binlog, 0, "", 0);
    }

    public static BinlogWrapper warp(Binlog binlog, int retryCount, String retryCause, long retryTimestamp) {
        return new BinlogWrapper(binlog, new Retry(retryCount, retryTimestamp, retryCause), new HashMap<>(32));
    }
}
