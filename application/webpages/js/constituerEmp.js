$(document).ready(function () {
    chargerAgenda();
});



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

/**
 * 
 * @param horairesCoursJSON
 * @param unTemps un temps "brute" ; Type "Time"
 * @param estDebut accroche ver un début d'un crénau, faix si c'est la fin
 * @param minTemps un sol, le temps retourné sera égal ou plus d'unTemps
 * @returns
 */
function trouverPlusProcheGrille(horairesCoursJSON, unTemps, estDebut, fractionnement, minTemps) {
    var closestTime = null;
    var closestDiff = 24 * 60;

    // Boucle sur les plages de la grille EDT
    jQuery.each(horairesCoursJSON, function (index, value) {

        // Determine la duree du fractionnement correspondant a la plage en cours 
        var startTimePlage = new Time(value.heureDebut, value.minuteDebut);
        var endTimePlage = new Time(value.heureFin, value.minuteFin);
        var plageDuree = endTimePlage.diff(startTimePlage) / fractionnement;

        // Boucle sur les plages fractionnees de la plage courante 
        for (var i = 0; i < fractionnement; i++) {

            // Defini les horaires du fractionnement 
            startTime = startTimePlage.ajoute(i * plageDuree);
            endTime = startTimePlage.ajoute(plageDuree);

            var f = function (time) {
                var diffTime = Math.abs(time.diff(unTemps));
                if (diffTime < closestDiff && (minTemps == null || time.plusQue(minTemps))) {
                    closestTime = time;
                    closestDiff = diffTime;
                }
            }

            //Si unTemps est englobé par un crénau, sélectionne le
            if (unTemps.toMinutes() > startTime.toMinutes() && unTemps.toMinutes() < endTime.toMinutes()) {
                closestTime = estDebut ? startTime : endTime;
                return false;
            }

            if (estDebut) {
                f(startTime);
            } else {
                f(endTime);
            }
        }
    });
    
    if (closestTime == null) { 
        closestTime = unTemps;
    }
    return closestTime;
}



/**
 * Trouver la fin après un resize
 * 
 * La plage particulaire n'est pas prise en compte, uniquement la durée des cours dans le paramétrage de l'etablissement
 * 
 * @param horairesCoursJSON
 * @param debut le debut de l'emploi de temps
 * @param fin l'heure de fin produite par le glissement de souris
 * @param fractionnement
 * @returns l'heure de fin de l'emploi de temps
 */
function trouverPlusProcheGrilleResize(horairesCoursJSON, debut, fin, fractionnement, dureeCours, minuteDelta) {

    // Cherche la plage correspondant a l'heure de debut
    var closestTime = null;
    var closestDiff = 24 * 60;

    // Determine la duree de fractionnement a prendre en compte pour aligner l'heure de fin 
    // Sur un multiple de cette durée
    var dureeFractionnement = null;
    jQuery.each(horairesCoursJSON, function (index, value) {

        // Horaires de la plage courante 
        var startTime = new Time(value.heureDebut, value.minuteDebut);
        var endTime = new Time(value.heureFin, value.minuteFin);

        if (debut.toMinutes() >= startTime.toMinutes() && debut.toMinutes() < endTime.toMinutes()) {
            dureeFractionnement = (endTime.toMinutes() - startTime.toMinutes()) / fractionnement;
            return false;
        }
    });
    if (dureeFractionnement == null || dureeFractionnement <= 0) {
        dureeFractionnement = dureeCours / fractionnement; 
    }
     
    // Aligne l'heure de fin sur un multiple de la duree de fractionnement definie
    var dureeResize = fin.toMinutes() - debut.toMinutes();
    var nbrFractionnement = Math.round(dureeResize / dureeFractionnement);
    if (nbrFractionnement == null || nbrFractionnement <= 0) {
        nbrFractionnement = 1;
    }
    var grilleEvenementFin = debut.ajoute(nbrFractionnement * dureeFractionnement);
    return grilleEvenementFin;
}

