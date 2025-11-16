package com.example.todo.config.todo;

import com.example.todo.domain.member.enumerate.AdminTodoPolicy;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "todo")
public class TodoPolicyProperties {

    private AdminTodoPolicy adminPolicy = AdminTodoPolicy.MANAGE_OWN_ONLY;

    public AdminTodoPolicy getAdminPolicy() {
        return adminPolicy;
    }

    public void setAdminPolicy(AdminTodoPolicy adminPolicy) {
        this.adminPolicy = adminPolicy;
    }
}