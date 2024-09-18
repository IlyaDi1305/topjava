package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.user.AdminRestController;
import ru.javawebinar.topjava.web.user.ProfileRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // Java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            ProfileRestController profileUserController = appCtx.getBean(ProfileRestController.class);
            User createdUser = adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            System.out.println("Созданный пользователь: " + createdUser);
            User updatedUser = new User(createdUser.getId(), "updatedUserName", "updatedEmail@mail.ru", "newPassword", Role.ADMIN);
            adminUserController.update(updatedUser, createdUser.getId());
            User retrievedUser = profileUserController.get(createdUser.getId());
            System.out.println("Обновленный пользователь: " + retrievedUser);
            if (retrievedUser.getName().equals("updatedUserName") &&
                    retrievedUser.getEmail().equals("updatedEmail@mail.ru")) {
                System.out.println("Обновление прошло успешно!");
            } else {
                System.out.println("Ошибка при обновлении.");
            }
            User createdUser2 = profileUserController.create(
                    new User(null, "userName2", "email2@mail.ru", "password", Role.USER));
            System.out.println("Созданный пользователь 2: " + createdUser2);
        }
    }
}
