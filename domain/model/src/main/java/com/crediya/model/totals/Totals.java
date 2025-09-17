package com.crediya.model.totals;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Totals {
    private String totalKey;
    private Long totalValue;
    private LocalDateTime updateDate;
}
