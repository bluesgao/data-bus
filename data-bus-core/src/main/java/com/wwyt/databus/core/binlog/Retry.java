package com.wwyt.databus.core.binlog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Retry implements Serializable {
    private static final long serialVersionUID = 1L;
    private int retryCount;
    private long retryTimestamp;
    private String retryCause;
}
