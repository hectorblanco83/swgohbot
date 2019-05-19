package hb.swgohbot.hb.swgohbot.setup;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * Spring context's static provider, used to retrieve spring context anywhere in the app
 *
 * @author Hector Blanco
 */
@Component
public class SpringContextProvider implements ApplicationContextAware {
	
	/*
	 * The spring's application context
	 */
	private static ApplicationContext context;
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}
	
	
	/**
	 * @return the Spring context
	 */
	public static ApplicationContext getContext() {
		return context;
	}
	
}
