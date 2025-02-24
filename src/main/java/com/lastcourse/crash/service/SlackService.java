package com.lastcourse.crash.service;

import com.lastcourse.crash.model.registration.Registration;
import com.lastcourse.crash.model.slack.SlackNotificationBlock;
import com.lastcourse.crash.model.slack.SlackNotificationMessage;
import com.lastcourse.crash.model.slack.SlackNotificationText;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class SlackService {
  private static final Logger logger = LoggerFactory.getLogger(SlackService.class);
  private static final RestClient restClient = RestClient.create(); // 외부의 API를 요청하기 위해 등록

  public void sendSlackNotification(Registration registration) {
    var linkText = getRegistrationPageLinkText(registration);

    var slackNotificationMessage = new SlackNotificationMessage(
        List.of(
            new SlackNotificationBlock(
                "section",
                new SlackNotificationText(
                    "mrkdwn",
                    linkText
                )
            )
        )
    );


    var response = restClient
        .post()
        .uri("https://hooks.slack.com/services/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")
        .body(slackNotificationMessage) // 리퀘스트 바디
        .retrieve()
        .body(String.class); // 리스폰스 바디
    logger.info(response);
  }

  private String getRegistrationPageLinkText(Registration registration) {
    var baseLink = "https://dev-jayce.github.io/crash/registration.html?registration=";
    var registrationId = registration.registrationId();
    var username = registration.user().username();
    var sessionId = registration.session().sessionId();
    var link = baseLink + registrationId + "," + username + "," + sessionId;

    return ":collision: *CRASH* <" + link + "|Registration Details>";
  }

}
