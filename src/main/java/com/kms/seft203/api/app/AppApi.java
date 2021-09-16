package com.kms.seft203.api.app;

import com.kms.seft203.domain.app.AppVersion;
import com.kms.seft203.domain.app.AppVersionRepository;
import static com.kms.seft203.utils.UrlConstraint.*;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(APP_URL)
@RequiredArgsConstructor
public class AppApi {

    private final AppVersionRepository appVersionRepo;

    @GetMapping(APP_VERSION_URL)
    public AppVersion getCurrentVersion() {
        List<AppVersion> versions = appVersionRepo.findAll();
        return versions.get(versions.size() - 1);
    }
}
