package com.lastcourse.crash.config;

import com.lastcourse.crash.model.crashsession.CrashSessionCategory;
import com.lastcourse.crash.model.crashsession.CrashSessionPostRequestBody;
import com.lastcourse.crash.model.sessionspeaker.SessionSpeaker;
import com.lastcourse.crash.model.sessionspeaker.SessionSpeakerPostRequestBody;
import com.lastcourse.crash.model.user.UserSignUpRequestBody;
import com.lastcourse.crash.service.CrashSessionService;
import com.lastcourse.crash.service.SessionSpeakerService;
import com.lastcourse.crash.service.UserService;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.stream.IntStream;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

  private static final Faker faker = new Faker();

  @Autowired
  private UserService userService;

  @Autowired
  private SessionSpeakerService sessionSpeakerService;

  @Autowired
  private CrashSessionService crashSessionService;

  @Bean
  public ApplicationRunner applicationRunner() {
    return new ApplicationRunner() {
      @Override
      public void run(ApplicationArguments args) throws Exception {
        // 유저 및 세션스피커 생성
        createTestUsers();

        createTestSessionSpeakers(10);
      }
    };
  }

  private void createTestUsers() {
    userService.signUp(new UserSignUpRequestBody("jayce", "1234", "Dev jayce","jayce@crash.com"));
    userService.signUp(new UserSignUpRequestBody("jay", "1234", "Dev jay","jay@crash.com"));
    userService.signUp(new UserSignUpRequestBody("rose", "1234", "Dev rose","rose@crash.com"));
    // userService.signUp(new UserSignUpRequestBody(faker.name().fullName(), "1234", faker.name().fullName(),faker.internet().emailAddress())); 하드 코딩을 안하고 이렇게 코드를 작성하면 랜덤으로 사용자 정보가 데이터에 들어감
  }

  private void createTestSessionSpeakers(int numberOfSpeakers) {
    var sessionSpeakers =
        IntStream.range(0, numberOfSpeakers).mapToObj(i -> createTestSessionSpeaker()).toList();

    sessionSpeakers.forEach(
        sessionSpeaker -> {
          int numberOfSessions = new Random().nextInt(4) + 1;
          IntStream.range(0, numberOfSessions)
              .forEach(i -> createTestCrashSession(sessionSpeaker));
        });
  }


  private SessionSpeaker createTestSessionSpeaker() {
    var name = faker.name().fullName();
    var company = faker.company().name();
    var description = faker.shakespeare().romeoAndJulietQuote();


   return
       sessionSpeakerService.createSessionSpeaker(new SessionSpeakerPostRequestBody(company, name, description));
  }

  private void createTestCrashSession(SessionSpeaker sessionSpeaker) {
    var title = faker.book().title();
    var body = faker.shakespeare().asYouLikeItQuote()
        + faker.shakespeare().hamletQuote()
        + faker.shakespeare().kingRichardIIIQuote()
        + faker.shakespeare().romeoAndJulietQuote();
    crashSessionService.createCrashSession(new CrashSessionPostRequestBody(
        title,
        body,
        getRandomCategory(),
        ZonedDateTime.now().plusDays(new Random().nextInt(2) + 1),
        sessionSpeaker.speakerId()
    ));
  }

  private CrashSessionCategory getRandomCategory() {
    var categories = CrashSessionCategory.values();
    int randomIndex = new Random().nextInt(categories.length);

    return categories[randomIndex];
  }
}
