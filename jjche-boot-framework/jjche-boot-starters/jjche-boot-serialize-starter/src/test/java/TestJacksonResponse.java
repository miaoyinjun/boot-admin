import controller.CoreTestController;
import org.jjche.common.wrapper.response.R;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import vo.LoginVO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CoreTestController.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestJacksonResponse {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void Test1() {
        //使用这种方式转换restTemplate接收返回LinkedHashMap类型的对象
        ParameterizedTypeReference<R<LoginVO>> typeRef = new ParameterizedTypeReference<R<LoginVO>>() {
        };

        ResponseEntity<R<LoginVO>> loginVOWrapper = this.restTemplate.exchange("http://localhost:" + port + "/http-convert/jackson", HttpMethod.GET, null, typeRef);
        LoginVO loginVO = loginVOWrapper.getBody().getData();
        assertNotNull(loginVO);
        assertNotNull(loginVO.getName());
        assertFalse(loginVO.getABoolean());
        assertNotNull(loginVO.getALong());
        assertNotNull(loginVO.getAInteger());
        assertNotNull(loginVO.getAFloat());
        assertNotNull(loginVO.getADouble());
        assertNotNull(loginVO.getABigDecimal());
        assertNotNull(loginVO.getAList());

        assertNull(loginVO.getName2());
    }
}