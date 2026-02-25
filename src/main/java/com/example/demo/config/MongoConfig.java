package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

import java.util.HashMap;
import java.util.Map;
@Slf4j
public class MongoConfig
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {

        String[] profiles = context.getEnvironment().getActiveProfiles();
        if (profiles.length == 0) return;

        String activeProfile = profiles[0];

        String secretName = "demo/mongo-" + activeProfile;
        log.info("SecretKey:" +activeProfile);


        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(Region.EU_NORTH_1)
                .build();

        String secretString = client.getSecretValue(
                GetSecretValueRequest.builder()
                        .secretId(secretName)
                        .build()
        ).secretString();

        JSONObject json = new JSONObject(secretString);

        String mongoUri = json.getString("spring.mongodb.uri");

        Map<String, Object> props = new HashMap<>();
        props.put("spring.mongodb.uri", mongoUri);

        context.getEnvironment()
                .getPropertySources()
                .addFirst(new MapPropertySource("awsSecret", props));
    }
}