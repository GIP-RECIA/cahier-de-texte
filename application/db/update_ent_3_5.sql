-- Chapitre EP Cahier de texte 1.1.1.3: rendre le type de devoir factultatif
-- Mise à jour de la table cahier_devoir pour supprimer le not null 
-- sur la colonne id_type_devoir 
ALTER TABLE cahier_courant.cahier_devoir ALTER COLUMN id_type_devoir DROP NOT NULL;


--Modification de la table pièce jointe
ALTER TABLE cahier_courant.cahier_piece_jointe ADD COLUMN chemin character varying(250);
ALTER TABLE cahier_courant.cahier_piece_jointe ALTER COLUMN chemin SET STORAGE EXTENDED;
ALTER TABLE cahier_courant.cahier_piece_jointe ALTER COLUMN chemin SET NOT NULL;
ALTER TABLE cahier_courant.cahier_piece_jointe ALTER COLUMN chemin SET DEFAULT '/Mes Documents'::character varying;

--Mofification cahier_courant enseignant pour stockage
ALTER TABLE cahier_courant.cahier_enseignant ADD COLUMN depot_stockage character varying(50);


--Création de la table inspecteur
CREATE TABLE cahier_courant.cahier_inspecteur
(
  id Integer NOT NULL,
  nom character varying(50) NOT NULL,
  prenom character varying(50) NOT NULL,
  uid character varying(8) NOT NULL,
  civilite character varying(10),
  CONSTRAINT id_cahier_inspecteur PRIMARY KEY (id)
);


--Création de la table ouverture inspecteur
CREATE TABLE cahier_courant.cahier_ouverture_inspecteur
(
  id_etablissement integer NOT NULL,
  id_enseignant integer NOT NULL,
  id_inspecteur integer NOT NULL,
  date date NOT NULL,
  nomDirecteur character varying(100) NOT NULL,
  uidDirecteur character varying(8) NOT NULL,
  CONSTRAINT id_ouverture_inspecteur PRIMARY KEY (id_etablissement, id_enseignant, id_inspecteur),
  CONSTRAINT id_etablissement FOREIGN KEY (id_etablissement)
      REFERENCES cahier_courant.cahier_etablissement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT id_inspecteur FOREIGN KEY (id_inspecteur)
      REFERENCES cahier_courant.cahier_inspecteur (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION            
);
