import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import cn.hutool.http.HttpStatus;
import controller.TestFilterController;
import org.jjche.common.constant.FilterEncryptionConstant;
import org.jjche.common.enums.FilterEncryptionEnum;
import org.jjche.filter.property.FilterEncryptionApplicationProperties;
import org.jjche.filter.property.FilterEncryptionProperties;
import org.jjche.filter.util.EncryptionUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {TestFilterController.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("rsa")
public class TestEncryptionRSAFilter {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FilterEncryptionProperties encryptionProperties;

    @Test
    public void Test1() {
        List<FilterEncryptionApplicationProperties> applications = encryptionProperties.getApplications();
        FilterEncryptionApplicationProperties application = applications.get(1);
        String appId = application.getId();
        String privateKey = application.getPrivateKey();
        String publicKey = application.getPublicKey();
        String baseUrl = "http://localhost:" + port + "/filter/test?";
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("pageIndex", "1");
        queryMap.put("pageSize", "10");
        queryMap.put("name", "中as1");
        String queryOrderedString = MapUtil.sortJoin(queryMap, "&", "=", false);
        String nonce = RandomUtil.randomString(FilterEncryptionConstant.NONCE_LENGTH);
        String timestamp = String.valueOf(System.currentTimeMillis());

        HttpHeaders headers = new HttpHeaders();
        headers.add(FilterEncryptionEnum.APP_ID.getKey(), appId);
        headers.add(FilterEncryptionEnum.NONCE.getKey(), nonce);
        headers.add(FilterEncryptionEnum.TIMESTAMP.getKey(), timestamp);

        /** rsa签名 */
        byte[] data = EncryptionUtil.signData(queryOrderedString, timestamp, nonce);

        RSA rsa = SecureUtil.rsa(privateKey, publicKey);
        Sign sign = SecureUtil.sign(SignAlgorithm.SHA256withRSA);
        sign.setPrivateKey(rsa.getPrivateKey());
        //签名
        byte[] signed = sign.sign(data);
        String mySign = Base64.encode(signed);
        headers.add(FilterEncryptionEnum.SIGN.getKey(), mySign);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl + queryOrderedString, HttpMethod.GET, requestEntity, String.class);

        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.HTTP_OK);
        String body = responseEntity.getBody();
        assertEquals(body, "test");
    }

}