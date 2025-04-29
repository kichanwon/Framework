package edu.kh.project.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:/config.properties")
public class DBConfig {

	@Autowired
	private ApplicationContext applicationContext;  // application scope 현재 프로젝트 컨텍스트

	// HikariConfig : 커넥션 풀 설정
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public HikariConfig hikariConfig() {
		return new HikariConfig();
	}

	// DataSource : 커넥션 풀 생성
	@Bean
	public DataSource dataSource(HikariConfig config) {
		DataSource dataSource = new HikariDataSource(config);
		return dataSource;
	}



//////////////////////////// Mybatis 설정 추가 ////////////////////////////
// SqlSessionFactory : SqlSession을 만드는 객체
	@Bean
	public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);

// 매퍼 파일이 모여있는 경로 지정
		sessionFactoryBean.setMapperLocations(
			applicationContext.getResources("classpath:/mappers/**.xml")
			);

// 별칭 지정
// -> 해당 패키지에 있는 모든 클래스가 클래스명으로 별칭이 지정됨
		sessionFactoryBean.setTypeAliasesPackage("edu.kh.project");

// 마이바티스 설정 파일 경로 지정
		sessionFactoryBean.setConfigLocation(
			applicationContext.getResource("classpath:mybatis-config.xml")
			);

// SqlSession 객체 반환
		return sessionFactoryBean.getObject();
	}

// SqlSessionTemplate : 기본 SQL 실행 + 트랜잭션 처리
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sessionFactory) {
		return new SqlSessionTemplate(sessionFactory);
	}

// DataSourceTransactionManager : 트랜잭션 매니저
	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}