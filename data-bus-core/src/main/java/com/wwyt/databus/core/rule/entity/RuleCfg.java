package com.wwyt.databus.core.rule.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ToString
public class RuleCfg {
    @NotBlank(message = "RuleId不能为空")
    @Length(max = 32)
    private String id;
    @NotBlank(message = "desc备注信息不能为空")
    @Length(max = 255)
    private String desc;
    @Valid
    @NotEmpty(message = "binlog监控配置不能为空")
    private List<Monitor> monitor;
    //@NotEmpty(message = "数据获取器配置不能为空")
    private Fetcher fetcher;
    @Valid
    @NotEmpty(message = "数据处理器配置不能为空")
    private List<Processor> processor;
}
