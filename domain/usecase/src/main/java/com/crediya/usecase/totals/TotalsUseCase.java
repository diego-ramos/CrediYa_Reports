package com.crediya.usecase.totals;

import com.crediya.model.totals.Totals;
import com.crediya.model.totals.gateways.TotalsRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TotalsUseCase {
    private final TotalsRepository totalsRepository;

    public Mono<Totals> saveTotals(Totals totals) {
        return totalsRepository.saveTotals(totals);
    }
}
