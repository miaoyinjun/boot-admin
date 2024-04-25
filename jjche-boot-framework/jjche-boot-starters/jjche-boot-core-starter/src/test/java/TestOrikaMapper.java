//import dto.LoginDTO;
//import org.jjche.core.convert.OriKaMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import orika.LoginDtoToLoginVoMapper;
//import vo.LoginVO;
//import vo.UserVO;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(classes = {OriKaMapper.class, LoginDtoToLoginVoMapper.class})
//public class TestOrikaMapper {
//
//    @Autowired
//    private OriKaMapper oriKaMapper;
//
//    @Test
//    public void Test1() {
//        UserVO userVO = new UserVO();
//        userVO.setName("abc中");
//        LoginVO loginVO = oriKaMapper.map(userVO, LoginVO.class);
//        assertEquals(loginVO.getName(), userVO.getName());
//
//        LoginDTO loginDTO = oriKaMapper.map(loginVO, LoginDTO.class);
//        assertEquals(loginVO.getName(), loginDTO.getNickname());
//    }
//
//}
