package es.enolgor.app;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEvent.Type;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.enolgor.logging.ForcedTriggeringPolicy;

public class App extends ResourceConfig{
	private final static Properties appProperties = AppProperties.getProperties();
	
	static{
		System.getProperties().putAll(appProperties);
		LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		ctx.reconfigure();
		ForcedTriggeringPolicy.roll();
		Logger logger = LoggerFactory.getLogger(App.class);
		logger.info("Initializing application");
		appProperties.forEach((key, value) -> logger.info(String.format("App Property: %s = %s", key, value))); 
	}
	
	public App() {
		super();
		register(new AppBinder());
		register(new AppListener());
		register(JacksonFeature.class);
	}
	
	public abstract static class ApplicationListenerAdapter implements ApplicationEventListener{
		@Override
		public void onEvent(ApplicationEvent event) {
			if(event.getType() == Type.INITIALIZATION_APP_FINISHED) onApplicationInit();
			if(event.getType() == Type.DESTROY_FINISHED) onApplicationDestroy();
		}

		@Override
		public RequestEventListener onRequest(RequestEvent requestEvent) {
			return null;
		}
		
		public abstract void onApplicationInit();
		
		public abstract void onApplicationDestroy();
	}
}
