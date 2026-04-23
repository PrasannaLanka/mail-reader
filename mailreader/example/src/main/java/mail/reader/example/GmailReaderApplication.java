package mail.reader.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class GmailReaderApplication {

	public static void main(String[] args) {
		System.setProperty("https.proxyHost", "172.16.24.44");
		System.setProperty("https.proxyPort", "3128");
		System.setProperty("http.proxyHost", "172.16.24.44");
		System.setProperty("http.proxyPort", "3128");
		Authenticator.setDefault(
				new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								"softwarece1",
								"Hmis@1234".toCharArray()
						);
					}
				}
		);
		SpringApplication.run(GmailReaderApplication.class, args);
	}

}
