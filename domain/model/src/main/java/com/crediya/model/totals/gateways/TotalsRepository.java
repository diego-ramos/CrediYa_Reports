package com.crediya.model.totals.gateways;

import com.crediya.model.totals.Totals;
import reactor.core.publisher.Mono;

public interface TotalsRepository {
    Mono<Totals> saveTotals(Totals totals);
}
