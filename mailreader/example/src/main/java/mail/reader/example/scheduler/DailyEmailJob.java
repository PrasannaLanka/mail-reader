package mail.reader.example.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import mail.reader.example.service.GmailService;

@Component
@RequiredArgsConstructor
public class DailyEmailJob {

    private final GmailService gmailService;

    // For testing (every 1 minute)
    // later change to @Scheduled(cron = "0 0 6 * * ?")
    @Scheduled(fixedRate = 1200000)
    public void runDaily() {
        System.out.println("Scheduler triggered at: " + java.time.LocalDateTime.now());
        gmailService.processEmails();
    }
}
