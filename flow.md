graph TD
Client[User's Browser / App] --> APIGateway[API Gateway Service];

    subgraph "Your Backend (Private Network)"
        APIGateway -->|/api/v1/users/**| UserService[User Service];
        APIGateway -->|/api/v1/posts/**| PostService[Post Service];
        APIGateway -->|/api/v1/accounts/**| AccountService[Social Account Service];
        APIGateway -->|/api/v1/media/**| MediaService[Media Service];
        APIGateway -->|/api/v1/analytics/**| AnalyticsService[Analytics Service];
        APIGateway -->|/api/v1/ai/**| AiService[AI Service];

        PostService -->|Feign| AccountService;
        PostService -->|Feign| AnalyticsService;
        PostService -->|Feign| AiService;
        
        SchedulerService[Scheduler Service] -->|Feign| AccountService;

        AnalyticsService -->|Feign| AccountService;

        AccountService -->|Kafka| AnalyticsService;
        AccountService -->|Kafka| KafkaBroker[Kafka];
        PostService -->|Kafka| KafkaBroker;
        KafkaBroker --> SchedulerService;
        
        subgraph "Authentication"
            Auth[Keycloak / Auth Service]
            APIGateway -.->|Validates JWT| Auth
            UserService -.->|Listens to User Events| Kafka
            Kafka -->|keycloak.user.created| UserService
        end

    end