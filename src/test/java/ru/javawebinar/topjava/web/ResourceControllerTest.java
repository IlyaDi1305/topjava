package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ResourceControllerTest extends AbstractControllerTest {
    @Test
    void getStyleCss() throws Exception {
        perform(get("/resources/css/style.css"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/css;charset=UTF-8")));
    }
}