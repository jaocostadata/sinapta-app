package br.com.sinapta.ecossistema.common.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

/**
 * JPA converter applied to any sensitive String field (passwords, API
 * tokens). Spring Boot registers Hibernate's Spring-aware bean container
 * automatically, so this converter can be @Autowired like a normal bean.
 */
@Component
@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private final CryptoService cryptoService;

    public AttributeEncryptor(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return cryptoService.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return cryptoService.decrypt(dbData);
    }
}
