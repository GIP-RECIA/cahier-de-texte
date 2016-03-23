package org.crlr.metier.utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.apache.commons.collections.CollectionUtils;
import org.crlr.dto.application.base.TypeJour;
import org.crlr.dto.application.emploi.DetailJourEmploiDTO;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.DateUtils;

/**
 * @author G-CG34-FRMP
 *
 */
public class EmploiDeTempsReport {
	protected final Log log = LogFactory.getLog(getClass());

	//Constanst trouvé par expérimentation  
	final int totalWidth = 800;
	
	int totalTableauHeight = 519;
	final int grilleWidth = 100;

	final int jourLibelleHauteur = 22;

	int minHeure;
	int maxHeure;

	JasperDesign jasperDesign;

	List<TypeJour> joursPourAfficher;

	/**
	 * @author G-CG34-FRMP
	 *
	 */
	public static class JourColonne {
		int xGauche;
		int xDroite;

		/**
		 * @return w
		 */
		int width() {
			return xDroite - xGauche;
		}
	}

	private Map<TypeJour, JourColonne> jourColonneMap;

	/**
	 * @param jr .
	 * @param heureDebut .
	 * @param heureFin .
	 * @param joursPourAfficher .
	 * @param listeFromDatabase .
	 */
	public EmploiDeTempsReport(JasperDesign jr,
			int heureDebut, int heureFin,
			List<TypeJour> joursPourAfficher,
			List<DetailJourEmploiDTO> listeFromDatabase) {

		this.joursPourAfficher = joursPourAfficher;
		jourColonneMap = new HashMap<TypeJour, JourColonne>();
		// calculer debut et fin en terme de mintues total du début et fin de la
		// grille

		minHeure = heureDebut;
		maxHeure = heureFin;

		if (CollectionUtils.isEmpty(joursPourAfficher)) {
			log.info("pas de jours définis, utilise tout le jours / sept jours");
			joursPourAfficher.addAll(Arrays.asList(TypeJour.values()));
			//return;
		}

		this.jasperDesign = jr;
		
		totalTableauHeight = jr.getDetailSection().getBands()[0].getHeight() - jourLibelleHauteur;

		construireJourColonneMap();
		construireJourLibelles();
		construireLigneVerticales();
		construireGrilleLibelles();
		construireCases(listeFromDatabase);
	}

	/**
	 * 
	 */
	private void construireJourColonneMap() {

		int x = grilleWidth;
		int jourWidth = (totalWidth - grilleWidth) / joursPourAfficher.size();

		for (TypeJour jour : joursPourAfficher) {

			JourColonne jourColonne = new JourColonne();

			jourColonne.xDroite = x + jourWidth;
			jourColonne.xGauche = x;

			x += jourWidth;

			if (jourColonneMap.containsKey(jour)) {
				log.error("Jour existe déjà");
				return;
			}

			jourColonneMap.put(jour, jourColonne);
		}
	}

	
	/**
	 * @param band b 
	 * @param x x
	 */
	private void creerLigneVerticale(JRDesignBand band, int x) {
	    JRDesignLine line = new JRDesignLine();

        line.setX(x);

        line.setWidth(1);
        line.setY(0);
        line.setHeight(totalTableauHeight + jourLibelleHauteur);
        
        log.info("Ligne x {0} y {1}", x, 0);
        band.addElement(line);
	}
	/**
	 * 
	 */
	private void construireLigneVerticales() {

		JRDesignBand detailBand = (JRDesignBand) jasperDesign
				.getDetailSection().getBands()[0];

		for (TypeJour jour : joursPourAfficher) {
			JourColonne jourColonne = jourColonneMap.get(jour);

			creerLigneVerticale(detailBand, jourColonne.xGauche);
		}
		
		creerLigneVerticale(detailBand, totalWidth - 1);
	}

