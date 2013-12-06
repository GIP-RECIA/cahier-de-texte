
--Mise à jour de cahier_type_devoir
ALTER TABLE cahier_courant.cahier_type_devoir ALTER libelle TYPE character varying(20);
ALTER TABLE cahier_courant.cahier_type_devoir ADD COLUMN id_etablissement integer;
ALTER TABLE cahier_courant.cahier_type_devoir ADD CONSTRAINT id_etablissement_type_devoir FOREIGN KEY (id_etablissement) REFERENCES cahier_courant.cahier_etablissement (id)  ON UPDATE NO ACTION ON DELETE NO ACTION;

UPDATE cahier_courant.cahier_type_devoir SET  id_etablissement=1;
ALTER TABLE cahier_courant.cahier_type_devoir ALTER COLUMN id_etablissement SET NOT NULL;

--Mise à jour de cahier_etablissement
ALTER TABLE cahier_courant.cahier_etablissement ADD COLUMN horaire_cours text;
ALTER TABLE cahier_courant.cahier_etablissement ADD COLUMN alternance_semaine text;
ALTER TABLE cahier_courant.cahier_etablissement ADD COLUMN jours_ouvres text;
ALTER TABLE cahier_courant.cahier_etablissement ADD COLUMN duree_cours integer;
ALTER TABLE cahier_courant.cahier_etablissement ADD COLUMN heure_debut integer;
ALTER TABLE cahier_courant.cahier_etablissement ADD COLUMN heure_fin integer;
ALTER TABLE cahier_courant.cahier_etablissement ADD COLUMN minute_debut integer;
ALTER TABLE cahier_courant.cahier_etablissement ADD COLUMN horaire_scindee boolean;
ALTER TABLE cahier_courant.cahier_etablissement ADD COLUMN ouverture boolean;

--Mise à jour de cahier_annee_scolaire
ALTER TABLE cahier_courant.cahier_annee_scolaire ADD COLUMN periode_vacance text;
ALTER TABLE cahier_courant.cahier_annee_scolaire ADD COLUMN ouverture_ent boolean;

--Mise à jour de cahier_etab_enseignant
ALTER TABLE cahier_courant.cahier_etab_enseignant ADD COLUMN saisie_simplifiee boolean;

--Mise à jour de cahier_enseignant_enseignement
ALTER TABLE cahier_courant.cahier_enseignant_enseignement ADD COLUMN id_etablissement integer;
ALTER TABLE cahier_courant.cahier_enseignant_enseignement ADD CONSTRAINT id_etablissement FOREIGN KEY (id_etablissement) REFERENCES cahier_courant.cahier_etablissement (id)    ON UPDATE NO ACTION ON DELETE NO ACTION;

UPDATE cahier_courant.cahier_enseignant_enseignement SET  id_etablissement=1;
ALTER TABLE cahier_courant.cahier_enseignant_enseignement ALTER COLUMN id_etablissement SET NOT NULL;

ALTER TABLE cahier_courant.cahier_enseignant_enseignement DROP CONSTRAINT id_enseignant_enseignement;
ALTER TABLE cahier_courant.cahier_enseignant_enseignement ADD CONSTRAINT id_enseignant_enseignement PRIMARY KEY (id_enseignement, id_enseignant, id_etablissement);



--Création de la table libellé enseignement
CREATE TABLE cahier_courant.cahier_libelle_enseignement
(
  id_enseignement integer NOT NULL,
  id_etablissement integer NOT NULL,
  libelle character varying(8) NOT NULL,
  CONSTRAINT id_libelle_enseignement_etab_enseig PRIMARY KEY (id_enseignement, id_etablissement),
  CONSTRAINT id_enseignement FOREIGN KEY (id_enseignement)
      REFERENCES cahier_courant.cahier_enseignement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_etablissement FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

--Création de la table cahier emploi
CREATE TABLE cahier_courant.cahier_emploi
(
  id integer NOT NULL,
  jour character varying(10) NOT NULL,
  type_semaine character(1) NOT NULL,
  heure_debut integer NOT NULL,
  heure_fin integer NOT NULL,
  minute_debut integer NOT NULL,
  minute_fin integer NOT NULL,
  id_sequence integer,
  id_enseignant integer NOT NULL,
  id_classe integer,
  id_groupe integer,
  id_enseignement integer NOT NULL,
  id_etablissement integer NOT NULL,
  description character varying(50),
 -- date_debut_valide date,
 -- date_fin_valide date,
  CONSTRAINT pimaire_id_emploi PRIMARY KEY (id),
  CONSTRAINT id_classe_emploi FOREIGN KEY (id_classe)
      REFERENCES cahier_courant.cahier_classe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignant_emploi FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignement_emploi FOREIGN KEY (id_enseignement)
      REFERENCES cahier_courant.cahier_enseignement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_groupe_emploi FOREIGN KEY (id_groupe)
      REFERENCES cahier_courant.cahier_groupe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_sequence_emploi FOREIGN KEY (id_sequence)
      REFERENCES cahier_courant.cahier_sequence (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- création de cahier_preferences_utilisateur
CREATE TABLE cahier_courant.cahier_preferences_utilisateur
(
  uid character varying(8) NOT NULL,
  preferences character varying(200),
  CONSTRAINT pk_pref PRIMARY KEY (uid)
);