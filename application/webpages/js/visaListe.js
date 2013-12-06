$(document).ready(function () {
	bindResizeFunction(resizePanel);
});

function resizePanel() {
    var div = jQuery('#formulaire_principal\\:idListeVisaPanel');
    if (div != null) {
        div.height(jQuery(window).height() - 60 );
    }
}


function chargerAgenda() {
        
        // Recupere la date du lundi de debut de la semaine selectionnee
        var stringDate = jQuery("#dateLundi").val();
        if (stringDate == null) {
            console.log("Problem null JSON object dateLundi");
            return;
        }
        var longDate = parseInt(stringDate);
        var startDate = new Date(longDate);
        console.log("START DATE = " + startDate);
        
        // Recupere et parse la liste des elements a afficher dans l'agenda
        var agendaJSON = jQuery('#agendaJSON').val();
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
                  eventClick: null,
                  dayNames: ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'],
                  select: null
                }));
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


function handleEventAddVisa(e) {
	console.log("handleEventAddVisa");
	console.log(e);
	if (e.status == "success") {
		setModificationFormulaire(true);
		resizePanel();
	}	 
}

function handleEventDeleteVisa(e) {
	console.log("handleEventDeleteVisa");
	console.log(e);
	if (e.status == "success") {
		setModificationFormulaire(true);
		resizePanel();
	}	 
}