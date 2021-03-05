package com.wyyt.databus.app.config;

import com.alibaba.fastjson.JSON;
import com.wwyt.databus.core.rule.RuleCfgHolder;
import com.wwyt.databus.core.rule.entity.RuleCfg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 加载规则配置文件
 */
@Slf4j
@Configuration
public class RuleCfgParseConfig {

    //@Value("${databus.rule.path}")
    //protected String rulePath;
    @Value("#{'${databus.rule.files}'.split(',')}")
    private String[] ruleFileList;

    @PostConstruct
    public void init() {
        //todo 文件读取问题
        //String path = new ClassPathResource(rulePath).getPath();
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
/*        StringBuilder ruleStr = new StringBuilder();
        InputStreamReader input = null;
        BufferedReader br = null;
        try {
            log.info("配置文件路径:{}",resource.getFile().getAbsoluteFile());

            //input = new InputStreamReader(resource.getInputStream());
            input = new InputStreamReader(this.getClass().getResourceAsStream(rulePath));
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
        }*/

        log.info("*** 开始处理规则配置文件 *** ruleFileList:{}", JSON.toJSONString(ruleFileList));
        for (String ruleFileName : ruleFileList) {
            log.info("*** 开始解析规则配置文件-{}", ruleFileName);
            //获取文件内容
            String ruleStr = getFileContent(ruleFileName);
            //文件解析
            List<RuleCfg> ruleCfgList = null;
            try {
                ruleCfgList = JSON.parseArray(ruleStr, RuleCfg.class);
            } catch (Exception e) {
                log.error("规则配置文件{}解析失败", ruleFileName, e.getMessage());
                throw new RuntimeException("规则配置文件解析失败", e);
            }
            log.debug("rule-cfg文件解析:{}", JSON.toJSONString(ruleCfgList));

            for (RuleCfg ruleCfg : ruleCfgList) {
                // 验证配置是否合法
                //RuleCfgValidor.isValid(ruleCfg);
                RuleCfgHolder.putRuleCfg(ruleCfg);
            }
            log.info("*** 完成解析规则配置文件-{},规则数量{}", ruleFileName, ruleCfgList.size());
        }
        log.info("*** 结束处理规则配置文件 ***");
    }

    private String getFileContent(String fileName) {
        Resource resource = new ClassPathResource(fileName);

        String content = null;
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            content = new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.debug("content:{}", content);

        if (content == null || content.length() <= 0) {
            throw new RuntimeException("文件读取失败:" + fileName);
        }
        return content;
    }

}
