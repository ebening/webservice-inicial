package com.uadec.ws.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uadec.core.cm.ConnectionCm;
import com.uadec.core.cm.FileCm;
import com.uadec.core.util.Constantes;
import com.uadec.core.util.CoreUtil;
import com.uadec.entity.model.Bitacora;
import com.uadec.entity.model.CatDocumentos;
import com.uadec.entity.model.CatTipoCuenta;
import com.uadec.entity.model.CatTipoTramite;
import com.uadec.entity.model.DetalleCuentas;
import com.uadec.entity.model.DocumentoImportado;
import com.uadec.entity.model.RelTramiteDocumento;
import com.uadec.entity.model.Solicitud;
import com.uadec.entity.model.Usuario;
import com.uadec.entity.service.BitacoraService;
import com.uadec.entity.service.CatDocumentoService;
import com.uadec.entity.service.CatTipoCuentaService;
import com.uadec.entity.service.CatTipoTramiteService;
import com.uadec.entity.service.DetalleCuentaService;
import com.uadec.entity.service.DocumentoImportadoService;
import com.uadec.entity.service.RelTramiteDocumentoService;
import com.uadec.entity.service.SolicitudService;
import com.uadec.entity.service.UsuarioService;
import com.uadec.ws.model.CMDocumento;
import com.uadec.ws.model.CMExpediente;
import com.uadec.ws.model.DataContent;

@Transactional
@Service
public class DataContentService {
	
	private final static Logger log = Logger.getLogger(DataContentService.class);
	@Autowired
	private SolicitudService solicitudService;
	@Autowired
	private DocumentoImportadoService documentoImportadoService;
	@Autowired
	private DetalleCuentaService detalleCuentaService;
	@Autowired
	private RelTramiteDocumentoService relTramiteDocumentoService;
	@Autowired
	private BitacoraService bitacoraService;
	@Autowired
	private CatTipoCuentaService catTipoCuentaService;
	@Autowired
	private CatTipoTramiteService catTipoTramiteService;
	@Autowired
	private CatDocumentoService catDocumentoService;
	@Autowired
	private UsuarioService usuarioService;
	
