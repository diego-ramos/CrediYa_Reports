package com.crediya.model.totals.gateways;

import com.crediya.model.totals.Total;
import reactor.core.publisher.Mono;

public interface TotalsRepository {
    Mono<Total> saveTotals(Total total);
    Mono<Total> getTotalByKey(String totalKey);
}
