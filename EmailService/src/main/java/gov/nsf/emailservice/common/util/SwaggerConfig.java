package gov.nsf.emailservice.common.util;

import gov.nsf.emailservice.controller.EmailController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = {EmailController.class})
/**
 * This Class is the config class for generating Swagger API documentation
 *
 */
public class SwaggerConfig extends WebMvcConfigurerAdapter {

	@Value("${env.name}")
	private String environment = "PROD";


	@Bean
	/**
	 * creates the Docket object
	 * @return
	 */
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
          .groupName("Feature Services")
          .apiInfo(apiInfo())
          .useDefaultResponseMessages(false)
          .enable(StringUtils.endsWithIgnoreCase(environment, "PROD")?false:true)
          .select()
          .apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any())
          .build();

    }
	/**
	 * API information
	 * @return
	 */
	 private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	            .title("Email Service API")
	            .description("Email Service API Docs")
							.contact(new Contact("DIS SIS MyNSF Features Project Team", "", "test@nsf.gov"))
	            .license("Email Service")
	            .licenseUrl("https://mynsf.nsf.gov")
	            .version("1.0.0")
	            .build();
	    }
}
