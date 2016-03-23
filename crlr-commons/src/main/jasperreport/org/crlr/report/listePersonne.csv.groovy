# Nom;Prénom;Age
<% listePersonne.each { personne ->
out << personne.nom << ";" << personne.prenom << ";" << personne.age << "\n"
} %>