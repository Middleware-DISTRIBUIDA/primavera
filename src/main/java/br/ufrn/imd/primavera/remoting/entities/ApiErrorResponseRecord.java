package br.ufrn.imd.primavera.remoting.entities;

public record ApiErrorResponseRecord(Integer status, String url, String message) {

}