package org.crlr.web.servlet.filter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextReplacedElementFactory;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.resource.ImageResource;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;




public class ImgReplacedElementFactory extends ITextReplacedElementFactory  {
/*
 * On pourrait ne pas étendre  ITextReplacedElementFactory 
 * mais seulement implémenter ReplacedElementFactory
 * 
 * ITextReplacedElementFactory est le ReplacedElementFactory par défaut de ITextRenderer utilisé dans Html2PdfFilter
 * 
 */
	protected final Log log = LogFactory.getLog(getClass());
	
	private HttpServletRequest request ;
	
	private ServletContext context;
	
	private String realAppliPath;
	
	private Pattern pattern4icon;
	
	private Pattern pattern4LaTex;
	
	private String url4icon;
	
	private String appliName;
	
	
	
	private void scaleToOutputResolution(Image image , SharedContext sharedContext) {
        float factor = sharedContext.getDotsPerPixel();
        log.debug("DotsPerPixel = {0}", factor);
        factor = factor * 3 / 4;
        log.debug("factor = {0}", factor);
        if (factor != 1.0f) {
            image.scaleAbsolute(image.getPlainWidth() * factor, image.getPlainHeight() * factor);
        }
    }

	public ImgReplacedElementFactory(HttpServletRequest request, ITextOutputDevice out) {
		super(out);
		this.request = request;
		context = request.getSession().getServletContext();
		realAppliPath = context.getRealPath("");
		int  index = realAppliPath.lastIndexOf('/');
		appliName = realAppliPath.substring(index) ;
		pattern4icon = Pattern.compile(appliName + "/javax.faces.resource(/[^/]+)\\.xhtml\\?ln=images/icones");
		url4icon = "file:" + realAppliPath + "/resources/images/icones";
		
		
		String paternString = "((http.?://)?" + request.getServerName() + "(:" + request.getServerPort()+  ")?)?" + appliName + "/images/latex/([^/]+)";
		pattern4LaTex = Pattern.compile(paternString);
		
		
		
	}




	@Override
	public ReplacedElement createReplacedElement(LayoutContext c,
			BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
		Element e = box.getElement();
		FSImage fsImage = null;
		
		int width = 0;
		int height =0;
		
		if (e == null) {
			return null;
		}
		
		
		String nodeName = e.getNodeName();
		
		if (nodeName.equals("img")) {
			String source = e.getAttribute("src");
			
			Matcher m = pattern4icon.matcher(source);
			if (m.matches()) {
				source = url4icon + m.group(1);
				
				ImageResource ir= uac.getImageResource(source);
				
				fsImage =ir.getImage();
			} else {
				
				m = pattern4LaTex.matcher(source);
				if (m.matches()) {
					log.debug("Image latex {0}", m.group(4));
					String uniqueImageFileName = m.group(4);
					
					 byte[] imageContent = (byte[]) request.getSession()
				                .getAttribute(uniqueImageFileName);
					
					 try {
						Image image = Image.getInstance(imageContent);
					//	int factor = 10;
						scaleToOutputResolution(image, c.getSharedContext());
					//	image.scaleAbsolute(image.getPlainWidth() * factor, image.getPlainHeight() * factor);
						fsImage = new ITextFSImage(image);
					} catch (BadElementException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					 
				} else {
					log.debug("Not Icon Nor Latex");
				source = context.getRealPath(source);
				}
			
			}
			
			if (fsImage != null) {
				if (cssWidth != -1 || cssHeight != -1) {
					log.debug("img csswidth ={0} cssheight={1}", cssWidth, cssHeight);
					fsImage.scale(cssWidth, cssHeight);
				} else if (width >  0 && height > 0) {
					log.debug("img width ={0} height={1}", width, height);
					fsImage.scale(width, height);
				}
			
				return new ITextImageElement(fsImage);
			}
		} 
		return super.createReplacedElement(c, box, uac, cssHeight, cssWidth);
	}

	public String getRealAppliPath() {
		return realAppliPath;
	}

	public String getAppliName() {
		return appliName;
	}

}
