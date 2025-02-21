package com.lastcourse.crash.model.crashsession;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public record CrashSessionPatchRequestBody(
     String title,
     String body,
     CrashSessionCategory category,
     ZonedDateTime dateTime,
     Long speakerId
    // speakerId 만 받을 수 있는 이유는 CrashSession을 발표하는 SessionSpeaker 정보와
    // CrashSession정보는 ADMIN권한을 가진 유저만 부여받은 유저만 API를 호출하고 데이터를 생성할 수 있기 때문에
    // CrashSesison 정보를 생성하기 이전에 SessionSpeaker 발표자에 대한 정보를 먼저 다 생성한 이후에
    // 이 CrashSession을 생성한다고 상정하고 개발
) {

}