	/**
	 * Metodo para Guardar los datos en BD y en CM
	 * @param instance objeto con todos los valores a guardar
	 * @return instance con el PID que devuelve el CM
	 */
	public DataContent saveDataContent(DataContent instance) {
		// Datos de la solicitud
		Solicitud solicitud = new Solicitud();
		solicitud = instance.getSolicitud();
		CatTipoTramite catTipoTramite = catTipoTramiteService.findCatTipoTramiteById(solicitud.getCatTipoTramite().getIdTipoTramite());
		solicitud.setCatTipoTramite(catTipoTramite);
		solicitudService.saveSolicitud(solicitud);

		// Datos del documento
		DocumentoImportado documentoImportado = new DocumentoImportado();
		documentoImportado = instance.getDocumentoImportado();
		documentoImportado.setSolicitud(solicitud);
		Usuario usuario = new Usuario();
		usuario.setLogin(documentoImportado.getUsuario().getLogin());
//		usuario = usuarioService.findUsuarioById(1L);
		usuario = usuarioService.findUsuarioByLogin(usuario);
		documentoImportado.setUsuario(usuario);
		CatDocumentos catDocumentos = catDocumentoService.findCatDocumentosById(solicitud.getCatTipoTramite().getIdTipoTramite());
		documentoImportado.setCatDocumentos(catDocumentos);
		documentoImportadoService.saveDocumentoImportado(documentoImportado);

		// Relacionar el tamite con el documento
		RelTramiteDocumento relTramiteDocumento = new RelTramiteDocumento();
		relTramiteDocumento.setCatDocumentos(documentoImportado.getCatDocumentos());
		relTramiteDocumento.setCatTipoTramite(solicitud.getCatTipoTramite());
		relTramiteDocumento.setIdTramiteDoc(documentoImportado.getIdDocumentoImportado());
		relTramiteDocumentoService.saveRelTramiteDocumento(relTramiteDocumento);

		// Detalle de la cuenta
		DetalleCuentas detalleCuenta = new DetalleCuentas();
		for(String numero : instance.getCatCuenta().getDescripcion().split(",")){
			CatTipoCuenta buscarTipoCuenta = new CatTipoCuenta();
			buscarTipoCuenta.setDescripcion(Constantes.TIPO_CUENTA);
			detalleCuenta = getDetalleCuenta(solicitud, numero, buscarTipoCuenta);
			detalleCuentaService.saveDetalleCuentas(detalleCuenta);
		}
		for(String numero : instance.getCatSubCuenta().getDescripcion().split(",")){
			CatTipoCuenta buscarTipoCuenta = new CatTipoCuenta();
			buscarTipoCuenta.setDescripcion(Constantes.TIPO_SUBCUENTA);
			detalleCuentaService.saveDetalleCuentas(getDetalleCuenta(solicitud, numero, buscarTipoCuenta));
		}
		for(String numero : instance.getCatSubSubCuenta().getDescripcion().split(",")){
			CatTipoCuenta buscarTipoCuenta = new CatTipoCuenta();
			buscarTipoCuenta.setDescripcion(Constantes.TIPO_SUBSUBCUENTA);
			detalleCuentaService.saveDetalleCuentas(getDetalleCuenta(solicitud, numero, buscarTipoCuenta));
		}
		
		File fileTemp;
		ConnectionCm cm = null;
		try {
			cm = ConnectionCm.getInstance();
			fileTemp = CoreUtil.saveFile(instance.getFile().getBytes(),
					solicitud.getIdSolicitud() + "_" + instance.getFile().getOriginalFilename());
			cm.uploadDocument(new CMExpediente(instance));
			String pid = cm.uploadDocument(new CMDocumento(null, new FileCm(instance.getFile().getOriginalFilename(),instance.getFile().getBytes()), solicitud, usuario,
					documentoImportado, documentoImportado.getCatDocumentos()));
			if (pid != null) {
				log.info(" >>>>>> PID ====> " + pid);
				fileTemp.delete();
				documentoImportado.setPid(pid);
				instance.setDocumentoImportado(documentoImportado);
				documentoImportadoService.updateDocumentoImportado(documentoImportado);
			} else {
				throw new RuntimeException("Error al guardar archivo en el Content Manager");
			}
		} catch (IOException e) {
			log.error(e);
			
		} finally {
			if (cm != null) {
				cm.disconect();
			}
		}

		bitacoraService.saveBitacora(new Bitacora(usuario, "DOCUMENTO", Constantes.upload, instance.getIp(), new Date(),
				"Se cargo archivo al CM con nombre " + documentoImportado.getNombredoc()));
		return instance;
	}
	
	private DetalleCuentas getDetalleCuenta(Solicitud solicitud, String numero, CatTipoCuenta tipoCuenta) {
		DetalleCuentas detalleCuenta = new DetalleCuentas();
		detalleCuenta.setNumero(numero);
		detalleCuenta.setCiclo(solicitud.getCiclo());
		detalleCuenta.setCuentaPagar(solicitud.getCuentaPagar());
		CatTipoCuenta catTipoCuenta = catTipoCuentaService.findCatTipoCuentasByDescripcion(tipoCuenta);
		detalleCuenta.setCatTipoCuenta(catTipoCuenta);
		return detalleCuenta;
	}
	
	public void updateDocumentoImportado(DocumentoImportado instance) {
		DocumentoImportado documentoImportado = documentoImportadoService.findDocumentoImportadoById(instance.getIdDocumentoImportado());
		if(documentoImportado!=null) {
			documentoImportadoService.updateDocumentoImportado(documentoImportado);
		}
	}
	
	public void saveBitacora(Bitacora bitacora) {
		bitacoraService.saveBitacora(bitacora);
	}
}
