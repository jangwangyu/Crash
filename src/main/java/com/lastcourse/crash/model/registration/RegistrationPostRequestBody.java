package com.lastcourse.crash.model.registration;

import com.lastcourse.crash.model.crashsession.CrashSessionCategory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public record RegistrationPostRequestBody(
    @NotNull Long sessionId
) {

}
