DROP SCHEMA IF EXISTS stagify CASCADE;
CREATE SCHEMA stagify;

CREATE TABLE stagify.utilisateurs(
    id_utilisateur      SERIAL PRIMARY KEY,
    pseudo              CHARACTER VARYING(30),
    mdp                 CHARACTER(60),
    nom                 CHARACTER VARYING(50),
    prenom              CHARACTER VARYING(30),
    date_naissance      TIMESTAMP,
    tel                 CHARACTER VARYING(15), 
    email               CHARACTER VARYING(100),
    date_inscription    TIMESTAMP,
    annee_academique    CHARACTER(9),
    est_admin           BOOLEAN,
    nb_contacts         INTEGER,
    etat_plus_avance    INTEGER,
    num_version         INTEGER
);

CREATE TABLE stagify.entreprises(
    id_entreprise       SERIAL PRIMARY KEY,
    denomination        CHARACTER VARYING(50),
    adresse             CHARACTER VARYING(30),
    numero              CHARACTER VARYING(10),
    boite               CHARACTER VARYING(5),
    code_postal         CHARACTER(4),
    ville               CHARACTER VARYING(30),
    email               CHARACTER VARYING(50),
    tel                 CHARACTER VARYING(15),
    est_black_liste     BOOLEAN,
    est_supprime		BOOLEAN,
    num_version         INTEGER
);

CREATE TABLE stagify.personnes_contact(
    id_personne_contact SERIAL PRIMARY KEY,
    nom                 CHARACTER VARYING(50),
    prenom              CHARACTER VARYING(30),
    tel                 CHARACTER VARYING(15),
    email               CHARACTER VARYING(50),
    entreprise          INTEGER  REFERENCES stagify.entreprises(id_entreprise),
    num_version         INTEGER
);

CREATE TABLE stagify.contacts(
    id_contact          SERIAL PRIMARY KEY,
    utilisateur         INTEGER  REFERENCES stagify.utilisateurs(id_utilisateur),
    entreprise          INTEGER  REFERENCES stagify.entreprises(id_entreprise),
    personne_contact    INTEGER  REFERENCES stagify.personnes_contact(id_personne_contact),
    etat                INTEGER,
    annee_academique    CHARACTER(9),
    num_version         INTEGER
);

CREATE TABLE stagify.stages(
    id_stage            SERIAL PRIMARY KEY,
    annee_academique    CHARACTER(9),
    date_signature      TIMESTAMP,
    adresse             CHARACTER VARYING(30),
    numero              CHARACTER VARYING(10),
    boite               CHARACTER VARYING(5),
    code_postal         CHARACTER(4),
    ville               CHARACTER VARYING(30),
    responsable         INTEGER  REFERENCES stagify.personnes_contact(id_personne_contact),
    entreprise          INTEGER  REFERENCES stagify.entreprises(id_entreprise),
    utilisateur         INTEGER  REFERENCES stagify.utilisateurs(id_utilisateur),
    num_version         INTEGER
);

-- UTILISATEURS
-- bri ; Admin;10.
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('bri', '$2a$10$MKZg2YfqxLyICMfAoyF3kOSgorfyIVUC7.Typlurmz.QzBLiZMt02', 'Lehmann', 'Brigitte', '1975-01-01 00:00:00', '+32495352104', 'brigitte.lehmann@vinci.be', '2018-02-28 18:00:00', '0000-0000', 't', 0, 0, 1);
-- lau ; Admin;10.
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('lau', '$2a$10$MKZg2YfqxLyICMfAoyF3kOSgorfyIVUC7.Typlurmz.QzBLiZMt02', 'Leleux', 'Laurent', '1990-01-01 00:00:00', '+32495673210', 'laurent.leleux@vinci.be', '2018-02-28 18:00:05', '0000-0000', 't', 0, 0, 1);
-- don ; Admin;10.
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('don', '$2a$10$MKZg2YfqxLyICMfAoyF3kOSgorfyIVUC7.Typlurmz.QzBLiZMt02', 'Grolaux', 'Donatien', '1985-01-01 00:00:00', '+32497524731', 'donatien.grolaux@vinci.be', '2018-02-28 18:00:05', '0000-0000', 't', 0, 0, 1);
-- Achille ; mdpuser
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('Achille', '$2a$10$AvfajQ3KJrIwtaRL893v6eVmNKqCTjpg1wKU8DiwsLnN5wUKW.Iz6', 'Talon', 'Achille', '1995-07-08 00:00:00', '+32498643411', 'achille.talon@student.vinci.be', '2018-03-01 15:45:05', '2017-2018', 'f', 1, 0, 1);
-- Basile ; mdpuser
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('Basile', '$2a$10$AvfajQ3KJrIwtaRL893v6eVmNKqCTjpg1wKU8DiwsLnN5wUKW.Iz6', 'Lasile', 'Basile', '1998-04-02 00:00:00', '+32471613482', 'basile.lasile@student.vinci.be', '2018-03-01 16:00:00', '2017-2018', 'f', 0, 0, 1);
-- Caroline ; mdpuser
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('Caroline', '$2a$10$AvfajQ3KJrIwtaRL893v6eVmNKqCTjpg1wKU8DiwsLnN5wUKW.Iz6', 'Smith', 'Caroline', '1999-06-06 00:00:00', '+32495673104', 'caroline.smith@student.vinci.be', '2018-03-01 16:01:01', '2017-2018', 'f', 3, 0, 1);
-- student2016 ; mdpuser
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('student2016', '$2a$10$AvfajQ3KJrIwtaRL893v6eVmNKqCTjpg1wKU8DiwsLnN5wUKW.Iz6', 'Student', 'DeuxMilleSeize', '1999-06-06 00:00:00', '+32495673104', 'student.2016@student.vinci.be', '2017-03-01 16:01:01', '2016-2017', 'f', 1, 4, 2);
