package project.camus.springcloud.config.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties.KeyStore;
import org.springframework.cloud.config.server.encryption.EncryptionController;
import org.springframework.cloud.config.server.encryption.KeyStoreTextEncryptorLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.rsa.crypto.RsaAlgorithm;
import project.camus.aws.service.AwsKmsService;

@Configuration
public class EncryptionConfig {

    private final String keyStorePassword;

    private final AwsKmsService awsKmsService;

    public EncryptionConfig(@Value("${key-store.password}") String keyStorePassword,
        AwsKmsService awsKmsService) {

        this.keyStorePassword = keyStorePassword;
        this.awsKmsService = awsKmsService;
    }

    @Primary
    @Bean
    public KeyStoreTextEncryptorLocator keyStoreTextEncryptorLocator() {

        KeyStore keyStore = new KeyStore();
        keyStore.setLocation(new ClassPathResource(".keystore/camusConfigEncKey.jks"));
        keyStore.setAlias("camusConfigEncKey");
        keyStore.setPassword(awsKmsService.decrypt(keyStorePassword));

        KeyStoreTextEncryptorLocator locator = new KeyStoreTextEncryptorLocator(
            new KeyStoreKeyFactory(keyStore.getLocation(), keyStore.getPassword().toCharArray()),
            keyStore.getSecret(), keyStore.getAlias());

        locator.setRsaAlgorithm(RsaAlgorithm.DEFAULT);
        locator.setStrong(true);

        return locator;
    }

    @Bean
    public EncryptionController encryptionController() {

        return new EncryptionController(keyStoreTextEncryptorLocator());
    }
}
