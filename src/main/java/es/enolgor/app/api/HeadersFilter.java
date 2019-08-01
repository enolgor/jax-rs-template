package es.enolgor.app.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import es.enolgor.configuration.Configuration;
import es.enolgor.configuration.Configuration.Header;

@Provider
public class HeadersFilter implements ContainerResponseFilter {

	@Inject Configuration configuration;

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext cres) throws IOException {
		configuration.getServerConfiguration().getHeaders().parallelStream().filter(Header::isEnabled).forEach(header -> cres.getHeaders().add(header.getKey(), header.getValue()));
	}

}
