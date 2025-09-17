package com.crediya.api;

import com.crediya.model.totals.gateways.TotalsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TotalsHandlerV1 {
    private final TotalsRepository totalsRepository;

    public Mono<ServerResponse> getTotals(ServerRequest serverRequest) {
        return Mono.justOrEmpty(serverRequest.queryParam("totalKey"))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Missing query param totalKey")))
                .flatMap(totalsRepository::getTotalByKey)
                .flatMap(total -> ServerResponse.ok().bodyValue(total))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
