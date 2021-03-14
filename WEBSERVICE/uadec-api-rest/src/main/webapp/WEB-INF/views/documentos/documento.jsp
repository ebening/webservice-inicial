<%@page language="java" isELIgnored="false" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>Parametros </title>
</head>
<body>
<div id="contentarea">      
	<div id="content">
		<form action="${pageContext.request.contextPath}/ws/data/uploadFile" method="POST" enctype="multipart/form-data">
			<table cellpadding="0" cellspacing="0" id="viewTable">
				<tbody>
				<tr>
						<td colspan="2">
						<h1 style="background-color: #EEEEEE;">Cargar Documentos a CM</h1>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							<b>Parametros Solicitud:</b>
						</td>
						<td>
							<input type="hidden" id="solicitud_idSolicitud" />
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							FECHA SOLICITUD:
						</td>
						<td>
							<input id="solicitud_fechaSolicitud" name="solicitud.fechaSolicitudStr" type="text" value="30/08/2016" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							FONDO:
						</td>
						<td>
							<input id="solicitud_fondo"  name="solicitud.fondo" value="12345678901234567890" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							CUENTA BANCARIA:
						</td>
						<td>
							<input id="solicitud_NCbancaria"  name="solicitud.NCbancaria" value="12345678901234567890" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							UNIDAD ORGANIZACIONAL:
						</td>
						<td>
							<input id="solicitud_unidadOrg" name="solicitud.unidadOrg" value="12345678901234567890" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							IMPORTE:
						</td>
						<td>
							<input id="solicitud_importe" name="solicitud.importeStr" value="0.0" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							AUTORIZADOR:
						</td>
						<td>
							<input id="solicitud_autorizador"  name="solicitud.autorizador" value="autorizador" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							ELABORADOR:
						</td>
						<td>
							<input id="solicitud_elaborador" name="solicitud.elaborador" value="elaborador" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							CONTRATO:
						</td>
						<td>
							<input id="solicitud_contrato" name="solicitud.contrato" value="contrato" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							RFC DEL PROVEEDOR:
						</td>
						<td>
							<input id="solicitud_rfcProveedor" name="solicitud.rfcProveedor" value="RFC" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							CHEQUE:
						</td>
						<td>
							<input id="solicitud_cheque" name="solicitud.cheque" value="cheque" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							TIPO DE TRAMITE:
						</td>
						<td>
							<input id="solicitud_idTipoTramite" name="solicitud.catTipoTramite.idTipoTramite" value="1" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							CUENTA PAGAR:
						</td>
						<td>
							<input id="solicitud_cuentaPagar" name="solicitud.cuentaPagar" value="cuenta" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							CICLO:
						</td>
						<td>
							<input id="solicitud_ciclo"  name="solicitud.ciclo" value="2016" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							FOLIO DE VENTANILLA:
						</td>
						<td>
							<input id="solicitud_folioVentanilla" name="solicitud.folioVentanilla" value="folioventanilla" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							FOLIO SDR:
						</td>
						<td>
							<input id="solicitud_folioSdr" name="solicitud.folioSdr" value="foliosdr" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							NUMER DE COMPROMISO:
						</td>
						<td>
							<input id="solicitud_NCompromiso"  name="solicitud.NCompromiso" value="compromiso" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							CLAVE DE BENEFICIARIO:
						</td>
						<td>
							<input id="solicitud_claveBeneficiario"  name="solicitud.claveBeneficiario" value="cbeneficiario" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							<b>Parametros Documento:</b>
						</td>
						<td>
							
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							TIPO DE DOCUMENTO:
						</td>
						<td>
							<input id="documentoimportado_nombredoc" name="documentoImportado.catDocumentos.idDocumento" value="1" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							NOMBRE DE DOCUMENTO:
						</td>
						<td>
							<input id="documentoimportado_nombredoc" name="documentoImportado.nombredoc" value="nombre" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							USUARIO IMPORTADOR:
						</td>
						<td>
							<input id="documentoimportado_usuariologin" name="documentoImportado.usuario.login" value="WEBSERVICE" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							<label>ADJUNTAR ARCHIVO</label>:
						</td>
						<td>
							<input id="pdf" name="file" type="file">
							<label class="file-selected"></label>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							<b>Parametros Cuenta:</b>
						</td>
						<td>
							
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							CUENTA:
						</td>
						<td>
							<input id="detallecuentas_numero" name="catCuenta.descripcion" value="0001,0002,0003"  style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							SUBCUENTA:
						</td>
						<td>
							<input id="detallecuentas_idTipoSubCuenta" name="catSubCuenta.descripcion" value="2000,2001" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td class="label" valign="top">
							SUBSUBCUENTA:
						</td>
						<td>
							<input id="detallecuentas_idTiposubsubCuenta" name="catSubSubCuenta.descripcion" value="3001,3002,3003,3004" style="width:300px;"/>
						</td>
					</tr>
					<tr>
						<td align="right" colspan="2" style="background-color: #EEEEEE;"><br>
							<span class="inputbutton"><input class="savebutton" id="save" type="submit" value="Guardar"/></span>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		<div class="clear">&nbsp;</div>
	</div>
	</div>
</body>
</html>