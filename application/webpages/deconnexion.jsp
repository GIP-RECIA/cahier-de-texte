<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" session="false" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page import="java.io.*"%>
<%@ page import="org.crlr.web.utils.FacesUtils"%>
<%@ page import="org.crlr.message.ConteneurMessage"%>
<%@ page import="org.crlr.message.Message"%>
<%@ page import="org.crlr.exception.metier.*"%>
<%@ page import="org.crlr.web.utils.MessageUtils"%>
<%
final StringWriter sw = new StringWriter();
final PrintWriter writer = new PrintWriter(sw);

String messages = null;
final Boolean withException;
Throwable cause = null;

if (exception != null) {
    exception.printStackTrace(writer);
    withException = true;
    cause = (exception.getCause() != null && exception.getCause().getCause() != null) ? exception.getCause().getCause() : exception.getCause();
    if (cause instanceof MetierException) {
        messages = generateMessages(cause);
    } else if (cause instanceof MetierRuntimeException) {
        messages = generateMessages(cause);
    }
} else {
    withException = false;
}

if (messages == null) {
    messages = "Aucun messages reçus des services métiers (erreur inattendue)";
}
%>
<%!

public String generateMessages(final Throwable cause) {
    String messages = "";
    final ConteneurMessage cm = ((MetierRuntimeException) cause).getConteneurMessage();
    if (cm != null) {
        for (Message message : cm.getMessages()) { 
            messages += message.getCode() + " : " + MessageUtils.getTexteRegleByCode(message.getCode(), message.getParametres()) + "</br>";
        }
    }
    
    return messages;
}

%>
<c:set var="imagesDir" value="/images/" />
<c:set var="iconesDir" value="/images/icones/" />

<head>
<title>Vous avez été déconnecté de l'application</title>
<link rel="styleSheet"
	href="/[APPLICATION.NAME]/css/general.css"
	media="screen" />
</head>
<body>


<table cellspacing="0" cellpadding="0" class="centpourcent"
	id="tableTopTop">
	<tr>
		<td class="zoneappli">
		<table class="centpourcent" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<div>
				<table cellspacing="0" cellpadding="0">
					<thead>
						<tr>
							<th>
							<h2>Vous avez été déconnecté de l'application</h2>
							</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>
							<ul>
								<li><a href="/[APPLICATION.NAME]/index.jsp">Veuillez
								utiliser ce lien pour vous reconnecter</a></li>
							</ul>
							</td>
						</tr>
					</tbody>
					<tfoot>
						<tr>
							<td></td>
						</tr>
					</tfoot>
				</table>
				</div>	
				<div>
					Un problème est survenu, merci de contacter votre administrateur ENT
				</div>		
					<div style="height: 500px; overflow: auto; overflow-y: none;">
					<table border="1" cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th colspan="2">
								<h2>Exception de l'application</h2>
								</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>
								<h3>Cause mère</h3>
								</td>
								<td>
								<h3>Messages métier</h3>
								</td>
							</tr>
							<tr>
								<td><%=cause%></td>
								<td><%=messages%></td>
							</tr>
							<tr>
								<td colspan="2">
								<h3>Exception</h3>
								</td>
							</tr>
							<tr>
								<td colspan="2"><%=exception%></td>
							</tr>
							<tr>
								<td colspan="2">
								<h3>Pile</h3>
								</td>
							</tr>
							<tr>
								<td colspan="2"><%=sw.toString()%></td>
							</tr>
						</tbody>
						<tfoot>
							<tr>
								<td></td>
							</tr>
						</tfoot>
					</table>
					</div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>