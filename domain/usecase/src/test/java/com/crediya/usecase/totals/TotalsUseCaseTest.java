package com.crediya.usecase.totals;

import com.crediya.model.exception.BusinessException;
import com.crediya.model.exception.message.BusinessErrorMessage;
import com.crediya.model.totals.Total;
import com.crediya.model.totals.gateways.TotalsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TotalsUseCaseTest {

    private TotalsRepository totalsRepository;
    private TotalsUseCase totalsUseCase;

    @BeforeEach
    void setUp() {
        totalsRepository = mock(TotalsRepository.class);
        totalsUseCase = new TotalsUseCase(totalsRepository);
    }

    @Test
    void saveTotals_shouldSetUpdateDateAndCallRepository() {
        // Arrange
        Total total = new Total();
        total.setTotalKey("approved_applications");
        total.setTotalValue("5");

        when(totalsRepository.saveTotals(any(Total.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // Act
        Mono<Total> result = totalsUseCase.saveTotals(total);

        // Assert
        StepVerifier.create(result)
                .assertNext(savedTotal -> {
                    assert savedTotal.getTotalKey().equals("approved_applications");
                    assert savedTotal.getTotalValue() == "5";
                    assert savedTotal.getUpdateDate() != null; // updateDate is set
                })
                .verifyComplete();

        verify(totalsRepository).saveTotals(any(Total.class));
    }

    @Test
    void getTotalByKey_shouldReturnTotal_whenExists() {
        // Arrange
        String key = "approved_applications";
        Total total = new Total();
        total.setTotalKey(key);
        total.setTotalValue("10");

        when(totalsRepository.getTotalByKey(key)).thenReturn(Mono.just(total));

        // Act
        Mono<Total> result = totalsUseCase.getTotalByKey(key);

        // Assert
        StepVerifier.create(result)
                .assertNext(t -> {
                    assert t.getTotalKey().equals(key);
                    assert t.getTotalValue() == "10";
                })
                .verifyComplete();

        verify(totalsRepository).getTotalByKey(key);
    }

    @Test
    void getTotalByKey_shouldReturnError_whenNotExists() {
        // Arrange
        String key = "non_existing_key";
        when(totalsRepository.getTotalByKey(key)).thenReturn(Mono.empty());

        // Act
        Mono<Total> result = totalsUseCase.getTotalByKey(key);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                ((BusinessException) throwable).getBusinessErrorMessage() == BusinessErrorMessage.TOTAL_KEY_NOT_EXISTS
                )
                .verify();

        verify(totalsRepository).getTotalByKey(key);
    }
}
