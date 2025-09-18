package com.crediya.sqs.listener;

import com.crediya.model.totals.Total;
import com.crediya.usecase.totals.TotalsUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.io.IOException;
import java.util.List;
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
            // Parse only once
            JsonNode totalListNode = objectMapper.readTree(message.body()).get("totalList");

            List<Total> totals = objectMapper.readValue(
                    totalListNode.traverse(),
                    new TypeReference<List<Total>>() {}
            );

            return Flux.fromIterable(totals)
                    .flatMap(totalsUseCase::saveTotals) // or concatMap if strict order
                    .then(); // convert Flux<Total> → Mono<Void>
        } catch (JsonProcessingException e) {
            log.error("Error parsing SQS message", e);
            return Mono.error(e);
        } catch (IOException e) {
            log.error("I/O error parsing SQS message", e);
            return Mono.error(e);
        }
    }

}
