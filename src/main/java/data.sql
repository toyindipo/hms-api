INSERT INTO hms.users (email, password, firstname, lastname, phone_number, role) VALUES ('doctor@mail.com', '$2a$04$yA1vnRjMHmzg/UYzU.4wIO7.bIvE.4fHNspGUyMOoFCvQff216PEu', 'Joe', 'Doctor', '080334444', 'DOCTOR');
INSERT INTO hms.users (email, password, firstname, lastname, phone_number, role) VALUES ('doctor2@mail.com', '$2a$04$yA1vnRjMHmzg/UYzU.4wIO7.bIvE.4fHNspGUyMOoFCvQff216PEu', 'John', 'Doctor', '080334444', 'DOCTOR');
INSERT INTO hms.users (email, password, firstname, lastname, phone_number, role) VALUES ('pharmacy@mail.com', '$2a$04$yA1vnRjMHmzg/UYzU.4wIO7.bIvE.4fHNspGUyMOoFCvQff216PEu', 'Doe', 'Pharmacist', '080334444', 'PHARMACY');
INSERT INTO hms.users (email, password, firstname, lastname, phone_number, role) VALUES ('lab@mail.com', '$2a$04$yA1vnRjMHmzg/UYzU.4wIO7.bIvE.4fHNspGUyMOoFCvQff216PEu', 'Peter', 'LAB', '080334444', 'LAB_TECH');
INSERT INTO hms.users (email, password, firstname, lastname, phone_number, role) VALUES ('pharmacy2@mail.com', '$2a$04$yA1vnRjMHmzg/UYzU.4wIO7.bIvE.4fHNspGUyMOoFCvQff216PEu', 'Kyle', 'Pharmacist', '080334444', 'PHARMACY');
INSERT INTO hms.users (email, password, firstname, lastname, phone_number, role) VALUES ('lab2@mail.com', '$2a$04$yA1vnRjMHmzg/UYzU.4wIO7.bIvE.4fHNspGUyMOoFCvQff216PEu', 'Ronald', 'LAB', '080334444', 'LAB_TECH');
INSERT INTO hms.users (email, password, firstname, lastname, phone_number, role) VALUES ('account@mail.com', '$2a$04$yA1vnRjMHmzg/UYzU.4wIO7.bIvE.4fHNspGUyMOoFCvQff216PEu', 'Joe', 'Account', '080334444', 'ACCOUNT');
INSERT INTO hms.users (email, password, firstname, lastname, phone_number, role) VALUES ('receptionist@mail.com', '$2a$04$yA1vnRjMHmzg/UYzU.4wIO7.bIvE.4fHNspGUyMOoFCvQff216PEu', 'Front Desk', 'Doctor', '080334444', 'FRONT_DESK');
INSERT INTO hms.users (email, password, firstname, lastname, phone_number, role) VALUES ('admin@mail.com', '$2a$04$yA1vnRjMHmzg/UYzU.4wIO7.bIvE.4fHNspGUyMOoFCvQff216PEu', 'Doe', 'Admin', '080334444', 'ADMIN');

INSERT INTO hms.drug_group(group_name, description) VALUES ('Antibiotics', 'Antibiotics');
INSERT INTO hms.drug_group(group_name, description) VALUES ('Vitamin', 'Vitamin');
INSERT INTO hms.drug_group(group_name, description) VALUES ('Analgesic', 'Analgesic');

INSERT INTO hms.drugs(drug_name, group_id, description, amount) VALUES (1, 'Amoxicillin', 'Amoxicillin', 500.00);
INSERT INTO hms.drugs(drug_name, group_id, description, amount) VALUES (1, 'Penicillin', 'Penicillin', 300.00);
INSERT INTO hms.drugs(drug_name, group_id, description, amount) VALUES (3, 'Vitamin D', 'Vitamin D', 250.00);
INSERT INTO hms.drugs(drug_name, group_id, description, amount) VALUES (2, 'Vitamin E', 'Vitamin E', 300.00);
INSERT INTO hms.drugs(drug_name, group_id, description, amount) VALUES (3, 'Paracetamol', 'Paracetamol', 50.00);
INSERT INTO hms.drugs(drug_name, group_id, description, amount) VALUES (3, 'Ibuprofen', 'Ibuprofen', 150.00);


INSERT INTO hms.drug_inventory(drug_id, count) VALUES (1, 100);
INSERT INTO hms.drug_inventory(drug_id, count) VALUES (2, 10);
INSERT INTO hms.drug_inventory(drug_id, count) VALUES (3, 50);
INSERT INTO hms.drug_inventory(drug_id, count) VALUES (4, 100);
INSERT INTO hms.drug_inventory(drug_id, count) VALUES (5, 60);
INSERT INTO hms.drug_inventory(drug_id, count) VALUES (6, 15);




