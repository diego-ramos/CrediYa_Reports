package com.crediya.dynamodb;

import com.crediya.model.totals.Total;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DynamoDBTemplateAdapterTest {

    private DynamoDBTemplateAdapter spyAdapter;

    @BeforeEach
    void setUp() {
        DynamoDbEnhancedAsyncClient mockClient = mock(DynamoDbEnhancedAsyncClient.class);
        ObjectMapper mockMapper = mock(ObjectMapper.class);

        // Spy so we can override just save/getById
        spyAdapter = Mockito.spy(new DynamoDBTemplateAdapter(mockClient, mockMapper));
    }

    @Test
    void saveTotals_shouldDelegateToSave() {
        Total input = new Total("key1", "100", null);
        Total saved = new Total("key1", "100", null);

        // Stub the spy’s save (prevent hitting real DynamoDB)
        doReturn(Mono.just(saved)).when(spyAdapter).save(any(Total.class));

        StepVerifier.create(spyAdapter.saveTotals(input))
                .expectNext(saved)
                .verifyComplete();

        verify(spyAdapter, times(1)).save(input);
    }

    @Test
    void getTotalByKey_shouldDelegateToGetById() {
        String key = "approved_applications";
        Total total = new Total(key, "200", null);

        // Stub the spy’s getById
        doReturn(Mono.just(total)).when(spyAdapter).getById(key);

        StepVerifier.create(spyAdapter.getTotalByKey(key))
                .expectNext(total)
                .verifyComplete();

        verify(spyAdapter, times(1)).getById(key);
    }
}
