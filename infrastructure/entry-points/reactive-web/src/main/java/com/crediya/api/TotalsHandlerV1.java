package com.crediya.api;

import com.crediya.model.exception.BusinessException;
import com.crediya.model.exception.TechnicalException;
import com.crediya.model.exception.message.TechnicalErrorMessage;
import com.crediya.usecase.totals.TotalsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TotalsHandlerV1 {
    private final TotalsUseCase totalsUseCase;

    public Mono<ServerResponse> getTotals(ServerRequest serverRequest) {
        return Mono.justOrEmpty(serverRequest.queryParam("totalKey"))
                .switchIfEmpty(Mono.error(new TechnicalException(TechnicalErrorMessage.NO_KEY_ERROR)))
                .flatMap(totalsUseCase::getTotalByKey)
                .flatMap(total -> ServerResponse.ok().bodyValue(total))
                .onErrorResume(BusinessException.class,
                        e -> ServerResponse.badRequest().bodyValue(e.getBusinessErrorMessage().toString()));
    }
}
