# SoundClown - Music Streaming Platform

SoundClown is a modern music streaming platform built with Spring Boot that allows users to share and discover music. The platform supports various user roles, subscription plans, and comprehensive music management features.

## 🚀 Features

### Authentication & Authorization
- User registration with email or phone number
- JWT-based authentication
- Role-based access control (RBAC)
- Multiple user types: Client (Basic, Plus, Pro) and Admin (Basic, Manager, God)

### Music Management
- Create and manage songs
- Create and manage albums
- Artist profiles and management
- Genre categorization
- Comprehensive metadata support (release dates, descriptions, lyrics)

### Subscription Management
- Multiple subscription tiers (Basic, Plus, Pro)
- Subscription upgrade capabilities
- Subscription status tracking

## 🛠 Technical Stack

- **Backend**: Java 17, Spring Boot 3.x
- **Security**: Spring Security with JWT
- **Database**: JPA/Hibernate
- **Build Tool**: Maven
- **Containerization**: Docker
- **Architecture**: Microservices

## 📁 Project Structure

The project follows a microservices architecture with the following main modules:

### Auth Service
Handles authentication, authorization, and user management:
```
auth/
├── src/main/java/com/soundclown/auth/
    ├── application/
    ├── domain/
    ├── infrastructure/
    └── presentation/
```

### Track Service
Manages music-related entities (songs, albums, artists, genres):
```
track/
├── src/main/java/com/soundclown/track/
    ├── application/
    ├── domain/
    ├── infrastructure/
    └── presentation/
```

### Common Module
Shared utilities and configurations:
```
common/
├── src/main/java/com/soundclown/common/
```

## 🔐 API Endpoints

### Authentication API
- `POST /auth/login` - User login
- `POST /auth/register/email` - Register with email
- `POST /auth/register/phone-number` - Register with phone number
- `POST /auth/admins/register/email` - Register admin with email (Admin only)
- `POST /auth/admins/register/phone-number` - Register admin with phone number (Admin only)

### Subscription API
- `POST /subscription/upgrade` - Upgrade subscription
- `POST /subscription/cancel` - Cancel subscription
- `GET /subscription` - Get subscription details

### Music Management API

#### Songs
- `POST /api/v1/songs` - Create song
- `PUT /api/v1/songs/{id}` - Update song
- `GET /api/v1/songs/{id}` - Get song by ID
- `GET /api/v1/songs` - Get all songs
- `GET /api/v1/songs/artist/{artistId}` - Get songs by artist
- `GET /api/v1/songs/album/{albumId}` - Get songs by album
- `DELETE /api/v1/songs/{id}` - Delete song

#### Albums
- `POST /api/v1/albums` - Create album
- `PUT /api/v1/albums/{id}` - Update album
- `GET /api/v1/albums/{id}` - Get album by ID
- `GET /api/v1/albums` - Get all albums
- `GET /api/v1/albums/artist/{artistId}` - Get albums by artist
- `DELETE /api/v1/albums/{id}` - Delete album
- `POST /api/v1/albums/{albumId}/genres/{genreId}` - Add genre to album
- `DELETE /api/v1/albums/{albumId}/genres/{genreId}` - Remove genre from album

#### Artists
- `POST /api/v1/artists` - Create artist
- `PUT /api/v1/artists/{id}` - Update artist
- `GET /api/v1/artists/{id}` - Get artist by ID
- `GET /api/v1/artists` - Get all artists
- `DELETE /api/v1/artists/{id}` - Delete artist
- `POST /api/v1/artists/{artistId}/genres/{genreId}` - Add genre to artist
- `DELETE /api/v1/artists/{artistId}/genres/{genreId}` - Remove genre from artist

#### Genres
- `POST /api/v1/genres` - Create genre (Admin only)
- `PUT /api/v1/genres/{id}` - Update genre (Admin only)
- `GET /api/v1/genres/{id}` - Get genre by ID
- `GET /api/v1/genres` - Get all genres
- `DELETE /api/v1/genres/{id}` - Delete genre (Admin only)

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Docker and Docker Compose
- Maven

### Running the Application

1. Clone the repository:
```bash
git clone https://github.com/yourusername/soundclown.git
cd soundclown
```

2. Build the project:
```bash
mvn clean package
```

3. Start the services using Docker Compose:
```bash
docker-compose up
```

## 🔒 Security

The application implements several security measures:

- JWT-based authentication
- Role-based access control
- Password encryption using BCrypt
- CORS configuration
- Request validation
- Exception handling