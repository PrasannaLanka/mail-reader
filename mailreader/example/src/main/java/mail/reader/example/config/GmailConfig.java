package mail.reader.example.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Collections;

@Configuration
public class GmailConfig {

    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    @Bean
    public Gmail gmail() throws Exception {

        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        var jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                jsonFactory,
                new InputStreamReader(new FileInputStream("credentials.json"))
        );

        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        httpTransport,
                        jsonFactory,
                        clientSecrets,
                        Collections.singleton(GmailScopes.GMAIL_READONLY))
                        .setDataStoreFactory(new FileDataStoreFactory(
                                new java.io.File(TOKENS_DIRECTORY_PATH)))
                        .setAccessType("offline")
                        .build();

        var credential =
                new com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp(
                        flow,
                        new com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver.Builder()
                                .setPort(8888)
                                .build()
                ).authorize("user");


        return new Gmail.Builder(
                httpTransport,
                jsonFactory,
                credential)
                .setApplicationName("gmail-reader")
                .build();
    }
}
