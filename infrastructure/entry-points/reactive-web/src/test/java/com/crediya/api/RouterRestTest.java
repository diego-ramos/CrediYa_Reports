package com.crediya.api;

import com.crediya.model.totals.Total;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.*;

class RouterRestTest {

    private TotalsHandlerV1 totalsHandler;
    private RouterRest routerRest;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        totalsHandler = mock(TotalsHandlerV1.class);
        routerRest = new RouterRest();

        RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(totalsHandler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void getTotals_shouldReturnJsonList() {
        // Arrange: mock handler to return some totals
        Total total = new Total();
        total.setTotalKey("APPROVED");
        total.setTotalValue("10");

        when(totalsHandler.getTotals(any()))
                .thenReturn(ServerResponse.ok().body(Mono.just(List.of(total)), List.class));

        // Act & Assert
        webTestClient.get()
                .uri("/api/v1/reportes?totalKey=applications_approved")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json")
                .expectBodyList(Total.class)
                .hasSize(1)
                .consumeWith(response -> {
                    List<Total> totals = response.getResponseBody();
                    assert totals != null;
                    assert totals.get(0).getTotalKey().equals("APPROVED");
                    assert totals.get(0).getTotalValue().equals("10");
                });

        // Verify handler called
        verify(totalsHandler).getTotals(any());
    }
}
