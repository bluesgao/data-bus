package com.wyyt.databus.processor;

import com.alibaba.fastjson.JSON;
import com.wwyt.databus.plugin.common.constants.EsCfgConstants;
import com.wwyt.databus.plugin.common.constants.RedisCfgConstants;
import com.wwyt.databus.plugin.common.enums.EventType;
import com.wwyt.databus.plugin.processor.DataProcessor;
import com.wwyt.databus.plugin.processor.DataProcessorResult;
import com.wyyt.databus.ds.EsBuilder;
import com.wyyt.databus.ds.EsProps;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName：ElasticSearchProcessor
 * @Description：
 * @Author：bluesgao
 * @Date：2021/1/18 11:16
 **/
@Slf4j
public class ElasticSearchProcessor implements DataProcessor {


    @Override
    public DataProcessorResult process(Map<String, Object> params, String event, Map<String, Object> data) {
        log.info("ElasticSearchProcessor ****开始处理****");
        if (params != null && params.size() > 0) {
            //进行参数验证
            String checkResult = checkParams(params);
            if (checkResult != null && checkResult.length() > 0) {
                return DataProcessorResult.fail(checkResult);
            }
        }

        RestHighLevelClient esClient = getElasticSearchClient(params);
        if (esClient == null) {
            return DataProcessorResult.fail(String.format("es client创建失败[%s]", JSON.toJSON(params)));
        }

        String index = params.get(EsCfgConstants.index).toString();

        if (!indexExist(esClient, index)) {
            //关闭 es client
            closeElasticSearchClient(esClient);
            return DataProcessorResult.fail(String.format("es index不存在[%s]", JSON.toJSON(index)));
        }

        if (event.equalsIgnoreCase(EventType.INSERT.getEvent())) {
            log.info("INSERT 处理中");

        } else if (event.equalsIgnoreCase(EventType.UPDATE.getEvent())) {
            log.info("UPDATE 处理中");

        } else if (event.equalsIgnoreCase(EventType.DELETE.getEvent())) {
            log.info("DELETE 处理中");

        }
        return DataProcessorResult.fail("");
    }


    @Override
    public String getName() {
        return ElasticSearchProcessor.class.getCanonicalName();
    }

    /**
     * 验证索引是否存在
     *
     * @param esClient
     * @param index
     * @return
     */
    private boolean indexExist(RestHighLevelClient esClient, String index) {
        GetRequest getRequest = new GetRequest();
        getRequest.index(index);
        boolean ret = false;
        try {
            ret = esClient.exists(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    private boolean indexDoc(RestHighLevelClient client, String index, String docId, Map<String, Object> data) {
        // 1、创建索引请求  //索引  // mapping type  //文档id
        IndexRequest indexRequest = new IndexRequest(index);
        indexRequest.id(docId);
        // 2、准备文档数据
        indexRequest.source(data);
        //3、发送请求
        IndexResponse indexResponse = null;
        try {
            // 同步方式
            indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            e.printStackTrace();
            // 捕获，并处理异常
            //判断是否版本冲突、create但文档已存在冲突
            if (e.status() == RestStatus.CONFLICT) {
                System.out.println("冲突了，请在此写冲突处理逻辑！" + e.getDetailedMessage());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (indexResponse.status() == RestStatus.CREATED) {
            return true;
        } else {
            return false;
        }
    }

    private void upsertDoc(RestHighLevelClient client, String index, String docId, long version, Map<String, Object> data) {
        // 1、创建索引请求  //索引  // mapping type  //文档id
        UpdateRequest request = new UpdateRequest(index, docId);
/*        Map<String, Object> parameters = singletonMap("count", 4);

        Script inline = new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG,
                "ctx._source.field += params.count", parameters);
        request.script(inline);*/
        request.docAsUpsert(true);
        request.doc(data);
        request.version(version);
        request.versionType(VersionType.EXTERNAL);

        try {
            UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void deleteDoc(RestHighLevelClient client, String index, String docId) {
        DeleteRequest request = new DeleteRequest(index, docId);
        try {
            DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * es 客户端
     *
     * @param params
     * @return
     */
    private RestHighLevelClient getElasticSearchClient(Map<String, Object> params) {
        EsProps props = new EsProps();
        props.setClusterHostPort(params.get(EsCfgConstants.clusterHostPort).toString());
        props.setUsername(params.get(RedisCfgConstants.username).toString());
        props.setPassword(params.get(RedisCfgConstants.password).toString());
        RestHighLevelClient esClient = EsBuilder.build(props);
        return esClient;
    }

    /**
     * 关闭es client
     *
     * @param esClient
     */
    private void closeElasticSearchClient(RestHighLevelClient esClient) {
        try {
            esClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 参数检查
     *
     * @param params
     * @return
     */
    private String checkParams(Map<String, Object> params) {
        StringBuilder err = new StringBuilder();
        if (Objects.isNull(params.get(EsCfgConstants.clusterHostPort))) {
            err.append("clusterHostPort为空;");
        } else if (Objects.isNull(params.get(RedisCfgConstants.username))) {
            err.append("username为空;");
        } else if (Objects.isNull(params.get(RedisCfgConstants.password))) {
            err.append("password为空;");
        }
        return err.toString();
    }
}
