package com.example.todo.config.todo;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TodoPolicyProperties.class)
public class TodoConfig {
}