jQuery(document).ready(function () {
  chargerAgenda();
});

var taillePaneauSeancePrecedente = 0;

function handleResizeSaisirSeance() {
    resizePanelSeance();
}

function chargerAgenda() {
    
    // Recupere la date du lundi de debut de la semaine selectionnee
    var stringDate = jQuery("#dateLundi").val();
    if (stringDate == null) {
        console.log("Problem null JSON object dateLundi");
        return;
    }
//    var longDate = parseInt(stringDate);
    var startDate = new Date(stringDate);
    console.log("stringDate = "+ stringDate  + " START DATE = " + startDate);
    
    // Recupere et parse la liste des elements a afficher dans l'agenda
    // on a decouper le json car jQuery(..).val() tronque avec Chrome. 
    var agendaJSON = jQuery('#agendaJSON').val();
    agendaJSON += jQuery('#agendaJSON2').val();
    agendaJSON += jQuery('#agendaJSON3').val();
    console.log("agendaJSON = " + agendaJSON);
    var listeAgendaJSON = jQuery.parseJSON(agendaJSON);
    if (listeAgendaJSON == null) {
        console.log("Problem null JSON object agendaJSON");
        return;
    }
    
    // Recupere la grille horaire
    var tmpJSON = jQuery('#horairesJSON').val();
    var horairesJSON = jQuery.parseJSON(tmpJSON);
    
    var year = startDate.getFullYear();
    var month = startDate.getMonth();
    var date = startDate.getDate();
    
    console.log("startDate = " + startDate);
    
    var etablissementJSON = jQuery.parseJSON(jQuery("#etablissementJSON").val());
    
    var minTimeData = etablissementJSON.heureDebut;
    var maxTimeData = etablissementJSON.heureFin + 1;
    
    var weekends = calcShowWeekend(etablissementJSON); 
    
    // Cacule les cases de l'agenda
    var agendaEvents = getListeEventFromAgenda(startDate, listeAgendaJSON);
    
    jQuery(".calendar").html("");
    
    // Cree l'objet agenda
    var calendar = jQuery(".calendar").fullCalendar(

            getAgendaConfiguration({
              minTime: minTimeData,
              maxTime: maxTimeData,
              weekends: weekends,
              year: year,
              month: month,
              date: date,
              selectable: true,
              editable: false,
              selectHelper: true,
              slotMinutes: 30,
              height:null,
              contentHeight:null,
              aspectRatio:0.5,
              timeFormat: "",
              columnFormat: {
                  month: 'ddd',
                  // Mon
                  week: 'dddd<br/>dd/MM',
                  // Mon 9/7
                  day: 'dddd M/d' // Monday 9/7
              },
              events: agendaEvents,
              eventAfterRender: eventAfterRenderSaisirSeance,
              eventClick: clickFunctionSaisirSeance,
              dayNames: ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'],
              select: selectSaisirSeance

            }));
    
    
      
   bindResizeFunction(handleResizeSaisirSeance);
      
}

/** 
 * Declenche lors du changement d'onglet 
 * @return 
 */
function onchangeOnglet() {
     resizePanelSeance();
}

/**
 * Positionne le scroll de la div au debut de la suite des seance precedente.
 * la taille est stockee dans taillePaneauSeancePrecedente par la fonction stockerTailleScrollSeancePrecedente.
 */
function scrollSuiteSeance() {
    
    var div = jQuery('#formulaire_principal\\:idListeDesSeancePrecedente');
    if (div != null) {
        div.scrollTop(taillePaneauSeancePrecedente);
    }    
}

/**
 * Appelle lors du click sur le bouton d'affichage des seance precedente suivantes.
 * Stcoke dans la var la taille courante du paneau pour pouvoir ensuite faire 
 * le bon positionnement.
 */
function stockerTailleScrollSeancePrecedente() {
    var div = jQuery('#formulaire_principal\\:contentSeancePrecedente');
    if (div != null) {
        taillePaneauSeancePrecedente = div.height();
    } else {
        taillePaneauSeancePrecedente = 0;
    }
}


 /**
  * Retaille les div suite a une action.
  * @return
  */
