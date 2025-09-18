package com.crediya.usecase.totals;

import com.crediya.model.totals.Totals;
import com.crediya.model.totals.gateways.TotalsRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TotalsUseCase {
    private static final String APPROVED_APPLICATION_KEY = "approved_applications";

    private final TotalsRepository totalsRepository;

    public Mono<Totals> saveTotals(Totals totals) {
        if (APPROVED_APPLICATION_KEY.equals(totals.getTotalKey())) {
            return totalsRepository.getTotalByKey(APPROVED_APPLICATION_KEY)
                    .flatMap(totalResult -> {
                        totalResult.setTotalValue(totals.getTotalValue());
                        totalResult.setUpdateDate(LocalDateTime.now());
                        return totalsRepository.saveTotals(totalResult);
                    });
        }
        return Mono.empty();
    }
}