function chargerGrilleHoraire() {

    var date = new Date();

    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();



    logFormat("Date {0} y {1} m {2} d {3}", date, y, m, d);

    //console.log($('#calendarData').data("horairesCoursJSON"));
    var horairesCoursJSON = jQuery.parseJSON($('#calendarData').data("horairesCoursJSON"));

    var etablissementJSON = jQuery.parseJSON(jQuery("#etablissementJSON").val());
    var minTimeData = etablissementJSON.heureDebut;
    var maxTimeData = etablissementJSON.heureFin + 1;

    var grilleEvents = [];

    jQuery.each(horairesCoursJSON, function (index, value) {
        logFormat("Grille horaire {0}: {1}:{2} ==> {3}:{4}", index, value.heureDebut, value.minuteDebut, value.heureFin, value.minuteFin);
        var startTime = new Time(value.heureDebut, value.minuteDebut);
        var endTime = new Time(value.heureFin, value.minuteFin);
        grilleEvents.push({
            title: 'Plage ' + (index + 1),
            start: new Date(y, m, d, value.heureDebut, value.minuteDebut),
            end: new Date(y, m, d, value.heureFin, value.minuteFin),
            allDay: false,
            className: 'grilleEvent',
            backgroundColor: 'grey'
        });
    });

    logFormat("Construire calendar grille min time {0} max time {1} etab id {2}", minTimeData, maxTimeData, etablissementJSON.id);

    var calendarGrille = $('div.calendarGrille').fullCalendar(getAgendaConfiguration({

        minTime: minTimeData,
        maxTime: maxTimeData,
        defaultView: 'agendaDay',
        columnFormat: {
            month: '', // Mon
            week: '', // Mon 9/7
            day: ''
        },
        year: y,
        month: m,
        date: d,
        weekends: true,
        selectable: false,
        axisFormat: "",
        editable: false,
        events: grilleEvents,
        selectHelper: true,
        eventAfterRender: function (event, element, view) {

            applyTooltip(event, element);
            jQuery(element).css("width", "70px");
        }
    }));


    logFormat("Fin construction calendar grille");


    $('div.calendarGrille .fc-content > div > div > div').css('overflow-y', 'hidden');

    $('.calendarGrille th.fc-agenda-axis').remove();

    $('.calendarGrille div.grilleEvent').each(function (index, elem) {
        $(elem).css("left", "0");
    });

    logFormat('Cal grille len {0} events {1}', $('.calendarGrille .fc-widget-content').length, jQuery('div.calendarGrille fc-event').length);

    $('.calendarGrille .fc-widget-header, .calendarGrille .fc-widget-content').css({
        "border": "0",
        "margin": "0px 0px 0px 0px",
        "padding": "1px 0px 0px 0px"
    });

}

function chargerAgenda() {
    chargerGrilleHoraire();
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

    var retour = emploiDeTempsDTO.caseDescription;




    return retour;
}

