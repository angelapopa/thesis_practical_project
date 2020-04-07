package at.uibk.epc.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class EpcAPIApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>>classes = new HashSet<Class<?>>();
		classes.add(EpcResource.class);
		return classes;
	}
}
