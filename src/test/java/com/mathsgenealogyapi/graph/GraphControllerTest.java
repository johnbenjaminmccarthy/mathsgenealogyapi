package com.mathsgenealogyapi.graph;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GraphController.class)
class GraphControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GraphService graphService;

    @Test
    public void make_request_for_graph_with_negative_number_of_max_generations_up_should_receive_400_error() throws Exception {
        mvc.perform(get("/api/graph?id={id}&maxGenerationsUp={maxGenerationsUp}&maxGenerationsDown={maxGenerationsDown}", 217413, -10, 5))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void make_request_for_graph_with_negative_number_of_max_generations_down_should_receive_400_error() throws Exception {
        mvc.perform(get("/api/graph?id={id}&maxGenerationsUp={maxGenerationsUp}&maxGenerationsDown={maxGenerationsDown}", 217413, 5, -10))
                .andExpect(status().isBadRequest());
    }

}