# 🚀 PayPilot – AI-Powered Shopping Copilot

PayPilot is an AI-powered shopping assistant that helps users find the right products based on their requirements and budget.

Instead of simply searching for products, PayPilot understands the user's request, analyzes available products, and recommends the most suitable option.

## ✨ Features

- 🤖 AI-powered product recommendations
- 💰 Budget-aware shopping assistance
- 🎯 Best Match recommendation
- 💵 Cheapest option
- ⭐ Best Value recommendation
- 🧠 Explainable AI – shows why a product was selected
- 🛒 Smart shopping cart
- ➕➖ Quantity controls and item removal
- ❤️ Save for Later
- 💳 Secure Razorpay payment integration
- ✅ Payment verification
- 📊 Payment activity and audit logs
- 🎁 Recommended add-ons / upselling
- 📱 Responsive and modern UI

## 🧠 How PayPilot Works

1. User enters a natural-language shopping request.
2. PayPilot analyzes the request using AI.
3. Products are searched based on category, requirements, and budget.
4. PayPilot selects the most suitable product.
5. The system explains why the product was recommended.
6. The user can add the product to the cart.
7. The user can review the cart and proceed to payment.
8. Razorpay processes and verifies the payment.
9. Payment activity is recorded through audit logs.

## 🛠️ Technology Stack

### Frontend
- HTML
- CSS
- JavaScript

### Backend
- Java
- Spring Boot
- Spring Data JPA
- REST APIs

### Database
- MySQL

### AI
- Google Gemini API

### Payments
- Razorpay Payment Gateway

### Tools
- Maven
- Git & GitHub
- Visual Studio Code

## 🏗️ Project Structure

```text
Paypilot-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/Paypilot/Paypilot_backend/
│   │   │       ├── controller/
│   │   │       ├── model/
│   │   │       ├── repository/
│   │   │       └── service/
│   │   └── resources/
│   │       ├── static/
│   │       │   └── index.html
│   │       └── application.properties
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── .gitignore

⚙️ Setup & Installation
1. Clone the repository
git clone https://github.com/chandanarajasri/paypilot-AI
cd Paypilot-backend

2. Configure MySQL
Create a MySQL database named:
CREATE DATABASE paypilot;
Update application.properties with your local MySQL credentials.

3. Configure API Keys
Set the following environment variables:
GEMINI_API_KEY=your_gemini_api_key
RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret
Never commit API keys, passwords, or other secrets to GitHub.

4. Run the application
Using Maven:
./mvnw spring-boot:run

On Windows:
mvnw.cmd spring-boot:run

The application runs on:
http://localhost:8081

💡 Example Prompts
Try PayPilot with requests such as:
Headphones under ₹3000
Power bank for travel
Laptop under ₹60000
Gift under ₹2000

🔐 Security
PayPilot keeps sensitive credentials outside the source code using environment variables.

The project also uses:
Payment signature verification
Audit logging
Git secret protection through .gitignore

🎯 Problem Solved
Traditional shopping search requires users to manually compare multiple products, prices, and features.
PayPilot simplifies this process by acting as an AI shopping copilot that understands the user's intent and helps them make a faster and more informed purchase decision.

🚀 Future Improvements
Real-time product availability
Delivery-time based recommendations
More advanced product comparison
Personalized shopping history
Real-time discounts and offers
Multi-store product comparison

👨‍💻 Project
Built as part of the Razorpay AI Buildathon.

⭐ If you find PayPilot interesting, consider giving the repository a star!

