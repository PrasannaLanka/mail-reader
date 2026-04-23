package mail.reader.example.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "excel-service", url = "http://localhost:8081")
public interface ExcelClient {

    @PostMapping(value = "/parse-excel",
            consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    String sendExcel(@RequestBody byte[] file);
}
