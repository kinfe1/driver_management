# Driver Management API

## Why `/api/v1` was added

`/api/v1` was added to support API versioning. This allows the frontend to call stable, versioned endpoints while the backend evolves. In this project, many endpoints support both:

- `/api/...` for existing/unversioned clients
- `/api/v1/...` for the new versioned frontend expectations

If you prefer a single namespace, the aliases can be removed and the server can keep only the requested `/api/v1/...` routes.

## Response wrapper

All successful responses use the `ApiResponse<T>` wrapper:

```json
{
  "success": true,
  "message": "...",
  "data": { ... }
}
```

## 1. Authentication & User Profile Endpoints

### GET `/api/auth/me` or `/api/v1/auth/me`

- Purpose: Fetch the currently logged-in user.
- Response data:
  - `id`
  - `name`
  - `email`
  - `role`

Example response:

```json
{
  "success": true,
  "message": "Current user fetched successfully",
  "data": {
    "id": "...",
    "name": "System Admin",
    "email": "admin@example.com",
    "role": "SUPER_ADMIN"
  }
}
```

### POST `/api/auth/register` or `/api/v1/auth/register`

Request body:

```json
{
  "name": "System Admin",
  "email": "admin@example.com",
  "password": "StrongPass123!",
  "role": "SUPER_ADMIN"
}
```

Response data: created user object.

### POST `/api/auth/login` or `/api/v1/auth/login`

Request body:

```json
{
  "email": "admin@example.com",
  "password": "StrongPass123!"
}
```

Response data:

```json
{
  "token": "...",
  "user": {
    "id": "...",
    "name": "System Admin",
    "email": "admin@example.com",
    "role": "SUPER_ADMIN"
  }
}
```

### GET `/api/drivers/me/profile` or `/api/v1/drivers/me/profile`

- Purpose: Fetch the logged-in driver's profile.
- Response data includes profile fields and a mocked `assignedVehicle` / `profileCompleteness`.

### PUT `/api/drivers/me/profile` or `/api/v1/drivers/me/profile`

Request body:

```json
{
  "firstName": "Aamin",
  "lastName": "Paner",
  "email": "aamin@example.com",
  "phone": "+251911000000",
  "licenseNumber": "LIC-1234"
}
```

Response data: updated driver profile.

### GET `/api/drivers/me/preferences` or `/api/v1/drivers/me/preferences`

- Purpose: Fetch driver preferences.
- Response data:
  - `language`
  - `timezone`
  - `notificationMethod`

### PUT `/api/drivers/me/preferences` or `/api/v1/drivers/me/preferences`

Request body:

```json
{
  "language": "en",
  "timezone": "UTC",
  "notificationMethod": "EMAIL"
}
```

Response data: current preference values.

## 2. Dashboard & Analytics Endpoints (Admin Portal)

### GET `/api/v1/admin/dashboard/stats`

Response data:

```json
{
  "totalDrivers": 150,
  "activeDrivers": 120,
  "suspendedDrivers": 20,
  "expiredLicenses": 10
}
```

### GET `/api/v1/admin/dashboard/compliance-overview`

Response data:

```json
{
  "validDocumentsPercent": 72,
  "expiringSoonPercent": 18,
  "expiredPercent": 10,
  "validDocumentsCount": 108,
  "expiringSoonCount": 27,
  "expiredCount": 15
}
```

### GET `/api/v1/admin/dashboard/recent-events`

Response data: list of events with:
- `event`
- `driver`
- `time`
- `status`

### GET `/api/v1/admin/reports/export`

- Purpose: Trigger report export.
- Response data returns a file URI/path placeholder.

## 3. Driver Management Endpoints (Admin Portal)

### GET `/api/drivers`, `/api/v1/drivers`, or `/api/v1/admin/drivers`

Query parameters:
- `search` (by name, email, license)
- `status` (ACTIVE, SUSPENDED, PENDING_APPROVAL)
- `page` (default `0`)
- `limit` (default `20`)

Response data: list of driver objects.

### POST `/api/drivers`, `/api/v1/drivers`, or `/api/v1/admin/drivers`

Request body:

```json
{
  "firstName": "Ali",
  "lastName": "Bekele",
  "email": "driver@example.com",
  "phone": "+251911000000",
  "licenseNumber": "LIC-1001",
  "status": "ACTIVE"
}
```

Response data: created driver object.

### GET `/api/drivers/{id}`, `/api/v1/drivers/{id}`, or `/api/v1/admin/drivers/{id}`

Response data: single driver object.

### PUT `/api/drivers/{id}`, `/api/v1/drivers/{id}`, or `/api/v1/admin/drivers/{id}`

Request body: same as create.

Response data: updated driver object.

## 4. Document Management Endpoints

### POST `/api/documents/upload` or `/api/v1/documents/upload`

Request body:

```json
{
  "driverId": "...",
  "documentType": "Driver License",
  "fileName": "license.pdf",
  "fileUrl": "https://...",
  "expiryDate": "2027-05-01"
}
```

Response data: saved document metadata.

### PUT `/api/documents/{documentId}` or `/api/v1/documents/{documentId}`

Request body: same as upload.

### GET `/api/documents/{documentId}/preview` or `/api/v1/documents/{documentId}/preview`

Response data: document metadata.

### GET `/api/documents/{documentId}/download` or `/api/v1/documents/{documentId}/download`

Response data: the document file URL.

### GET `/api/documents/driver/{driverId}` or `/api/v1/documents/driver/{driverId}`

Response data: list of document metadata for a driver.

## 5. Vehicle & Route Assignment Endpoints

### GET `/api/vehicle-assignments` or `/api/v1/admin/vehicle-assignments`

Response data: list of vehicle assignment records.

### POST `/api/vehicle-assignments` or `/api/v1/admin/vehicle-assignments`

Request body:

```json
{
  "driverId": "...",
  "vehicleId": "...",
  "assignmentDate": "2026-07-16"
}
```

Response data: saved assignment.

### DELETE `/api/vehicle-assignments/{assignmentId}` or `/api/v1/admin/vehicle-assignments/{assignmentId}`

Response data: removed assignment ID.

## Notes

- Some frontend-requested endpoints such as `/api/v1/drivers/{driverId}/assignment-history` and `/api/v1/admin/drivers/{driverId}/documents` are not currently implemented in this codebase.
- The existing implementation uses a shared response wrapper and basic in-memory defaults for preferences.
