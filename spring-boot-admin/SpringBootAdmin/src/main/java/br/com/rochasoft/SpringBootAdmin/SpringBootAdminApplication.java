package br.com.rochasoft.SpringBootAdmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;
import de.codecentric.boot.admin.server.web.client.InstanceExchangeFilterFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

// https://start.spring.io/
//

@Configuration(proxyBeanMethods = false)
@EnableAutoConfiguration
@EnableAdminServer
@Lazy(false)
public class SpringBootAdminApplication
{

	private static final Logger log = LoggerFactory.getLogger(SpringBootAdminApplication.class);

	public static void main(String[] args)
	{
		SpringApplication app = new SpringApplication(SpringBootAdminApplication.class);
		app.setApplicationStartup(new BufferingApplicationStartup(1500));
		app.run(args);
	}

	// tag::customization-instance-exchange-filter-function[]
	@Bean
	public InstanceExchangeFilterFunction auditLog()
	{
		return (instance, request, next) -> next.exchange(request).doOnSubscribe((s) ->
		{
			if (HttpMethod.DELETE.equals(request.method()) || HttpMethod.POST.equals(request.method()))
			{
				log.info("{} for {} on {}", request.method(), instance.getId(), request.url());
			}
		});
	}
	// end::customization-instance-exchange-filter-function[]

	// tag::customization-http-headers-providers[]
	@Bean
	public HttpHeadersProvider customHttpHeadersProvider() {
		return (instance) -> {
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("X-CUSTOM", "My Custom Value");
			return httpHeaders;
		};
	}
	// end::customization-http-headers-providers[]

	@Bean
	public HttpTraceRepository httpTraceRepository() {
		return new InMemoryHttpTraceRepository();
	}

	@Bean
	public AuditEventRepository auditEventRepository() {
		return new InMemoryAuditEventRepository();
	}

}