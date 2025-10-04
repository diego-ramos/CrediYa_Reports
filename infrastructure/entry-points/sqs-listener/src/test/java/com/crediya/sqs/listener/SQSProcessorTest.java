package com.crediya.sqs.listener;

import com.crediya.model.totals.Total;
import com.crediya.usecase.totals.TotalsUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.model.Message;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SQSProcessorTest {

    private TotalsUseCase totalsUseCase;
    private ObjectMapper objectMapper;
    private SQSProcessor processor;

    @BeforeEach
    void setUp() {
        totalsUseCase = mock(TotalsUseCase.class);
        objectMapper = mock(ObjectMapper.class);
        processor = new SQSProcessor(totalsUseCase, objectMapper);
    }

    @Test
    void apply_shouldSaveTotals_whenValidMessage() throws Exception {
        // Arrange
        Message message = Message.builder()
                .body("{\"totalList\":[{\"totalKey\":\"APPROVED\",\"totalValue\":10}]}")
                .build();

        Total total = new Total();
        total.setTotalKey("APPROVED");
        total.setTotalValue("10");

        JsonNode totalListNode = mock(JsonNode.class);

        when(objectMapper.readTree(anyString())).thenReturn(mock(JsonNode.class));
        when(objectMapper.readTree(anyString()).get("totalList")).thenReturn(totalListNode);
        when(objectMapper.readValue(eq(totalListNode.traverse()), any(TypeReference.class)))
                .thenReturn(List.of(total));

        when(totalsUseCase.saveTotals(total)).thenReturn(Mono.just(total));

        // Act
        Mono<Void> result = processor.apply(message);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(totalsUseCase).saveTotals(total);
    }

    @Test
    void apply_shouldReturnError_whenJsonProcessingException() throws Exception {
        // Arrange
        Message message = Message.builder().body("invalid-json").build();
        when(objectMapper.readTree(anyString())).thenThrow(JsonProcessingException.class);

        // Act
        Mono<Void> result = processor.apply(message);

        // Assert
        StepVerifier.create(result)
                .expectError(JsonProcessingException.class)
                .verify();

        verifyNoInteractions(totalsUseCase);
    }

//    @Test
//    void apply_shouldReturnError_whenIOException() throws Exception {
//        // Arrange
//        Message message = Message.builder().body("{\"totalList\":[] }").build();
//
//        // Simulate IOException from readTree (allowed)
//        when(objectMapper.readTree(anyString())).thenThrow(new IOException("I/O error"));
//
//        // Act
//        Mono<Void> result = processor.apply(message);
//
//        // Assert
//        StepVerifier.create(result)
//                .expectErrorMatches(throwable ->
//                        throwable instanceof IOException &&
//                                throwable.getMessage().equals("I/O error")
//                )
//                .verify();
//
//        verifyNoInteractions(totalsUseCase);
//    }


}
