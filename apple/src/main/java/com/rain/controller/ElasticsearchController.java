package com.rain.controller;

import com.rain.annotation.aopLog.Loggable;
import com.rain.responseVo.ResultVo;
import io.swagger.annotations.Api;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * elasticsearch curd demo
 *
 * @author zhangzhenyan
 * 2024-01-23
 **/
@Api(description = "es demo")
@RestController
@RequestMapping(value = "/elasticsearch")
@Loggable(loggable = true)
public class ElasticsearchController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(ElasticsearchController.class);

    private static final String INDEX_NAME = "study_index_name";

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @PostMapping("/insertDocument")
    public ResultVo<String> insertDocument(String documentId) throws Exception {
        IndexResponse indexResponse = insertDocument(restHighLevelClient, documentId, "{\"field1\":\"value1\",\"field2\":\"value2\"}");
        logger.info("Document indexed with ID: " + indexResponse.getId());
        return ResultVo.success(indexResponse.getId(), "insertDocument success...");
    }

    @PostMapping("/getDocument")
    public ResultVo<Map<String, Object>> getDocument(String documentId) throws Exception {
        return ResultVo.success(getDocument(restHighLevelClient, documentId), "getDocument success...");
    }

    @PostMapping("/updateDocument")
    public ResultVo<String> updateDocument(String documentId) throws Exception {
        UpdateResponse updateResponse = updateDocument(restHighLevelClient, documentId, "{\"field1\":\"updated_value1\"}");
        logger.info("Document indexed with ID: " + updateResponse.getId());
        return ResultVo.success(updateResponse.getId(), "updateDocument success...");
    }

    @PostMapping("/deleteDocument")
    public ResultVo<String> deleteDocument(String documentId) throws Exception {
        DeleteResponse deleteResponse = deleteDocument(restHighLevelClient, documentId);
        logger.info("Document indexed with ID: " + deleteResponse.getId());
        return ResultVo.success(deleteResponse.getId(), "deleteDocument success...");
    }

    private IndexResponse insertDocument(RestHighLevelClient client, String documentId, String document) throws IOException {
        IndexRequest request = new IndexRequest(INDEX_NAME).id(documentId).source(document, XContentType.JSON);

        return client.index(request, RequestOptions.DEFAULT);
    }

    private Map<String, Object> getDocument(RestHighLevelClient client, String documentId) throws IOException {
        GetRequest getRequest = new GetRequest(INDEX_NAME, documentId);
        return client.get(getRequest, RequestOptions.DEFAULT).getSource();
    }

    private UpdateResponse updateDocument(RestHighLevelClient client, String documentId, String updatedDocument) throws IOException {
        UpdateRequest request = new UpdateRequest(INDEX_NAME, documentId).doc(updatedDocument, XContentType.JSON);

        return client.update(request, RequestOptions.DEFAULT);
    }

    private DeleteResponse deleteDocument(RestHighLevelClient client, String documentId) throws IOException {
        DeleteRequest request = new DeleteRequest(INDEX_NAME, documentId);

        return client.delete(request, RequestOptions.DEFAULT);
    }
}
