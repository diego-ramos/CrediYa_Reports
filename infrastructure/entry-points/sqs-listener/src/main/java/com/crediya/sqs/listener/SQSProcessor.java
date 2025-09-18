package com.crediya.sqs.listener;

import com.crediya.model.totals.Totals;
import com.crediya.usecase.totals.TotalsUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final TotalsUseCase totalsUseCase;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> apply(Message message) {
       log.info("SQS Message received: {}", message.body());
        try {
            return totalsUseCase.saveTotals(objectMapper.readValue(message.body(), Totals.class)).then();
        } catch (JsonProcessingException e) {
            log.error("Error parsing SQS message", e);
            throw new RuntimeException(e);
        }
    }
}
