/**
 * 
 */
package com.uadec.ws.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.uadec.core.cm.BaseFolderCm;
import com.uadec.core.cm.ChildCm;
import com.uadec.core.util.PropertyUtil;
import com.uadec.entity.model.Solicitud;
import com.uadec.ws.util.ConstantesWs;

/**
 * @author adinfi
 *
 */
public class CMExpediente extends BaseFolderCm implements Serializable {

	private static final long serialVersionUID = 9143580773269827713L;
	private final static Logger log = Logger.getLogger(CMExpediente.class);

	private DataContent content;

	public CMExpediente(DataContent instance) {
		setModel(instance);
	}

	public DataContent getContent() {
		return content;
	}

	public void setModel(DataContent content) {
		this.content = content;
	}

	@Override
	public void mapAttributesValues(Map<String, Object> values, Map<String, List<ChildCm>> childsValues) {
		Solicitud solicitud = getContent().getSolicitud();
		values.put("UAC_ID_SOL", solicitud.getIdSolicitud());
		values.put("UAC_CICLO", solicitud.getCiclo());
		values.put("UAC_CUENTA_PAGAR", solicitud.getCuentaPagar());
		// values.put("UAC_FECHA_SOL",new
		// java.sql.Date(solicitud.getFechaSolicitud().getTime()));
		values.put("UAC_FECHA_SOL", solicitud.getFechaSolicitud());
		values.put("UAC_FONDO", solicitud.getFondo());
		values.put("UAC_NCBANCARIA", solicitud.getNCbancaria());
		values.put("UAC_UNID_ORG", solicitud.getUnidadOrg());
		values.put("UAC_IMPORTE", solicitud.getImporte().toString());
		values.put("UAC_AUTORIZADOR", solicitud.getAutorizador());
		values.put("UAC_ELABORADOR", solicitud.getElaborador());
		values.put("UAC_CONTRATO", solicitud.getContrato());
		values.put("UAC_RFC_PROVEEDOR", solicitud.getRfcProveedor());
		values.put("UAC_CHEQUE", solicitud.getCheque());
		values.put("UAC_FOLIO_V", solicitud.getFolioVentanilla());
		values.put("UAC_FOLIO_S", solicitud.getFolioSdr());
		values.put("UAC_N_COMPROMISO", solicitud.getNCompromiso());
		values.put("UAC_C_BENEFICIARIO", solicitud.getClaveBeneficiario());
		values.put("UAC_T_TRAMITE", solicitud.getCatTipoTramite().getDescripcion());
		// values.put("UAC_CUENTA",getCatCuentas().getDescripcion());
		// values.put("UAC_SCUENTA",getCatCuentas().getDescripcion());
		// values.put("UAC_SSCUENTA",getCatCuentas().getDescripcion());
		
		

		childsValues.put("CUENTA",ChildCm.getChilds("UAC_CUENTA", (Object[]) getContent().getCatCuenta().getDescripcion().split(",")));
		childsValues.put("SCUENTA",ChildCm.getChilds("UADEC_SCUENTA", (Object[]) getContent().getCatSubCuenta().getDescripcion().split(",")));
		childsValues.put("SSCUENTA",ChildCm.getChilds("UADEC_SSCUENTA", (Object[]) getContent().getCatSubSubCuenta().getDescripcion().split(",")));
	}

	@Override
	public String getValueSearch() {
		return getContent().getSolicitud().getIdSolicitud().toString();
	}

	@Override
	public String getModel() {
		return PropertyUtil.getProperty(ConstantesWs.UAC_EXPEDIENTE_KEY_MODEL);
	}

	@Override
	public String getKeySearch() {
		return ConstantesWs.UAC_EXPEDIENTE_KEY_SEARCH;
	}
}