	/**
	 * 
	 */
	private void construireJourLibelles() {

		// JRDesignBand columnHeaderBand = (JRDesignBand)
		// jasperDesign.getColumnHeader();

		JRDesignBand detailBand = (JRDesignBand) jasperDesign
				.getDetailSection().getBands()[0];

		detailBand.setHeight(jourLibelleHauteur);

		for (TypeJour jour : joursPourAfficher) {

			JourColonne jourColonne = jourColonneMap.get(jour);

			JRDesignTextField jourLibelle = new JRDesignTextField();
			jourLibelle.setX(jourColonne.xGauche);
			jourLibelle.setY(0);

			jourLibelle.setWidth(jourColonne.width() - 1);
			jourLibelle.setHeight(jourLibelleHauteur);

			JRDesignExpression jrExp = new JRDesignExpression();
			jrExp.setText("\"" + jour.name() + "\"");
			jourLibelle.setExpression(jrExp);

			jourLibelle.setStyle(jasperDesign.getStylesMap().get("jourLabel"));

			detailBand.addElement(jourLibelle);

		}
	}

	/**
	 * @param minutes m
	 * @return r
	 */
	private int getYPixelPourMinute(int minutes) {
		int debutDecalage = minHeure * 60;
		int finDecalage = maxHeure * 60;
		int totalDecalage = finDecalage - debutDecalage;

		return (int) Math.floor(jourLibelleHauteur
				+ (1.0 * (minutes - debutDecalage) / totalDecalage)
				* totalTableauHeight);
	}

	/**
	 * 
	 */
	private void construireGrilleLibelles() {

		JRDesignBand detailBand = (JRDesignBand) jasperDesign
				.getDetailSection().getBands()[0];

		// La dernière heure n'a pas un libellé.
		for (int h = minHeure; h < maxHeure; ++h) {
			JRDesignTextField text = new JRDesignTextField();

			int minuteDebut = h * 60;
			int minuteFin = h * 60 + 60;

			int yDebut = getYPixelPourMinute(minuteDebut);
			int yFin = getYPixelPourMinute(minuteFin);

			text.setX(0);
			text.setY(yDebut);
			text.setWidth(grilleWidth);
			text.setHeight(yFin - yDebut);
			
			log.info("Grille x {0} y {1} width {2} min debut {3}", text.getX(), text.getY(), text.getWidth(), minuteDebut);
			
			text.setStyle(jasperDesign.getStylesMap().get("grille"));
			JRDesignExpression jrExp = new JRDesignExpression();

			jrExp.addTextChunk("\"" + DateUtils.formatTime(h, 0)
					+ "\"");
			text.setExpression(jrExp); 
			text.setMarkup("styled");
			detailBand.addElement(text);
		}
	}

	/**
	 * @param listeFromDatabase d
	 */
	private void construireCases(List<DetailJourEmploiDTO> listeFromDatabase) {

		JRDesignBand detailBand = (JRDesignBand) jasperDesign
				.getDetailSection().getBands()[0];

		detailBand.setHeight(totalTableauHeight + jourLibelleHauteur);

		for (DetailJourEmploiDTO event : listeFromDatabase) {
			JRDesignTextField text = new JRDesignTextField();

			int minuteDebut = event.getHeureDebut() * 60
					+ event.getMinuteDebut();
			int minuteFin = event.getHeureFin() * 60 + event.getMinuteFin();

			int yDebut = getYPixelPourMinute(minuteDebut);
			int yFin = getYPixelPourMinute(minuteFin);

			JourColonne jourColonne = jourColonneMap.get(event.getJour());

			if (jourColonne == null) {
				log.warning("Jour n'existe pas");
				continue;
			}

			text.setX(jourColonne.xGauche + 1); //+1 pour la bordure
			text.setY(yDebut);
			text.setWidth(jourColonne.width()-1);
			
			log.info("case x {0} y {1} width", text.getX(), text.getY(), text.getWidth());
			
			text.setHeight(yFin - yDebut);
			text.setStyle(jasperDesign.getStylesMap().get("cases"));
						
			text.setBackcolor(event.getTypeCouleur().getColor());
			
			JRDesignExpression jrExp = new JRDesignExpression();
			// log.info("Event desc {0}", event.getPrintDescription());
			jrExp.addTextChunk("\"" + event.getPrintDescription() + "\"");
			text.setExpression(jrExp);
			text.setMarkup("styled");
			detailBand.addElement(text);
			// jr.getDetailSection().getBands()[0].

		}
	}
	
	

}