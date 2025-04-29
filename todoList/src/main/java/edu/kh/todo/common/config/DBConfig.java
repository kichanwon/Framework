package edu.kh.todo.common.config;

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

/* 

[application.properties/config.properties]
     ↓
[HikariConfig (Hikari 설정)]  ← @ConfigurationProperties
     ↓
[DataSource (HikariDataSource)] ← DB 연결 풀 생성
     ↓
[SqlSessionFactory] ← DataSource 연결, Mapper/설정 적용
     ↓
[SqlSessionTemplate] ← SQL 실행, 트랜잭션 연동
     ↓
[DataSourceTransactionManager] ← 트랜잭션 처리


@Configuration  :   현재 클래스가 스프링 설정 클래스임을 명시하는 어노테이션

@PropertySource :   properties 파일의 내용을 이용하겠다는 어노테이션
                    다른 properties도 추가하고 싶으면 어노테이션을 계속 추가

@Autowired      :   스프링 컨테이너에서 해당 타입의 객체를 찾아서 의존성 주입(DI)

ApplicationContext :스프링 컨테이너
                    스프링 컨테이너에서 관리되는 모든 객체를 찾아서 관리하는 객체

@Bean           :   개발자가 수동으로 bean을 등록하는 어노테이션
                    @Bean 어노테이션이 작성된 메서드에서 반환된 객체는
                    Spring Container가 관리함(IOC)

@ConfigurationProperties(prefix = "spring.datasource.hikari")
properties 파일의 내용을 이용해서 생성되는 bean을 설정하는 어노테이션
prefix를 지정하여 spring.datasource.hikari으로 시작하는 설정을 모두 적용




*/
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
		sessionFactoryBean.setTypeAliasesPackage("edu.kh.todo");

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