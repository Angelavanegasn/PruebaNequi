package com.vanegas.angela.franquicias_api.application.dto;

public class ProductoConSucursal {
    
private String productoId;
	private String productoNombre;
	private int stock;
	private String sucursalId;
	private String sucursalNombre;

	public ProductoConSucursal() {
	}

	public ProductoConSucursal(String productoId, String productoNombre, int stock, String sucursalId, String sucursalNombre) {
		this.productoId = productoId;
		this.productoNombre = productoNombre;
		this.stock = stock;
		this.sucursalId = sucursalId;
		this.sucursalNombre = sucursalNombre;
	}

	public String getProductoId() {
		return productoId;
	}

	public void setProductoId(String productoId) {
		this.productoId = productoId;
	}

	public String getProductoNombre() {
		return productoNombre;
	}

	public void setProductoNombre(String productoNombre) {
		this.productoNombre = productoNombre;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(String sucursalId) {
		this.sucursalId = sucursalId;
	}

	public String getSucursalNombre() {
		return sucursalNombre;
	}

	public void setSucursalNombre(String sucursalNombre) {
		this.sucursalNombre = sucursalNombre;
	}
}