function resizePanelSeance() {
      $(".calendar").fullCalendar('option', 'height', $(window).height() - 120);
      $('div.calendar').fullCalendar('option', 'height', $(window).height() - 120);
      
      var div = jQuery('#formulaire_principal\\:idListeDesSeancePrecedente');
      if (div != null) {
          div.height(jQuery(window).height() - 120 -54 -32);
      }
      div = jQuery('#formulaire_principal\\:idListeDesDevoirs');
      if (div != null) {
          div.height(jQuery(window).height() - 120 -54 -40);
      }
      div = jQuery('#formulaire_principal\\:idDivTabSeance');
      if (div != null) {
          div.height(jQuery(window).height() - 120 -54);
      }
}
 
  
  
/**
 * Fonction qui applique le tool tip sur un evenemtn de l'agenda 
 * @param event
 * @param element
 * @param vie
 * @return
 */
function eventAfterRenderSaisirSeance(event, element, vie) {
    var title = event.description.replace(/\n/g, "<br/>");
    
    var jqElement = element;
    jqElement.qtip({
    position: {
        my: 'top left',  // Position my top left...
        at: 'bottom right', // at the bottom right of...
        target: 'mouse',
        adjust: {x: 10,y: 10 }        
    },
    content: {
            text: title
        }
  });
}
 
 /**
  * Declenche l'affichage du detail de la plage pour une edition.
  * @param plageAgenda 
  * @return 
  */
 function functionSaisirSeance(index) {
     var events = jQuery(".calendar").fullCalendar('clientEvents');
     calEvent = events[index]; 
     
     /** Edition d'une seance */ 
     if (calEvent.plageAgenda.seance != null && (calEvent.plageAgenda.seance.id != null) ) { 
         afficherSeanceSelected(calEvent.index);
         setModificationFormulaire(false);
     } else {
         
         /** Clique sur une case EDT sans seance pour laquelle il y a plusieurs sequence possible */
         if (calEvent.plageAgenda.afficheSelectionSequence == true) {
             afficherSelectionSequence(calEvent.index);    
             
         /**
             * Clique sur une case EDT sans seance pour laquelle il existe une
             * seulle sequence possible
             */
         } else {
             afficherNouvelleSeance(calEvent.index);
             setModificationFormulaire(false);
         }
     }     
 }
 
 /**
  * Methode appelee lors du clique sur une case de l'agenda.
  * @param calEvent
  * @param jsEvent
  * @param view
  * @return
  */
 function clickFunctionSaisirSeance (calEvent, jsEvent, view) {
     
	 logMessage("clickFunctionSaisirSeance");
     /** Si modification en cours, 
      * il faut afficher la confirmation avant de continuer.
      * On stocke l'id de la plage pour pouvoir pour pouvoir pousuivre ensuite sur cette case. 
      */
     if (getModificationFormulaire()) {
         
    	 logMessage("clickFunctionSaisirSeance modification formulaire");
    	 
         // Affecte l'indice de la case selectionnée
         var index = calEvent.index;
         
         var spanLienNavigation = $('#idLienNavigationPopupConfirmSauvegardeModification');
         spanLienNavigation.attr('title', 'idLienClickSurPlage');
         
         var spanOngletNavigation = $('#idOngletNavigationPopupConfirmSauvegardeModification');
         spanOngletNavigation.attr('title', null);
         
         var spanRFNavigation = $('#idRFPopupConfirmSauvegardeModification');
         spanRFNavigation.attr('title', null);
         
         var spanMenuNavigation = $('#idPopupNavigationMenu');
         spanMenuNavigation.attr('title', null);
         
         clickSurPlage(index);
         RichFaces.$('formulaire_principal:idPopupConfirmSauvegardeModification').show();
         return false;
     }
      
     logMessage("clickFunctionSaisirSeance appelle functionSaisirSeance {0}", calEvent.index);
     functionSaisirSeance(calEvent.index);
 }

/**
 * Methode appelee lors du selection (+double click) sur une case complement vide.
 * @param debut
 * @param fin
 * @param allDay
 * @param jsEvent
 * @param view
 * @return
 */
