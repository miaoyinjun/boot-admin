//package orika;
//
//
//import dto.LoginDTO;
//import ma.glasnost.orika.CustomMapper;
//import ma.glasnost.orika.MappingContext;
//import org.springframework.stereotype.Component;
//import vo.LoginVO;
//
///**
// * <p>
// * 测试自定义转换
// * </p>
// *
// * @author miaoyj
// * @version 1.0.9-SNAPSHOT
// * @since 2020-07-09
// */
//@Component
//public class LoginDtoToLoginVoMapper extends CustomMapper<LoginDTO, LoginVO> {
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void mapAtoB(LoginDTO loginDTO, LoginVO loginVO, MappingContext context) {
//        loginVO.setName(loginDTO.getNickname());
//        super.mapAtoB(loginDTO, loginVO, context);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void mapBtoA(LoginVO loginVO, LoginDTO loginDTO, MappingContext context) {
//        loginDTO.setNickname(loginVO.getName());
//        super.mapBtoA(loginVO, loginDTO, context);
//    }
//}
