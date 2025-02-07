package com.github.leroyguillaume.keycloak.bcrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.bouncycastle.crypto.RuntimeCryptoException;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.management.RuntimeErrorException;

/**
 * @author <a href="mailto:pro.guillaume.leroy@gmail.com">Guillaume Leroy</a>
 */
public class BCryptPasswordHashProvider implements PasswordHashProvider {
    private final int defaultIterations;
    private final String providerId;

    public BCryptPasswordHashProvider(String providerId, int defaultIterations) {
        this.providerId = providerId;
        this.defaultIterations = defaultIterations;
    }

    @Override
    public boolean policyCheck(PasswordPolicy policy, PasswordCredentialModel credential) {
        int policyHashIterations = policy.getHashIterations();
        if (policyHashIterations == -1) {
            policyHashIterations = defaultIterations;
        }

        return credential.getPasswordCredentialData().getHashIterations() == policyHashIterations
                && providerId.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(String rawPassword, int iterations) {
        String encodedPassword = encode(rawPassword, iterations);

        // bcrypt salt is stored as part of the encoded password so no need to store salt separately
        return PasswordCredentialModel.createFromValues(providerId, new byte[0], iterations, encodedPassword);
    }

    @Override
    public String encode(String rawPassword, int iterations) {
        int cost;
        if (iterations == -1) {
            cost = defaultIterations;
        } else {
            cost = iterations;
        }

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] sha256Password = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            String encodedPassword = new String(sha256Password, StandardCharsets.UTF_8).replace("-", "");
            return BCrypt.with(BCrypt.Version.VERSION_2A).hashToString(cost, encodedPassword.toCharArray());
        } catch (Exception ex) {
            throw new RuntimeCryptoException(ex.toString());
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean verify(String rawPassword, PasswordCredentialModel credential) {
        final String hash = credential.getPasswordSecretData().getValue();

        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] sha256Password = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            String encodedPassword = new String(sha256Password, StandardCharsets.UTF_8).replace("-", "");
            BCrypt.Result verifier = BCrypt.verifyer().verify(encodedPassword.toCharArray(), hash.toCharArray());
            return verifier.verified;
        } catch (Exception ex) {
            throw new RuntimeCryptoException(ex.toString());
        }
    }
}