function selectSaisirSeance(debut, fin, allDay, jsEvent, view) {

    var calendar = jQuery(".calendar");
    
    //Enlève la sélection peinte sur l'écran 
    calendar.fullCalendar('unselect');

    attendrePourDoubleClique(jQuery(".calendar")[0], function () {

      // Recupere la grille horaire
      var tmpJSON = jQuery("#horairesJSON").val();
      var horairesJSON = jQuery.parseJSON(tmpJSON);
        
      var evenementDate = new Date(debut);
      var evenementDebut = new Time(debut);
      var evenementFin = new Time(fin);

      //Pour une clique sans tirage, utilise la grille horaire
      if (evenementFin.diff(evenementDebut) == 15) {
      	var grilleEvenementDebut = trouverPlusProcheGrille(horairesJSON, evenementDebut, true);
      	var grilleEvenementFin = trouverPlusProcheGrille(horairesJSON, evenementFin, false, grilleEvenementDebut);

      	//Mettre les résultats dans début et fin
      	grilleEvenementDebut.setMinuteHeure(debut);
      	grilleEvenementFin.setMinuteHeure(fin);
      }

      console.log("Création d'un évènement sur debut " + grilleEvenementDebut + " / fin" + grilleEvenementFin);

      var stringDate = Date.parse(evenementDate);
      
      if (getModificationFormulaire()) {
          
          // Stocke dans le form les info necessaire a poursuivre apres la confirmation de sauvegarde
          conserverInfoNouvelleSeancelibre(stringDate, debut.getHours(), debut.getMinutes(), fin.getHours(), fin.getMinutes());
          
          // Prepare le lien pour la popup de confirmation et affiche la popup 
          var spanLienNavigation = $('#idLienNavigationPopupConfirmSauvegardeModification');
          spanLienNavigation.attr('title', 'idLienClickSurPlageVide');
          
          var spanOngletNavigation = $('#idOngletNavigationPopupConfirmSauvegardeModification');
          spanOngletNavigation.attr('title', null);
          
          var spanMenuNavigation = $('#idPopupNavigationMenu');
          spanMenuNavigation.attr('title', null);
          
          var spanRFNavigation = $('#idRFPopupConfirmSauvegardeModification');
          spanRFNavigation.attr('title', null);
          return false;
      }
      
      // Pas de modification en cours
      preparerNouvelleSeancelibre(stringDate, debut.getHours(), debut.getMinutes(), fin.getHours(), fin.getMinutes());
    });
}

// Traitement de nouvelle seance sur case vide 
function preparerNouvelleSeancelibre(stringDate, heureDebut, minuteDebut, heureFin, minuteFin) {
    ajouterNouvelleSeanceLibre();
    afficherNouvelleSeanceLibre(stringDate, heureDebut, minuteDebut, heureFin, minuteFin);
}

/**
 * Construit la liste des Events utilises par le composant GoogleAgenda 
 * à partir de la liste agenda. 
 * @param dateLundi : date du lundi de début de semaine.
 * @param listeAgenda : liste d'elementde type AgendaSeanceDTO : seance ou EDT
 * @return une liste d'Event
 */
