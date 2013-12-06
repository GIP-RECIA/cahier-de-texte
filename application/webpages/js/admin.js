jQuery(document).ready(function () {
	console.log("Admin doc ready");
  reloadAgenda();
});

function changeTitreOnTab(outil) {
  var titre = '';
  if (outil != 'ANNEE_SCOLAIRE') {
    titre = "Administration pour l'établissement #{admin.form.libelleEtab}";
  } else {
    titre = "Administration globale du cahier de texte";
  }
  jQuery('#formulaire_principal\\:admincahier_header').innerHTML = titre;
}

function reloadAgenda() {
  var modificationAutorise = jQuery('#calendarData').data("modificationAutorise");
  var horairesCoursJSON = jQuery.parseJSON($('#calendarData').data("horairesCoursJSON"));
  
  logFormat("reloadAgenda {0} {1}", modificationAutorise, horairesCoursJSON);
  
  if (horairesCoursJSON != null) {
    chargerAgenda(horairesCoursJSON, modificationAutorise);
  }
}

function chargerAgenda(horairesCoursJSON, modifiable) {

  var d = 5;
  var m = 2;
  var y = 2012;

  var date = new Date(y, m, d);

  if (horairesCoursJSON.length > 0) {
    var minTimeData = horairesCoursJSON[0].heureDebut;
    var maxTimeData = horairesCoursJSON[horairesCoursJSON.length - 1].heureFin + 1;
  }

  var minTimeData = parseInt(jQuery('#etabHeureDebut').val());
  var maxTimeData = parseInt(jQuery('#etabHeureFin').val()) + 1;

  logFormat("chargerAgenda min {0} max {1} modifiable {2}", minTimeData, maxTimeData, modifiable);

  var grilleEvents = [];

  jQuery.each(horairesCoursJSON, function (index, value) {
    console.log(index + ': ' + value.heureDebut + ' ' + value.minuteDebut + ' ' + value.heureFin);
    var startTime = new Time(value.heureDebut, value.minuteDebut);
    var endTime = new Time(value.heureFin, value.minuteFin);
    grilleEvents.push({
      title: value.titre,
      start: new Date(y, m, d, value.heureDebut, value.minuteDebut),
      end: new Date(y, m, d, value.heureFin, value.minuteFin),
      allDay: false,
      className: 'grilleEvent',
      backgroundColor: 'grey',
      index: index
    });
  });

  // Definition de l'evenement onclick si la grille est modifiable
  var clickFunction = null;
  if (modifiable) {
    logFormat('Definition de la fonction onclick.  modifiable {0}', modifiable);
    clickFunction = function (calEvent, jsEvent, view) {
      console.log('afficherPlageSelected');
      afficherPlagePopup(calEvent.index);
    }
  }

  // Definition de l'evenement Resize si la grille est modifiable
  var resizeFunction = null;
  if (modifiable) {
    resizeFunction = function (event, dayDelta, minuteDelta, revertFunc) {
      setPlageSelectedIndex(event.index);
      setModificationFormulaire(true);
      affecterPlageSelected(event.start.getHours(), event.start.getMinutes(), event.end.getHours(), event.end.getMinutes());
    };
  }

  // Definition de l'événement Drop si la grille est modifiable 
  var dropFunction = null;
  if (modifiable) {
    dropFunction = function (event, dayDelta, minuteDelta, allDay, revertFunc) {
      setModificationFormulaire(true);
      setPlageSelectedIndex(event.index);
      affecterPlageSelected(event.start.getHours(), event.start.getMinutes(), event.end.getHours(), event.end.getMinutes());
      
      rerenderAgenda();
    };
  }

  //Déclare pour pouvoir utiliser dans selectFunction
  var calendarGrille = null;

  var selectFunction = null;
  if (modifiable) {  	
  	var selectFunction = function (debut, fin, allDay, jsEvent, view) {

      var dureeCours = parseInt(jQuery('#calendarData').data("dureeCours"));

      logFormat("Calendar::select {0} fin {1}.  Durée {2}", debut, fin, dureeCours);

      var evenementDebut = new Time(debut);
      var evenementFin = evenementDebut.ajoute(dureeCours);

      logFormat("Création d'un évènement avec debut {0} / fin {1} ", evenementDebut.toString(), evenementFin.toString());

      //Créer une case en mettant le prochain index
      setPlageSelectedIndex(grilleEvents.length);

      //Mettre les valeurs
      affecterPlageSelected(evenementDebut.heure, evenementDebut.minute, evenementFin.heure, evenementFin.minute);

      afficherPlagePopup(grilleEvents.length);
      
      //jQuery('.showPopupEvent').click();

      setModificationFormulaire(true);

    };
  }
  jQuery('div.calendarGrille').html('');
  calendarGrille = jQuery('div.calendarGrille').fullCalendar(getAgendaConfiguration({

    columnFormat: {
      month: 'ddd',
      // Mon
      week: 'dddd',
      // Mon 9/7
      day: ''
    },
    minTime: minTimeData,
    maxTime: maxTimeData,
    defaultView: 'agendaDay',
    year: y,
    month: m,
    date: d,
    height: 600,
    weekends: false,
    selectable: true,
    editable: modifiable,
    selectHelper: true,
    eventClick: clickFunction,
    eventResize: resizeFunction,
    eventDrop: dropFunction,
    select: selectFunction,
    events: grilleEvents
  }));

  var resizeFunc = function () {
      jQuery(".calendarGrille").fullCalendar('option', 'height', jQuery(window).height() - 217);
      jQuery('div.calendarGrille').fullCalendar('option', 'height', jQuery(window).height() - 217);
    };

  bindResizeFunction(resizeFunc);

}