function chargerAgendaImpl(jsonElemNom, calElemNom) {

    console.log('chargerAgendaImpl ' + jsonElemNom + "  " + calElemNom);

    //Ce date represente un lundi
    var d = 5;
    var m = 2;
    var y = 2012;

    var startDate = new Date(y, m, d);


    var listeEmploiDeTempsJSON = jQuery.parseJSON(jQuery('#listeEmploiDeTempsJSON').val());

    var horairesCoursJSON = jQuery.parseJSON($('#calendarData').data("horairesCoursJSON"));
    var etablissementJSON = jQuery.parseJSON(jQuery("#etablissementJSON").val());
    var minTimeData = etablissementJSON.heureDebut;
    var maxTimeData = etablissementJSON.heureFin + 1;

    console.log("MinTime: " + minTimeData);
    console.log("MaxTime: " + maxTimeData);

    if (listeEmploiDeTempsJSON == null) {
        console.log("Problem null JSON object: " + jsonElemNom);
        return;
    }

    var agendaEvents = transformerJSONenEvents(startDate, listeEmploiDeTempsJSON);

    var joursOuvres = etablissementJSON.joursOuvres;

    var weekends = joursOuvres.match(/SAMEDI/) != null || joursOuvres.match(/DIMANCHE/) != null;

    var calendar = jQuery(calElemNom).fullCalendar(

    getAgendaConfiguration({

        minTime: minTimeData,
        maxTime: maxTimeData,
        year: y,
        month: m,
        date: d,
        selectable: true,
        editable: true,
        selectHelper: true,
        weekends: weekends,
        select: function (debut, fin, allDay, jsEvent, view) {

            attendrePourDoubleClique(jQuery(calElemNom)[0], function () {

                var fractionnement = etablissementJSON.fractionnement;
                var evenementDebut = new Time(debut);
                var evenementFin = new Time(fin);

                //Pour une clique sans tirage, utilise la grille horaire
                if (evenementFin.diff(evenementDebut) == 15) {
                    var grilleEvenementDebut = trouverPlusProcheGrille(horairesCoursJSON, evenementDebut, true, 1);
                    var grilleEvenementFin = trouverPlusProcheGrille(horairesCoursJSON, evenementFin, false, 1, grilleEvenementDebut);

                    //Mettre les résultats dans début et fin
                    grilleEvenementDebut.setMinuteHeure(debut);
                    grilleEvenementFin.setMinuteHeure(fin);
                }

                //Créer une case 
                creerDetailJourEmploiDTOSel();

                //Mettre les valeurs
                affecterDetailJourEmploiDTOSel(['DIMANCHE', 'LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI', 'SAMEDI'][debut.getDay()],
                debut.getHours(), debut.getMinutes(), fin.getHours(), fin.getMinutes());

                afficherEventPopup(agendaEvents.length);

                setModificationFormulaire(true);


            });
        },
        eventClick: function (calEvent, jsEvent, view) {

            attendrePourDoubleClique(jsEvent.target, function () {
                console.log('Event: ' + calEvent.title);
                console.log('Start: ' + calEvent.start);
                console.log('Stop: ' + calEvent.end);
                console.log('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
                console.log('View: ' + view.name);

                //EmploiJoursDTO 
                jQuery.each(horairesCoursJSON, function (index, value) {
                    console.log(index + ': ' + value.heureDebut + ' ' + value.minuteDebut + ' ');
                });

                afficherEventPopup(calEvent.index);

                setModificationFormulaire(true);

            });


        },
        eventResize: function (event, dayDelta, minuteDelta, revertFunc) {

            console.log("eventResize The end date of " + event.title + "has been moved " + dayDelta + " days and " + minuteDelta + " minutes.");

            console.log("Start " + event.start);

            console.log("Stop " + event.end);

            var eventStopTime = new Time(event.end.getHours(), event.end.getMinutes());

            var eventStartTime = new Time(event.start.getHours(), event.start.getMinutes());

            var fractionnement = etablissementJSON.fractionnement;

            var dureeCours = etablissementJSON.dureeCours;

            logFormat("Fractionnement est {0}, durée des cours est {1}", fractionnement, dureeCours);

            var closestTime = trouverPlusProcheGrilleResize(horairesCoursJSON, eventStartTime, eventStopTime, fractionnement, dureeCours, minuteDelta);

            event.end.setMinutes(closestTime.minute);
            event.end.setHours(closestTime.heure);

            calendar.fullCalendar('updateEvent', event);

            setDetailJourEmploiDTOSelIndex(event.index);
            affecterDetailJourEmploiDTOSel(['DIMANCHE', 'LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI', 'SAMEDI'][event.start.getDay()], event.start.getHours(), event.start.getMinutes(), event.end.getHours(), event.end.getMinutes());

            setModificationFormulaire(true);
            //showPopupEvent();


        },
        eventDrop: function (event, dayDelta, minuteDelta, allDay, revertFunc) {

            console.log(
            event.title + " was moved " + dayDelta + " days and " + minuteDelta + " minutes.");

            if (allDay) {
                console.log("Event is now all-day");
            } else {
                console.log("Event has a time-of-day");
                console.log("Start date " + event.start);
                console.log("End date " + event.end);
            }

            console.log("EventDrop. event end: " + event.end);
            console.log("EventDrop. event start: " + event.start);

            var eventDebut = new Time(event.start);
            var fractionnement = etablissementJSON.fractionnement;
            var grilleEvenementDebut = trouverPlusProcheGrille(horairesCoursJSON, eventDebut, true, fractionnement);

            console.log("EventDrop. event grille debut: " + grilleEvenementDebut);

            var diffTemps = grilleEvenementDebut.diff(eventDebut);
            var eventFin = new Time(event.end);
            eventFin = eventFin.ajoute(diffTemps);

            grilleEvenementDebut.setMinuteHeure(event.start);
            eventFin.setMinuteHeure(event.end);

            calendar.fullCalendar('updateEvent', event);

            setDetailJourEmploiDTOSelIndex(event.index);
            affecterDetailJourEmploiDTOSel(['DIMANCHE', 'LUNDI', 'MARDI', 'MERCREDI', 'JEUDI', 'VENDREDI', 'SAMEDI'][event.start.getDay()], event.start.getHours(), event.start.getMinutes(), event.end.getHours(), event.end.getMinutes());


            setModificationFormulaire(true);

            //if (!confirm("Are you sure about this change?")) {
            //  revertFunc();
            //}
        },
        events: agendaEvents
    }));


    $(calElemNom + ' .fc-content > div > div > div').scroll(function () {
        var length = $(this).scrollTop();
        $('div.calendarGrille .fc-content > div > div > div').scrollTop(length);
    });
    
    var resizeFunc = function () {
        jQuery(calElemNom).fullCalendar('option', 'height', $(window).height() - 217);
        jQuery('div.calendarGrille').fullCalendar('option', 'height', $(window).height() - 217 - 17);
    };

    bindResizeFunction(resizeFunc);

    //Alignement IE
    if (jQuery.browser.msie) {
        jQuery('div.calendar > table.fc-header').remove();
    }

    resizeFunc();
    
    logFormat("# events: {0}", $('.cahierEvent').length);
    

}

/**
 * Si une modification est en cours, on demande la confirmation de l'utilisateur. 
 * S'il accepte, les modifications sont perdues sinon, on navigue vers la nouvelle periode.
 * si pas de modification en cours, on passe directement à la nouvelle periode.
 * En cas d'annulation si modification, le lien idAnnuleChangementPeriode est executé pour 
 * restaurer la valeur de période initiale.  
 * @return true si on passe a la nouvelle periode, false sion 
 */
function confirmationAjoutPeriode(valOrigine) {
    if (!getModificationFormulaire()) return true;

    if (confirm('Vous aller perdre vos modifications. Voulez-vous vraiment changer de période ?')) {
        return true;
    }

    var liste = jQuery('#formulaire_principal\\:periodeSaisieListe');

    liste.val(valOrigine);

    return false;
}