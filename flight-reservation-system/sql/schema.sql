USE flightdb;

CREATE TABLE IF NOT EXISTS Airport (
    Airport_Id INT AUTO_INCREMENT PRIMARY KEY,
    Airport_Name VARCHAR(100) UNIQUE NOT NULL,
    ZIP VARCHAR(3),
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    City VARCHAR(100),
    Country VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Airline_Company (
    Airline_Company_Id INT AUTO_INCREMENT PRIMARY KEY,
    Company_Name VARCHAR(100) UNIQUE NOT NULL
);


CREATE TABLE IF NOT EXISTS Airplane (
    Airplane_Id INT AUTO_INCREMENT PRIMARY KEY,
    Seating_Capacity INT NOT NULL,
    Airline_Company_Id INT,
    FOREIGN KEY (Airline_Company_Id) REFERENCES Airline_Company(Airline_Company_Id)
);

CREATE TABLE IF NOT EXISTS Seat (
    Seat_Id INT AUTO_INCREMENT PRIMARY KEY,
    Availability BOOLEAN DEFAULT TRUE,
    Location VARCHAR(10) NOT NULL,
    Class VARCHAR(20), 
    Airplane_Id INT,
    FOREIGN KEY (Airplane_Id) REFERENCES Airplane(Airplane_Id),
    CONSTRAINT unique_seat_location UNIQUE (Location, Airplane_id)
);



CREATE TABLE IF NOT EXISTS Flight_Trip (
    Flight_Trip_Id INT AUTO_INCREMENT PRIMARY KEY,
    Distance DOUBLE,
    Depart_Time TIMESTAMP,
    Arrival_Time TIMESTAMP,
    Depart_Airport INT,
    Arrival_Airport INT,
    Price DOUBLE,
    CONSTRAINT unique_trip UNIQUE (Depart_Time, Arrival_Time, Depart_Airport, Arrival_Airport),

    FOREIGN KEY (Depart_Airport) REFERENCES Airport(Airport_Id),
    FOREIGN KEY (Arrival_Airport) REFERENCES Airport(Airport_Id)
);



CREATE TABLE IF NOT EXISTS Hop (
    Hop_Id INT AUTO_INCREMENT PRIMARY KEY,
    Airplane_Id INT,
    Depart_Time TIMESTAMP,
    Arrival_Time TIMESTAMP,
    Depart INT,
    Arrival INT,
    CONSTRAINT unique_hop UNIQUE (Airplane_Id, Depart_Time, Depart, Arrival, Arrival_Time),
    FOREIGN KEY (Airplane_Id) REFERENCES Airplane(Airplane_Id),
    FOREIGN KEY (Depart) REFERENCES Airport(Airport_Id),
    FOREIGN KEY (Arrival) REFERENCES Airport(Airport_Id)
);


CREATE TABLE IF NOT EXISTS Flight_Trip_Hop (
    Flight_Trip_Id INT,
    Hop_Id INT,

    PRIMARY KEY (Flight_Trip_Id, Hop_Id),
    FOREIGN KEY (Flight_Trip_Id) REFERENCES Flight_Trip(Flight_Trip_Id),
    FOREIGN KEY (Hop_Id) REFERENCES Hop(Hop_Id)
);


CREATE TABLE IF NOT EXISTS Fare (
    Hop_Id INT,
    Amount DOUBLE,
    Currency VARCHAR(10),
    Discount DOUBLE,    
    Tax DOUBLE,
    Final_Amount DOUBLE,
    PRIMARY KEY (Hop_Id),
    FOREIGN KEY (Hop_Id) REFERENCES Hop(Hop_Id)
);

CREATE TABLE IF NOT EXISTS User (
    User_Id INT AUTO_INCREMENT PRIMARY KEY,
    FName VARCHAR(100),
    LName VARCHAR(100),
    Email VARCHAR(100) UNIQUE,
    Phone VARCHAR(20),
    User_Type VARCHAR(50),
    Password VARCHAR(100)
);




CREATE TABLE IF NOT EXISTS Traveller (
    Traveller_Id INT,

    User_Id INT,
    Flight_Trip_Id INT,
    Seat_Id INT,
    Step INT,


    PRIMARY KEY (User_Id, Flight_Trip_Id, Seat_Id),
    FOREIGN KEY (Seat_Id) REFERENCES Seat(Seat_Id),
    FOREIGN KEY (User_Id) REFERENCES User(User_Id),
    FOREIGN KEY (Flight_Trip_Id) REFERENCES Flight_Trip(Flight_Trip_Id)
);


/* insertion d'aéroports */

INSERT IGNORE INTO Airport (Airport_Name, ZIP, latitude, longitude, City, Country)
VALUES ('Charles de Gaulle Airport', '750', 49.0097, 2.5479, 'Paris', 'France');

INSERT IGNORE INTO Airport (Airport_Name, ZIP, latitude, longitude, City, Country)
VALUES ('Heathrow Airport', 'LHR', 51.4700, -0.4543, 'London', 'United Kingdom');

INSERT IGNORE INTO Airport (Airport_Name, ZIP, latitude, longitude, City, Country)
VALUES ('Frankfurt Airport', 'FRA', 50.0379, 8.5622, 'Frankfurt', 'Germany');

INSERT IGNORE INTO Airport (Airport_Name, ZIP, latitude, longitude, City, Country)
VALUES ('Amsterdam Airport Schiphol', 'AMS', 52.3086, 4.7681, 'Amsterdam', 'Netherlands');

INSERT IGNORE INTO Airport (Airport_Name, ZIP, latitude, longitude, City, Country)
VALUES ('Madrid-Barajas Airport', 'MAD', 40.4936, -3.5664, 'Madrid', 'Spain');

INSERT IGNORE INTO Airport (Airport_Name, ZIP, latitude, longitude, City, Country)
VALUES ('Barcelona-El Prat Airport', 'BCN', 41.2974, 2.0833, 'Barcelona', 'Spain');

INSERT IGNORE INTO Airport (Airport_Name, ZIP, latitude, longitude, City, Country)
VALUES ('Munich Airport', 'MUC', 48.3538, 11.7861, 'Munich', 'Germany');

INSERT IGNORE INTO Airport (Airport_Name, ZIP, latitude, longitude, City, Country)
VALUES ('Zurich Airport', 'ZRH', 47.4502, 8.5619, 'Zurich', 'Switzerland');

INSERT IGNORE INTO Airport (Airport_Name, ZIP, latitude, longitude, City, Country)
VALUES ('Vienna International Airport', 'VIE', 48.1103, 16.5697, 'Vienna', 'Austria');




/* insertion de companies aériennes */ 


INSERT IGNORE INTO Airline_Company (Company_Name) VALUES ('Air France');
INSERT IGNORE INTO Airline_Company (Company_Name) VALUES ('Lufthansa');
INSERT IGNORE INTO Airline_Company (Company_Name) VALUES ('British Airways');
INSERT IGNORE INTO Airline_Company (Company_Name) VALUES ('Iberia');
INSERT IGNORE INTO Airline_Company (Company_Name) VALUES ('KLM');
INSERT IGNORE INTO Airline_Company (Company_Name) VALUES ('Swiss International Air Lines');
INSERT IGNORE INTO Airline_Company (Company_Name) VALUES ('Austrian Airlines');
INSERT IGNORE INTO Airline_Company (Company_Name) VALUES ('Turkish Airlines');



/* insertion d'avions */

INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (220, 1);
INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (182, 1);

INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (244, 2);
INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (196, 2);

INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (257, 3);
INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (237, 3);

INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (210, 4);
INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (285, 4);

INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (205, 5);
INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (289, 5);

INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (257, 6);
INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (237, 6);

INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (210, 7);
INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (285, 7);

INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (205, 8);
INSERT INTO Airplane (Seating_Capacity, Airline_Company_Id) VALUES (289, 8);



/* insertion de hop */

INSERT IGNORE INTO Hop (Airplane_Id, Depart_Time, Arrival_Time, Depart, Arrival)
VALUES (1,  '2023-10-01 10:00:00', '2023-10-01 12:00:00', 1, 2);

INSERT IGNORE INTO Hop (Airplane_Id, Depart_Time, Arrival_Time, Depart, Arrival)
VALUES (2, '2023-10-02 14:00:00', '2023-10-02 16:00:00', 2, 3);

INSERT IGNORE INTO Hop (Airplane_Id, Depart_Time, Arrival_Time, Depart, Arrival)
VALUES (3,  '2025-04-15 07:00:00', '2025-04-15 12:13:00',5,7);

INSERT IGNORE INTO Hop (Airplane_Id, Depart_Time, Arrival_Time, Depart, Arrival)
VALUES (4, '2025-04-14 16:00:00', '2025-04-14 18:48:00', 8, 2);

INSERT IGNORE INTO Hop (Airplane_Id, Depart_Time, Arrival_Time, Depart, Arrival)
VALUES (5, '2025-04-17 13:00:00', '2025-04-17 16:24:00', 4, 6);

INSERT IGNORE INTO Hop (Airplane_Id, Depart_Time, Arrival_Time, Depart, Arrival)
VALUES (6, '2025-04-17 13:00:00', '2025-04-17 16:24:00', 1, 5);

INSERT IGNORE INTO Hop (Airplane_Id, Depart_Time, Arrival_Time, Depart, Arrival)
VALUES (7, '2025-04-17 17:00:00', '2025-04-17 18:00:00', 5, 3);

    

INSERT IGNORE INTO Fare (Hop_Id, Amount, Currency, Discount, Tax, Final_Amount)
VALUES (1, 100.00, 'USD', 0.10, 0.05, 95.00);
INSERT IGNORE INTO Fare (Hop_Id, Amount, Currency, Discount, Tax, Final_Amount)
VALUES (2, 150.00, 'USD', 0.15, 0.05, 135.00);
INSERT IGNORE INTO Fare (Hop_Id, Amount, Currency, Discount, Tax, Final_Amount)
VALUES (3, 200.00, 'USD', 0.20, 0.05, 180.00);
INSERT IGNORE INTO Fare (Hop_Id, Amount, Currency, Discount, Tax, Final_Amount)
VALUES (4, 250.00, 'USD', 0.25, 0.05, 225.00);
INSERT IGNORE INTO Fare (Hop_Id, Amount, Currency, Discount, Tax, Final_Amount)
VALUES (5, 300.00, 'USD', 0.30, 0.05, 270.00);
INSERT IGNORE INTO Fare (Hop_Id, Amount, Currency, Discount, Tax, Final_Amount)
VALUES (6, 350.00, 'USD', 0.35, 0.05, 315.00);
INSERT IGNORE INTO Fare (Hop_Id, Amount, Currency, Discount, Tax, Final_Amount)
VALUES (7, 400.00, 'USD', 0.40, 0.05, 360.00);



-- Associe les Flight_Trip aux Hops
INSERT IGNORE INTO Flight_Trip_Hop (Flight_Trip_Id, Hop_Id)
SELECT ft.Flight_Trip_Id, h.Hop_Id
FROM Flight_Trip ft
JOIN Hop h ON ft.Depart_Airport = h.Depart AND ft.Arrival_Airport = h.Arrival
WHERE NOT EXISTS (
    SELECT 1 
    FROM Flight_Trip_Hop fth 
    WHERE fth.Flight_Trip_Id = ft.Flight_Trip_Id AND fth.Hop_Id = h.Hop_Id
);
