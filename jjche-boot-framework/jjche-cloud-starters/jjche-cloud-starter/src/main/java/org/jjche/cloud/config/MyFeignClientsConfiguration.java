package org.jjche.cloud.config;

/**
 * <p>
 * 为支持复杂对象类型查询参数自动配置类
 * </p>
 *
 * @author miaoyj
 * @since 2022-04-25
 */
//@Configuration
public class MyFeignClientsConfiguration {

//    @Autowired(required = false)
//    private List<AnnotatedParameterProcessor> annotatedArgumentResolvers = new ArrayList<>();

//ISysBaseAPI必须配置configuration = MyFeignClientsConfiguration.class
//    @Bean
//    public Contract feignContract(FormattingConversionService feignConversionService) {
//        //在原配置类中是用ConversionService类型的参数，但ConversionService接口不支持addConverter操做，使用FormattingConversionService仍然能够实现feignContract配置。
//        feignConversionService.addConverter(new UniversalReversedEnumConverter());
//        return new SpringMvcContract(this.parameterProcessors, feignConversionService);
//    }


}
