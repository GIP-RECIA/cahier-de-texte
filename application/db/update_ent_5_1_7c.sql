--  
-- Changement de gestion des couleurs partagée entre les sequences seance et case de emploie du temps
-- creation de la table couleur_enseignement_classe
-- ----------------------------------------------------------------------------------------------
BEGIN;


drop table if exists cahier_courant.cahier_couleur_enseignement_classe;


DROP SEQUENCE IF EXISTS cahier_couleur_enseignement cascade;


CREATE TABLE cahier_courant.cahier_couleur_enseignement_classe
(
  id integer NOT NULL, 
  id_etablissement integer not null,
  id_enseignant integer not null,
  id_enseignement integer not null,
  id_classe integer, 
  id_groupe integer,
  couleur varchar(10),
  CONSTRAINT id_couleur_enseignement_classe PRIMARY KEY (id ),
  constraint unique_couleur_enseignement_classe UNIQUE(id_etablissement, id_enseignant, id_enseignement, id_classe, id_groupe),
  CONSTRAINT cahier_couleur_enseignant FOREIGN KEY (id_enseignant)
      REFERENCES cahier_courant.cahier_enseignant (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_couleur_enseignement FOREIGN KEY (id_enseignement)
      REFERENCES cahier_courant.cahier_enseignement (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_couleur_classe FOREIGN KEY (id_classe)
      REFERENCES cahier_courant.cahier_classe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT cahier_couleur_groupe FOREIGN KEY (id_groupe)
      REFERENCES cahier_courant.cahier_groupe (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE cahier_couleur_enseignement MINVALUE 1;

-- mise a jour des couleurs existante
-- attention il peut avoir des doublons
insert into cahier_courant.cahier_couleur_enseignement_classe
select nextval('cahier_couleur_enseignement'),
id_etablissement,
id_enseignant,
id_enseignement,
id_classe,
id_groupe,
couleur_case
from cahier_courant.cahier_emploi;

COMMIT;

begin;
alter table cahier_courant.cahier_sequence
drop COLUMN if exists couleur_sequence ;

commit;



