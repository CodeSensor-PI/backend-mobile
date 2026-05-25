package com.psi.rizerio.backend.dashboard.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KpiResponse {

    private FinancialKpi financial;
    private PresenceKpi presence;
    private EngagementKpi engagement;
    private WorkloadKpi workload;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinancialKpi {
        private double paid;
        private double pending;
        private double projected;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PresenceKpi {
        private int total;
        private int noShow;
        private double noShowRate;
        private Map<String, Integer> noShowByDayOfWeek;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EngagementKpi {
        private int active;
        private int inactive;
        private int atRisk;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkloadKpi {
        private double currentHours;
        private double maxHours;
    }
}
