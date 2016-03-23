-- Mise à jour sur la table année scolaire
-- Ajout du champs periode scolaire
ALTER TABLE cahier_courant.cahier_annee_scolaire ADD COLUMN periode_scolaire text;

-- Mise à jour sur la table établissement
-- Modification de la taille du champs désignation
ALTER TABLE cahier_courant.cahier_etablissement ALTER designation TYPE character varying(100);

-- Ajout du champs import
ALTER TABLE cahier_courant.cahier_etablissement ADD COLUMN import Boolean NOT NULL DEFAULT false;

-- Ajout du champs date_import
ALTER TABLE cahier_courant.cahier_etablissement ADD COLUMN date_import character varying(15);

-- Mise à jour sur la table cahier_emploi
-- Ajout du champs couleur_case
ALTER TABLE cahier_courant.cahier_emploi ADD COLUMN couleur_case character varying(10);

-- Ajout du champs code_salle
ALTER TABLE cahier_courant.cahier_emploi ADD COLUMN code_salle character varying(15);