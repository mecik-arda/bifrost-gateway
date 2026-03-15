# Bifrost Security Gateway

Bifrost is a high-performance, cybersecurity-focused API Gateway built with Spring Boot. Inspired by the mythical bridge connecting Midgard to Asgard, this gateway serves as a resilient guardian for microservices architecture, intercepting and neutralizing malicious traffic before it reaches core systems.

## Security Architecture

Bifrost operates as a stateless security intermediary. It intercepts every incoming HTTP request and passes it through a chain of security filters (The Five Runes) before routing it to the destination service.

### The Security Filter Chain (The Five Runes)

1. **Mjolnir (Web Application Firewall):** - Uses RegEx-based pattern matching to identify common injection attacks.
   - Detects SQL Injection (SQLi) patterns such as `UNION SELECT`, `OR 1=1`, and `DROP TABLE`.
   - Neutralizes Cross-Site Scripting (XSS) attempts by filtering `<script>` tags and JavaScript event handlers.

2. **Heimdall (Brute Force Detection):** - Monitors authentication failure rates per IP address.
   - Implements a temporary lockout mechanism for IPs exceeding the threshold of failed JWT validation attempts.

3. **Runestone (Correlation & Traceability):** - Injects a `X-Bifrost-Request-ID` (UUID) into every request header.
   - Enables distributed tracing across microservices, making it easier to track the lifecycle of a request in logs.

4. **Valkyrie (Proactive Monitoring):** - Exposed via `/public/health` endpoint.
   - Provides granular status reports of the gateway's internal security modules.

5. **Odin's Eye (Advanced Rate Limiting):** - Powered by the **Token Bucket Algorithm** (via Bucket4j).
   - Prevents Resource Exhaustion and Denial of Service (DoS) attacks.
   - Configurable throughput limits (Requests Per Second) at the global or endpoint level.

## Technical Specifications

- **Security Model:** Stateless JWT (JSON Web Token) Authentication.
- **Filter Precedence:** Custom filter ordering ensures that IP Blacklisting and WAF checks occur before expensive JWT validation.
- **Logging:** Integration with Slf4j and MDC for correlated security event logging.

## Installation and Execution

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Build and Run
```
# Clone the repository
git clone [https://github.com/mecik-arda/bifrost-gateway.git](https://github.com/mecik-arda/bifrost-gateway.git)

# Compile and package
mvn clean install

# Launch the gateway
mvn spring-boot:run
```
### Configuration

Custom security policies are defined in src/main/resources/application.yml:
YAML

bifrost:
  security:
    blacklist-ips: []
    rate-limit:
      capacity: 10
      refill-tokens: 1
    jwt:
      secret: ${JWT_SECRET:your_default_secret}

Tech Stack

    Spring Boot 3.x

    Spring Security (Stateless)

    Bucket4j (Rate Limiting)

    Auth0 Java-JWT (Security Tokens)

Author

Arda Mecik - GitHub Profile
License

This project is licensed under the MIT License - see the LICENSE file for details.
