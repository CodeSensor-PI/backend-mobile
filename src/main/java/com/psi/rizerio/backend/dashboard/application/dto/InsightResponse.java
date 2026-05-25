package com.psi.rizerio.backend.dashboard.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsightResponse {
    private String insightRetencao;
    private String insightFinanceiro;
    private String insightPosicionamento;
}
