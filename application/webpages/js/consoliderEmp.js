function confirmationChange() {
  if ($('inputModification').value == 'true') {
    if (confirm('Vous n\'avez pas sauvegardé les modifications de la semaine en cours, vous allez les perdre. Etes vous sûr de vouloir continuer ?')) {
      $('inputModification').value = 'false';
      return true;
    } else return false;
  } else {
    return true;
  }
}


$(document).ready(function () {
  chargerAgenda();
});


function chargerAgenda() {
  chargerAgendaImpl("listeEmploiDeTempsJSON", ".calendar");
}

/**
 * @param emploiDeTempsDTO JSON de un emploi de temps DTO
 * @returns Le titre à afficher dans les cases
 */
function getTitreFromDTO(emploiDeTempsDTO) {
  if (!emploiDeTempsDTO) {
    console.log("Erreur dans DTO: " + emploiDeTempsDTO);
    return "";
  }

  var retour = "";

  if (emploiDeTempsDTO.matiere.libellePerso) {
    retour += emploiDeTempsDTO.matiere.libellePerso + '\n';
  } else if (emploiDeTempsDTO.matiere.intitule) {
    retour += emploiDeTempsDTO.matiere.intitule + '\n';
  }

  if (emploiDeTempsDTO.civiliteNomPrenom) {
    retour += emploiDeTempsDTO.civiliteNomPrenom + '\n';
  }

  if (emploiDeTempsDTO.groupeOuClasse && emploiDeTempsDTO.groupeOuClasse.designation) {
    retour += emploiDeTempsDTO.groupeOuClasse.designation + '\n'
  }

  if (emploiDeTempsDTO.codeSalle) {
    retour += '\nSalle ' + emploiDeTempsDTO.codeSalle + '\n';
  }

  retour += emploiDeTempsDTO.description ? emploiDeTempsDTO.description : '';

  return retour;
}

function chargerAgendaImpl(jsonElemNom, calElemNom) {

  console.log('chargerAgendaImpl ' + jsonElemNom + "  " + calElemNom);

  var listeEmploiDeTempsJSON = jQuery.parseJSON(jQuery('#listeEmploiDeTempsJSON').val());

  var etablissementJSON = jQuery.parseJSON($('#calendarData').data("etablissementJSON"));
  var minTimeData = etablissementJSON.heureDebut;
  var maxTimeData = etablissementJSON.heureFin + 1;
  
  var weekends = calcShowWeekend(etablissementJSON); 

  console.log("MinTime: " + minTimeData);
  console.log("MaxTime: " + maxTimeData);

  if (listeEmploiDeTempsJSON == null) {
    console.log("Problem null JSON object: " + jsonElemNom);
    return;
  }

  //Ce date represente un lundi
  var startDate = new Date(2012, 2, 5);

  console.log("Num events : " + listeEmploiDeTempsJSON.length);

  var agendaEvents = transformerJSONenEvents(startDate, listeEmploiDeTempsJSON);

  var calendar = jQuery(calElemNom).fullCalendar(

  getAgendaConfiguration({
    date: startDate.getDate(),
    year: startDate.getFullYear(),
    month: startDate.getMonth(),
    minTime: minTimeData,
    maxTime: maxTimeData,
    weekends: weekends,
    editable: false,
    selectHelper: true,
    selectable: false,
    eventClick: function (calEvent, jsEvent, view) {

      attendrePourDoubleClique(jsEvent.target, function () {
        console.log('Event: ' + calEvent.title);
        console.log('Start: ' + calEvent.start);
        console.log('Stop: ' + calEvent.end);
        console.log('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
        console.log('View: ' + view.name);


        afficherEventPopup(calEvent.index);


      });


    },
    events: agendaEvents
  }));

  $(window).resize(function() {
    logFormat("window height {0}", $(window).height());
    $(calElemNom).fullCalendar('option', 'height', $(window).height() - 217);
    
  });
  
  $(calElemNom).fullCalendar('option', 'height', $(window).height() - 217);
  

}