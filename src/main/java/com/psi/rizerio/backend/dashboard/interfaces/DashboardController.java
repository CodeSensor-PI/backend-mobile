package com.psi.rizerio.backend.dashboard.interfaces;

import com.psi.rizerio.backend.auth.domain.User;
import com.psi.rizerio.backend.dashboard.application.DashboardService;
import com.psi.rizerio.backend.dashboard.application.dto.KpiResponse;
import com.psi.rizerio.backend.dashboard.application.dto.TrendResponse;
import com.psi.rizerio.backend.dashboard.application.dto.InsightResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/kpis")
    public ResponseEntity<KpiResponse> getKpis(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) UUID psychologistId) {
        UUID id = resolvePsychologistId(user, psychologistId);
        return ResponseEntity.ok(dashboardService.getKpis(id));
    }

    @GetMapping("/trends")
    public ResponseEntity<List<TrendResponse>> getTrends(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) UUID psychologistId) {
        UUID id = resolvePsychologistId(user, psychologistId);
        return ResponseEntity.ok(dashboardService.getTrends(id));
    }

    @GetMapping("/insights")
    public ResponseEntity<InsightResponse> getInsights(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) UUID psychologistId) {
        UUID id = resolvePsychologistId(user, psychologistId);
        return ResponseEntity.ok(dashboardService.getInsights(id));
    }

    private UUID resolvePsychologistId(User user, UUID psychologistId) {
        if (psychologistId != null) {
            return psychologistId;
        }
        if (user != null) {
            return user.getId();
        }
        return dashboardService.getFirstPsychologistId();
    }
}
