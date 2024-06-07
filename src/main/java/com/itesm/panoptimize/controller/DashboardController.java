package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.dashboard.*;
import com.itesm.panoptimize.model.Notification;
import com.itesm.panoptimize.service.DashboardService;
import com.itesm.panoptimize.service.CalculateSatisfactionService;
import com.itesm.panoptimize.service.CalculatePerformanceService;

import com.itesm.panoptimize.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.util.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final CalculateSatisfactionService satisfactionService;
    private final DashboardService dashboardService;
    private final CalculatePerformanceService calculatePerformanceService;
    private final UserService userService;

    @Autowired
    public DashboardController(DashboardService dashboardService,
                               CalculateSatisfactionService satisfactionService,
                               CalculatePerformanceService calculatePerformanceService,
                               UserService userService) {
        this.dashboardService = dashboardService;
        this.satisfactionService = satisfactionService;
        this.calculatePerformanceService = calculatePerformanceService;
        this.userService = userService;
    }

    @GetMapping("/customer-satisfaction")
    public ResponseEntity<CustomerSatisfactionDTO> calculateSatisfaction() {
        CustomerSatisfactionDTO result = satisfactionService.getSatisfactionLevels();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get the metrics data", description = "Get the metrics data by time frame, agent and workspace number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Found the data",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MetricsDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Data not found",
                    content = @Content),
    })
    @PostMapping("/combined-metrics")
    public ResponseEntity<CombinedMetricsDTO> getCombinedMetrics(@Valid @RequestBody DashboardDTO dashboardDTO, Principal principal) {
        String firebaseId = principal.getName();
        String instanceId = userService.getInstanceIdFromFirebaseId(firebaseId);
        CombinedMetricsDTO combinedMetrics = dashboardService.getDashboardData(instanceId, dashboardDTO);
        return ResponseEntity.ok(combinedMetrics);
    }

    @GetMapping("/Notifications")
    public ResponseEntity<List<Notification>> getNotifications() {
        List<Notification> notifications = dashboardService.getNotifications().stream().toList();
        if (notifications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/Notifications/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Notification notification = dashboardService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/Notifications")
    public ResponseEntity<Notification> addNotification(@RequestBody Notification notification) {
        Notification newNotification = dashboardService.addNotification(notification);
        return ResponseEntity.ok(newNotification);
    }

    @DeleteMapping("/Notifications/{id}")
    public ResponseEntity<Boolean> deleteNotification(@PathVariable Long id) {
        return ResponseEntity.ok(
                dashboardService.deleteNotification(id)
        );
    }

    @PatchMapping("/Notifications/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable Long id, @RequestBody Notification notification) {
        return ResponseEntity.ok(dashboardService.updateNotification(id, notification));
    }


    @GetMapping("/filters")
    public ResponseEntity<DashboardFiltersDTO> getFilters(@RequestAttribute String instanceId) {
        System.out.println(instanceId);
        DashboardFiltersDTO filters = dashboardService.getFilters(instanceId);
        return ResponseEntity.ok(filters);
    }
}
