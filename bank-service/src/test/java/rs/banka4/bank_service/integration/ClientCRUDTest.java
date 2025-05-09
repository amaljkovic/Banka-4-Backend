package rs.banka4.bank_service.integration;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import rs.banka4.bank_service.repositories.ClientRepository;
import rs.banka4.testlib.integration.DbEnabledTest;

@SpringBootTest
@AutoConfigureMockMvc
@DbEnabledTest
public class ClientCRUDTest {
    @Test
    void emptyOutOfTheBox(@Autowired ClientRepository clientRepo) {
        /* Verify that there's no clients OOTB. */
        assertThat(clientRepo.count()).isEqualTo(0);
    }
}
