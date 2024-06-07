package com.itesm.panoptimize;

import com.itesm.panoptimize.repository.AgentPerformanceRepository;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
public class AgentPerformanceControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private String firebaseToken;

    public AgentPerformanceControllerTests() throws Exception {
    }

    @BeforeEach
    public void setUp() throws IOException {
        firebaseToken = getFirebaseToken();
    }

    private String getFirebaseToken() throws IOException {
        String apiKey = "AIzaSyA2efAQdi2Vgtzl7aI080kouPzIiC8C2MA";
        String username = "test@example.com";
        String password = "password123";

        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + apiKey;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.addHeader("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            json.put("email", username);
            json.put("password", password);
            json.put("returnSecureToken", true);

            StringEntity entity = new StringEntity(json.toString());
            request.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                JSONObject responseJson = new JSONObject(responseBody);
                return responseJson.getString("idToken");
            }
        } catch (JSONException e) {
            throw new IOException("Error parsing JSON response", e);
        }
    }

    @Autowired
    private AgentPerformanceRepository agentPerformanceRepository;

    @Test
    public void testGetAgentMetricsToday()throws Exception{
        mockMVC.
    }

}
