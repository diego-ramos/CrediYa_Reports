package com.crediya.model.totals;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Total {
    private String totalKey;
    private String totalValue;
    private LocalDateTime updateDate;
}
