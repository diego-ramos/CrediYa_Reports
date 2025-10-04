package com.crediya.dynamodb.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TemplateAdapterOperationsTest {

    static class DummyEntity {
        String id;
        String name;
    }

    @DynamoDbBean
    public static class DummyData {
        private String id;
        private String name;

        @DynamoDbPartitionKey
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    static class DummyTemplateAdapter extends TemplateAdapterOperations<DummyEntity, String, DummyData> {
        DummyTemplateAdapter(DynamoDbEnhancedAsyncClient client, ObjectMapper mapper) {
            super(client, mapper, data -> {
                DummyEntity entity = new DummyEntity();
                entity.id = data.id;
                entity.name = data.name;
                return entity;
            }, "dummyTable");
        }
    }

    private DynamoDbEnhancedAsyncClient client;
    private DynamoDbAsyncTable<DummyData> table;
    private ObjectMapper mapper;
    private DummyTemplateAdapter adapter;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        client = mock(DynamoDbEnhancedAsyncClient.class);
        table = mock(DynamoDbAsyncTable.class);
        mapper = mock(ObjectMapper.class);

        when(client.<DummyData>table(eq("dummyTable"), any())).thenReturn(table);


        adapter = new DummyTemplateAdapter(client, mapper);
    }

    @Test
    void getById_shouldCallGetItemAndMapResult() {
        // Arrange
        DummyData data = new DummyData();
        data.id = "123";
        data.name = "test";

        when(table.getItem(ArgumentMatchers.<Key>any()))
                .thenReturn(CompletableFuture.completedFuture(data));

        when(mapper.map(any(), eq(DummyData.class))).thenReturn(data);

        // Act
        Mono<DummyEntity> result = adapter.getById("123");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(entity -> entity != null && "test".equals(entity.name))
                .verifyComplete();
    }

    @Test
    void save_shouldMapAndCallPutItem() {
        DummyEntity entity = new DummyEntity();
        DummyData data = new DummyData();

        when(mapper.map(entity, DummyData.class)).thenReturn(data);
        when(table.putItem(data)).thenReturn(CompletableFuture.completedFuture(null));

        Mono<DummyEntity> result = adapter.save(entity);

        StepVerifier.create(result)
                .expectNextMatches(e -> e == entity) // use matches to check object identity
                .verifyComplete();

        verify(table).putItem(data);
    }

    @Test
    void delete_shouldCallDeleteItemAndMapResult() {
        DummyEntity entity = new DummyEntity();
        DummyData data = new DummyData();

        when(mapper.map(entity, DummyData.class)).thenReturn(data);
        when(table.deleteItem(data)).thenReturn(CompletableFuture.completedFuture(data));

        Mono<DummyEntity> result = adapter.delete(entity);

        StepVerifier.create(result)
                .expectNextMatches(e -> e != null)
                .verifyComplete();

        verify(table).deleteItem(data);
    }

}
