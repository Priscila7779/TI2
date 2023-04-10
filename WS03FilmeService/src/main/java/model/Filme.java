package model;

import java.io.Serializable;
import java.time.LocalDate;

public class Filme implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String DESCRICAO_PADRAO = "Novo Filme";
	public static final int MAX_ESTOQUE = 1000;
	private int id;
	private String nome;
	private String genero;
	private String diretor;	
	private LocalDate dataLancamento;
	
	public Filme() {
		id = -1;
		nome = DESCRICAO_PADRAO;
		genero = DESCRICAO_PADRAO;
		diretor = DESCRICAO_PADRAO;
		dataLancamento = LocalDate.now();
	}

	public Filme(int id, String nome, String genero, String diretor, LocalDate lancamento) {
		setId(id);
		setNome(nome);
		setGenero(genero);
		setDiretor(diretor);
		setDataLancamento(lancamento);
	}		
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
			this.nome = nome;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		if (genero.length() >= 3)
			this.genero = genero;
	}

	public String getDiretor() {
		return diretor;
	}
	
	public void setDiretor(String diretor) {
			this.diretor = diretor;
	}
	
	public LocalDate getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(LocalDate dataLancamento) {
			this.dataLancamento = dataLancamento;
	}


	/**
	 * Método sobreposto da classe Object. É executado quando um objeto precisa
	 * ser exibido na forma de String.
	 */
	@Override
	public String toString() {
		return "Filme: " + nome + "   Genero: " + genero + "   Diretor: " + diretor + "   Lançamento: "
				+ dataLancamento;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.getId() == ((Filme) obj).getId());
	}	
}
