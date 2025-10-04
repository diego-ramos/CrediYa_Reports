package com.crediya.api;

import com.crediya.model.totals.Total;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/reportes",
                    produces = { MediaType.APPLICATION_JSON_VALUE },
                    method = RequestMethod.GET,
                    beanClass = TotalsHandlerV1.class,
                    beanMethod = "getTotals",
                    operation = @Operation(
                            operationId = "getTotals",
                            summary = "List Applications Totals",
                            parameters = {
                                    @Parameter(name = "totalKey", description = "key to get the data", required = false),
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Return total successfully", content = @Content(schema = @Schema(implementation = Total.class))),
                                    @ApiResponse(responseCode = "400", description = "Error de validación"),
                                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(TotalsHandlerV1 totalsHandlerV1) {
        return RouterFunctions
                .route()
                .path("/api/v1", builder -> builder
                        .GET("/reportes", totalsHandlerV1::getTotals))
                .build();
    }
}
