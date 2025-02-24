package com.lastcourse.crash.model.slack;

public record SlackNotificationBlock(
  String type,
  SlackNotificationText text
){}

