package es.um.atica.sai.security.authentication.factories;

import es.um.atica.sai.security.authentication.method.AuthenticationMethodNoUmu;
import es.um.atica.seam.security.authentication.credentials.CredentialsDefaultUmu;
import es.um.atica.seam.security.authentication.credentials.CredentialsUmu;
import es.um.atica.seam.security.authentication.factories.AuthenticationFactory;
import es.um.atica.seam.security.authentication.method.AuthenticationMethod;

public class AuthenticationFactoryNoUmu implements AuthenticationFactory {

    public AuthenticationMethod createAuthenticationMethod() {
        return new AuthenticationMethodNoUmu();
    }

    public CredentialsUmu createCredentials() {
        return new CredentialsDefaultUmu();
    }
}