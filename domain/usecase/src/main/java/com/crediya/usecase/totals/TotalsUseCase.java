package com.crediya.usecase.totals;

import com.crediya.model.exception.BusinessException;
import com.crediya.model.exception.message.BusinessErrorMessage;
import com.crediya.model.totals.Total;
import com.crediya.model.totals.gateways.TotalsRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TotalsUseCase {
    private static final String APPROVED_APPLICATION_KEY = "approved_applications";

    private final TotalsRepository totalsRepository;

    public Mono<Total> saveTotals(Total total) {
        total.setUpdateDate(LocalDateTime.now());
        return totalsRepository.saveTotals(total);
    }

    public Mono<Total> getTotalByKey(String applicationKey) {
        return totalsRepository.getTotalByKey(applicationKey)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessErrorMessage.TOTAL_KEY_NOT_EXISTS)));
    }
}
