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
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('achille', '$2a$10$AvfajQ3KJrIwtaRL893v6eVmNKqCTjpg1wKU8DiwsLnN5wUKW.Iz6', 'Legros', 'Achille', '1996-10-13 00:00:00', '+32498643411', 'Achille.legros@student.vinci.be', '2015-03-01 15:45:05', '2014-2015', 'f', 1, 4, 1);
-- Basile ; mdpuser
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('basile', '$2a$10$AvfajQ3KJrIwtaRL893v6eVmNKqCTjpg1wKU8DiwsLnN5wUKW.Iz6', 'Lemaigre', 'Basile', '1995-10-12 00:00:00', '+32471613482', 'basile@student.vinci.be', '2016-03-01 16:00:00', '2015-2016', 'f', 1, 4, 1);
-- Caroline ; mdpuser
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('caro', '$2a$10$AvfajQ3KJrIwtaRL893v6eVmNKqCTjpg1wKU8DiwsLnN5wUKW.Iz6', 'Lalongue', 'Caroline', '1999-11-14 00:00:00', '+32495673104', 'Caro.lal@student.vinci.be', '2018-03-01 16:01:01', '2017-2018', 'f', 3, 2, 1);
-- student2016 ; mdpuser
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('theo', '$2a$10$AvfajQ3KJrIwtaRL893v6eVmNKqCTjpg1wKU8DiwsLnN5wUKW.Iz6', 'Legrand', 'Théophile', '1996-10-12 00:00:00', '+32495673104', 'theo.phile@student.vinci.be', '2018-03-01 16:01:01', '2017-2018', 'f', 3, 2, 1);
INSERT INTO "stagify"."utilisateurs" ("pseudo", "mdp", "nom", "prenom", "date_naissance", "tel", "email", "date_inscription", "annee_academique", "est_admin", "nb_contacts", "etat_plus_avance", "num_version") VALUES ('theophane', '$2a$10$AvfajQ3KJrIwtaRL893v6eVmNKqCTjpg1wKU8DiwsLnN5wUKW.Iz6', 'Lebon', 'Théophane', '1997-01-10 00:00:00', '+32495673104', 'theo.phane@student.vinci.be', '2018-03-01 16:01:01', '2017-2018', 'f', 0, 0, 1);


-- ENTREPRISES
INSERT INTO "stagify"."entreprises" ("denomination", "adresse", "numero", "boite", "code_postal", "ville", "email", "tel", "est_black_liste","est_supprime", "num_version") VALUES ('Xattrapp', 'Avenue de l''Eglise', '666', NULL, '1000', 'Bruxelles', 'contact@xattrapp.be', '+3223549462', 'f','f', 1);
INSERT INTO "stagify"."entreprises" ("denomination", "adresse", "numero", "boite", "code_postal", "ville", "email", "tel", "est_black_liste","est_supprime", "num_version") VALUES ('Yggdrasil', 'Avenue de Mai', '103', NULL, '1200', 'Woluwé-Saint-Lambert', 'info@yggdrasil.be', '+3223546121', 'f','f', 1);
INSERT INTO "stagify"."entreprises" ("denomination", "adresse", "numero", "boite", "code_postal", "ville", "email", "tel", "est_black_liste","est_supprime", "num_version") VALUES ('Zebu', 'Rue Gray', '21', NULL, '1050', 'Ixelles', 'contact@zebu.be', '+3223549565', 'f','f', 1);
INSERT INTO "stagify"."entreprises" ("denomination", "adresse", "numero", "boite", "code_postal", "ville", "email", "tel", "est_black_liste","est_supprime", "num_version") VALUES ('Zepabu', 'Rue Bleu', '21', NULL, '1050', 'Ixelles', 'contact@zepabu.be', '+3223549565', 'f','f', 1);

-- PERSONNES DE CONTACT
INSERT INTO "stagify"."personnes_contact" ("nom", "prenom", "tel", "email", "entreprise", "num_version") VALUES ('Colonel', 'Marguerite', '+32495683101', 'marguerite.colonel@xattrapp.be', 1, 1);
INSERT INTO "stagify"."personnes_contact" ("nom", "prenom", "tel", "email", "entreprise", "num_version") VALUES ('Général', 'Noémie', '+32497685214', 'noemie.general@xattrapp.be', 1, 1);
INSERT INTO "stagify"."personnes_contact" ("nom", "prenom", "tel", "email", "entreprise", "num_version") VALUES ('Caporal', 'Ortense', '+32498653410', 'ortense_caporal@yggdrasil.be', 2, 1);
INSERT INTO "stagify"."personnes_contact" ("nom", "prenom", "tel", "email", "entreprise", "num_version") VALUES ('Fantassin', 'Zaiplussoif', '+32498653410', 'zf@zebu.be', 3, 1);


-- CONTACTS
INSERT INTO "stagify"."contacts" ("utilisateur", "entreprise", "personne_contact", "etat", "annee_academique", "num_version") VALUES (6, 1, 1, 2, '2017-2018', 1);
INSERT INTO "stagify"."contacts" ("utilisateur", "entreprise", "personne_contact", "etat", "annee_academique", "num_version") VALUES (6, 2, 3, 2, '2017-2018', 1);
INSERT INTO "stagify"."contacts" ("utilisateur", "entreprise", "personne_contact", "etat", "annee_academique", "num_version") VALUES (6, 3, NULL, 0, '2017-2018', 1);
INSERT INTO "stagify"."contacts" ("utilisateur", "entreprise", "personne_contact", "etat", "annee_academique", "num_version") VALUES (5, 2, NULL, 4, '2015-2016', 1);
INSERT INTO "stagify"."contacts" ("utilisateur", "entreprise", "personne_contact", "etat", "annee_academique", "num_version") VALUES (4, 2, NULL, 4, '2014-2015', 1);
INSERT INTO "stagify"."contacts" ("utilisateur", "entreprise", "personne_contact", "etat", "annee_academique", "num_version") VALUES (7, 2, NULL, 0, '2017-2018', 1);
INSERT INTO "stagify"."contacts" ("utilisateur", "entreprise", "personne_contact", "etat", "annee_academique", "num_version") VALUES (7, 1, 2, 1, '2017-2018', 1);
INSERT INTO "stagify"."contacts" ("utilisateur", "entreprise", "personne_contact", "etat", "annee_academique", "num_version") VALUES (7, 3, 4, 0, '2017-2018', 1);


-- STAGES 
INSERT INTO "stagify"."stages" ("annee_academique", "date_signature", "adresse", "numero", "boite", "code_postal", "ville", "responsable", "entreprise", "utilisateur", "num_version") VALUES ('2015-2016', '2015-04-02 16:01:01', 'Avenue de Mai', '103', NULL, '1200', 'Woluwé-Saint-Lambert', 3, 2, 5, 1);
INSERT INTO "stagify"."stages" ("annee_academique", "date_signature", "adresse", "numero", "boite", "code_postal", "ville", "responsable", "entreprise", "utilisateur", "num_version") VALUES ('2014-2015', '2014-04-02 16:01:01', 'Avenue de Mai', '103', NULL, '1200', 'Woluwé-Saint-Lambert', 3, 2, 4, 1);

