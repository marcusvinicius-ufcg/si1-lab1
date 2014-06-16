package models;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.*;

import models.Exception.MetaException;
import play.data.validation.Constraints.Required;

@Entity
public class Meta implements Comparable<Meta> {
	
	@Id
	@GeneratedValue
	private Long id;

	@Column
	@Required
	private String descricao;

	@Column
	@Required
	private int prioridade;

	@Column
	@Temporal(TemporalType.DATE)
	@Required
	private Date dataFinalizacao;

	@Column(name = "status")
	private boolean status;
	
	@Transient
	private static int MINIMO = 1;
	@Transient
	private static int BAIXO = 2;
	@Transient
	private static int NORMAL = 3;
	@Transient
	private static int ALTO = 4;
	@Transient
	private static int URGENTE = 5;
	@Transient
	private static int MAXIMO_DE_SEMANAS = 6;

	public Meta() {
		this.status = false;
	}
	
	public int getSemana(){
		Calendar data = new GregorianCalendar();
		data.setTime(getDataFinalizacao());
		return data.get(Calendar.WEEK_OF_YEAR);
	}
	

	public Date getDataFinalizacao() {
		return dataFinalizacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public void setDataFinalizacao(Date dataFinalizacao) throws MetaException {
		//Data minima aceita
		Calendar dataMinima = new GregorianCalendar();
		dataMinima.set(Calendar.DAY_OF_MONTH, dataMinima.get(Calendar.DAY_OF_MONTH) -1 );
		
		Calendar dataMaxima = new GregorianCalendar();
		
		//numero representa a semana atual no ano
		int semanaDoAno = dataMinima.get(Calendar.WEEK_OF_YEAR);
		
		//somando a semana atual mas o maximo de semanas aceito no sistema
		dataMaxima.set(Calendar.WEEK_OF_YEAR,  semanaDoAno + MAXIMO_DE_SEMANAS);
		

		if ((dataFinalizacao.compareTo(dataMinima.getTime()) != -1)
				&& (dataFinalizacao.compareTo(dataMaxima.getTime()) != 1)) {
			this.dataFinalizacao = dataFinalizacao;
			
		} else {
			
			throw new MetaException("Data acima de 6 Semanas nao é Permitida");
		}

	}

	public void setStatus(boolean status) {
		this.status = status;

	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) throws MetaException {
		if (descricao == null || descricao.isEmpty()) {
			throw new MetaException("Descrição Inválida");
		}
		this.descricao = descricao;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public String getPrioridadeFormatada() {

		switch (prioridade) {

		case 1:
			return "MINIMO";

		case 2:
			return "BAIXO";

		case 3:
			return "NORMAL";

		case 4:
			return "ALTO";

		case 5:
			return "URGENTE";

		default:
			break;
		}
		return "----";

	}

	public void setPrioridade(int prioridade) throws MetaException {

		if (prioridade < MINIMO || prioridade > URGENTE) {
			throw new MetaException("Prioridade Inválida!");
		}
		this.prioridade = prioridade;
	}

	public String getDataFormatada() {
		return String.format("%td de %<tB de %<tY", dataFinalizacao);
	}

	public boolean isStatus() {
		return status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataFinalizacao == null) ? 0 : dataFinalizacao.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Meta other = (Meta) obj;
		if (dataFinalizacao == null) {
			if (other.dataFinalizacao != null)
				return false;
		} else if (!dataFinalizacao.equals(other.dataFinalizacao))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		return true;
	}

	@Override
	public int compareTo(Meta o) {
		return this.dataFinalizacao.compareTo(o.getDataFinalizacao());
	}
	
	
}
