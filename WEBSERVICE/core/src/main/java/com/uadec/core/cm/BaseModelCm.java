package com.uadec.core.cm;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibm.mm.sdk.common.DKChildCollection;
import com.ibm.mm.sdk.common.DKConstant;
import com.ibm.mm.sdk.common.DKDDO;
import com.ibm.mm.sdk.common.DKException;
import com.ibm.mm.sdk.common.DKUsageError;
import com.ibm.mm.sdk.server.DKDatastoreICM;
import com.uadec.core.util.CoreUtil;
public abstract class BaseModelCm implements Modelable{

	private final static Logger log = Logger.getLogger(BaseModelCm.class);
	private String pid;
//	private <KeyValCm> values;
	/**
	 * @return El pid del documento.
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * @param pid
	 *            - El pid para asignar.
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}
	public void setValuesAttributes(DKDatastoreICM dsIcm, List<AtributoCm> atributos, DKDDO document){
		mapAttributesValues(values,childValues);
		Object value, correctValue;
		for(AtributoCm attr : atributos){
			if(values.containsKey(attr.getName())){
				value = values.get(attr.getName());
				correctValue = CoreUtil.convertToValue(attr.getName(), value, attr.getClazz());
				try {
					log.info("Setting value for attribute: "+ attr.getName());
					document.setData(document.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, attr.getName()), correctValue);
				} catch (DKUsageError e) {
					log.error("Error al establecer el valor del atributo: "+ attr.getName() + " para el modelo "+this.getModel(),e);
				}
			}else{
				log.info("El valor para el atributo: " + attr.getName() + " no ha sido establecido.");
			}
			
		}
		for(String childName : childValues.keySet()){
			try {
				
				short id = document.dataId(DKConstant.DK_CM_NAMESPACE_CHILD, childName);
				if(id!=0){
					DKChildCollection children = (DKChildCollection) document.getData(id);
					if(children==null){
						children = new DKChildCollection();
						document.setData(id, children);
					}
					List<ChildCm> childs = childValues.get(childName);
//					Map<String,DKDDO> childsCreados = new HashMap<String,DKDDO>();
					if(childs!=null && !childs.isEmpty()){
						for(ChildCm childAttr : childs){
//							DKDDO child = childsCreados.get(childName);
//							if(child!=null){
								DKDDO child = dsIcm.createChildDDO(this.getModel(), childName);
								child.setData(child.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, childAttr.getName()),childAttr.getValue());
								children.addElement(child);
//							}else{
//								child.setData(child.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, childAttr.getName()),childAttr.getValue());
//							}
							
							
						}
					}
					
				}
			} catch (DKUsageError e) {
				log.error(e);
			} catch (DKException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}