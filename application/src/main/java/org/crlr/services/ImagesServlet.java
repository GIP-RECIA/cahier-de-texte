package org.crlr.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.crlr.dto.application.devoir.DevoirDTO;
import org.crlr.dto.application.seance.ImageDTO;
import org.crlr.dto.application.seance.PrintSeanceDTO;
import org.crlr.dto.application.sequence.PrintSequenceDTO;
import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.PropertiesUtils;

/**
 * 
 *
 */
public class ImagesServlet extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2095155965358888336L;

    protected static final Log log = LogFactory.getLog(ImagesServlet.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        String uniqueImageFileName = request.getPathInfo().substring(1);
        
        // byte[] imageContent = (byte[]) request.getSession().getAttribute();
        byte[] imageContent = (byte[]) request.getSession()
                .getAttribute(uniqueImageFileName);
        
        
        
        response.setContentType("image/gif");

        if (imageContent == null) {
            log.warning("Latex image pas trouvé {0}", uniqueImageFileName);
            super.doGet(request, response);
            return;
        }
        
        log.debug("Requete pour image clé [{0}].  Taille de contenu : [{1}]", uniqueImageFileName, imageContent.length);

        response.setContentLength(imageContent.length);
        // Write byte[] to response.getOutputStream() the usual way.

        response.getOutputStream().write(imageContent);

        response.getOutputStream().close();

    }

    /**
     * @param latexText t
     * @return l'image
     */
    public static ImageDTO genererImageDTO(final String latexText) {
        try {
            final Properties properties = PropertiesUtils
                    .load("/config.properties");

            String exec = properties.getProperty("mimetex.path");

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            byte[] md5bytes = messageDigest.digest(latexText.getBytes("UTF-8"));

            String md5 = new String(Hex.encodeHex(md5bytes));
            
            log.debug("Latex executable {0} avec text {1}", exec, latexText);

            ProcessBuilder pb = new ProcessBuilder(exec, "\"" + latexText
                    + "\"", "-d"); // outputs directement ver StdOut);

            pb.redirectErrorStream(true);

            Process p = pb.start();

            InputStream inputStream = p.getInputStream();

            byte[] imageContent = IOUtils.toByteArray(inputStream);

            ImageDTO image = new ImageDTO();
            // image.setInputStream(inputStream);
            image.setInputStream(new ByteArrayInputStream(imageContent));
            // image.setFileName(file.getAbsolutePath());
            // image.setFile(file); 
            log.info("Byte stream size {0} {1}.  Image clé : [{2}]", imageContent.length, image.getInputStream(), md5);
            image.setFileKey(md5);
            image.setFileContents(imageContent);

            return image;

        } catch (NoSuchAlgorithmException ex) {
            log.error(ex, "Exception");
        } catch (UnsupportedEncodingException ex) {
            log.error(ex, "Exception");
        } catch (IOException ex) {
            log.error(ex, "Exception");
        }
        
        //Créer un DTO 'erreur'
        ImageDTO image = new ImageDTO();
        
        image.setInputStream(new ByteArrayInputStream(new byte[]{}));
        
        image.setFileKey("Erreur");
        image.setFileContents(new byte[]{});

        return image;
    }
    
    /**
     * @param printSequenceDTO dto
     */
    public static void processPrintSequenceDTO(PrintSequenceDTO printSequenceDTO) {
        
        for(PrintSeanceDTO seance : printSequenceDTO.getListeSeances()) {
            processPrintSeanceDTO(seance);
        }
                
    }
    
    /**
     * Mettre les liens d'image dans le séance DTO
     * @param printSeanceDTO dto
     */
    public static void processPrintSeanceDTO(PrintSeanceDTO printSeanceDTO) {
        
        List<ImageDTO> listeImages = new ArrayList<ImageDTO>();
        
        String desc = processDTO(listeImages, printSeanceDTO.getDescription());
        
        printSeanceDTO.setDescriptionPDF(desc);
        printSeanceDTO.setListeImages(listeImages);
        log.info("Num images in seance {0} : {1}", printSeanceDTO.getId(), listeImages.size());
        
        for(DevoirDTO devoir : printSeanceDTO.getDevoirs()) {
            ImagesServlet.processDevoirDTO(devoir);
            log.info("Num images in DEVOIR seance {0} devoir {2} : {1}", printSeanceDTO.getId(), devoir.getListeImages().size(), devoir.getId());
        }
    }
    
    /**
     * @param devoirDTO dto
     */
    public static void processDevoirDTO(DevoirDTO devoirDTO) {
        List<ImageDTO> listeImages = new ArrayList<ImageDTO>();
        
        String desc = processDTO(listeImages, devoirDTO.getDescription());
        
        devoirDTO.setDescription(desc);
        devoirDTO.setListeImages(listeImages);
    }

    /**
     * Pour des pdfs (print séances / print séquences).
     * @param listeImages [out] 
     * @param dtoDesc [in]
     * @return description avec des renvoies.
     */
    public static String processDTO(List<ImageDTO> listeImages, final String dtoDesc) {

        Pattern p = Pattern.compile("&lt;latex&gt;(.*?)&lt;/latex&gt;",
                Pattern.CASE_INSENSITIVE);
                
        Matcher matcher = p.matcher(dtoDesc);
        
        String desc = dtoDesc;

        int i = 0;
        

        while (matcher.find()) {

            final String match = matcher.group(0);
            final String latexContents = matcher.group(1);

            ImageDTO image = genererImageDTO(latexContents);

            listeImages.add(image);

            ++i;

            image.setLabel("Expression # " + i);

            desc = desc.replaceAll(Pattern.quote(match), "( Expression " + i + ")");
            
            matcher = p.matcher(desc);

        }

        //desc = desc.replaceAll("\n", "");
        //desc = desc.replaceAll("<br/>", "<br>");

        log.info(desc);
        return desc;


    }

    /**
     * Créer html avec des lien images.  Les images sont générer en memoire dans la session. 
     * 
     * @param latexText chaine avec événtuellement des balises Latex.
     * @return la chaine avec des basiles <img ... /> 
     */
    public static String genererLatexImage(final String latexText) {
        
        log.debug("genererLatexImage text={0}", latexText);
        
        if (latexText==null) {
            return null;
        }
        
        if (FacesContext.getCurrentInstance() == null) {
            log.warning("TEST mode?  Faces Context est null");
            return latexText;
        }
        
        String desc = latexText;

        Matcher matcher = Pattern.compile("&lt;latex&gt;(.*?)&lt;/latex&gt;",
                Pattern.CASE_INSENSITIVE).matcher(latexText);
        
        ExternalContext ec = FacesContext.getCurrentInstance()
                .getExternalContext();
        
        
        
        final String urlPrefix = ec.getRequestScheme() + "://" + ec.getRequestServerName() + ":"
                + ec.getRequestServerPort()
                + ec.getRequestContextPath() + "/images/latex/";

        log.debug("Images latex URL début {0}", urlPrefix);
        
        while (matcher.find()) {
            String match = matcher.group(0);
            String latexContents = matcher.group(1);

            ImageDTO image = genererImageDTO(latexContents);

            
            
            ec.getSessionMap().put(image.getFileKey(),
                    image.getFileContents());
            
            
            final String url = "<img src=\'" + urlPrefix
                    + image.getFileKey() + "\'/>";
            
            log.debug("Remplacement de {0} avec latex chaine {2} par {1}", match, url, latexContents);
            
            desc = desc.replace(
                    match, url);

            
        }
        
        log.debug("genererLatexImage texte transformé={0}", desc);
        
        return desc;

    }
}
