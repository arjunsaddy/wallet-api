package wallet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class WalletControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void createWalletShouldReturn200() throws Exception {

                mockMvc.perform(
                                post("/wallet/create"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.balance").value(0.0));
        }

        @Test
        void walletNotFoundShouldReturn400() throws Exception {

                mockMvc.perform(get("/wallet/999999"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message")
                                                .value("Wallet not found"))
                                .andExpect(jsonPath("$.status").value(400));
        }

        @Test
        void depositShouldIncreaseBalance() throws Exception {

                String response = mockMvc.perform(post("/wallet/create"))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                JsonNode json = objectMapper.readTree(response);

                Long walletId = json.get("id").asLong();

                mockMvc.perform(
                                post("/wallet/" + walletId + "/deposit")
                                                .contentType("application/json")
                                                .content("""
                                                                {
                                                                  "amount": 100
                                                                }
                                                                """))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.balance").value(100.0));
        }

        @Test
        void withdrawShouldDecreaseBalance() throws Exception {

                // Create wallet
                String response = mockMvc.perform(post("/wallet/create"))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                JsonNode json = objectMapper.readTree(response);
                Long walletId = json.get("id").asLong();

                // Deposit $100
                mockMvc.perform(
                                post("/wallet/" + walletId + "/deposit")
                                                .contentType("application/json")
                                                .content("""
                                                                {
                                                                  "amount": 100
                                                                }
                                                                """))
                                .andExpect(status().isOk());

                // Withdraw $25
                mockMvc.perform(
                                post("/wallet/" + walletId + "/withdraw")
                                                .contentType("application/json")
                                                .content("""
                                                                {
                                                                  "amount": 25
                                                                }
                                                                """))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.balance").value(75.0));
        }

        @Test
        void withdrawMoreThanBalanceShouldReturn400() throws Exception {

                String response = mockMvc.perform(post("/wallet/create"))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                JsonNode json = objectMapper.readTree(response);
                Long walletId = json.get("id").asLong();

                mockMvc.perform(
                                post("/wallet/" + walletId + "/deposit")
                                                .contentType("application/json")
                                                .content("""
                                                                {
                                                                  "amount": 100
                                                                }
                                                                """))
                                .andExpect(status().isOk());

                mockMvc.perform(
                                post("/wallet/" + walletId + "/withdraw")
                                                .contentType("application/json")
                                                .content("""
                                                                {
                                                                  "amount": 200
                                                                }
                                                                """))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("Insufficient funds"))
                                .andExpect(jsonPath("$.status").value(400));
        }

        @Test
        void negativeDepositShouldReturn400() throws Exception {

                // Create wallet
                String response = mockMvc.perform(post("/wallet/create"))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                // Get generated wallet ID
                JsonNode json = objectMapper.readTree(response);
                Long walletId = json.get("id").asLong();

                // Try depositing a negative amount
                mockMvc.perform(
                                post("/wallet/" + walletId + "/deposit")
                                                .contentType("application/json")
                                                .content("""
                                                                {
                                                                  "amount": -100
                                                                }
                                                                """))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message")
                                                .value("Deposit amount must be positive"))
                                .andExpect(jsonPath("$.status").value(400));
        }

        @Test
        void getTransactionsShouldReturnWalletTransactions() throws Exception {

                // Create wallet
                String response = mockMvc.perform(post("/wallet/create"))
                                .andExpect(status().isOk())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                JsonNode json = objectMapper.readTree(response);
                Long walletId = json.get("id").asLong();

                // Deposit $100
                mockMvc.perform(
                                post("/wallet/" + walletId + "/deposit")
                                                .contentType("application/json")
                                                .content("""
                                                                {
                                                                  "amount": 100
                                                                }
                                                                """))
                                .andExpect(status().isOk());

                // Withdraw $25
                mockMvc.perform(
                                post("/wallet/" + walletId + "/withdraw")
                                                .contentType("application/json")
                                                .content("""
                                                                {
                                                                  "amount": 25
                                                                }
                                                                """))
                                .andExpect(status().isOk());

                // Get transaction history
                mockMvc.perform(get("/wallet/" + walletId + "/transactions"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].type").value("DEPOSIT"))
                                .andExpect(jsonPath("$[0].amount").value(100.0))
                                .andExpect(jsonPath("$[1].type").value("WITHDRAW"))
                                .andExpect(jsonPath("$[1].amount").value(25.0));
        }

}