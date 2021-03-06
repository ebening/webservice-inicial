package com.uadec.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * DocumentoImportado entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "DOCUMENTO_IMPORTADO", schema = "DB2ADMIN")

public class DocumentoImportado implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -917715671472621664L;
	private Long idDocumentoImportado;
	private Solicitud solicitud;
	private Usuario usuario;
	private CatDocumentos catDocumentos;
	private String nombredoc;
	private String pid;

	// Constructors

	/** default constructor */
	public DocumentoImportado() {
	}

	/** full constructor */
	public DocumentoImportado(Solicitud solicitud, Usuario usuario, CatDocumentos catDocumentos, String nombredoc,
			String pid) {
		this.solicitud = solicitud;
		this.usuario = usuario;
		this.catDocumentos = catDocumentos;
		this.nombredoc = nombredoc;
		this.pid = pid;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	@Column(name = "ID_DOCUMENTO_IMPORTADO", unique = true, nullable = false)

	public Long getIdDocumentoImportado() {
		return this.idDocumentoImportado;
	}

	public void setIdDocumentoImportado(Long idDocumentoImportado) {
		this.idDocumentoImportado = idDocumentoImportado;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_SOLICITUD")

	public Solicitud getSolicitud() {
		return this.solicitud;
	}

	public void setSolicitud(Solicitud solicitud) {
		this.solicitud = solicitud;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO")

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOCUMENTO")

	public CatDocumentos getCatDocumentos() {
		return this.catDocumentos;
	}

	public void setCatDocumentos(CatDocumentos catDocumentos) {
		this.catDocumentos = catDocumentos;
	}

	@Column(name = "NOMBREDOC", length = 20)

	public String getNombredoc() {
		return this.nombredoc;
	}

	public void setNombredoc(String nombredoc) {
		this.nombredoc = nombredoc;
	}

	@Column(name = "PID", length = 250)

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}