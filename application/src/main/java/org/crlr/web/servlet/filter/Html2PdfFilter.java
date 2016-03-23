package org.crlr.web.servlet.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.itextpdf.text.DocumentException;


//import org.apache.xmlgraphics.util.MimeConstants;




/**
 * 
 * @author legay
 *
 */
public class Html2PdfFilter extends GenericFilter {

	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

		

		log.debug("filtre before wrapper ");
		/* on utilise un wrapper sur la reponse pour la modifier */

		CharResponseWrapper wrapper = new CharResponseWrapper(httpResponse);

		/* demande de la réponse d'origine */
		chain.doFilter(request, wrapper);

		/* la reponse org est en xhtml */
		String org = wrapper.toString();

	

		/* creation du convertiseur xhtml -> pdf */
		ITextRenderer renderer = new ITextRenderer();
		 
		 /* creation de l'outil de gestion des icons (pour les trouver sur le disque) */
		 ImgReplacedElementFactory iref = new ImgReplacedElementFactory(httpRequest, renderer.getOutputDevice());
		 
		 /* pour trouver les css :  on remplace les appels a la servlet ressources de jsf par un lien direct relatif au context */
		 Pattern pattern = Pattern.compile(iref.getAppliName() + "/javax.faces.resource/([^/]+)\\.xhtml\\?ln=css");
		 log.debug("css patern  : {0}" , pattern.pattern());
		 Matcher m = pattern.matcher(org);
		 org = m.replaceAll("file:" + iref.getRealAppliPath() + "/resources/css/$1");
		 
		 /* Pour les autres resources on transform l'url en path relatif */
		 pattern = Pattern.compile("url\\(" + iref.getAppliName() + "/resources/");
		  m = pattern.matcher(org);
		 org = m.replaceAll("url(file:"+ iref.getRealAppliPath() + "/resources/");
		 
		 /* on supprime les tags de style mal interpretés par ITextRenderer */ 
		 pattern = Pattern.compile("<style [^<]+</style>");
		 m = pattern.matcher(org);
		 org = m.replaceAll("");
		 
		 /* on supprime les tags link de style ecss mal interpretés par ItextRenderer */
		 pattern = Pattern.compile(
				 		"<link type=\"text/css\" rel=\"stylesheet\" href=\"" +
				 		iref.getAppliName() + "/rfRes/[^/]+/>"); 
		 
		 m = pattern.matcher(org);
		 org = m.replaceAll("");
	//	 ChainedReplacedElementFactory cef = new ChainedReplacedElementFactory();
		 
		 /* on initialise le convertissuer avec notre gestionaire de remplacement*/
		 renderer.getSharedContext().setReplacedElementFactory(iref);
		
		 
			log.debug("avant conversion {0}", org);
		 
		 
		 /* on lui fournis le .xhtml a convertir */
		 renderer.setDocumentFromString(org);
		 
		 /* on lance l'analyse du xhtml  */
		 renderer.layout();
		 
		 /* on prepare l'entête de la réponse */
		httpResponse.setContentType("application/pdf");
		httpResponse.setCharacterEncoding("UTF-8");
		final String header = httpRequest.getHeader("User-Agent");
		final boolean msie = header.matches("(.*)MSIE(.*)");

		final boolean chrome = header.matches("(.*)Chrome(.*)");
		if (!msie) {
			// Tout autre navigateur
			if (chrome) {
				httpResponse.setContentType("application/download");
			}
			responseWithNoCache(httpResponse);
		} else {
			// IE
			responseWithNoCache(httpResponse);
			httpResponse.setHeader("Content-disposition",
					"attachment; filename=seanceList.pdf");
		}

		OutputStream out = httpResponse.getOutputStream();

		try {
			/* on lance la conversion en pdf  dans le flux de sortie  */
			log.debug("debut de conversion :");
			renderer.createPDF(out);
			log.debug("fin de conversion");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderer.finishPDF();

		out.flush();
		out.close();
        
        httpResponse.flushBuffer();
            
	
	
	}

	  /**
     * Response with no cache.
     *
     * @param httpResponse the http response
     */
    private void responseWithNoCache(HttpServletResponse httpResponse) {
        httpResponse.setHeader("Cache-control",
                               "private,must-revalidate,no-store,max-age=0");
        httpResponse.setHeader("Expires", "Mon, 31 Dec 2007 13:00:00 GMT");
    }	
	
	
}
