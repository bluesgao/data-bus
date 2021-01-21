package com.wyyt.databus.app.config;

import com.alibaba.fastjson.JSON;
import com.wwyt.databus.core.rule.RuleCfgHolder;
import com.wwyt.databus.core.rule.RuleCfgValidor;
import com.wwyt.databus.core.rule.entity.RuleCfg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 加载规则配置文件
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
/*        List<String> ruleList = FileUtil.listFileNames(rulePath);
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
        });*/

        //文件读取
        StringBuilder ruleStr = new StringBuilder();
        InputStreamReader input = null;
        BufferedReader br = null;
        try {
            Resource resource = new ClassPathResource(rulePath);
            input = new InputStreamReader(resource.getInputStream());
            br = new BufferedReader(input);
            String str = null;
            while ((str = br.readLine()) != null) {
                ruleStr.append(str);
            }

        } catch (Exception e) {
            log.error("rule-cfg文件读取失败", e.getMessage());
            throw new RuntimeException("rule-cfg文件读取失败", e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //文件解析
        List<RuleCfg> ruleCfgList = null;
        try {
            ruleCfgList = JSON.parseArray(ruleStr.toString(), RuleCfg.class);
        } catch (Exception e) {
            log.error("rule-cfg文件解析失败", e.getMessage());
            throw new RuntimeException("rule-cfg文件解析失败", e);

        }

        for (RuleCfg ruleCfg : ruleCfgList) {
            // 验证配置是否合法
            RuleCfgValidor.isValid(ruleCfg);
            RuleCfgHolder.putRuleCfg(ruleCfg);
        }

        log.info("*** 结束-加载规则配置文件 *** ruleCfgList:{}", JSON.toJSONString(ruleCfgList));

    }
}
