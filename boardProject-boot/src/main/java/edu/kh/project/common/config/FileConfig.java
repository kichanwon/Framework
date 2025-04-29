package edu.kh.project.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;

@Configuration
@PropertySource("classpath:/config.properties")
public class FileConfig implements WebMvcConfigurer{
	
	@Value("${spring.servlet.multipart.location}")
	private String uploadPath;
	@Value("${spring.servlet.multipart.file-size-threshold}")
	private long maxFileThreshold;
	@Value("${spring.servlet.multipart.max-request-size}")
	private long maxRequestSize;
	@Value("${spring.servlet.multipart.max-file-size}")
	private long maxFileSize;


	@Value("${my.profile.resource-handler}")
	private String profileResourceHandler;
	@Value("${my.profile.resource-locations}")
	private String profileResourceLocations;


	@Value("${my.board.resource-handler}")
	private String boardResourceHandler;
	@Value("${my.board.resource-location}")
	private String boardResourceLocation;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler("/myPage/file/**")
		.addResourceLocations("file:///C:/uploadFiles/test/");

		registry
		.addResourceHandler(profileResourceHandler)
		.addResourceLocations(profileResourceLocations);

		registry
		.addResourceHandler(boardResourceHandler)
		.addResourceLocations(boardResourceLocation);
	}
	
	// MultipartResolver setting
	@Bean
	public MultipartConfigElement configElement(){
		MultipartConfigFactory factory = new MultipartConfigFactory();

		factory.setFileSizeThreshold(DataSize.ofBytes(maxFileThreshold));
		factory.setMaxRequestSize(DataSize.ofBytes(maxRequestSize));
		factory.setMaxFileSize(DataSize.ofBytes(maxFileSize));
		factory.setLocation(uploadPath);

		return factory.createMultipartConfig();
	}

	@Bean
	public MultipartResolver multipartResolver() {
		StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
		
		return resolver;
	}

}