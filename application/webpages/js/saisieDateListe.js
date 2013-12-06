/**
 * Cette fonction est appelee suite à la selection d'une date dans la liste deroulante.
 * @param idValeurFinal : id de class associé au composant input qui stocke la valeur de la date choisie.
 * @param idValeurListe : id de class associé au comosant select qui représente les choix de date.
 */
function onChangeDateListe(idValeurFinal, idValeurListe) {
    var valeurFinal = jQuery("." + idValeurFinal); 
    var valeurListe = jQuery("." + idValeurListe); 
    var valeurListeSelected = ""; 
    if (valeurListe != null) {
       valeurListeSelected = valeurListe.val();
       if (valeurListeSelected != null && valeurListeSelected.length == 8) {
           valeurListeSelected = 
                   valeurListeSelected.substr(6,2) + "/" + 
                   valeurListeSelected.substr(4,2) + "/" + 
                   valeurListeSelected.substr(0,4);  
       } else {
           valeurListeSelected = "";
       }
    } else {
    }
    if (valeurFinal != null) {
       valeurFinal.val(valeurListeSelected); 
    } else {
    }
}

/**
* Cette fonction est appelee suite à la selection d'une date dans le composant rich:calendar.
* @param idValeurFinal : id de class associé au composant input qui stocke la valeur de la date choisie.
* @param idValeurListe : id de class associé au comosant rich:calendar 
*/
function onChangeDateCalendar(idValeurFinal, idValeurCalendar) {
    var valeurFinal = jQuery('.' + idValeurFinal); 
    var valeurCalendar = jQuery('.' + idValeurCalendar); 
    var valeurCalendarSelected = ""; 
    if (valeurCalendar != null) {
       valeurCalendarSelected = valeurCalendar.val();
    } else {
    }
    if (valeurFinal != null) {
       valeurFinal.val(valeurCalendarSelected); 
    } else {
    }
}
