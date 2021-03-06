package com.uadec.servicios.controller;

import java.util.HashMap;
import java.util.Map;
/**
 * Controlador que admnistra los servicios.
 * @author Carlos Guzman
 * @date 2013-08-12
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@RestController
public class ServiciosController {		
	Logger log = Logger.getLogger(getClass());

	@Autowired
	RequestMappingHandlerMapping requestMapping;

	@Autowired
	private XmlWebApplicationContext context;

	/**
	 * Genera un String con todos los servicios expuestos.
	 * 
	 * @param request Peticion http.
	 * @return String de todas las peticiones.
	 */
	@RequestMapping(value = "/showall", method = RequestMethod.GET)
	public String getServices(HttpServletRequest request) {
		Map<RequestMappingInfo, HandlerMethod> mappings = requestMapping.getHandlerMethods();
		StringBuffer sb = new StringBuffer();
		for (RequestMappingInfo mapping : mappings.keySet()) {
			sb.append("<b>").append(mapping.getPatternsCondition()).append("</b>");
			sb.append(", ").append(mapping.getMethodsCondition()).append(" - <br/>");
		}
		return sb.toString();
	}

	/**
	 * Recarga el contexto de los servicios.
	 * 
	 * @param request Peticion http.
	 * @return String
	 */
	@RequestMapping(value = "/reload", method = RequestMethod.GET)
	public String reloadServices(HttpServletRequest request) {
		context.refresh();
		return "Reloaded";
	}
	/**
	 * Metodo del cliente para llamar a ejecutar el webservice que carga archivos al CM
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> model = new HashMap<String, Object>();
		return new ModelAndView("documentos/documento", model);
	}
	


	
}
