# Social Media Management Platform

**AI-Powered Social Media Management for Modern Creators**

[![License: BSL](https://img.shields.io/badge/License-BSL-0630F0.svg)](https://mariadb.com/bsl11/)
[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18+-61DAFB.svg)](https://reactjs.org/)

---

## Overview

A comprehensive social media management platform that helps creators, marketers, and businesses manage their social media presence across multiple platforms. Built with microservices architecture, it offers intelligent scheduling, cross-platform publishing, real-time analytics, and AI-powered features.

### Why Use This Platform?

- 🤖 **AI-Powered**: Optimal posting times, hashtag suggestions, and content variations
- 📱 **Multi-Platform**: LinkedIn, Twitter, Instagram, Facebook, TikTok support
- 📊 **Analytics**: Real-time metrics and engagement tracking
- 🔔 **Smart Notifications**: Never miss important interactions
- 🎨 **Adaptive Composer**: Dynamic UI for each platform's unique features
- 🔒 **Secure**: OAuth2, encrypted tokens, GDPR-compliant

---

## Key Features

### Content Management
- Multi-platform post composer with live preview
- Platform-specific fields (LinkedIn PDFs, Twitter threads, Instagram carousels)
- Media library with image, video, and document support
- Draft management and versioning

### Intelligent Scheduling
- Timezone-aware scheduling
- Visual content calendar
- Queue management with retry logic
- Bulk scheduling support

### Multi-Platform Publishing
- **LinkedIn**: Posts, articles, PDFs, videos
- **Twitter/X**: Tweets, threads, polls, media
- **Instagram**: Posts, reels, carousels, stories
- **Facebook**: Posts, photos, videos, events

### Analytics & Insights
- Real-time engagement metrics (likes, comments, shares)
- Platform-specific dashboards
- Trend analysis and reporting
- Data export (CSV/PDF)

### AI Features
- **Best Posting Time**: ML-based engagement predictions
- **Hashtag Generator**: Context-aware suggestions
- **Content Variations**: Platform-optimized content
- **Smart Reminders**: Maintain posting consistency

### Notifications
- Multi-channel alerts (in-app, email, push)
- Smart notification rules
- Daily/weekly digests
- Deep linking to content

---

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.5+, Java 21+
- **Database**: PostgreSQL 15+
- **Message Queue**: Apache Kafka
- **Job Scheduler**: Apache Kafka
- **Security**: Spring Security, OAuth2, JWT

### Frontend
- **Framework**: React 18+, TypeScript
- **UI**: Tailwind CSS, shadcn/ui
- **Charts**: Recharts
- **State**: Redux Toolkit / Zustand

### Infrastructure
- **Containers**: Docker, Docker Compose
- **Orchestration**: Kubernetes
- **CI/CD**: GitHub Actions
- **Monitoring**: Prometheus, Grafana
- **Tracing**: OpenTelemetry

---

## Services

| Service | Responsibility | Technology |
|---------|--------------------------------|------------|
| **API Gateway** | Routing, authentication | Spring Cloud Gateway |
| **User Service** | Profiles, preferences, settings, User authentication, OAuth2, JWT tokens | Spring Security |
| **Scheduler Service** | Job scheduling, timezone handling | Spring Boot |
| **Post Service** | Multi-platform publishing, retries | Spring Boot, Apache Kafka |
| **Social Account Service** | Manage multiple social accounts | Spring Kafka, Apache Kafka |
| **Analytics Service** |Suggests Best Time for Post | Spring Boot |
| **AI Service** | Best time, hashtags, variations | Spring Boot |
| **Notification Service** | Multi-channel notifications | Spring Boot, FCM |
| **Media Service** | Stores to media assets of file | Spring Boot, MINIO S3 |

---

## Getting Started

### Prerequisites

- Java 21+
- Node.js 22+
- Docker & Docker Compose
- Git

### Quick Start

```bash
# Clone the repository
git clone https://github.com/ZRishu/social-media-scheduler-backend.git
cd social-media-scheduler-backend

# Start infrastructure services (PostgreSQL, Redis)
docker-compose up -d

# Configure environment variables
change .env.example to .env
Set environment variables in services according to the requirements
# Edit .env with your social platform API keys

# Start frontend (new terminal)
cd frontend
npm install
npm run dev
```

### Access the Application

- **Web App**: http://localhost:3000
- **API Gateway**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

---

## API Documentation

Full API documentation is available via Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

### Authentication

All requests require a JWT token:
```bash
Authorization: Bearer <your_jwt_token>
```

### Example Request

```bash
# Create a new post
curl -X POST http://localhost:8080/api/v1/content/posts \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "content": "Hello World!",
    "platforms": ["linkedin", "twitter"],
    "scheduledAt": "2025-11-01T14:00:00Z"
  }'
```

---

## Contributing

We welcome contributions! Here's how:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

### Guidelines

- Follow existing code style
- Write tests for new features
- Update documentation as needed
- Keep commits atomic and descriptive

---

## License

This project is licensed under the BSL License - see the [LICENSE](LICENSE) file for details.

---
