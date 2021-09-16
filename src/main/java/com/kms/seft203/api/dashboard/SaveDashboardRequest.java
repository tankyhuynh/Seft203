package com.kms.seft203.api.dashboard;

import com.kms.seft203.domain.dashboard.Dashboard;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SaveDashboardRequest extends Dashboard {
}