function getListeEventFromAgenda(dateLundi, listeAgenda) {
    
    // Initialisation
    var agendaEvents = [];

    // Boucle sur chaque element de la liste
    jQuery.each(listeAgenda, function (index, value) {
        console.log('listeAgenda' + index + ': ' + value);
        
        // Initialise les attributs de la case
        var title = '';
        var start = null;
        var end = null;
        var className = '';
        var index = index;
        var aDate = new Date(value.date);
        var description = value.description;

        var borderColor = value.couleurBordure;
        var backgroundColor = value.couleurCase;
        var textColor = value.couleurTexte;
        var selected = value.selected;
        
        var classSelected = ""
        if (selected == true) {
            classSelected = "Selected";
        }
        
        // Charge une seance
        if (value.seance != null) {
        	
        	title = value.seance.sequenceDTO.groupesClassesDTO.designation;
                                             
            console.log(value.seance.sequenceDTO);      
            start = new Date(aDate.getFullYear(), aDate.getMonth(), aDate.getDate(), value.seance.heureDebut, value.seance.minuteDebut);
            end   = new Date(aDate.getFullYear(), aDate.getMonth(), aDate.getDate(), value.seance.heureFin, value.seance.minuteFin);
            if (value.detail != null) { 
                className = 'saisirSeance' + classSelected + 'Event cahierEventIndex' + index;
            } else {
                className = 'saisirSeance' + classSelected + 'EventLibre cahierEventIndex' + index;
                description += "<br/>__________________________<br/>(Séance hors emploi du temps)";
            }
            afficheSelectionSequence = false;
     
        // Charge une case EDT sans seance correspondante
        } else {
            title = value.detail.groupeOuClasse.designation;
            start = new Date(aDate.getFullYear(), aDate.getMonth(), aDate.getDate(), value.detail.heureDebut, value.detail.minuteDebut);
            end   = new Date(aDate.getFullYear(), aDate.getMonth(), aDate.getDate(), value.detail.heureFin, value.detail.minuteFin);
            className = 'saisirSeance' + classSelected + 'EventVide cahierEventIndex' + index;
            afficheSelectionSequence = value.afficheSelectionSequence;
        }
        
        agendaEvents.push({
            index: index,
            title: title,
            allDay: false,
            start: start,
            end: end,
            className: className,
            borderColor: borderColor,
            backgroundColor: backgroundColor,
            textColor: textColor,
            description: description,
            plageAgenda: value, 
            afficheSelectionSequence: afficheSelectionSequence
        });
        
        
    });
    console.log('agendaEvents : ' + agendaEvents);
    return agendaEvents;
 }
 
 function basculeAffichageAgenda() {
	 console.log("basculeAffichageAgenda"); 
   var objectAgenda = jQuery('#idAgenda');
   var isHidden = !objectAgenda.is(":visible");
   objectAgenda.toggle();
     
     var objectIconeAgenda = jQuery('#formulaire_principal\\:idIconeMasqueAgenda');
     
     var motif;
     var repla;
     if (isHidden) {
         motif = "arrowRight.png";
         repla = "arrowLeft.png";
     } else {
         motif = "arrowLeft.png";
         repla = "arrowRight.png";
     }
     var reg=new RegExp("(" + motif + ")", "g");
     var srcOrigine = objectIconeAgenda.attr("src");
     var scrNouveau = srcOrigine.replace(reg,repla);
     objectIconeAgenda.attr("src", scrNouveau);
 }


 /**
  * 
  * @param horairesCoursJSON
  * @param unTemps un temps "brute" ; Type "Time"
  * @param estDebut accroche ver un début d'un crénau, faix si c'est la fin
  * @param minTemps un sol, le temps retourné sera égal ou plus d'unTemps
  * @returns
  */
 function trouverPlusProcheGrille(horairesCoursJSON, unTemps, estDebut, minTemps) {
   var closestTime = null;
   var closestDiff = 24 * 60;
   
   logFormat("Un temps {0}  min {1}", unTemps, unTemps.toMinutes());

   jQuery.each(horairesCoursJSON, function (index, value) {
     console.log(index + ': ' + value.heureDebut + ' ' + value.minuteDebut + ' ');
     var startTime = new Time(value.heureDebut, value.minuteDebut);
     var endTime = new Time(value.heureFin, value.minuteFin);

     logFormat("Start {0} {1} End {2} {3}", startTime, startTime.toMinutes(), endTime, endTime.toMinutes());
     var f = function (time) {
         var diffTime = Math.abs(time.diff(unTemps));
         //console.log("Créneau Time : " + time + " Time: " + unTemps + " Diff " + diffTime + " " + time.diff(unTemps));
         if (diffTime < closestDiff && (minTemps == null || time.plusQue(minTemps))) {
           closestTime = time;
           closestDiff = diffTime;
           //console.log("Affectation time " + time);
         }
       }
     
     //Si unTemps est englobé par un crénau, sélectionne le
     if (unTemps.toMinutes() >= startTime.toMinutes() && 
             unTemps.toMinutes() <= endTime.toMinutes() ) {
         closestTime = estDebut ? startTime : endTime;
         return false;
     }

     if (estDebut) {
         f(startTime);
     } else {
         f(endTime);
     }
     
     



   });

   console.log(closestTime);
   return closestTime;
 }
  
/**
 * Selectionne l'onglet choisi dans le tab d'edition de la seance
 * @param nomOnglet : nom de l'onglet
 * @return rien du tout
 */ 
function selectionnerOnglet(nomOnglet) {
    var rfTabPanel = RichFaces.$('formulaire_principal:idtabPanelDetailJour');
    if (rfTabPanel != null) {
        rfTabPanel.switchToItem(nomOnglet);
    } 
}