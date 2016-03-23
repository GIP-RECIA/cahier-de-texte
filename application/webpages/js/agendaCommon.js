function transformerJSONenEvents(dateLundi, listeEmploiDeTempsJSON) {
  var agendaEvents = [];

  var y = dateLundi.getFullYear();
  var m = dateLundi.getMonth();
  var d = dateLundi.getDate();

  jQuery.each(listeEmploiDeTempsJSON, function (index, value) {
    logFormat('listeEmploiDeTempsJSON index {0} {1}:{2} heure fin {3} color {4}', index, value.heureDebut, value.minuteDebut, value.heureFin, value.typeCouleur[0]);
    var startTime = new Time(value.heureDebut, value.minuteDebut);
    var endTime = new Time(value.heureFin, value.minuteFin);
    agendaEvents.push({
      title: getTitreFromDTO(value),
      start: new Date(y, m, d + value.jour[0], value.heureDebut, value.minuteDebut),
      end: new Date(y, m, d + value.jour[0], value.heureFin, value.minuteFin),
      allDay: false,
      className: 'cahierEvent cahierEventIndex' + index,
      borderColor: 'grey',
      backgroundColor: value.typeCouleur[0],
      textColor: '#000000;',
      index: index
    });

    logFormat("Agenda object {0}", agendaEvents[agendaEvents.length - 1]);
  });

  //console.log(stringFormat("# agendaEvents {0}", agendaEvents.length));
  return agendaEvents;
}



function applyTooltip(agendaEvent, element) {
	var title = agendaEvent.title.replace(/\n/g, "<br/>");
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



var defaults = {
  header: {
    left: '',
    center: '',
    right: ''
  },
  allDaySlot: false,
  height: 200,
  weekends: false,
  slotMinutes: 15,
  firstDay: 1,
  columnFormat: {
    month: 'ddd',
    week: 'dddd',
    day: 'dddd M/d' 
  },
  axisFormat: "HH'H'mm",
  timeFormat: "H:mm - {H:mm}",
  dayNames: ['Dimanche', 'Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi'],
  defaultView: 'agendaWeek',
  eventAfterRender: function (event, element, view) {
    applyTooltip(event, element);
  }
};

function calcShowWeekend(etablissementJSON) {
	var joursOuvres = etablissementJSON.joursOuvres;  
  var weekends = joursOuvres.match(/SAMEDI/) != null || joursOuvres.match(/DIMANCHE/) != null; 

  return weekends;
}

function getAgendaConfiguration(customProps) {
  return jQuery.extend({}, defaults, customProps);
}


///////////////////////////////////////////////////
//Créer un object de type 'Time'
function Time(heure, minute) {
  if (arguments.length == 2) {
    this.heure = heure;
    this.minute = minute;
  } else if (arguments.length == 1) {
  	//Suppose l'objet est un object javascript Date
    var dateObj = heure;
    this.heure = dateObj.getHours();
    this.minute = dateObj.getMinutes();
    
    logFormat("Time constructor h {1}  m {2}", this.heure, this.minute);
  }
}

//Retourne la différence en minutes entre deux Time
Time.prototype.diff = function (timeObj) {
  return (
  (this.heure * 60 + this.minute) - (timeObj.heure * 60 + timeObj.minute));
}

//Ajoute des minutes un cet objet Time 
Time.prototype.ajoute = function (minutes) {
  var totaleMinutes = this.heure * 60 + this.minute + minutes;
  logFormat("totaleMinutes {0}. h {1}  m {2}  min {3}", totaleMinutes, this.heure, this.minute, minutes);
  return new Time(Math.floor(totaleMinutes / 60), totaleMinutes % 60);
}

//Affecte dateObj, pas l'object lui-même
Time.prototype.setMinuteHeure = function (dateObj) {
  dateObj.setMinutes(this.minute);
  dateObj.setHours(this.heure);
};

Time.prototype.equals = function (timeObj) {
  return this.heure == timeObj.heure && this.minute == timeObj.minute;
}

Time.prototype.plusQue = function (timeObj) {
  return this.heure == timeObj.heure ? this.minute > timeObj.minute : this.heure > timeObj.heure;
}

Time.prototype.toMinutes = function (timeObj) {
	return this.heure * 60 + this.minute;
}

Time.prototype.toString = function () {
  return this.heure + ":" + this.minute;
}
//
///////////////////////////////////////////////////
function attendrePourDoubleClique(domElem, funcAExecuter) {

	
	//Ne marche pas sur IE, fais une single clique pour tout le monde
	return funcAExecuter();		
	
	
  //console.log("domElem: " + domElem.innerHTML);
  var DOUBLE_CLICK_DELAY = 450; // unité 1 / 1000 secondes
  
 
  
  var jqOriginatingElem = jQuery(domElem);

  var foisClique = 1 + (jqOriginatingElem.data("clicks") ? jqOriginatingElem.data("clicks") : 0);

  logFormat("attendrePourDoubleClique {0}", foisClique);
  
  if (foisClique <= 1) {
    console.log("Single click");
    jqOriginatingElem.data("clicks", 1);

    //Après un cértain temps, remettre 0 
    var resetAZeroFunc = function () {
        console.log("Reset click compter à 0")
        jqOriginatingElem.data("clicks", 0);
      };

      
    var resetAZeroFuncId = setTimeout(resetAZeroFunc, DOUBLE_CLICK_DELAY);
    

    return;
  }

  console.log("Function à exécuter")
  jqOriginatingElem.data("clicks", 0);

  funcAExecuter();
}