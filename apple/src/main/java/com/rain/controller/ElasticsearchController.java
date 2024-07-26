package com.rain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rain.annotation.aopLog.Loggable;
import com.rain.api.apple.model.User;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * elasticsearch curd
 *
 * @author zhangzhenyan
 * 2024-01-23
 **/
@Api("elasticsearch curd")
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
        // build user
        User user = new User();
        user.setId(documentId);
        user.setName("cat");
        user.setPassword("120157229");
        user.setStatus(0);
        user.setSalary(16000);
        user.setPhone("8622489");

        // 创建 ObjectMapper 对象
        ObjectMapper mapper = new ObjectMapper();

        // 将对象转换为 JSON 字符串
        String json = mapper.writeValueAsString(user);
        logger.info("the user json is:" + json);

        // 往es写入数据
        IndexRequest request = new IndexRequest(INDEX_NAME).id(documentId).source(json, XContentType.JSON);

        IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        logger.info("Document indexed with ID: " + indexResponse.getId());

        return ResultVo.success(indexResponse.getId(), "insertDocument success...");
    }

    @PostMapping("/getDocument")
    public ResultVo<User> getDocument(String documentId) throws Exception {
        GetRequest getRequest = new GetRequest(INDEX_NAME, documentId);
        String jsonString = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT).getSourceAsString();
        if (StringUtils.isEmpty(jsonString)) {
            return ResultVo.error("根据id未找到数据.");
        }
        logger.info("jsonString is:" + jsonString);

        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(jsonString, User.class);
        logger.info("user info is:" + user.toString());

        return ResultVo.success(user, "getDocument success...");
    }

    @PostMapping("/updateDocument")
    public ResultVo<String> updateDocument(String documentId, String name) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);

        // 创建 ObjectMapper 对象
        ObjectMapper mapper = new ObjectMapper();

        // 将对象转换为 JSON 字符串
        String json = mapper.writeValueAsString(map);
        logger.info("the update json is:" + json);

        UpdateRequest request = new UpdateRequest(INDEX_NAME, documentId).doc(json, XContentType.JSON);
        UpdateResponse updateResponse = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        logger.info("Document indexed with ID: " + updateResponse.getId());
        return ResultVo.success(updateResponse.getId(), "updateDocument success...");
    }

    @PostMapping("/deleteDocument")
    public ResultVo<String> deleteDocument(String documentId) throws Exception {
        DeleteRequest request = new DeleteRequest(INDEX_NAME, documentId);
        DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        logger.info("Document indexed with ID: " + deleteResponse.getId());
        return ResultVo.success(deleteResponse.getId(), "deleteDocument success...");
    }
}
