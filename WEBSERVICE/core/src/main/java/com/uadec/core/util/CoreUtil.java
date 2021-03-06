package com.uadec.core.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CoreUtil {

	protected static Logger log = Logger.getLogger(CoreUtil.class);

	public static final String COMPOSITE_FIELD_SEPARATOR = ".";
	public static final String DATE_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";
	
	private static final Timestamp FECHA_HORA_VACIA = Timestamp.valueOf("1900-01-01 00:00:00");
	private static final String EMPTY_STRING = "";
	private static final Integer CERO_INT = 0;
	private static final Double CERO_DOUBLE = 0.0;
	
	public static ObjectMapper mapper = new ObjectMapper();
	public static ObjectMapper databaseMapper = new ObjectMapper();
	
	/*
	 * Se utiliza un mapper especial para la BD (databaseMapper), solo para
	 * evitar formatear las fechas al formato ISO. Con este mapper, la
	 * conversion de fechas sera en formato epoch porque es mucho mas eficiente
	 * que convertirlo a ISO. En resumen: La conversion de fechas entre los
	 * Servicios y la BD es en formato epoch. La conversion de fechas entre
	 * Clientes (ExtJS, etc) y los Servicios es en ISO.
	 */

	static {
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT_ISO));
		databaseMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
	}

	/**
	 * Serializa un objeto a un string en formato JSON.
	 * 
	 * @param object
	 *            Recibe el objeto que ser&aacute; serializado.
	 * @return String en formato JSON con los valore del objeto.
	 */
	public static String toJSON(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Convierte un objeto a un mapa de pareja nombre/valor (String/Object).
	 * 
	 * @param object
	 *            Recibe el objeto que ser&aacute; convertido en un mapa.
	 * @return mapa con los valores del objeto.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object object) {
		return (Map<String, Object>) mapper.convertValue(object, Map.class);
	}

	/**
	 * 
	 * @param object
	 *            instancia con los datos a convertir.
	 * @param clazz
	 *            tipo al que se hara la conversion.
	 * @return instancia de los valores convertidos.
	 */
	public static Object convertValue(Object object, Class<?> clazz) {
		return mapper.convertValue(object, clazz);
	}

	/**
	 * Convierte los valores de un mapa anidado a un mapa plano concatenando los
	 * keyset con el caracter punto.
	 * 
	 * @param input
	 *            Mapa de entrada.
	 * @return Mapa de clave-valor plano.
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toFlatMap(Map<String, Object> input) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (String key : input.keySet()) {
			Object value = input.get(key);
			if (value instanceof Map) {
				Map<String, Object> internalMap = toFlatMap((Map<String, Object>) value);
				for (String internalKey : internalMap.keySet()) {
					String completeKey = key + COMPOSITE_FIELD_SEPARATOR + internalKey;
					map.put(completeKey, internalMap.get(internalKey));
				}
			} else {
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * Genera un zip a partir de los outputstream pasados como parametros.
	 * 
	 * @param salida
	 *            El outputstream donde retornara el zip.
	 * @param parameters
	 *            Mapa con el hascode:nombre de cada outputstream pasado como
	 *            parametro
	 * @param entradas
	 *            Array de outputstream para zipear.
	 * @throws IOException
	 *             Error de escritura.
	 */
	public static void outputStreamsToZip(OutputStream salida, Map<String, Object> parameters, OutputStream... entradas)
			throws IOException {
		ZipOutputStream zos = new ZipOutputStream(salida);
		for (OutputStream os : entradas) {
			ZipEntry entry = new ZipEntry((String) parameters.get(String.valueOf(os.hashCode())));
			zos.putNextEntry(entry);
			zos.write(((ByteArrayOutputStream) os).toByteArray());
			zos.closeEntry();
		}
		zos.finish();
		zos.close();
		salida.close();
	}

	/**
	 * Metodo para truncar un valor a un numero de decimales.
	 * 
	 * @param number
	 *            numero a truncar.
	 * @param decimal
	 *            cantidad de decimales.
	 * @return El numero redondeado.
	 */
	public static Double truncar(Double numero, int decimales) {
		BigDecimal big = new BigDecimal(numero);
		return CoreUtil.truncar(big, decimales);
	}

	/**
	 * Metodo para truncar un valor a un numero de decimales.
	 * 
	 * @param number
	 *            numero a truncar.
	 * @param decimal
	 *            cantidad de decimales.
	 * @return El numero redondeado.
	 */
	public static Double truncar(BigDecimal numero, int decimales) {
		numero = numero.setScale(decimales, BigDecimal.ROUND_FLOOR);
		return numero.doubleValue();
	}

	/**
	 * Redondea un numero al numero de decimales especificado.
	 * 
	 * @param numero
	 *            El numero a redondear.
	 * @param decimales
	 *            Cantidad de decimales.
	 * @return El valor redondeado.
	 */
	public static BigDecimal redondear(Double numero, int decimales) {
		return CoreUtil.redondear(numero.toString(), decimales);
	}

	/**
	 * Redondea un numero al numero de decimales especificado.
	 * 
	 * @param numero
	 *            El numero a redondear.
	 * @param decimales
	 *            Cantidad de decimales.
	 * @return El valor redondeado.
	 */
	public static BigDecimal redondear(BigDecimal numero, int decimales) {
		return CoreUtil.redondear(String.valueOf(numero.doubleValue()), decimales);
	}

	/**
	 * Redondea un numero al numero de decimales especificado.
	 * 
	 * @param numero
	 *            El numero a redondear.
	 * @param decimales
	 *            Cantidad de decimales.
	 * @return El valor redondeado.
	 */
	public static BigDecimal redondear(String numero, int decimales) {
		BigDecimal newNumero = new BigDecimal(numero);
		return newNumero.setScale(decimales, BigDecimal.ROUND_HALF_UP);
	}
	
	public static File saveFile(byte[] bytes,String name){
		File file = new File(PropertyUtil.TEMP_URL+"/UAC_DOCUMENTO");
		FileOutputStream fos;
		try {
			if(!file.exists()){
				file.mkdirs();
			}
			file = new File(file,name);
			fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		return file;
	}
	
	public static Object convertToValue(String name, Object value, Class<?> tipo) {
		if(value==null || (value instanceof String && StringUtils.isEmpty((String) value))){
			Object val = getDefaultValue(tipo);
			log.info("El valor para el atributo "+name+" es null. default value: "+val);
			return val;
		}
		if(tipo.isInstance(value)){
			return value;
		}
		if(tipo.isAssignableFrom(String.class)){
			return String.valueOf(value);
		}
		if (tipo.isAssignableFrom(Date.class) || tipo.isAssignableFrom(Timestamp.class) || tipo.isAssignableFrom(Time.class)) {
			if (value instanceof Long) {
				return new Date(((Long) value).longValue());
			} else if (value instanceof java.util.Date) {
				return new Date(((java.util.Date) value).getTime());
			} else if (value instanceof String) {
				return new Date(Long.parseLong((String) value));
			}
		}
		if(tipo.isAssignableFrom(BigDecimal.class)){
			if (value instanceof Double) {
				return new BigDecimal((Double) value);
			} else if (!(value instanceof BigDecimal)) {
				return new BigDecimal(value.toString());
			}
		}
		if(tipo.isAssignableFrom(Double.class)){
			if (!(value instanceof Double)) {
				return Double.valueOf(value.toString());
			}
		}
		if(tipo.isAssignableFrom(Integer.class)){
			if (!(value instanceof Integer)) {
				return Integer.valueOf(value.toString());
			}
		}
		if(tipo.isAssignableFrom(Short.class)){
			if (!(value instanceof Short)) {
				return Short.valueOf(value.toString());
			}
		}
		log.info("El valor de tipo "+ value.getClass().getCanonicalName()+" no fue convertido para el atributo " + name+ " de tipo "+tipo.getCanonicalName());
		return value;
	}
	
	public static Object getDefaultValue(Class<?> type) {
		if(type.isAssignableFrom(Date.class)){
			return new Date(0);
		}
		if(type.isAssignableFrom(BigDecimal.class)){
			return new BigDecimal("0"); 
		}
		if(type.isAssignableFrom(Double.class)){
			return Double.valueOf("0");
		}
		if(type.isAssignableFrom(Integer.class)){
			return Integer.valueOf("0");
		}
		if(type.isAssignableFrom(Short.class)){
			return Short.valueOf("0");
		}
		if(type.isAssignableFrom(Timestamp.class)){
			return FECHA_HORA_VACIA;
		}
		if(type.isAssignableFrom(Time.class)){
			return new Time(0);
		}
		return "";
	}
}