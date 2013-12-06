begin;

-- Modifie la table des pieces jointes pour specifier le chemin par defaut
-- "Documents du CTN"
-- -----------------------------------------------------------------------
ALTER TABLE cahier_courant.cahier_piece_jointe 
    ALTER COLUMN chemin SET DEFAULT '/Documents du CTN';


-- Remplace dans la table des pieces jointes les noms des chemins 
-- commençant par "Mes Documents" en "Documents du CTN" 
-- --------------------------------------------------------------


update 
    cahier_courant.cahier_piece_jointe 
set 
    chemin = '/Documents du CTN' || substring(chemin from '/Mes Documents(.*)')
where 
    chemin like '/Mes Documents%'
;

commit;

