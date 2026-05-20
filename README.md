# Parking Guidance System

A comprehensive, role-based management system designed to streamline parking facility operations. This system automates the workflow from customer entry to exit, providing real-time monitoring for operators and extensive management tools for administrators.

##  Key Features

The application is divided into four main modules, each tailored to a specific user role:

### 1. Customer Module
* **Entry Services:** Enables customers to generate and print an entry ticket containing a unique Entry ID, their vehicle's plate number, and the exact transaction date/time.
* **Exit Services:** Allows customers to process their payment at the exit station by providing their Entry ID to calculate the final parking hours.

### 2. Entry Station Operator
* **Live Monitoring:** Provides a real-time view of all available free spots within the parking facility.
* **Customer Guidance:** Enables the operator to direct incoming customers to the nearest available parking spot.

### 3. Exit Station Operator
* **Automated Calculation:** Operators can input a customer's ticket ID to automatically calculate the total parking duration and process the checkout.

### 4. Admin Module
* **Infrastructure Management:** Add new spots to the parking lot and view the total capacity of the facility.
* **User Management:** Full control to add, update, or delete system users and assign them specific operational roles.
* **Financial & Operational Reporting:**
  * Generate and view shift reports detailing all payments collected.
  * Monitor active occupancy by viewing reports of all currently parked cars.

---

##  Technology Stack

* **Language:** Java
* **Architecture:** Object-Oriented Programming (OOP)
* **Data Storage:** File System Operations (The application utilizes local file handling for persistent data storage, operating independently without the need for external databases).

---

##  Installation & Usage

1. Clone this repository to your local machine.
2. Open the project in your preferred Java IDE (e.g., VS Code, IntelliJ, Eclipse).
3. Compile and run the main application file.
4. Log in using the designated Admin credentials to begin configuring the parking spots and creating operator accounts.
