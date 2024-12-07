
# Personal Finance Manager 💰- Backend

The **Java 17/Spring Boot 3.x** backend of Personal Finance Manager, an application designed to assist individuals affected by economic fluctuations or those looking to track and analyze their personal spending over a specific timeframe. This backend service integrates with **Python-implemented Optical Character Recognition (OCR)** technology for document processing and supports an [**Angular** frontend](https://github.com/victordoroftei/FE-personal-finance-manager) for a seamless user experience.

---

## Features
- **Expense Tracking**: Process and store spending data from receipts, invoices, and other documents.
- **Optical Character Recognition (OCR)**: Leverages Python for automated text extraction from images and scanned documents.
- **RESTful APIs**: Provides endpoints for user management, expense tracking, chart generation and analytics.
- **Database Integration**: Persistent storage of user, extraction and spending data.
- **Scalability**: Designed with a modular architecture and microservices in mind.

---

## Technology Stack
### **Backend**
- **Language**: Java
- **Framework**: Spring Boot
- **Database**: MySQL
- **Security**: Spring Security (JWT-based authentication)

### **OCR**
- **Language**: Python
- **Library**: PyTesseract (for **OCR**)

### **Frontend**
- **Framework**: Angular (developed in a [separate repository](https://github.com/victordoroftei/FE-personal-finance-manager))

---