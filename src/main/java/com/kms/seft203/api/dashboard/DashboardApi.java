package com.kms.seft203.api.dashboard;

import static com.kms.seft203.utils.UrlConstraint.DASHBOARDS_DELETE_URL;
import static com.kms.seft203.utils.UrlConstraint.DASHBOARDS_PUT_URL;
import static com.kms.seft203.utils.UrlConstraint.DASHBOARDS_URL;

import java.util.List;
import java.util.Optional;

import com.kms.seft203.domain.auth.UserRepository;
import com.kms.seft203.domain.dashboard.Dashboard;
import com.kms.seft203.domain.dashboard.DashboardRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(DASHBOARDS_URL)
@RequiredArgsConstructor
public class DashboardApi {

    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Dashboard>> getAll() {
        String currentUserId = getCurrentUserId();

        return ResponseEntity.ok(dashboardRepository.findAllByUserId(currentUserId));
    }

    @PostMapping
    public ResponseEntity<Dashboard> save(@RequestBody SaveDashboardRequest request) {
        String currentUserId = getCurrentUserId();

        Dashboard dashboard = new Dashboard();
        dashboard.setUserId(currentUserId);
        dashboard.setTitle(request.getTitle());
        dashboard.setLayoutType(request.getLayoutType());

        return ResponseEntity.ok(dashboardRepository.save(dashboard));
    }

    @PutMapping(DASHBOARDS_PUT_URL)
    public ResponseEntity<Dashboard> update(@PathVariable String id, @RequestBody SaveDashboardRequest request) {
        String currentUserId = getCurrentUserId();
        Optional<Dashboard> dashboardOptional = dashboardRepository.findById(id);
        if (dashboardOptional.isPresent()) {
            Dashboard newDashboard = convert(request);
            newDashboard.setId(id);
            newDashboard.setUserId(currentUserId);

            return ResponseEntity.ok(dashboardRepository.save(newDashboard));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping(DASHBOARDS_DELETE_URL)
    public ResponseEntity<Boolean> delete(@PathVariable String id) {
        Optional<Dashboard> dashboardOptional = dashboardRepository.findById(id);
        if (dashboardOptional.isPresent()) {
            dashboardRepository.delete(dashboardOptional.get());
            return ResponseEntity.ok(true);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    public Dashboard convert(SaveDashboardRequest request) {
        Dashboard dashboard = new Dashboard();
        dashboard.setTitle(request.getTitle());
        dashboard.setUserId(request.getUserId());
        dashboard.setLayoutType(request.getLayoutType());

        return dashboard;
    }

    public String getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = ((UserDetails) principal).getUsername();
        String userId = userRepository.findByUsername(username).get().getId();

        return userId;
    }

}
