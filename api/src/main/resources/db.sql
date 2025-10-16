DROP DATABASE IF EXISTS BabyfootDB;
CREATE DATABASE BabyfootDB
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE BabyfootDB;


CREATE TABLE user_babyfoot (
                               id_user INT AUTO_INCREMENT PRIMARY KEY,
                               name VARCHAR(100) NOT NULL,
                               surname VARCHAR(100) NOT NULL,
                               mail VARCHAR(150) NOT NULL UNIQUE,
                               password_user VARCHAR(255) NOT NULL
);


CREATE TABLE babyfoot (
                          id_babyfoot INT AUTO_INCREMENT PRIMARY KEY,
                          place VARCHAR(100),
                          statut_babyfoot VARCHAR(50) NOT NULL DEFAULT 'free',
                          usable BOOLEAN NOT NULL DEFAULT TRUE,

                          CONSTRAINT fk_booking_babyfoot
                              FOREIGN KEY (id_babyfoot)
                                  REFERENCES babyfoot(id_babyfoot)
                                  ON DELETE CASCADE
                                  ON UPDATE CASCADE
);


CREATE TABLE booking (
                         id_booking INT AUTO_INCREMENT PRIMARY KEY,
                         statut_booking VARCHAR(50) NOT NULL,
                         date_booking DATETIME NOT NULL,
                         id_user INT NOT NULL,
                         id_babyfoot INT NOT NULL,

                         CONSTRAINT fk_booking_user
                             FOREIGN KEY (id_user)
                                 REFERENCES user_babyfoot(id_user)
                                 ON DELETE CASCADE
                                 ON UPDATE CASCADE
);

CREATE TABLE tournament (
                            id_tournament INT AUTO_INCREMENT PRIMARY KEY,
                            date_tournament DATETIME NOT NULL,


                            CONSTRAINT fk_tournament_users
                                FOREIGN KEY (id_user)
                                    REFERENCES user_babyfoot(id_user)
                                    ON DELETE CASCADE
                                    ON UPDATE CASCADE
);

CREATE TABLE repairs (
                         id_reparation INT AUTO_INCREMENT PRIMARY KEY,
                         status_babyfoot VARCHAR(100),
                         start_date_repairs DATETIME NOT NULL,
                         end_date_repairs DATETIME NOT NULL,

                         CONSTRAINT fk_booking_babyfoot
                             FOREIGN KEY (id_babyfoot)
                                 REFERENCES babyfoot(id_babyfoot)
                                 ON DELETE CASCADE
                                 ON UPDATE CASCADE
);