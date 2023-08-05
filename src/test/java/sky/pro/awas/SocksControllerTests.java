package sky.pro.awas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sky.pro.awas.Function.EQUAL;

@WebMvcTest
public class SocksControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    SocksRepository socksRepository;
    @SpyBean
    SocksService socksService;

    private Function operation;

    @Test
    public void socksTest() throws Exception {
        final Long socksId = 1L;
        final String color = "red";
        final int cottonPart = 70;
        final int quantity = 20;
        operation = EQUAL;


        JSONObject socksObject = new JSONObject();
        socksObject.put("socksId", socksId);
        socksObject.put("color", color);
        socksObject.put("cottonPart", cottonPart);
        socksObject.put("quantity", quantity);

        Socks socks = new Socks(socksId, color, cottonPart, quantity);

        when(socksRepository.save(any(Socks.class))).thenReturn(socks);
        when(socksRepository.findBySocksId(any(Long.class))).thenReturn(socks);
        when(socksRepository.findAllByColorAndCottonPartAfter(any(String.class), any(Integer.class))).thenReturn(List.of(socks));
        when(socksRepository.findAllByColorAndCottonPartBefore(any(String.class), any(Integer.class))).thenReturn(List.of(socks));
        when(socksRepository.findAllByColorAndCottonPartEquals(any(String.class), any(Integer.class))).thenReturn(socks);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/socks/income/")
                        .content(socksObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.socksId").value(socksId))
                .andExpect(jsonPath("$.color").value(color))
                .andExpect(jsonPath("$.cottonPart").value(cottonPart))
                .andExpect(jsonPath("$.quantity").value(quantity));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/socks/outcome/" + socksId)
                        .content(socksObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.socksId").value(socksId))
                .andExpect(jsonPath("$.color").value(color))
                .andExpect(jsonPath("$.cottonPart").value(cottonPart))
                .andExpect(jsonPath("$.quantity").value(quantity));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/socks?color=" + color + "&operation=" + operation + "&cottonPart=" + cottonPart + "'")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }
}
