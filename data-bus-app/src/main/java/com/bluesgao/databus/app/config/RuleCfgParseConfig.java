package com.bluesgao.databus.app.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bluesgao.databus.app.common.Constants;
import com.bluesgao.databus.core.rule.RuleCfgHolder;
import com.bluesgao.databus.core.rule.RuleCfgValidor;
import com.bluesgao.databus.core.rule.entity.RuleCfg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

/**
 * 加载规则配置文件
 * 在DataSource之后加载，验证规则中是否有未配置的DataSource*
 */
@Slf4j
@Configuration
public class RuleCfgParseConfig {

    @Value("${databus.rule.path}")
    protected String rulePath;

    @PostConstruct
    public void init() {
        //todo 文件读取问题
        //String path = new ClassPathResource(rulePath).getPath();
        log.info("*** 开始-加载规则配置文件 *** path:{}", rulePath);
        List<String> ruleList = FileUtil.listFileNames(rulePath);
        if (CollectionUtils.isEmpty(ruleList)) {
            log.warn("{}, 未发现配置文件", rulePath);
        }
        ruleList.forEach(rule -> {
            if (rule.endsWith(Constants.SETTING_SUFFIX)) {
                File file = FileUtil.file(rulePath, rule);
                log.info("配置文件路径：{}", file.toString());
                String content = FileUtil.readUtf8String(file);
                if (StrUtil.isNotBlank(content)) {
                    RuleCfg ruleCfg = JSON.parseObject(content, RuleCfg.class);
                    log.info("{}", ruleCfg.toString());
                    // 验证配置是否合法
                    RuleCfgValidor.isValid(ruleCfg);
                    RuleCfgHolder.putRuleCfg(ruleCfg);
                }
            }
        });
    }
}
