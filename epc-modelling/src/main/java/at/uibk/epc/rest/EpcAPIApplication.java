package at.uibk.epc.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * EntryPoint as defined in the web.xml, since
 * Tomcat does not scan by default the @ApplicationPath annotations.
 *
 */
public class EpcAPIApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>>classes = new HashSet<Class<?>>();
		classes.add(EpcResource.class);
		return classes;
	}
	
}
