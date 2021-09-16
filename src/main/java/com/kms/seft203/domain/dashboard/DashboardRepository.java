package com.kms.seft203.domain.dashboard;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DashboardRepository extends JpaRepository<Dashboard, String> {
    
    List<Dashboard> findAllByUserId(String userId);
    Dashboard findOneByTitle(String title);

}
