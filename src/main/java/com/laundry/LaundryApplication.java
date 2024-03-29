package com.laundry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import static springfox.documentation.builders.PathSelectors.regex;

import javax.sql.DataSource;

@SpringBootApplication
@EnableSwagger2
public class LaundryApplication {

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Micro-service for booking laundry")
				.version("1.0")
				.build();
	}

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("greetings")
				.apiInfo(apiInfo())
				.select()
				.paths(regex("/v1.*"))
				.build();
	}

	@Bean
	public DataSource dataSource1() {
		final EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder
				.setType(EmbeddedDatabaseType.HSQL)
				.addScript("db.sql")
				.build();
	}

	@Bean
	public JdbcTemplate getJdbcTemplate(final DataSource dataSource1) {
		return new JdbcTemplate(dataSource1);
	}

	@Bean
	public KeyHolder keyHolder() {
		return new GeneratedKeyHolder();
	}

	public static void main(String[] args) {
		SpringApplication.run(LaundryApplication.class, args);
	}



}
