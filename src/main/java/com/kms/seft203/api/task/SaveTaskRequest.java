package com.kms.seft203.api.task;

import com.kms.seft203.domain.task.Task;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SaveTaskRequest extends Task {
}
