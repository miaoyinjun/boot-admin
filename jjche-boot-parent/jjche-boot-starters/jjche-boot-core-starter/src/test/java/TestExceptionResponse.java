import cn.hutool.http.HttpStatus;
import controller.CoreTestController;
import dto.LoginDTO;
import org.jjche.common.exception.enums.GlobalErrorCodeEnum;
import org.jjche.common.wrapper.response.R;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CoreTestController.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestExceptionResponse {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void TestAssert() {
        ResponseEntity<R> wrapperResponseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/core/assert", R.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        R wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getMessage(), CoreTestController.ASSERT_MSG);
    }

    @Test
    public void TestGetParamValid() {
        ResponseEntity<R> wrapperResponseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/core/getParamValid", R.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        R wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), GlobalErrorCodeEnum.PARAMETER_ERROR.getCode());
        assertEquals(wrapper.getMessage(), "name: 不能为空");
    }

    @Test
    public void TestGetParamCustomMsgValid() {
        ResponseEntity<R> wrapperResponseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/core/getParamCustomMsgValid?name=&sex=", R.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        R wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), GlobalErrorCodeEnum.PARAMETER_ERROR.getCode());
        assertTrue(CoreTestController.GET_PARAM_CUSTOM_MSG_VALID_USER_NAME_MSG.equals(wrapper.getMessage()) ||
                CoreTestController.GET_PARAM_CUSTOM_MSG_VALID_SEX_MSG.equals(wrapper.getMessage()));
    }

    @Test
    public void TestPOSTParamsValid() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> strEntity = new HttpEntity<String>("{}", headers);
        ResponseEntity<R> wrapperResponseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/core/POSTParamsValid", strEntity, R.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_BAD_REQUEST);
        R wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), GlobalErrorCodeEnum.PARAMETER_ERROR.getCode());
        assertTrue(LoginDTO.NICKNAME_MSG.equals(wrapper.getMessage()) || LoginDTO.PHONE_MSG.equals(wrapper.getMessage()));
    }

    @Test
    public void TestException() {
        ResponseEntity<R> wrapperResponseEntity = this.restTemplate.getForEntity("http://localhost:" + port + "/core/exception", R.class);
        assertEquals(wrapperResponseEntity.getStatusCode().value(), HttpStatus.HTTP_INTERNAL_ERROR);
        R wrapper = wrapperResponseEntity.getBody();
        assertNotNull(wrapper);
        assertEquals(wrapper.getCode(), GlobalErrorCodeEnum.UNKNOWN_ERROR.getCode());
        assertEquals(wrapper.getMessage(), GlobalErrorCodeEnum.UNKNOWN_ERROR.getMsg());
    }
}
