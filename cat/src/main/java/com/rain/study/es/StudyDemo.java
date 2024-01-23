package com.rain.study.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Map;

/**
 * elasticsearch curd demo
 *
 * @author zhangzhenyan
 * 2024-01-23
 **/
public class StudyDemo {

    private static final String INDEX_NAME = "study_index_name";

    public static void main(String[] args) {
        try (RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")))) {

            // 插入文档
            IndexResponse indexResponse = insertDocument(client, "1", "{\"field1\":\"value1\",\"field2\":\"value2\"}");
            System.out.println("Document indexed with ID: " + indexResponse.getId());

            // 查询文档
            System.out.println("before update");
            getDocument(client, "1");

            // 更新文档
            UpdateResponse updateResponse = updateDocument(client, "1", "{\"field1\":\"updated_value1\"}");
            System.out.println("Document updated with result: " + updateResponse.getResult());

            // 查询更新后的文档
            System.out.println("after update");
            getDocument(client, "1");

            // 删除文档
            DeleteResponse deleteResponse = deleteDocument(client, "1");
            System.out.println("Document deleted with result: " + deleteResponse.getResult());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static IndexResponse insertDocument(RestHighLevelClient client, String documentId, String document) throws IOException {
        IndexRequest request = new IndexRequest(INDEX_NAME).id(documentId).source(document, XContentType.JSON);

        return client.index(request, RequestOptions.DEFAULT);
    }

    private static void getDocument(RestHighLevelClient client, String documentId) throws IOException {
        GetRequest getRequest = new GetRequest(INDEX_NAME, documentId);
        Map<String, Object> source = client.get(getRequest, RequestOptions.DEFAULT).getSource();
        System.out.println(source);
    }

    private static UpdateResponse updateDocument(RestHighLevelClient client, String documentId, String updatedDocument) throws IOException {
        UpdateRequest request = new UpdateRequest(INDEX_NAME, documentId).doc(updatedDocument, XContentType.JSON);

        return client.update(request, RequestOptions.DEFAULT);
    }

    private static DeleteResponse deleteDocument(RestHighLevelClient client, String documentId) throws IOException {
        DeleteRequest request = new DeleteRequest(INDEX_NAME, documentId);

        return client.delete(request, RequestOptions.DEFAULT);
    }
}
