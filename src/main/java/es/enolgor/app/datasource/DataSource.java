package es.enolgor.app.datasource;

import es.enolgor.app.auth.UserTokenManager;
import es.enolgor.app.datasource.dao.providers.PetProvider;
import es.enolgor.configuration.Configuration;

public interface DataSource {
	public PetProvider getPetProvider();
	public UserTokenManager getUserTokenManager();
	public void onInit(Configuration configuration) throws Exception;
	public void onClose() throws Exception;
}
