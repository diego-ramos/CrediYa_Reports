package com.crediya.api;

import com.crediya.model.exception.BusinessException;
import com.crediya.model.exception.TechnicalException;
import com.crediya.model.exception.message.BusinessErrorMessage;
import com.crediya.model.exception.message.TechnicalErrorMessage;
import com.crediya.model.totals.Total;
import com.crediya.usecase.totals.TotalsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;

import static org.mockito.Mockito.*;

class TotalsHandlerV1Test {

    private TotalsUseCase totalsUseCase;
    private TotalsHandlerV1 handler;
    private ServerRequest request;

    @BeforeEach
    void setUp() {
        totalsUseCase = mock(TotalsUseCase.class);
        handler = new TotalsHandlerV1(totalsUseCase);
        request = mock(ServerRequest.class);
    }

    @Test
    void getTotals_shouldReturnTotal_whenKeyProvided() {
        // Arrange
        String key = "APPROVED";
        Total total = new Total();
        total.setTotalKey(key);
        total.setTotalValue("10");

        when(request.queryParam("totalKey")).thenReturn(Optional.of(key));
        when(totalsUseCase.getTotalByKey(key)).thenReturn(Mono.just(total));

        // Act
        Mono<ServerResponse> responseMono = handler.getTotals(request);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    // Check response status code
                    assert response != null;
                    assert response.statusCode().is2xxSuccessful();
                })
                .verifyComplete();

        verify(totalsUseCase).getTotalByKey(key);
    }

    @Test
    void getTotals_shouldReturnBadRequest_onBusinessException() {
        // Arrange
        String key = "APPROVED";
        BusinessException businessException = new BusinessException(BusinessErrorMessage.TOTAL_KEY_NOT_EXISTS);

        when(request.queryParam("totalKey")).thenReturn(Optional.of(key));
        when(totalsUseCase.getTotalByKey(key)).thenReturn(Mono.error(businessException));

        // Act
        Mono<ServerResponse> responseMono = handler.getTotals(request);

        // Assert
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assert response.statusCode().is4xxClientError();
                })
                .verifyComplete();

        verify(totalsUseCase).getTotalByKey(key);
    }

    @Test
    void getTotals_shouldReturnError_whenNoKeyProvided() {
        // Arrange
        when(request.queryParam("totalKey")).thenReturn(Optional.empty());

        // Act
        Mono<ServerResponse> responseMono = handler.getTotals(request);

        // Assert
        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof TechnicalException &&
                                ((TechnicalException) throwable).getTechnicalErrorMessage() == TechnicalErrorMessage.NO_KEY_ERROR
                )
                .verify();

        verifyNoInteractions(totalsUseCase);
    }
}
