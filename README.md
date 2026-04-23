# 📧 Gmail Reader - Spring Boot Application

This project is a **Spring Boot application** that connects to Gmail using OAuth 2.0, reads emails based on specific filters, downloads Excel attachments, and processes their content.

---

# 🚀 Features

- 🔐 OAuth 2.0 authentication with Gmail  
- 📥 Read emails from Gmail inbox  
- 🔍 Filter emails using search queries (sender, subject, attachments)  
- 📎 Download `.xlsx` attachments  
- 📊 Read and print Excel content using Apache POI  
- ⏱ Scheduled job support for automatic execution  

---

# 🏗 Tech Stack

- Java 17  
- Spring Boot  
- Gmail API  
- Apache POI (Excel processing)  
- OAuth 2.0  
- Maven  

---

# 📁 Project Structure

```
mail.reader.example
│
├── config          # Gmail OAuth configuration
├── service         # Gmail reading & Excel processing logic
├── scheduler       # Scheduled job
├── controller      # Test endpoint
└── GmailReaderApplication.java
```

---

# ⚙️ Setup Instructions

## 1️⃣ Create Google Cloud Project

Go to:  
https://console.cloud.google.com

Steps:

1. Create a new project  
2. Enable **Gmail API**  
3. Go to **APIs & Services → Credentials**  
4. Create **OAuth Client ID**  
5. Choose:
   - Application Type: Desktop App / Web App  
6. Download `credentials.json`  

---

## 2️⃣ Place Credentials File

Put the file here:

```
project-root/credentials.json
```

---

## 3️⃣ Configure OAuth Consent Screen

- Add your Gmail as **Test User**
- Keep app in **Testing mode**

---

## 4️⃣ Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

---

## 5️⃣ First-Time Authorization

- Browser will open automatically  
- Login with your Gmail  
- Grant permissions  
- Tokens will be stored in:

```
/tokens/
```

---

# 🧪 Testing the Application

## Option 1 — REST API

Open browser:

```
http://localhost:8080/test-read
```

## Option 2 — Scheduler

Runs automatically based on configuration:

```java
@Scheduled(fixedRate = 15000)
```

---

# 🔍 Gmail Query Example

```java
String query = "has:attachment filename:xlsx";
```

You can customize:

| Filter | Example |
|--------|--------|
| Sender | `from:abc@gmail.com` |
| Subject | `subject:Report` |
| Attachment | `has:attachment` |
| File Type | `filename:xlsx` |

---

# 📊 Sample Output

```
Searching with query: has:attachment filename:xlsx
Messages found: 1
Found attachment: report.xlsx

----- Excel Content -----
Name    Age
John    25
Mary    30
--------------------------
```

---

# 🔐 Security Considerations

⚠ Important:

- App uses **OAuth tokens** stored locally (`tokens/`)
- Tokens allow access to Gmail inbox
- Current scope:
  ```
  GMAIL_READONLY
  ```

### Risks

- Tokens can be misused if exposed  
- App can read entire mailbox (based on scope)  

### Recommendations

- Do NOT use personal Gmail in production  
- Use **Google Workspace account**  
- Use **Service Account + Domain-wide Delegation**  
- Secure `credentials.json` and `tokens/`  
- Restrict access permissions  

---

# 🌐 Proxy / Network Limitation

If running in a corporate network:

- Gmail API calls may fail with:
  ```
  SocketTimeoutException
  ```
- This is due to proxy/firewall restrictions  

### Solution

- Configure proxy in application  
- Whitelist:
  ```
  gmail.googleapis.com
  ```

---

# 📌 Current Limitations

- Reads only `.xlsx` files  
- No duplicate email handling  
- No database storage  
- No retry logic for failures  
- Depends on network/proxy availability  

---

# 🔮 Future Enhancements

- Add subject-based filtering  
- Store processed email IDs  
- Save Excel data to database  
- Integrate with downstream services (Feign)  
- Add retry & error handling  
- Production-grade security  

---

# 👨‍💻 Conclusion

✔ The solution works for reading emails and processing attachments  
⚠ Requires proper network and security setup for production use  
❌ Not recommended with personal Gmail for sensitive data  
