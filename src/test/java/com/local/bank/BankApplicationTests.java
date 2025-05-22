package com.local.bank;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"springfox.documentation.enabled=false"
})
class BankApplicationTests {

	@Test
	void contextLoads() {
	}

}
